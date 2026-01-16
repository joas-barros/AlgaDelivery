package com.algaworks.algadelivery.delevery.tracking.domain.repository;

import com.algaworks.algadelivery.delevery.tracking.domain.model.ContactPoint;
import com.algaworks.algadelivery.delevery.tracking.domain.model.Delivery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeliveryRepositoryTest {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Test
    public void shouldPersist() {
        Delivery delivery = Delivery.draft();

        delivery.editPreparationDetails(createdValidPreparationDetails());

        delivery.addItem("Computador", 2);
        delivery.addItem("Notebook", 2);

        deliveryRepository.saveAndFlush(delivery);

        Delivery persistedDelivery = deliveryRepository.findById(delivery.getId()).orElseThrow();

        assertEquals(2, persistedDelivery.getItems().size());

    }

    private Delivery.PreparationDetails createdValidPreparationDetails() {
        return Delivery.PreparationDetails.builder()
                .sender(ContactPoint.builder()
                        .name("Sender Name")
                        .phone("123456789")
                        .zipCode("12345-678")
                        .street("Sender Street")
                        .number("100")
                        .complement("Apt 1")
                        .build())
                .recipient(ContactPoint.builder()
                        .name("Recipient Name")
                        .phone("987654321")
                        .zipCode("87654-321")
                        .street("Recipient Street")
                        .number("200")
                        .complement("Suite 2")
                        .build())
                .distanceFee(new java.math.BigDecimal("15.00"))
                .courierPayout(new java.math.BigDecimal("10.00"))
                .expectedDeliveredTime(Duration.ofHours(5))
                .build();
    }

}