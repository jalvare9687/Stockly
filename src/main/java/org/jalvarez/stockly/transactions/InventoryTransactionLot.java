package org.jalvarez.stockly.transactions;

import jakarta.persistence.*;
import lombok.*;
import org.jalvarez.stockly.inventory.model.InventoryLot;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "inventory_transaction_lot", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"transaction_id", "lot_id"})
})
public class InventoryTransactionLot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transaction_id", nullable = false)
    private InventoryTransaction transaction;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "inventory_lot_id", nullable = false)
    private InventoryLot lot;

    /*
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

     */

    @Column(name = "qty_delta", nullable = false, precision = 10, scale = 2)
    private BigDecimal qtyDelta;

}
