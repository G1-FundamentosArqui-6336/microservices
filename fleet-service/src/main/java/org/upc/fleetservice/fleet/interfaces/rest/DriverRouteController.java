package org.upc.fleetservice.fleet.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.upc.fleetservice.fleet.domain.model.queries.GetAllRoutesByDriverId;
import org.upc.fleetservice.fleet.domain.services.RouteQueryService;
import org.upc.fleetservice.fleet.interfaces.rest.resources.RouteResource;
import org.upc.fleetservice.fleet.interfaces.rest.transform.RouteResourceFromEntityAssembler;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/drivers/{driverId}/routes", produces = APPLICATION_JSON_VALUE) // Corrected path for clarity
@Tag(name = "Drivers")
public class DriverRouteController {

    private final RouteQueryService routeQueryService;

    public DriverRouteController(RouteQueryService routeQueryService) {
        this.routeQueryService = routeQueryService;
    }

    @Operation(summary = "Get all routes for a specific driver",
            description = "Retrieves a list of all routes that have been assigned to a specific driver by their unique identifier.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of routes retrieved successfully",
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = RouteResource.class)))),
                    @ApiResponse(responseCode = "404", description = "Driver not found",
                            content = @Content(mediaType = APPLICATION_JSON_VALUE))
            })
    @GetMapping
    public ResponseEntity<List<RouteResource>> getRoutesByDriverId(@PathVariable Long driverId) {
        var getAllRoutesByDriverIdQuery = new GetAllRoutesByDriverId(driverId);
        var routes = routeQueryService.handle(getAllRoutesByDriverIdQuery);
        var routeResources = routes.stream()
                .map(RouteResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(routeResources);
    }
}