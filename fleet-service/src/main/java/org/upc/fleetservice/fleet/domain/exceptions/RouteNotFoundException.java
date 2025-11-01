package org.upc.fleetservice.fleet.domain.exceptions;

public class RouteNotFoundException extends RuntimeException {
    public RouteNotFoundException(Long routeId) {
        super("Route with ID " + routeId + " not found.");
    }
}