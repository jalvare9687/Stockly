package org.jalvarez.stockly.ingredient.dto;

import org.jalvarez.stockly.util.enums.UnitEnum;

import java.math.BigDecimal;

public record IngredientDto(
        Long id,
        String name,
        String description,
        String ingredientType,
        Boolean isPerishable,
        BigDecimal parLevelQty,
        BigDecimal unitCost,
        UnitEnum baseUom
) {
}
