package org.upc.fleetservice.fleet.domain.model.commands;

public record AssignVehicleToRouteCommand(Long routeId, Long vehicleId) {
}
