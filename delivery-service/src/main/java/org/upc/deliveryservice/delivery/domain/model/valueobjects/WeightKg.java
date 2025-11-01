package org.upc.deliveryservice.delivery.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.Objects;

@Embeddable
public class WeightKg {

    @Getter
    private double weightKg;

    protected WeightKg() {}

    public WeightKg(double weightKg) {
        if (weightKg < 0) {
            throw new IllegalArgumentException("Peso inválido");
        }
        this.weightKg = weightKg;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof WeightKg w && this.weightKg == w.weightKg;
    }

    @Override
    public int hashCode() {
        // 5. El método hashCode funciona igual gracias al autoboxing
        return Objects.hash(weightKg);
    }
}