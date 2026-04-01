package org.jalvarez.stockly.sales.dto;

import java.math.BigDecimal;

public record SaleLineDto (
        Long id,
        Long saleId,
        String menuItemName,
        BigDecimal qtySold,
        BigDecimal unitPrice
) {

}
