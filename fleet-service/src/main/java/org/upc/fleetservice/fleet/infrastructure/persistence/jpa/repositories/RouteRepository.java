package org.upc.fleetservice.fleet.infrastructure.persistence.jpa.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.upc.fleetservice.fleet.domain.model.aggregates.Driver;
import org.upc.fleetservice.fleet.domain.model.aggregates.Route;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> findAllByDriver(Driver driver);

}
