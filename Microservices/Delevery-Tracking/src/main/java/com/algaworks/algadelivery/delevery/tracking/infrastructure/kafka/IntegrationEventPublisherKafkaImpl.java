package com.algaworks.algadelivery.delevery.tracking.infrastructure.kafka;

import com.algaworks.algadelivery.delevery.tracking.infrastructure.event.IntegrationEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class IntegrationEventPublisherKafkaImpl implements IntegrationEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publish(Object event, String key, String topic) {
        SendResult<String, Object> result = kafkaTemplate.send(topic, key, event).join();
        log.info("Message publish: \n\tTopic: {}\n\tKey: {}\n\tPartition: {}\n\tOffset: {}\n\tEvent: {}",
                result.getRecordMetadata().topic(),
                result.getProducerRecord().key(),
                result.getRecordMetadata().partition(),
                result.getRecordMetadata().offset(),
                event);
    }
}
