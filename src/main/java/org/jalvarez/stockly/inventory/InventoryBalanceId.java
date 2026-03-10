package org.jalvarez.stockly.inventory;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.*;
import org.jalvarez.stockly.ingredient.Ingredient;

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
