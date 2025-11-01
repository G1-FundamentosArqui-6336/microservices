package org.upc.fleetservice.fleet.domain.exceptions;


public class RouteAlreadyAssignedVehicleException extends RuntimeException {

    public RouteAlreadyAssignedVehicleException(Long existingVehicleId) {
        super("Route already has a vehicle assigned. (Vehicle ID: " + existingVehicleId + ")");
    }
}