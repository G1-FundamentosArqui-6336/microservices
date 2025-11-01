package org.upc.deliveryservice.delivery.domain.model.events;


import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public final class OrderCompletedEvent extends ApplicationEvent {

    private final Long orderId;

    private final Long routeId;

    public OrderCompletedEvent(Object source,Long orderId, Long routeId) {
        super(source);
        this.orderId = orderId;
        this.routeId = routeId;
    }

}