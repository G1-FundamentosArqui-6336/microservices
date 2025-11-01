package org.upc.fleetservice.fleet.domain.model.valueobjects;

import jakarta.persistence.Embeddable;


@Embeddable
public record OrderId(Long orderId) {

    public OrderId {
        if (orderId == null || orderId < 0) {
            throw new IllegalArgumentException("Order ID cannot be null or negative.");
        }
    }

    public OrderId() {
        this(0L);
    }
}