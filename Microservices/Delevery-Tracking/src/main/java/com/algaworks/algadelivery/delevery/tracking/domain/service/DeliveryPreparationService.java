package com.algaworks.algadelivery.delevery.tracking.domain.service;

import com.algaworks.algadelivery.delevery.tracking.api.model.ContactPointInput;
import com.algaworks.algadelivery.delevery.tracking.api.model.DeliveryInput;
import com.algaworks.algadelivery.delevery.tracking.api.model.ItemInput;
import com.algaworks.algadelivery.delevery.tracking.domain.exception.DomainException;
import com.algaworks.algadelivery.delevery.tracking.domain.model.ContactPoint;
import com.algaworks.algadelivery.delevery.tracking.domain.model.Delivery;
import com.algaworks.algadelivery.delevery.tracking.domain.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryPreparationService {

    private final DeliveryRepository deliveryRepository;

    @Transactional
    public Delivery draft(DeliveryInput input) {
        Delivery delivery = Delivery.draft();
        handlePreparation(input, delivery);
        return deliveryRepository.saveAndFlush(delivery);
    }

    @Transactional
    public Delivery edit(UUID deliveryId, DeliveryInput input) {

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DomainException("Delivery not found"));

        delivery.removeItems();
        handlePreparation(input, delivery);
        return deliveryRepository.saveAndFlush(delivery);
    }

    private void handlePreparation(DeliveryInput input, Delivery delivery) {
        ContactPointInput senderInput = input.getSender();
        ContactPointInput recipientInput = input.getRecipient();

        ContactPoint sender = ContactPoint.builder()
                .street(senderInput.getStreet())
                .number(senderInput.getNumber())
                .complement(senderInput.getComplement())
                .zipCode(senderInput.getZipCode())
                .phone(senderInput.getPhone())
                .name(senderInput.getName())
                .build();


        ContactPoint recipient = ContactPoint.builder()
                .street(recipientInput.getStreet())
                .number(recipientInput.getNumber())
                .complement(recipientInput.getComplement())
                .zipCode(recipientInput.getZipCode())
                .phone(recipientInput.getPhone())
                .name(recipientInput.getName())
                .build();

        Duration expectedDeliveredTime = Duration.ofHours(3);

        BigDecimal payout = new BigDecimal("10.00");

        BigDecimal distanceFee = new BigDecimal("10.00");

        var preparationDetails = Delivery.PreparationDetails.builder()
                .recipient(recipient)
                .sender(sender)
                .expectedDeliveredTime(expectedDeliveredTime)
                .courierPayout(payout)
                .distanceFee(distanceFee)
                .build();

        delivery.editPreparationDetails(preparationDetails);

        for (ItemInput item : input.getItems()) {
            delivery.addItem(item.getName(), item.getQuantity());
        }
    }
}
