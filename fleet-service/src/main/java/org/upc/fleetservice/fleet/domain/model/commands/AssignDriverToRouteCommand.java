package org.upc.fleetservice.fleet.domain.model.commands;

public record AssignDriverToRouteCommand(Long routeId, Long driverId) {
}
