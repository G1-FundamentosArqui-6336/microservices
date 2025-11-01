package org.upc.fleetservice.fleet.application.internal.outboundservice;


import org.upc.fleetservice.fleet.application.internal.outboundservice.acl.rest.resource.OrderResource;

public interface ExternalOrderService {
    OrderResource fetchOrderById(Long orderId);
    void markAsInTransitOrderById(Long orderId);

}
