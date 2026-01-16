package com.algaworks.algadelivery.delevery.tracking.domain.model;

import com.algaworks.algadelivery.delevery.tracking.domain.exception.DomainException;
import lombok.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter(AccessLevel.PRIVATE)
@Getter
public class Delivery {

    @EqualsAndHashCode.Include
    private UUID id;
    private UUID courierId;

    private DeliveryStatus status;

    private OffsetDateTime placedAt;
    private OffsetDateTime assignedAt;
    private OffsetDateTime expectedDeliveredAt;
    private OffsetDateTime fulfilledAt;

    private BigDecimal distanceFee;
    private BigDecimal courierPayout;
    private BigDecimal totalCost;

    private Integer totalItems;

    private ContactPoint sender;
    private ContactPoint recipient;

    private List<Item> items = new ArrayList<>();

    public static Delivery draft() {
        Delivery delivery = new Delivery();
        delivery.setId(UUID.randomUUID());
        delivery.setStatus(DeliveryStatus.DRAFTED);
        delivery.setTotalItems(0);
        delivery.setTotalCost(BigDecimal.ZERO);
        delivery.setCourierPayout(BigDecimal.ZERO);
        delivery.setDistanceFee(BigDecimal.ZERO);
        return delivery;
    }

    public UUID addItem(String name, Integer quantity) {
        Item item = Item.brandNew(name, quantity);
        this.items.add(item);
        calculateTotalItems();
        return item.getId();
    }

    public void removeItem(UUID itemId) {
        this.items.removeIf(item -> item.getId().equals(itemId));
        calculateTotalItems();
    }

    public void changeItemQuantity(UUID itemId, Integer newQuantity) {
        Item item = this.items.stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item not found with id: " + itemId));

        item.setQuantity(newQuantity);
        calculateTotalItems();
    }

    public void removeItems() {
        this.items.clear();
        calculateTotalItems();
    }

    public void editPreparationDetails(PreparationDetails preparationDetails) {
        verifyIfCanBeEdited();
        setSender(preparationDetails.getSender());
        setRecipient(preparationDetails.getRecipient());
        setDistanceFee(preparationDetails.getDistanceFee());
        setCourierPayout(preparationDetails.getCourierPayout());

        setExpectedDeliveredAt(
                OffsetDateTime.now().plus(preparationDetails.getExpectedDeliveredTime())
        );
        setTotalCost(
                preparationDetails.getDistanceFee().add(preparationDetails.getCourierPayout())
        );
    }

    private void verifyIfCanBeEdited() {
        if (!getStatus().equals(DeliveryStatus.DRAFTED)){
            throw new DomainException("Delivery cannot be edited in its current status.");
        }
    }

    public void place() {
        verifyIfCanBePlaced();
        changeStatusTo(DeliveryStatus.WAITING_FOR_COURIER);
        setPlacedAt(OffsetDateTime.now());
    }

    private void verifyIfCanBePlaced() {
        if (!isFilled()) {
            throw new DomainException("Delivery is not completely filled.");
        }

        if (!getStatus().equals(DeliveryStatus.DRAFTED)){
            throw new DomainException("Delivery cannot be placed in its current status.");
        }
    }

    private boolean isFilled() {
        return this.sender != null
                && this.recipient != null
                && this.totalCost != null;
    }

    public void markAsDelivered() {
        this.changeStatusTo(DeliveryStatus.DELIVERY);
        this.setFulfilledAt(OffsetDateTime.now());
    }

    public void pickUp(UUID courierId) {
        this.setCourierId(courierId);
        this.changeStatusTo(DeliveryStatus.IN_TRANSIT);
        this.setAssignedAt(OffsetDateTime.now());
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(this.items);
    }

    private void calculateTotalItems() {
        int totalItems = getItems().stream().mapToInt(Item::getQuantity).sum();
        setTotalItems(totalItems);
    }

    private void changeStatusTo(DeliveryStatus newStatus) {
        if (newStatus != null && getStatus().canNotChangeTo(newStatus)) {
            throw new DomainException(
                    String.format("Cannot change delivery status from %s to %s",
                            getStatus(),
                            newStatus)
            );
        }
        setStatus(newStatus);
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class PreparationDetails {

        private ContactPoint sender;
        private ContactPoint recipient;
        private BigDecimal distanceFee;
        private BigDecimal courierPayout;
        private Duration expectedDeliveredTime;
    }
}
