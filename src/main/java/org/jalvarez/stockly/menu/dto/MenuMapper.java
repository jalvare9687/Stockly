package org.jalvarez.stockly.menu.dto;

import org.jalvarez.stockly.menu.model.MenuItem;
import org.jalvarez.stockly.menu.model.RecipeLine;

import java.util.List;

public class MenuMapper {
    public static MenuItemDto toMenuItemDto(MenuItem menuItem) {
        if (menuItem == null) {
            return null;
        }

        return new MenuItemDto(
                menuItem.getId(),
                menuItem.getName(),
                menuItem.getPrice(),
                menuItem.getIsActive()
        );
    }
    public static SingleMenuItemDto toSingularMenuItemDto(MenuItem menuItem) {
        if (menuItem == null) {
            return null;
        }

        List<RecipeLineDto> recipeLineDtos = menuItem.getRecipeLines().stream().map(MenuMapper::toRecipeLineDto)
                .toList();
        return new SingleMenuItemDto(
                menuItem.getId(),
                menuItem.getName(),
                menuItem.getPrice(),
                menuItem.getIsActive(),
                recipeLineDtos
        );
    }

    public static RecipeLineDto toRecipeLineDto(RecipeLine recipeLine) {
        if (recipeLine == null) {
            return null;
        }
        return new RecipeLineDto(
                recipeLine.getId(),
                recipeLine.getIngredient().getName(),
                recipeLine.getMenuItem().getName(),
                recipeLine.getQtyPerItem()
        );
    }
}
