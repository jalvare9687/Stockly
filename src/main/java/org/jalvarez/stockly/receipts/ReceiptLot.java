package org.jalvarez.stockly.receipts;

import jakarta.persistence.*;
import lombok.*;
import org.jalvarez.stockly.deliveries.model.DeliveryLine;
import org.jalvarez.stockly.inventory.model.InventoryLot;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "receipt_lot", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"delivery_line_id", "inventory_lot_id"})
})
public class ReceiptLot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "delivery_line_id", nullable = false)
    private DeliveryLine deliveryLine;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "inventory_lot_id", nullable = false)
    private InventoryLot inventoryLot;

    @Column(name = "quantity", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;

}
