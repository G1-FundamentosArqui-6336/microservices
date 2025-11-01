package org.upc.fleetservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.upc.fleetservice.fleet.application.internal.outboundservice.acl.rest.OrderIntegrationClient;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients(clients = OrderIntegrationClient.class)
public class FleetServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FleetServiceApplication.class, args);
    }

}
