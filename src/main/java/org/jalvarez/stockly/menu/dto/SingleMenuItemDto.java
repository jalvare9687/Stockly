package org.jalvarez.stockly.menu.dto;

import java.math.BigDecimal;
import java.util.List;

public record SingleMenuItemDto(
        Long id,
        String name,
        BigDecimal price,
        Boolean isActive,
        List<RecipeLineDto> recipeLines //should hold RecipeLineDto instead

) {
}
