package org.upc.fleetservice.fleet.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateVehicleResource(
        @NotBlank
        @Size(min = 2, max = 50)
        String plateNumber,
        @NotNull(message = "Capacity cannot be null")
        @Positive(message = "Capacity (kg) must be a positive number")
        Double capacityKg
) {}