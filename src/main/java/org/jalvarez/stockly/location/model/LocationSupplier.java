package org.jalvarez.stockly.location.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "location_supplier")
public class LocationSupplier {
    @EmbeddedId
    private LocationSupplierId id;

    @Column(name = "is_active_supplier", nullable = false)
    private Boolean isActiveSupplier;

    @Column(name = "default_lead_time_days", nullable = false)
    private Long defaultLeadTimeDays;

    @Column(name = "delivery_days", nullable = false)
    private Long deliveryDays; //change into schedule table

    @Column(name = "min_order_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal minOrderAmount;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
