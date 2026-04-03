package org.jalvarez.stockly.purchasing;

import jakarta.persistence.*;
import lombok.*;
import org.jalvarez.stockly.util.enums.PurchaseOrderStatus;
import org.jalvarez.stockly.location.model.Location;
import org.jalvarez.stockly.supplier.model.Supplier;
import org.jalvarez.stockly.user.User;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "purchase_orders")
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private User createdBy;

    //need supplier id- many purchase orders to one supplier
    @Enumerated(EnumType.STRING)
    @Column(name = "po_status", nullable = false)
    private PurchaseOrderStatus poStatus;

    @Column(name = "expected_at")
    private LocalDateTime expectedAt;
    @Column(name = "sent_at")
    private LocalDateTime sentAt;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
