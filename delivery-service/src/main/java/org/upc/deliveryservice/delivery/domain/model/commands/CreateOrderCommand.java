package org.upc.deliveryservice.delivery.domain.model.commands;

public record CreateOrderCommand (Long clientId,
                                  String addressLine,
                                  String city,
                                  String country,
                                  String postalCode,
                                  Double referenceLatitude,
                                  Double referenceLongitude,
                                  String description,
                                  Double weightKg){
}
