package com.algaworks.algadelivery.delevery.tracking.domain.model;

import java.util.Arrays;
import java.util.List;

public enum DeliveryStatus {
    DRAFTED,
    WAITING_FOR_COURIER(DRAFTED),
    IN_TRANSIT(WAITING_FOR_COURIER),
    DELIVERY(IN_TRANSIT);

    private final List<DeliveryStatus> previousStatuses;

    DeliveryStatus(DeliveryStatus... previousStatuses) {
        this.previousStatuses = Arrays.asList(previousStatuses);
    }

    public boolean canNotChangeTo(DeliveryStatus newStatus) {
        return !newStatus.previousStatuses.contains(this);
    }

    public boolean canChangeTo(DeliveryStatus newStatus) {
        return !canNotChangeTo(newStatus);
    }
}
