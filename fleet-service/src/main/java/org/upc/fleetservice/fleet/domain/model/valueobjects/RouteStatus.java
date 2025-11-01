package org.upc.fleetservice.fleet.domain.model.valueobjects;

public enum RouteStatus {
    /**
     * The route has been created and assigned orders, but has not started yet.
     */
    PLANNED,

    /**
     * The driver has started the delivery route.
     */
    IN_PROGRESS,

    /**
     * All orders on the route have been delivered.
     */
    COMPLETED,

    /**
     * The route was cancelled before it could be completed.
     */
    CANCELLED
}
