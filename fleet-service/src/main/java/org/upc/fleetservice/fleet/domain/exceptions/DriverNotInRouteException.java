package org.upc.fleetservice.fleet.domain.exceptions;

import org.upc.fleetservice.fleet.domain.model.valueobjects.DriverStatus;

public class DriverNotInRouteException extends RuntimeException {
    public DriverNotInRouteException(DriverStatus driverStatus) {
        super("Driver with status: " + driverStatus + " is not in route.");
    }
}