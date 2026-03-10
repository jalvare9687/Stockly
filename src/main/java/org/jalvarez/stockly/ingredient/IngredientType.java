package org.jalvarez.stockly.ingredient;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ingredient_type")
@Setter
@Getter
public class IngredientType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false, length = 200)
    private String type;

    @Column(name="is_perishable", nullable = false)
    private Boolean isPerishable;


}
