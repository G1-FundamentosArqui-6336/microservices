package org.upc.deliveryservice.delivery.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.Objects;

@Embeddable
public class Address {
    @Getter private String line;
    @Getter private String city;
    @Getter private String country;
    @Getter private String postalCode;

    protected Address() {}
    public Address(String line, String city, String country, String postalCode) {
        if (line == null || line.isBlank()) throw new IllegalArgumentException("Dirección requerida");
        this.line = line;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
    }
    @Override public boolean equals(Object o){ return o instanceof Address a &&
            Objects.equals(line,a.line) && Objects.equals(city,a.city) &&
            Objects.equals(country,a.country) && Objects.equals(postalCode,a.postalCode);}
    @Override public int hashCode(){ return Objects.hash(line,city,country,postalCode); }
}