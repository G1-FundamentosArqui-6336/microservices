package org.upc.fleetservice.fleet.domain.model.commands;

public record AddOrderToRouteCommand (Long routeId, Long orderId) {
}
