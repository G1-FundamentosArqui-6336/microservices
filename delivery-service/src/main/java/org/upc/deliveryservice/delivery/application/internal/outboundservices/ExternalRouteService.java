package org.upc.deliveryservice.delivery.application.internal.outboundservices;


public interface ExternalRouteService {
    void addDeliveredOrderToRoute(Long orderId,Long routeId);

}
