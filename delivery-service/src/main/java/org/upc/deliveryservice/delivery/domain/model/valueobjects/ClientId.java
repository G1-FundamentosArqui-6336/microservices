package org.upc.deliveryservice.delivery.domain.model.valueobjects;

import jakarta.persistence.Embeddable;



@Embeddable
public record ClientId(Long clientId) {


    public ClientId {
        if (clientId == null || clientId < 0) {
            throw new IllegalArgumentException("Client ID cannot be null or negative.");
        }
    }


    public ClientId() {
        this(0L);
    }
}