package org.upc.fleetservice.fleet.domain.exceptions;

import org.upc.fleetservice.fleet.domain.model.valueobjects.DriverStatus;
import org.upc.fleetservice.fleet.domain.model.valueobjects.VehicleStatus;

public class DriverNotAvailableException extends RuntimeException {
    public DriverNotAvailableException(DriverStatus driverStatus) {
        super("Driver with status: " + driverStatus + " is not available for a new route.");
    }
}