package org.upc.fleetservice.fleet.interfaces.rest.transform;


import org.upc.fleetservice.fleet.domain.model.commands.CreateRouteCommand;
import org.upc.fleetservice.fleet.interfaces.rest.resources.CreateRouteResource;

public class CreateRouteCommandFromResourceAssembler {
    public static CreateRouteCommand toCommandFromResource(CreateRouteResource resource){
        return new CreateRouteCommand(
                resource.title()
        );
    }
}
