package org.upc.fleetservice.fleet.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.upc.fleetservice.fleet.domain.model.aggregates.Vehicle;



@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

}
