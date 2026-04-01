package org.jalvarez.stockly.location.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LocationSupplierId implements Serializable {

    private Long supplierId;
    private Long locationId;
}