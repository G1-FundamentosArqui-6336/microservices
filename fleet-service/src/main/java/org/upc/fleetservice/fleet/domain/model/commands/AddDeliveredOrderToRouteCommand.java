package org.upc.fleetservice.fleet.domain.model.commands;

public record AddDeliveredOrderToRouteCommand (Long routeId, Long orderId) {
}
