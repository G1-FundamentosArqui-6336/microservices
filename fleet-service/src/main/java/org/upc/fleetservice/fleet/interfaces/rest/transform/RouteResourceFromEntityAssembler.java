package org.upc.fleetservice.fleet.interfaces.rest.transform;
import org.upc.fleetservice.fleet.domain.model.aggregates.Route;
import org.upc.fleetservice.fleet.interfaces.rest.resources.RouteResource;

import java.util.List;


public class RouteResourceFromEntityAssembler {
    public static RouteResource toResourceFromEntity(Route entity){
        return new RouteResource(
                entity.getId(),
                entity.getTitle(),
                entity.getVehicle() != null ? entity.getVehicle().getId() : null,
                entity.getDriver() != null ? entity.getDriver().getId() : null,
                entity.getOrderIds(),
                entity.getFinishedOrderIds(),
                entity.getRouteStatus().name()
        );
    }
}
