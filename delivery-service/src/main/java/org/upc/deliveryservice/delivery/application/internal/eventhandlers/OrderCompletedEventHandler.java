package org.upc.deliveryservice.delivery.application.internal.eventhandlers;


import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.upc.deliveryservice.delivery.application.internal.outboundservices.ExternalRouteService;
import org.upc.deliveryservice.delivery.domain.model.events.OrderCompletedEvent;
import org.upc.deliveryservice.delivery.domain.model.queries.GetOrderByIdQuery;
import org.upc.deliveryservice.delivery.domain.services.OrderQueryService;

@Service
public class OrderCompletedEventHandler {

    private final OrderQueryService orderQueryService;
    private final ExternalRouteService externalRouteService;


    public OrderCompletedEventHandler(OrderQueryService orderQueryService, ExternalRouteService externalRouteService) {
        this.orderQueryService = orderQueryService;
        this.externalRouteService=externalRouteService;
    }


    @EventListener(OrderCompletedEvent.class)
    public void on(OrderCompletedEvent event) {

        var getOrderByIdQuery = new GetOrderByIdQuery(event.getOrderId());
        var order = orderQueryService.handle(getOrderByIdQuery);
        if (order.isPresent()) {
            externalRouteService.addDeliveredOrderToRoute(event.getRouteId(),event.getOrderId());
        }
        System.out.println("OrderCompletedEventHandler executed");
    }
}
