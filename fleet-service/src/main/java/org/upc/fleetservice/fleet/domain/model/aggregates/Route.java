package org.upc.fleetservice.fleet.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.Getter;
import org.upc.fleetservice.fleet.domain.exceptions.RouteNotInProgressException;
import org.upc.fleetservice.fleet.domain.exceptions.RouteNotPlannedException;
import org.upc.fleetservice.fleet.domain.model.commands.CreateRouteCommand;
import org.upc.fleetservice.fleet.domain.model.events.RouteCompletedEvent;
import org.upc.fleetservice.fleet.domain.model.valueobjects.OrderId;
import org.upc.fleetservice.fleet.domain.model.valueobjects.RouteStatus;
import org.upc.fleetservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Getter
public class Route extends AuditableAbstractAggregateRoot<Route> {



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    private RouteStatus routeStatus;

    private  String title;


    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "route_orders", joinColumns = @JoinColumn(name = "route_id"))
    @Column(name = "order_id")
    private List<OrderId> orderIds = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "route_finished_orders", joinColumns = @JoinColumn(name = "route_id"))
    @Column(name = "order_id")
    private Set<OrderId> finishedOrderIds = new HashSet<>();




    public Route() {
    }

    public Route(CreateRouteCommand command) {
        this.title = command.title();
        this.routeStatus = RouteStatus.PLANNED;
    }

    public void addOrder(OrderId orderId) {
        if (this.routeStatus != RouteStatus.IN_PROGRESS) {
            throw new RouteNotInProgressException(this.routeStatus);
        }

        if (!this.orderIds.contains(orderId)) {
            this.orderIds.add(orderId);
        }
    }

    public void addDeliveredOrder(OrderId orderId) {
        if (this.routeStatus != RouteStatus.IN_PROGRESS) {
            throw new RouteNotInProgressException(this.routeStatus);
        }
        this.finishedOrderIds.add(orderId);
        this.isValidToFinishedRoute();
    }

    public void assignDriver(Driver driver) {
        if (this.routeStatus != RouteStatus.PLANNED) {
            throw new RouteNotPlannedException(this.routeStatus);
        }
        this.driver = driver;
    }

    public void assignVehicle(Vehicle vehicle) {
        if (this.routeStatus != RouteStatus.PLANNED) {
            throw new RouteNotPlannedException(this.routeStatus);
        }
        this.vehicle = vehicle;
    }

    public void markAsInProgress() {
        if (this.routeStatus != RouteStatus.PLANNED) {
            throw new RouteNotPlannedException(this.routeStatus);
        }
        this.routeStatus = RouteStatus.IN_PROGRESS;
    }

    public void completeRoute() {
        if (this.routeStatus != RouteStatus.IN_PROGRESS) {
            throw new RouteNotInProgressException(this.routeStatus);
        }
        this.routeStatus = RouteStatus.COMPLETED;
        this.registerEvent(new RouteCompletedEvent(this, this.getId(), this.driver.getId(),this.vehicle.getId()));
    }

    public void isValidToFinishedRoute() {
        if (this.finishedOrderIds.size() == this.orderIds.size()) {
            this.completeRoute();
        }
    }


}