package org.jalvarez.stockly.deliveries.model;

import jakarta.persistence.*;
import lombok.*;
import org.jalvarez.stockly.purchasing.PurchaseOrderLine;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "delivery_line", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"po_line_id", "delivery_id"})
})
public class DeliveryLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "po_line_id", nullable = false)
    private PurchaseOrderLine poLine; //A po line can be partially received- a receipt line references exactly one po line

    @Column(name = "received_qty", nullable = false, precision = 10, scale = 2)
    private BigDecimal receivedQty;

    @Column(name = "received_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal receivedCost;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
