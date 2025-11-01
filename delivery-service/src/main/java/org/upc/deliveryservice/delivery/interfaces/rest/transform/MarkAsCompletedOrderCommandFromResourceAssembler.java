package org.upc.deliveryservice.delivery.interfaces.rest.transform;

import org.upc.deliveryservice.delivery.domain.model.commands.MarkAsCompletedOrderCommand;
import org.upc.deliveryservice.delivery.interfaces.rest.resources.MarkAsCompletedOrderResource;


public class MarkAsCompletedOrderCommandFromResourceAssembler {

    public static MarkAsCompletedOrderCommand toCommandFromResource(Long orderId, MarkAsCompletedOrderResource resource) {
        return new MarkAsCompletedOrderCommand(
                orderId,
                resource.routeId(),
                resource.photoUrl(),
                resource.receiverName(),
                resource.signatureData()
        );
    }
}