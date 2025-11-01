package org.upc.deliveryservice.delivery.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class Evidence {

    private String photoUrl;
    private String receiverName;
    private String signatureData;

    public Evidence(String photoUrl, String receiverName, String signatureData) {
        this.photoUrl = photoUrl;
        this.receiverName = receiverName;
        this.signatureData = signatureData;
    }

    public boolean isValid() {
        return photoUrl != null && !photoUrl.isBlank();
    }
}