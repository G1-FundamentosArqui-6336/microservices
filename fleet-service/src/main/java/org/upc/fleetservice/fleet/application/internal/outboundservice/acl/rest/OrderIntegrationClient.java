package org.upc.fleetservice.fleet.application.internal.outboundservice.acl.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.cloud.openfeign.FeignClient;
import org.upc.fleetservice.fleet.application.internal.outboundservice.acl.rest.resource.OrderResource;

@FeignClient(name = "delivery-service")
public interface OrderIntegrationClient {

    @GetMapping("/api/v1/orders/{orderId}")
    OrderResource getOrderById(@PathVariable  Long orderId);

    @PatchMapping("/api/v1/orders/{orderId}/in-transit")
    void markInTransit(@PathVariable Long orderId);
}
