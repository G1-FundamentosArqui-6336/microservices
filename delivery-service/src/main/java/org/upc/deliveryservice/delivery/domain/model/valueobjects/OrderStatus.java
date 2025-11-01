package org.upc.deliveryservice.delivery.domain.model.valueobjects;

/**
 * Represents the lifecycle status of an Order.
 */
public enum OrderStatus {

    /**
     * The order has been received but no action has been taken yet.
     */
    RECEIVED,

    /**
     * The order is being prepared in the warehouse.
     */
    PROCESSING,

    /**
     * The order is ready and waiting to be assigned to a route.
     */
    READY_FOR_DISPATCH,

    /**
     * The order is part of a route that is currently in progress.
     */
    IN_TRANSIT,

    /**
     * The order has been successfully delivered to the customer.
     */
    DELIVERED,

    /**
     * The order has been cancelled by the customer or the system.
     */
    CANCELLED
}