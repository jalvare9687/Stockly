package org.jalvarez.stockly.inventory;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;

@Entity
@Table(name = "inventory_balance_view")
@Immutable
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class InventoryBalanceView {

    @EmbeddedId
    private InventoryBalanceId id;

    @Column(name = "on_hand_qty", nullable = false, precision = 10, scale = 2)
    private BigDecimal onHandQty;

}
