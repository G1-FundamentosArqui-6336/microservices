package org.upc.fleetservice.fleet.domain.exceptions;

import org.upc.fleetservice.fleet.domain.model.valueobjects.RouteStatus;

public class RouteNotPlannedException extends RuntimeException {
    public RouteNotPlannedException(RouteStatus actualStatus) {
        super("Route must be in PLANNED status to perform this action, but was: " + actualStatus);
    }
}