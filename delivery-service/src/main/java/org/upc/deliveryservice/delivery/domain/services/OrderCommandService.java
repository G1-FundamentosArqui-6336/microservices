package org.upc.deliveryservice.delivery.domain.services;

import org.upc.deliveryservice.delivery.domain.model.commands.CreateOrderCommand;
import org.upc.deliveryservice.delivery.domain.model.commands.MarkAsCompletedOrderCommand;
import org.upc.deliveryservice.delivery.domain.model.commands.MarkAsInTransitOrderCommand;
import org.upc.deliveryservice.delivery.domain.model.commands.MarkAsReadyForDispatchOrderCommand;


public interface OrderCommandService {
    Long handle(CreateOrderCommand command);
    void handle(MarkAsInTransitOrderCommand command);
    void handle(MarkAsReadyForDispatchOrderCommand command);
    void handle(MarkAsCompletedOrderCommand command);
}