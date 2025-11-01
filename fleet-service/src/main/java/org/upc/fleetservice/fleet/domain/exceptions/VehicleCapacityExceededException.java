package org.upc.fleetservice.fleet.domain.exceptions;

public class VehicleCapacityExceededException extends RuntimeException {
    public VehicleCapacityExceededException(Double totalWeightKg,Double capacityKg) {
        super("Vehicle does not have enough capacity. Required: " + totalWeightKg + "kg, Available: " + capacityKg + "kg.");
    }
}