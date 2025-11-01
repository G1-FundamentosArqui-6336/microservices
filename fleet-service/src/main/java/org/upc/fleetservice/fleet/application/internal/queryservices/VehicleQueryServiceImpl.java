package org.upc.fleetservice.fleet.application.internal.queryservices;
import org.springframework.stereotype.Service;
import org.upc.fleetservice.fleet.domain.model.aggregates.Vehicle;
import org.upc.fleetservice.fleet.domain.model.queries.GetAllVehiclesQuery;
import org.upc.fleetservice.fleet.domain.model.queries.GetVehicleByIdQuery;
import org.upc.fleetservice.fleet.domain.services.VehicleQueryService;
import org.upc.fleetservice.fleet.infrastructure.persistence.jpa.repositories.VehicleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleQueryServiceImpl implements VehicleQueryService {

    private final VehicleRepository vehicleRepository;

    public VehicleQueryServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public List<Vehicle> handle(GetAllVehiclesQuery query) {
        return vehicleRepository.findAll();
    }
    @Override
    public Optional<Vehicle> handle(GetVehicleByIdQuery query) {
        return vehicleRepository.findById(query.vehicleId());
    }
    
}
