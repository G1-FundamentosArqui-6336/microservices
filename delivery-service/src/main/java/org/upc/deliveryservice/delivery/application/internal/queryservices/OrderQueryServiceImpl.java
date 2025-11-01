package org.upc.deliveryservice.delivery.application.internal.queryservices;

import org.springframework.stereotype.Service;
import org.upc.deliveryservice.delivery.domain.model.aggregates.Order;
import org.upc.deliveryservice.delivery.domain.model.queries.*;
import org.upc.deliveryservice.delivery.domain.model.valueobjects.ClientId;
import org.upc.deliveryservice.delivery.domain.services.OrderQueryService;
import org.upc.deliveryservice.delivery.infraestructure.persistence.jpa.repositories.OrderRepository;

import java.util.List;
import java.util.Optional;

@Service
public class OrderQueryServiceImpl implements OrderQueryService {

    private final OrderRepository orderRepository;

    public OrderQueryServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    @Override
    public List<Order> handle(GetOrdersByClientIdQuery query) {
        var clientId = new ClientId(query.clientId());
        return orderRepository.findByClientId(clientId);
    }


    @Override
    public List<Order> handle(GetAllOrdersQuery query) {
        return orderRepository.findAll();
    }
    @Override
    public Optional<Order> handle(GetOrderByIdQuery query) {
        return orderRepository.findById(query.orderId());
    }


}
