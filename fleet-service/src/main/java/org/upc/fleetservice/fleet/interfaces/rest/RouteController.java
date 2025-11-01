package org.upc.fleetservice.fleet.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.upc.fleetservice.fleet.domain.model.commands.*;
import org.upc.fleetservice.fleet.domain.model.queries.GetAllRoutesQuery;
import org.upc.fleetservice.fleet.domain.model.queries.GetRouteByIdQuery;
import org.upc.fleetservice.fleet.domain.services.RouteCommandService;
import org.upc.fleetservice.fleet.domain.services.RouteQueryService;
import org.upc.fleetservice.fleet.interfaces.rest.resources.*;
import org.upc.fleetservice.fleet.interfaces.rest.transform.CreateRouteCommandFromResourceAssembler;
import org.upc.fleetservice.fleet.interfaces.rest.transform.RouteResourceFromEntityAssembler;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/routes", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Routes", description = "Routes Management Endpoints") // Corrected Tag name to plural for consistency
public class RouteController {
    private final RouteCommandService routeCommandService;
    private final RouteQueryService routeQueryService;

    public RouteController(RouteCommandService routeCommandService, RouteQueryService routeQueryService) {
        this.routeCommandService = routeCommandService;
        this.routeQueryService = routeQueryService;
    }

    @Operation(summary = "Create a new route",
            description = "Creates a new route with the provided data and returns the created route's details.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Route created successfully",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = RouteResource.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data provided",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
            })
    @PostMapping
    public ResponseEntity<RouteResource> createRoute(@Valid @RequestBody CreateRouteResource resource) {
        var createRouteCommand = CreateRouteCommandFromResourceAssembler.toCommandFromResource(resource);
        var routeId = routeCommandService.handle(createRouteCommand);
        var route = routeQueryService.handle(new GetRouteByIdQuery(routeId));
        if (route.isEmpty()) return ResponseEntity.badRequest().build();
        var routeResource = RouteResourceFromEntityAssembler.toResourceFromEntity(route.get());
        return new ResponseEntity<>(routeResource, HttpStatus.CREATED);
    }

    @Operation(summary = "Add an order to a route",
            description = "Adds an existing order to a specific route.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order added to route successfully",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = RouteResource.class))),
                    @ApiResponse(responseCode = "404", description = "Route or Order not found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
            })
    @PostMapping("/{routeId}/orders")
    public ResponseEntity<RouteResource> addOrderToRoute(@PathVariable Long routeId,@Valid @RequestBody AddOrderResource resource) {
        var command = new AddOrderToRouteCommand(routeId, resource.orderId());
        routeCommandService.handle(command);
        var route = routeQueryService.handle(new GetRouteByIdQuery(routeId));
        if (route.isEmpty()) return ResponseEntity.notFound().build();
        var routeResource = RouteResourceFromEntityAssembler.toResourceFromEntity(route.get());
        return ResponseEntity.ok(routeResource);
    }


    @Operation(summary = "Add an delivered order to a route",
            description = "Adds an existing delivered order to a specific route.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order delivered added to route successfully",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = RouteResource.class))),
                    @ApiResponse(responseCode = "404", description = "Route or Order not found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
            })
    @PostMapping("/{routeId}/delivered-orders")
    public ResponseEntity<RouteResource> addDeliveredOrderToRoute(@PathVariable Long routeId,@Valid @RequestBody AddOrderDeliveredResource resource) {
        var command = new AddDeliveredOrderToRouteCommand(routeId, resource.orderId());
        routeCommandService.handle(command);
        var route = routeQueryService.handle(new GetRouteByIdQuery(routeId));
        if (route.isEmpty()) return ResponseEntity.notFound().build();
        var routeResource = RouteResourceFromEntityAssembler.toResourceFromEntity(route.get());
        return ResponseEntity.ok(routeResource);
    }

    @Operation(summary = "Assign a driver to a route",
            description = "Assigns an existing driver to a specific route.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Driver assigned successfully",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = RouteResource.class))),
                    @ApiResponse(responseCode = "404", description = "Route or Driver not found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
            })
    @PatchMapping("/{routeId}/driver")
    public ResponseEntity<RouteResource> assignDriverToRoute(@PathVariable Long routeId,@Valid @RequestBody AssignDriverResource resource) {
        var command = new AssignDriverToRouteCommand(routeId, resource.driverId());
        routeCommandService.handle(command);
        var route = routeQueryService.handle(new GetRouteByIdQuery(routeId));
        if (route.isEmpty()) return ResponseEntity.notFound().build();
        var routeResource = RouteResourceFromEntityAssembler.toResourceFromEntity(route.get());
        return ResponseEntity.ok(routeResource);
    }

    @Operation(summary = "Assign a vehicle to a route",
            description = "Assigns an existing vehicle to a specific route.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Vehicle assigned successfully",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = RouteResource.class))),
                    @ApiResponse(responseCode = "404", description = "Route or Vehicle not found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
            })
    @PatchMapping("/{routeId}/vehicle")
    public ResponseEntity<RouteResource> assignVehicleToRoute(@PathVariable Long routeId,@Valid @RequestBody AssignVehicleResource resource) {
        var command = new AssignVehicleToRouteCommand(routeId, resource.vehicleId());
        routeCommandService.handle(command);
        var route = routeQueryService.handle(new GetRouteByIdQuery(routeId));
        if (route.isEmpty()) return ResponseEntity.notFound().build();
        var routeResource = RouteResourceFromEntityAssembler.toResourceFromEntity(route.get());
        return ResponseEntity.ok(routeResource);
    }

    @Operation(summary = "Get a route by its ID",
            description = "Retrieves the details of a single route by its unique identifier.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Route found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = RouteResource.class))),
                    @ApiResponse(responseCode = "404", description = "Route not found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
            })
    @GetMapping("/{routeId}")
    public ResponseEntity<RouteResource> getRouteById(@PathVariable Long routeId) {
        var route = routeQueryService.handle(new GetRouteByIdQuery(routeId));
        if (route.isEmpty()) return ResponseEntity.notFound().build();
        var routeResource = RouteResourceFromEntityAssembler.toResourceFromEntity(route.get());
        return ResponseEntity.ok(routeResource);
    }

    @Operation(summary = "Get all routes",
            description = "Retrieves a list of all existing routes.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of routes retrieved successfully",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = RouteResource.class))))
            })
    @GetMapping
    public ResponseEntity<List<RouteResource>> getAllRoutes() {
        var getAllRoutesQuery = new GetAllRoutesQuery();
        var routes = routeQueryService.handle(getAllRoutesQuery);
        var routeResources = routes.stream()
                .map(RouteResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(routeResources);
    }


    @Operation(summary = "Mark an route as in progress",
            description = "Updates the status of a specific route to 'IN_PROGRESS'.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Route status updated",
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = RouteResource.class))),
                    @ApiResponse(responseCode = "404", description = "Route not found",
                            content = @Content(mediaType = APPLICATION_JSON_VALUE))
            })
    @PatchMapping("/{routeId}/in-progress")
    public ResponseEntity<RouteResource> markRouteAsInProgress(@PathVariable Long routeId) {
        var command = new MarkAsInProgressRouteCommand(routeId);
        routeCommandService.handle(command);
        var route = routeQueryService.handle(new GetRouteByIdQuery(routeId));
        if (route.isEmpty()) return ResponseEntity.notFound().build();
        var routeResource = RouteResourceFromEntityAssembler.toResourceFromEntity(route.get());
        return ResponseEntity.ok(routeResource);
    }

}