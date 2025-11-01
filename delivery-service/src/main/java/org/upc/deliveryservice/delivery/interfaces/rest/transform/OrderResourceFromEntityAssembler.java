package org.upc.deliveryservice.delivery.interfaces.rest.transform;
import org.upc.deliveryservice.delivery.domain.model.aggregates.Order;
import org.upc.deliveryservice.delivery.interfaces.rest.resources.OrderResource;

public class OrderResourceFromEntityAssembler {

    public static OrderResource toResourceFromEntity(Order entity) {
        return new OrderResource(
                entity.getId(),
                entity.getClientId().clientId(),
                entity.getAddress().getLine(),
                entity.getAddress().getCity(),
                entity.getAddress().getCountry(),
                entity.getAddress().getPostalCode(),
                entity.getReference().getLatitude(),
                entity.getReference().getLongitude(),
                entity.getDescription(),
                entity.getWeightValue(),
                entity.getOrderStatus().name()
            );
    }
}
