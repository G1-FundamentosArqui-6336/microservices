package org.upc.fleetservice.fleet.domain.exceptions;

import org.upc.fleetservice.fleet.domain.model.valueobjects.RouteStatus;

public class RouteNotInProgressException extends RuntimeException {
    public RouteNotInProgressException(RouteStatus actualStatus) {
        super("Route must be in IN_PROGRESS status to perform this action, but was: " + actualStatus);
    }
}