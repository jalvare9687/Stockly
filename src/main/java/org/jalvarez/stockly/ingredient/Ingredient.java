package org.jalvarez.stockly.ingredient;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jalvarez.stockly.enums.UnitEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="ingredient", uniqueConstraints = {
        @UniqueConstraint(columnNames = "ingredient_name")
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ingredient_type_id", nullable = false)
    private IngredientType ingredientType;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", nullable = false, length = 200)
    private String description;

    @Column(name = "unit_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitCost;

    @Enumerated(EnumType.STRING)
    @Column(name = "base_uom", nullable = false)
    private UnitEnum baseUom;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "par_level_qty", nullable = false)
    private BigDecimal parLevelQty;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

