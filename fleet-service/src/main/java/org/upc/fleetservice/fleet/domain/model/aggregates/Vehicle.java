package org.upc.fleetservice.fleet.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.Getter;
import org.upc.fleetservice.fleet.domain.exceptions.InvalidVehicleStateTransitionException;
import org.upc.fleetservice.fleet.domain.exceptions.VehicleNotInRouteException;
import org.upc.fleetservice.fleet.domain.exceptions.VehicleNotOperationalException;
import org.upc.fleetservice.fleet.domain.model.commands.CreateVehicleCommand;
import org.upc.fleetservice.fleet.domain.model.valueobjects.VehicleStatus;
import org.upc.fleetservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

@Entity
@Getter
public class Vehicle extends AuditableAbstractAggregateRoot<Vehicle>{


    private String plateNumber;
    private Double capacityKg;
    private VehicleStatus vehicleStatus;

    public Vehicle() {
    }
    public Vehicle(CreateVehicleCommand command) {
        this.plateNumber = command.plateNumber();
        this.capacityKg = command.capacityKg();
        this.vehicleStatus = VehicleStatus.OPERATIONAL;
    }


    public void markAsInRoute() {
        if (this.vehicleStatus != VehicleStatus.OPERATIONAL) {
            throw new VehicleNotOperationalException(this.vehicleStatus);
        }
        this.vehicleStatus = VehicleStatus.ON_ROUTE;
    }


    public boolean hasCapacityFor(double totalWeightKg) {
        return totalWeightKg <= capacityKg;
    }


    /**
     * Frees the vehicle upon route completion.
     */
    public void returnFromRoute() {
        if (this.vehicleStatus != VehicleStatus.ON_ROUTE) throw new VehicleNotInRouteException(this.vehicleStatus);
        this.vehicleStatus = VehicleStatus.OPERATIONAL;
    }

    /**
     * Marks the vehicle as unavailable for routes due to maintenance.
     */
    public void sendToMaintenance() {
        this.vehicleStatus = VehicleStatus.IN_MAINTENANCE;
    }

    /**
     * Puts the vehicle back into service after maintenance.
     */
    public void markAsOperational() {
        if (this.vehicleStatus == VehicleStatus.IN_MAINTENANCE || this.vehicleStatus == VehicleStatus.OUT_OF_SERVICE) {
            this.vehicleStatus = VehicleStatus.OPERATIONAL;
        } else {
            throw new InvalidVehicleStateTransitionException(this.vehicleStatus);
        }
    }

}
