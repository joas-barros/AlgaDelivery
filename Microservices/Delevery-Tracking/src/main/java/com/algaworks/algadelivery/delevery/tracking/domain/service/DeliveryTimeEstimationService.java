package com.algaworks.algadelivery.delevery.tracking.domain.service;

import com.algaworks.algadelivery.delevery.tracking.domain.model.ContactPoint;

public interface DeliveryTimeEstimationService {
    DeliveryEstimate estimate(ContactPoint sender, ContactPoint receiver);
}
