package org.jalvarez.stockly.menu.dto;

import java.math.BigDecimal;

public record RecipeLineDto(
        Long id,
        String ingredientName,
        String menuItemName,
        BigDecimal qtyPerItem
) {
}
