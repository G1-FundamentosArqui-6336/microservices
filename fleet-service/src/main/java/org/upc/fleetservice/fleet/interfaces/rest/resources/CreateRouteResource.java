package org.upc.fleetservice.fleet.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateRouteResource(
        @NotBlank
        @Size(min = 2, max = 50)
        String title){
}
