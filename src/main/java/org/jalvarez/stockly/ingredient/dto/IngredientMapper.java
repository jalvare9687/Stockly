package org.jalvarez.stockly.ingredient.dto;

import org.jalvarez.stockly.ingredient.model.Ingredient;

public class IngredientMapper {
    public static IngredientDto toDto(Ingredient ingredient) {
        if (ingredient == null) {
            return null;
        }

        return new IngredientDto(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getDescription(),
                ingredient.getIngredientType().getType(),
                ingredient.getIngredientType().getIsPerishable(),
                ingredient.getParLevelQty(),
                ingredient.getUnitCost(),
                ingredient.getBaseUom()
        );
    }
}
