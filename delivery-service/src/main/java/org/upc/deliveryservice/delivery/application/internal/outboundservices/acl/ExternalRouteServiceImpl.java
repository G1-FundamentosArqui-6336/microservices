package org.upc.deliveryservice.delivery.application.internal.outboundservices.acl;

import org.springframework.stereotype.Service;
import org.upc.deliveryservice.delivery.application.internal.outboundservices.ExternalRouteService;
import org.upc.deliveryservice.delivery.application.internal.outboundservices.acl.resource.AddOrderDeliveredResource;
import org.upc.deliveryservice.delivery.application.internal.outboundservices.acl.rest.RouteIntegrationClient;


@Service
public class ExternalRouteServiceImpl implements ExternalRouteService {
    private final RouteIntegrationClient routeIntegrationClient;
    public ExternalRouteServiceImpl(RouteIntegrationClient routeIntegrationClient) {
        this.routeIntegrationClient = routeIntegrationClient;
    }

    @Override
    public void addDeliveredOrderToRoute(Long routeId, Long orderId){
        var payload=new AddOrderDeliveredResource(orderId);
        routeIntegrationClient.addDeliveredOrderToRoute(routeId,payload);
    }

}
