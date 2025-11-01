package org.upc.fleetservice.fleet.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.upc.fleetservice.fleet.domain.model.aggregates.Driver;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
}
