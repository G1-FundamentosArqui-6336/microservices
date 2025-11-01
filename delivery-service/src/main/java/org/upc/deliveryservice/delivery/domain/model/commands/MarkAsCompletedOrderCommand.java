package org.upc.deliveryservice.delivery.domain.model.commands;

public record MarkAsCompletedOrderCommand(
        Long orderId,
        Long routeId,
        String photoUrl,
        String receiverName,
        String signatureData
) {
}
