package org.upc.fleetservice.fleet.interfaces.rest.resources;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AssignDriverResource(
        @NotNull(message = "driverId  cannot be null")
        @Positive(message = "driverId  must be a positive number")
        Long driverId) {
}

