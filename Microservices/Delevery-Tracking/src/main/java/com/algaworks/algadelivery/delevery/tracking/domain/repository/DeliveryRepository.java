package com.algaworks.algadelivery.delevery.tracking.domain.repository;

import com.algaworks.algadelivery.delevery.tracking.domain.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {

}
