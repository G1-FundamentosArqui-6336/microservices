package org.upc.fleetservice.fleet.domain.services;


import org.upc.fleetservice.fleet.domain.model.aggregates.Vehicle;
import org.upc.fleetservice.fleet.domain.model.queries.GetAllVehiclesQuery;
import org.upc.fleetservice.fleet.domain.model.queries.GetVehicleByIdQuery;

import java.util.List;
import java.util.Optional;

public interface VehicleQueryService {

    List<Vehicle> handle(GetAllVehiclesQuery query);
    Optional<Vehicle> handle(GetVehicleByIdQuery query);

}
