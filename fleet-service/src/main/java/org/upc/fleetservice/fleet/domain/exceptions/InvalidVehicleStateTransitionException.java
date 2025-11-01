package org.upc.fleetservice.fleet.domain.exceptions;

import org.upc.fleetservice.fleet.domain.model.valueobjects.VehicleStatus;

public class InvalidVehicleStateTransitionException extends RuntimeException {

    public InvalidVehicleStateTransitionException(VehicleStatus vehicleStatus) {
        super("Vehicle cannot be marked operational from its current state: " + vehicleStatus);
    }
}

