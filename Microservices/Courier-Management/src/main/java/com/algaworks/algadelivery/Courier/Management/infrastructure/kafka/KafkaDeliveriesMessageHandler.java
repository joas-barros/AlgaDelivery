package com.algaworks.algadelivery.Courier.Management.infrastructure.kafka;

import com.algaworks.algadelivery.Courier.Management.domain.service.CourierDeliveryService;
import com.algaworks.algadelivery.Courier.Management.infrastructure.event.DeliveredFulfilledIntegrationEvent;
import com.algaworks.algadelivery.Courier.Management.infrastructure.event.DeliveredPlacedIntegrationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = {
        "deliveries.v1.events"
}, groupId = "courier-management")
@Slf4j
@RequiredArgsConstructor
public class KafkaDeliveriesMessageHandler {

    private final CourierDeliveryService courierDeliveryService;

    @KafkaHandler(isDefault = true)
    public void defaultHandler(@Payload Object payload) {
        log.info("Default Handler: {}", payload);
    }

    @KafkaHandler
    public void handle(@Payload DeliveredPlacedIntegrationEvent event) {
        log.info("Received DeliveredPlacedIntegrationEvent: {}", event);
        courierDeliveryService.assign(event.getDeliveryId());
    }

    @KafkaHandler
    public void handle(@Payload DeliveredFulfilledIntegrationEvent event) {
        log.info("Received DeliveredFulfilledIntegrationEvent: {}", event);
        courierDeliveryService.fulfill(event.getDeliveryId());
    }
}
