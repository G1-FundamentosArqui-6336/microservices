package org.upc.deliveryservice.delivery.domain.exceptions;

import org.upc.deliveryservice.delivery.domain.model.valueobjects.OrderStatus;

public class InvalidOrderStatusTransitionException extends RuntimeException {

    public InvalidOrderStatusTransitionException(OrderStatus expectedStatus, OrderStatus actualStatus) {
        super("Invalid order status transition. Expected: " + expectedStatus + ", but got: " + actualStatus);
    }

}