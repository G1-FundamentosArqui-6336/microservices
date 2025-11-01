package org.upc.fleetservice.fleet.domain.exceptions;

import org.upc.fleetservice.fleet.domain.model.valueobjects.VehicleStatus;

public class VehicleNotInRouteException extends RuntimeException {
    public VehicleNotInRouteException(VehicleStatus vehicleStatus) {
        super("Vehicle with status: " + vehicleStatus + " is not in route.");
    }
}