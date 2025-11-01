package org.upc.deliveryservice.delivery.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.upc.deliveryservice.delivery.domain.model.commands.MarkAsInTransitOrderCommand;
import org.upc.deliveryservice.delivery.domain.model.commands.MarkAsReadyForDispatchOrderCommand;
import org.upc.deliveryservice.delivery.domain.model.queries.GetAllOrdersQuery;
import org.upc.deliveryservice.delivery.domain.model.queries.GetOrderByIdQuery;
import org.upc.deliveryservice.delivery.domain.services.OrderCommandService;
import org.upc.deliveryservice.delivery.domain.services.OrderQueryService;
import org.upc.deliveryservice.delivery.interfaces.rest.resources.*;
import org.upc.deliveryservice.delivery.interfaces.rest.transform.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/orders", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Orders", description = "Order Management Endpoints")
public class OrderController {

    private final OrderCommandService orderCommandService;
    private final OrderQueryService orderQueryService;

    public OrderController(
            OrderCommandService orderCommandService,
            OrderQueryService orderQueryService) {
        this.orderCommandService = orderCommandService;
        this.orderQueryService = orderQueryService;
    }

    @Operation(summary = "Create a new order",
            description = "Creates a new order with the provided data and returns the created order's details.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Order created successfully",
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = OrderResource.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data",
                            content = @Content(mediaType = APPLICATION_JSON_VALUE))
            })
    @PostMapping
    public ResponseEntity<OrderResource> createOrder(@RequestBody CreateOrderResource resource) {
        var createOrderCommand = CreateOrderCommandFromResourceAssembler.toCommandFromResource(resource);
        var orderId = orderCommandService.handle(createOrderCommand);
        if (orderId == 0L) {
            return ResponseEntity.badRequest().build();
        }
        var getOrderByIdQuery = new GetOrderByIdQuery(orderId);
        var order = orderQueryService.handle(getOrderByIdQuery);
        if (order.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var orderResource = OrderResourceFromEntityAssembler.toResourceFromEntity(order.get());
        return new ResponseEntity<>(orderResource, HttpStatus.CREATED);
    }

    @Operation(summary = "Mark an order as ready for dispatch",
            description = "Updates the status of a specific order to 'READY_FOR_DISPATCH'.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order status updated",
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = OrderResource.class))),
                    @ApiResponse(responseCode = "404", description = "Order not found",
                            content = @Content(mediaType = APPLICATION_JSON_VALUE))
            })
    @PatchMapping("/{orderId}/ready-for-dispatch")
    public ResponseEntity<OrderResource> markOrderAsReadyForDispatch(@PathVariable Long orderId) {
        var command = new MarkAsReadyForDispatchOrderCommand(orderId);
        orderCommandService.handle(command);
        var order = orderQueryService.handle(new GetOrderByIdQuery(orderId));
        if (order.isEmpty()) return ResponseEntity.notFound().build();
        var orderResource = OrderResourceFromEntityAssembler.toResourceFromEntity(order.get());
        return ResponseEntity.ok(orderResource);
    }


    @Operation(summary = "Mark an order as in transit",
            description = "Updates the status of a specific order to 'IN_TRANSIT'.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order status updated",
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = OrderResource.class))),
                    @ApiResponse(responseCode = "404", description = "Order not found",
                            content = @Content(mediaType = APPLICATION_JSON_VALUE))
            })
    @PatchMapping("/{orderId}/in-transit")
    public ResponseEntity<OrderResource> markOrderAsInTransit(@PathVariable Long orderId) {
        var command = new MarkAsInTransitOrderCommand(orderId);
        orderCommandService.handle(command);
        var order = orderQueryService.handle(new GetOrderByIdQuery(orderId));
        if (order.isEmpty()) return ResponseEntity.notFound().build();
        var orderResource = OrderResourceFromEntityAssembler.toResourceFromEntity(order.get());
        return ResponseEntity.ok(orderResource);
    }

    @Operation(summary = "Mark an order as completed",
            description = "Updates the status of a specific order to 'COMPLETED' using an evidence ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order status updated",
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = OrderResource.class))),
                    @ApiResponse(responseCode = "404", description = "Order not found",
                            content = @Content(mediaType = APPLICATION_JSON_VALUE))
            })
    @PatchMapping("/{orderId}/completed")
    public ResponseEntity<OrderResource> markOrderAsCompleted(@PathVariable Long orderId, @RequestBody MarkAsCompletedOrderResource resource) {
        var command = MarkAsCompletedOrderCommandFromResourceAssembler.toCommandFromResource(orderId, resource);
        orderCommandService.handle(command);
        var order = orderQueryService.handle(new GetOrderByIdQuery(orderId));
        if (order.isEmpty()) return ResponseEntity.notFound().build();
        var orderResource = OrderResourceFromEntityAssembler.toResourceFromEntity(order.get());
        return ResponseEntity.ok(orderResource);
    }

    @Operation(summary = "Get an order by its ID",
            description = "Retrieves the details of a single order by its unique identifier.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order found",
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = OrderResource.class))),
                    @ApiResponse(responseCode = "404", description = "Order not found",
                            content = @Content(mediaType = APPLICATION_JSON_VALUE))
            })
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResource> getOrderById(@PathVariable Long orderId) {
        var getOrderByIdQuery = new GetOrderByIdQuery(orderId);
        var order = orderQueryService.handle(getOrderByIdQuery);
        if (order.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var orderResource = OrderResourceFromEntityAssembler.toResourceFromEntity(order.get());
        return ResponseEntity.ok(orderResource);
    }

    @Operation(summary = "Get all orders",
            description = "Retrieves a list of all existing orders.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of orders",
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = OrderResource.class))))
            })
    @GetMapping
    public ResponseEntity<List<OrderResource>> getAllOrders() {
        var getAllOrdersQuery = new GetAllOrdersQuery();
        var orders = orderQueryService.handle(getAllOrdersQuery);
        var orderResources = orders.stream()
                .map(OrderResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(orderResources);
    }
}