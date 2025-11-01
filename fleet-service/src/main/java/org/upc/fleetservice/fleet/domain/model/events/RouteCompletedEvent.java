package org.upc.fleetservice.fleet.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public final class RouteCompletedEvent extends ApplicationEvent {

    private final Long routeId;
    private final Long driverId;
    private final Long vehicleId;

    public RouteCompletedEvent(Object source,Long routeId,Long driverId, Long vehicleId) {
        super(source);
        this.routeId = routeId;
        this.driverId = driverId;
        this.vehicleId = vehicleId;
    }

}