package org.jalvarez.stockly.sales;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jalvarez.stockly.location.Location;

import java.time.LocalDateTime;

@Entity
@Table(name = "sale")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(name = "sold_at", nullable = false)
    private LocalDateTime soldAt;

    @PrePersist
    protected void onCreate() {
        this.soldAt = LocalDateTime.now();
    }

}
