package com.algaworks.algadelivery.delevery.tracking.infrastructure.event;

import com.algaworks.algadelivery.delevery.tracking.domain.event.DeliveryFulfilledEvent;
import com.algaworks.algadelivery.delevery.tracking.domain.event.DeliveryPickUpEvent;
import com.algaworks.algadelivery.delevery.tracking.domain.event.DeliveryPlacedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.algaworks.algadelivery.delevery.tracking.infrastructure.kafka.KafkaTopicConfig.DELIVERY_EVENTS_TOPIC_NAME;

@Component
@Slf4j
@RequiredArgsConstructor
public class DeliveryDomainEventHandler {

    private final IntegrationEventPublisher integrationEventPublisher;

    @EventListener
    public void handle(DeliveryPlacedEvent event) {
        log.info("Handling DeliveryPlacedEvent: {}", event.toString());
        integrationEventPublisher.publish(
                event,
                event.getDeliveryId().toString(),
                DELIVERY_EVENTS_TOPIC_NAME
        );
    }

    @EventListener
    public void handle(DeliveryPickUpEvent event) {
        log.info("Handling DeliveryPickUpEvent: {}", event.toString());
        integrationEventPublisher.publish(
                event,
                event.getDeliveryId().toString(),
                DELIVERY_EVENTS_TOPIC_NAME
        );
    }

    @EventListener
    public void handle(DeliveryFulfilledEvent event) {
        log.info("Handling DeliveryFulfilledEvent: {}", event.toString());
        integrationEventPublisher.publish(
                event,
                event.getDeliveryId().toString(),
                DELIVERY_EVENTS_TOPIC_NAME
        );
    }
}
