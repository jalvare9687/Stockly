package org.jalvarez.stockly.menu.dto;

import java.math.BigDecimal;

public record MenuItemDto (
        Long id,
        String name,
        BigDecimal price,
        Boolean isActive
){
}
