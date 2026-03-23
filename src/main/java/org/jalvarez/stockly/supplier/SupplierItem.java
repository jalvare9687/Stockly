package org.jalvarez.stockly.supplier;

import jakarta.persistence.*;
import lombok.*;
import org.jalvarez.stockly.util.enums.PackUom;
import org.jalvarez.stockly.ingredient.model.Ingredient;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(
        name = "supplier_item",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"supplier_id", "ingredient_id"}),
                @UniqueConstraint(columnNames = {"supplier_id", "vendor_sku"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupplierItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supplier_id", nullable = false) //needs to be unique
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    /**
     * Supplier’s SKU / product code for this ingredient.
     * Optional, but recommended if you have it.
     */
    @Column(name = "vendor_sku", length = 64, nullable = false) //needs to be unique code
    private String vendorSku;

    /**
     * Purchasing pack size (e.g., 24, 10.0, 2.5).
     * Represents how many "packUom" units are in one purchase unit (or one pack).
     */
    @Column(name = "pack_size", precision = 18, scale = 6, nullable = false)
    private BigDecimal packSize;

    /**
     * Unit of measure for the pack size (e.g., EACH, LB, KG, L).
     * Keep this as string enum or FK to a UOM table if you have one.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "pack_uom", length = 24, nullable = false)
    private PackUom packUom;

    /**
     * Last known / default cost for this supplier-item relationship.
     * Use for PO defaults; actual receipt cost should live on receipt lines/lots.
     */

    @Column(name = "default_cost", precision = 19, scale = 4, nullable = false)
    private BigDecimal defaultCost;

    /**
     * Mark the primary/preferred supplier for this ingredient.
     * If you need "only one primary per ingredient", enforce via app logic or partial unique index.
     */
    @Column(name = "is_primary", nullable = false)
    private boolean primary = false;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    void onCreate() {
        var now = OffsetDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

}