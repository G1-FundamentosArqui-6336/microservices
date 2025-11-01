package org.upc.deliveryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.upc.deliveryservice.delivery.application.internal.outboundservices.acl.rest.RouteIntegrationClient;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients(clients = RouteIntegrationClient.class)
public class DeliveryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeliveryServiceApplication.class, args);
    }

}
