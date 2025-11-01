package org.upc.fleetservice.fleet.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AssignVehicleResource (
        @NotNull(message = "vehicleId  cannot be null")
        @Positive(message = "vehicleId  must be a positive number")
        Long vehicleId) {
}
