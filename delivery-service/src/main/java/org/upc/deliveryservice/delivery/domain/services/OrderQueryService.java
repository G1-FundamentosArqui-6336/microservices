package org.upc.deliveryservice.delivery.domain.services;

import org.upc.deliveryservice.delivery.domain.model.aggregates.Order;
import org.upc.deliveryservice.delivery.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface OrderQueryService {
    List<Order> handle(GetOrdersByClientIdQuery query);
    List<Order> handle(GetAllOrdersQuery query);
    Optional<Order> handle(GetOrderByIdQuery query);
}
