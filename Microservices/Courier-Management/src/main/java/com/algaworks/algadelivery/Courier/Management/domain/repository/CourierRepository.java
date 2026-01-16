package com.algaworks.algadelivery.Courier.Management.domain.repository;

import com.algaworks.algadelivery.Courier.Management.domain.model.Courier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CourierRepository extends JpaRepository<Courier, UUID> {
}
