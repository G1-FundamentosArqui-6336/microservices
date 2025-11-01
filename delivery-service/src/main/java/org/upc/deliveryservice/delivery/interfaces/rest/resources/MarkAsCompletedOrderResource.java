package org.upc.deliveryservice.delivery.interfaces.rest.resources;

public record MarkAsCompletedOrderResource(
                                                  Long routeId,
                                                  String photoUrl,
                                                  String receiverName,
                                                  String signatureData) {
}
