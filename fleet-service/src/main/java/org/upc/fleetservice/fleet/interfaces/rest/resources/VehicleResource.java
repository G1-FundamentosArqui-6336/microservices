package org.upc.fleetservice.fleet.interfaces.rest.resources;

import java.math.BigDecimal;

public record VehicleResource(
        Long id,
        String plateNumber,
        Double capacityKg,
        String vehicleStatus
) {}
