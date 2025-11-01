package org.upc.deliveryservice.delivery.domain.model.aggregates;

import org.upc.deliveryservice.delivery.domain.exceptions.InvalidOrderStatusTransitionException;
import org.upc.deliveryservice.delivery.domain.model.commands.CreateOrderCommand;
import org.upc.deliveryservice.delivery.domain.model.events.OrderCompletedEvent;
import org.upc.deliveryservice.delivery.domain.model.valueobjects.*;
import jakarta.persistence.*;
import lombok.Getter;
import org.upc.deliveryservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

@Entity
@Getter
public class Order extends AuditableAbstractAggregateRoot<Order> {


    @Embedded
    private ClientId clientId;

    @Embedded
    private Address address;

    @Embedded
    private  Reference reference;

    private OrderStatus orderStatus;

    private String description;

    @Embedded
    @Getter
    private WeightKg weightKg;

    @Embedded
    @Getter
    private Evidence evidence;

    protected Order() {}

    public Order(CreateOrderCommand command) {
        this.clientId    = new ClientId(command.clientId());
        this.address     = new Address(command.addressLine(), command.city(), command.country(), command.postalCode());
        this.reference   = new Reference(command.referenceLatitude(), command.referenceLongitude());
        this.description       = command.description();
        this.weightKg = new WeightKg(command.weightKg());
        this.orderStatus =OrderStatus.RECEIVED;
    }

    public void markAsReadyForDispatch() {
        if (this.orderStatus != OrderStatus.RECEIVED) {
            throw new InvalidOrderStatusTransitionException(OrderStatus.RECEIVED,this.orderStatus);
        }
        this.orderStatus = OrderStatus.READY_FOR_DISPATCH;
    }


    public void markAsInTransit() {
        if (this.orderStatus != OrderStatus.READY_FOR_DISPATCH) {
            throw new InvalidOrderStatusTransitionException(OrderStatus.READY_FOR_DISPATCH,this.orderStatus);
        }
        this.orderStatus = OrderStatus.IN_TRANSIT;
    }

    public void markAsCompletedDelivery(String photoUrl, String receiverName, String signatureData, Long routeId) {
        if (this.orderStatus != OrderStatus.IN_TRANSIT) {
            throw new InvalidOrderStatusTransitionException(OrderStatus.IN_TRANSIT,this.orderStatus);
        }
        this.orderStatus = OrderStatus.DELIVERED;
        this.evidence = new Evidence(photoUrl, receiverName, signatureData);
        this.registerEvent(new OrderCompletedEvent(this,this.getId(), routeId));

    }

    public double getWeightValue() {
        return this.weightKg.getWeightKg();
    }



}
