package org.jalvarez.stockly.sales;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "sale_line", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"sale_id", "menu_item_id"})
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SaleLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale;

    @Column(name = "qty_sold", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantitySold;

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;
}
