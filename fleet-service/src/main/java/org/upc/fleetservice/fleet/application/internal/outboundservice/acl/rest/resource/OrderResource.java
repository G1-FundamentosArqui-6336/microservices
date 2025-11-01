package org.upc.fleetservice.fleet.application.internal.outboundservice.acl.rest.resource;

public record OrderResource(
        Long id,
        Long clientId,
        String addressLine,
        String city,
        String country,
        String postalCode,
        Double referenceLatitude,
        Double referenceLongitude,
        String notes,
        Double weightKg,
        String orderStatus
) {}