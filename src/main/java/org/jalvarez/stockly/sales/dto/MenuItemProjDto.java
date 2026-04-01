package org.jalvarez.stockly.sales.dto;

import java.math.BigDecimal;

public record MenuItemProjDto (
        String menuItemName,
        BigDecimal revenue
) {
}
