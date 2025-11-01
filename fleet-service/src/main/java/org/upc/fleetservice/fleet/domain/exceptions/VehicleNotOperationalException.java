package org.upc.fleetservice.fleet.domain.exceptions;

import org.upc.fleetservice.fleet.domain.model.valueobjects.VehicleStatus;

public class VehicleNotOperationalException extends RuntimeException {
    public VehicleNotOperationalException(VehicleStatus vehicleStatus) {
        super("Vehicle with status: " + vehicleStatus + " is not operational.");
    }
}