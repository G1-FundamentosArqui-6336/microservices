package org.upc.fleetservice.fleet.domain.model.valueobjects;

public enum VehicleStatus {
    /**
     * The vehicle is available and ready for assignment.
     */
    OPERATIONAL,

    /**
     * The vehicle is currently assigned to an active route.
     */
    ON_ROUTE,

    /**
     * The vehicle is temporarily unavailable due to maintenance.
     */
    IN_MAINTENANCE,

    /**
     * The vehicle is not usable for an extended period (e.g., severe damage).
     */
    OUT_OF_SERVICE

}
