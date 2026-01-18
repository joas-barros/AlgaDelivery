package com.algaworks.algadelivery.Courier.Management.infrastructure.event;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class DeliveredPlacedIntegrationEvent {
    private OffsetDateTime occurredAt;
    private UUID deliveryId;
}
