package org.upc.fleetservice.fleet.domain.exceptions;

public class VehicleNotFoundException extends RuntimeException {
    public VehicleNotFoundException(Long vehicleId) {
        super("Vehicle with ID %s not found".formatted(vehicleId));
    }
}
