package org.upc.deliveryservice.delivery.interfaces.rest;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.upc.deliveryservice.delivery.domain.model.queries.GetOrdersByClientIdQuery;
import org.upc.deliveryservice.delivery.domain.services.OrderCommandService;
import org.upc.deliveryservice.delivery.domain.services.OrderQueryService;
import org.upc.deliveryservice.delivery.interfaces.rest.resources.OrderResource;
import org.upc.deliveryservice.delivery.interfaces.rest.transform.OrderResourceFromEntityAssembler;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/client/{clientId}/orders", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Clients")
public class ClientOrderController {

    private final OrderCommandService orderCommandService;
    private final OrderQueryService orderQueryService;

    public ClientOrderController(
            OrderCommandService orderCommandService,
            OrderQueryService orderQueryService) {
        this.orderCommandService = orderCommandService;
        this.orderQueryService = orderQueryService;
    }

    /**
     * Obtiene todas las órdenes para un cliente específico.
     * Corresponde a handle(GetOrdersByClientIdQuery).
     */
    @GetMapping(params = "clientId")
    public ResponseEntity<List<OrderResource>> getOrdersByClientId(@PathVariable Long clientId) {
        var getOrdersByClientIdQuery = new GetOrdersByClientIdQuery(clientId);
        var orders = orderQueryService.handle(getOrdersByClientIdQuery);
        var orderResources = orders.stream()
                .map(OrderResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(orderResources);
    }
}
