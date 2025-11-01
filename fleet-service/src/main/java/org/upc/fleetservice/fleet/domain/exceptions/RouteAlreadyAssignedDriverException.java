package org.upc.fleetservice.fleet.domain.exceptions;

public class RouteAlreadyAssignedDriverException extends RuntimeException {

    public RouteAlreadyAssignedDriverException(Long existingDriverId) {
        super("Route already has a driver assigned. (Driver ID: " + existingDriverId + ")");
    }
}