package org.upc.fleetservice.fleet.domain.services;

import org.upc.fleetservice.fleet.domain.model.commands.CreateDriverCommand;
import org.upc.fleetservice.fleet.domain.model.commands.UpdateDriverStatusOnCompletedRouteCommand;

public interface DriverCommandService {
    Long handle(CreateDriverCommand command);
    void handle(UpdateDriverStatusOnCompletedRouteCommand command);
}
