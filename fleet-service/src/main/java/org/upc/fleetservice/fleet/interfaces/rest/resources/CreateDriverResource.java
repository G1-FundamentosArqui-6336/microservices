package org.upc.fleetservice.fleet.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateDriverResource(
        @NotBlank
        @Size(min = 9, max = 10)
        String licenceNumber) {
}
