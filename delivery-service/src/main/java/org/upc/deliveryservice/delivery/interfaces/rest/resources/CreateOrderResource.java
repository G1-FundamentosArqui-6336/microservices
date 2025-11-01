package org.upc.deliveryservice.delivery.interfaces.rest.resources;



public record CreateOrderResource(
        Long clientId,
        String addressLine,
        String city,
        String country,
        String postalCode,
        Double referenceLatitude,
        Double referenceLongitude,
        String notes,
        Double weightKg
) {}


