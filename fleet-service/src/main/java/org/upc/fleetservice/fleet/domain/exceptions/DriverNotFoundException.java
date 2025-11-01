package org.upc.fleetservice.fleet.domain.exceptions;

public class DriverNotFoundException extends RuntimeException {
    public DriverNotFoundException(Long driverId) {
        super("Driver with ID %s not found".formatted(driverId));
    }
}
