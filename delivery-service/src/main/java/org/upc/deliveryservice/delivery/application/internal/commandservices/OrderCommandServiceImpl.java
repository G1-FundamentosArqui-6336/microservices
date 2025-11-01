package org.upc.deliveryservice.delivery.application.internal.commandservices;


import org.springframework.stereotype.Service;
import org.upc.deliveryservice.delivery.domain.exceptions.OrderNotFoundException;
import org.upc.deliveryservice.delivery.domain.model.aggregates.Order;
import org.upc.deliveryservice.delivery.domain.model.commands.CreateOrderCommand;
import org.upc.deliveryservice.delivery.domain.model.commands.MarkAsCompletedOrderCommand;
import org.upc.deliveryservice.delivery.domain.model.commands.MarkAsInTransitOrderCommand;
import org.upc.deliveryservice.delivery.domain.model.commands.MarkAsReadyForDispatchOrderCommand;
import org.upc.deliveryservice.delivery.domain.services.OrderCommandService;
import org.upc.deliveryservice.delivery.infraestructure.persistence.jpa.repositories.OrderRepository;


@Service
public class OrderCommandServiceImpl implements OrderCommandService {

    private final OrderRepository orderRepository;
    public OrderCommandServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Long handle(CreateOrderCommand command) {
        var order = new Order(command);
        orderRepository.save(order);
        return order.getId();
    }

    @Override
    public void handle(MarkAsInTransitOrderCommand command) {
        orderRepository.findById(command.orderId()).map(order -> {
            order.markAsInTransit();
            orderRepository.save(order);
            return order.getId();
        }).orElseThrow(() -> new OrderNotFoundException(command.orderId()));
    }

    @Override
    public void handle(MarkAsReadyForDispatchOrderCommand command) {
        orderRepository.findById(command.orderId()).map(order -> {
            order.markAsReadyForDispatch();
            orderRepository.save(order);
            return order.getId();
        }).orElseThrow(() -> new OrderNotFoundException(command.orderId()));
    }

    @Override
    public void handle(MarkAsCompletedOrderCommand command) {
        orderRepository.findById(command.orderId()).map(order -> {
            order.markAsCompletedDelivery(
                    command.photoUrl(),
                    command.receiverName(),
                    command.signatureData(),
                    command.routeId()
            );
            orderRepository.save(order);
            return order.getId();
        }).orElseThrow(() -> new OrderNotFoundException(command.orderId()));
    }

}
