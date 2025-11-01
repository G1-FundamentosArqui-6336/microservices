package org.upc.fleetservice.fleet.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddOrderResource(
        @NotNull(message = "orderId cannot be null")
        @Positive(message = "orderId must be a positive number")
        Long orderId) {
}
