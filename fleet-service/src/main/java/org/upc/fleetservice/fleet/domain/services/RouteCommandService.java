package org.upc.fleetservice.fleet.domain.services;

import org.upc.fleetservice.fleet.domain.model.commands.*;

public interface RouteCommandService {

    Long handle(CreateRouteCommand command);
    void handle(AddOrderToRouteCommand command);
    void handle(AssignDriverToRouteCommand command);
    void handle(AssignVehicleToRouteCommand command);
    void handle(AddDeliveredOrderToRouteCommand command);
    void handle(MarkAsInProgressRouteCommand command);
}
