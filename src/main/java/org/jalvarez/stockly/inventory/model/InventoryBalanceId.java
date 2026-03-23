package org.jalvarez.stockly.inventory.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class InventoryBalanceId implements Serializable {

    private Long ingredientId;
    private Long locationId;
}
