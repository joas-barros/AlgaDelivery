package com.algaworks.algadelivery.delevery.tracking.domain.service;

import java.math.BigDecimal;

public interface CourierPayoutCalculationService {
    BigDecimal calculatePayout(Double distanceInKm);
}
