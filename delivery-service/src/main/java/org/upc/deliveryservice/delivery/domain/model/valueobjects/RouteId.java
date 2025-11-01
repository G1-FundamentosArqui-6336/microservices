package org.upc.deliveryservice.delivery.domain.model.valueobjects;

import jakarta.persistence.Embeddable;


@Embeddable
public record RouteId(Long routeId) {


    public RouteId {
        if (routeId == null || routeId < 0) {
            throw new IllegalArgumentException("routeId cannot be null or negative.");
        }
    }

    public RouteId() {
        this(0L);
    }
}