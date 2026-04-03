package org.jalvarez.stockly.deliveries.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jalvarez.stockly.util.enums.DeliveryStatus;
import org.jalvarez.stockly.location.model.Location;
import org.jalvarez.stockly.purchasing.PurchaseOrder;
import org.jalvarez.stockly.supplier.model.Supplier;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryLine> deliveryLines = new ArrayList<>();

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

    public void addDeliveryLine(DeliveryLine deliveryLine) { //represents a specific row
        deliveryLines.add(deliveryLine);
        deliveryLine.setDelivery(this);
    }
    public void removeDeliveryLine(DeliveryLine deliveryLine) {
        deliveryLines.remove(deliveryLine);
        deliveryLine.setDelivery(null);
    }
}
