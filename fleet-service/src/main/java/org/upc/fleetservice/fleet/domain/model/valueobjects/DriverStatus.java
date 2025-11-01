package org.upc.fleetservice.fleet.domain.model.valueobjects;

public enum DriverStatus {     /**
 * The driver is available and waiting for a route assignment.
 */
AVAILABLE,

    /**
     * The driver is currently executing a delivery route.
     */
    ON_ROUTE,

    /**
     * The driver is not available for assignments (e.g., day off, sick leave).
     */
    UNAVAILABLE,

    /**
     * The driver is on a scheduled break but may become available soon.
     */
    ON_BREAK

 }