package org.upc.fleetservice.fleet.application.internal.outboundservice.acl;

import org.springframework.stereotype.Service;
import org.upc.fleetservice.fleet.application.internal.outboundservice.ExternalOrderService;
import org.upc.fleetservice.fleet.application.internal.outboundservice.acl.rest.OrderIntegrationClient;
import org.upc.fleetservice.fleet.application.internal.outboundservice.acl.rest.resource.OrderResource;

@Service
public class ExternalOrderServiceImpl implements ExternalOrderService {
    private final OrderIntegrationClient orderIntegrationClient;

    public ExternalOrderServiceImpl(OrderIntegrationClient orderIntegrationClient) {
        this.orderIntegrationClient = orderIntegrationClient;
    }


    @Override
    public OrderResource fetchOrderById(Long orderId) {
        OrderResource ext = orderIntegrationClient.getOrderById(orderId);
        return new OrderResource(
                ext.id(),
                ext.clientId(),
                ext.addressLine(),
                ext.city(),
                ext.country(),
                ext.postalCode(),
                ext.referenceLatitude(),
                ext.referenceLongitude(),
                ext.notes(),
                ext.weightKg(),
                ext.orderStatus()
        );
    }
    @Override
    public void markAsInTransitOrderById(Long orderId) {
        orderIntegrationClient.markInTransit(orderId);
    }


}
