package org.jalvarez.stockly.ingredient.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
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
