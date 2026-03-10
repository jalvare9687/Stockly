package org.jalvarez.stockly.transactions;

import jakarta.persistence.*;
import lombok.*;
import org.jalvarez.stockly.deliveries.Delivery;
import org.jalvarez.stockly.enums.SourceType;
import org.jalvarez.stockly.enums.TransactionType;
import org.jalvarez.stockly.location.Location;
import org.jalvarez.stockly.purchasing.PurchaseOrder;
import org.jalvarez.stockly.sales.Sale;
import org.jalvarez.stockly.user.User;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "inventory_transaction")
public class InventoryTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //transaction id

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User createdByUserId;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    @Column(name = "description", nullable = false, length = 150)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sale_id")
    private Sale sale;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "occurred_at", nullable = false)
    private LocalDateTime occurredAt;


}
