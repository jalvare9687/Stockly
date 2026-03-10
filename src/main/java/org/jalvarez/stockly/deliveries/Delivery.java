package org.jalvarez.stockly.deliveries;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jalvarez.stockly.enums.DeliveryStatus;
import org.jalvarez.stockly.location.Location;
import org.jalvarez.stockly.purchasing.PurchaseOrder;
import org.jalvarez.stockly.supplier.Supplier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "delivery", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"supplier_id", "reference_number"})
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "purchase_order_id", nullable = false)
    private PurchaseOrder po;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(name = "expected_arrival", nullable = false)
    private LocalDateTime expectedArrival;

    @Column(name = "shipped_at")
    private LocalDateTime shippedAt;

    @Column(name = "arrived_at")
    private LocalDateTime arrivedAt;

    @Column(name = "received_closed_at")
    private LocalDateTime receivedClosedAt;

    @Column(name = "delivery_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    @Column(name = "reference_number", nullable = false)
    private String referenceNumber;
}
