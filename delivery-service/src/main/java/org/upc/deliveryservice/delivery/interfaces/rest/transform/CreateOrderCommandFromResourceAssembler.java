package org.upc.deliveryservice.delivery.interfaces.rest.transform;


import org.upc.deliveryservice.delivery.domain.model.commands.CreateOrderCommand;
import org.upc.deliveryservice.delivery.interfaces.rest.resources.CreateOrderResource;

public class CreateOrderCommandFromResourceAssembler {

    public static CreateOrderCommand toCommandFromResource(CreateOrderResource resource) {
        return new CreateOrderCommand(
                resource.clientId(),
                resource.addressLine(),
                resource.city(),
                resource.country(),
                resource.postalCode(),
                resource.referenceLatitude(),
                resource.referenceLongitude(),
                resource.notes(),
                resource.weightKg()
        );
    }


}


