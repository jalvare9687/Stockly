package org.jalvarez.stockly.ingredient.repository;

import org.jalvarez.stockly.ingredient.model.Ingredient;
import org.jalvarez.stockly.ingredient.model.IngredientType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> { // perishable, type
    List<Ingredient> findByIngredientType(IngredientType ingredientType, Pageable pageable);
    List<Ingredient> findByIngredientTypeIsPerishable(Boolean perishable, Pageable pageable);
    List<Ingredient> findByNameContainingIgnoreCase(String ingredientName, Pageable pageable);
    List<Ingredient> findByParLevelQtyLessThan(BigDecimal parLevelQty, Pageable pageable);
    Ingredient findById(long id);

}
