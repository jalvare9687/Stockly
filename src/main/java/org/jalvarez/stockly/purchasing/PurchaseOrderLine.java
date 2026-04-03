package org.jalvarez.stockly.purchasing;

import jakarta.persistence.*;
import lombok.*;
import org.jalvarez.stockly.util.enums.UnitEnum;
import org.jalvarez.stockly.supplier.model.SupplierItem;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "purchase_order_line", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"purchase_order_id", "supplier_item_id"}
        )
})
public class PurchaseOrderLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "purchase_order_id", nullable = false)
    private PurchaseOrder purchaseOrder;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supplier_item_id", nullable = false)
    private SupplierItem supplierItem;

    @Column(name = "qty_ordered", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantityOrdered;

    @Column(name = "unit_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitCost;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit", nullable = false)
    private UnitEnum unit;


}
