package org.jalvarez.stockly.ingredient.repository;

import org.jalvarez.stockly.ingredient.model.IngredientType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientTypeRepository extends JpaRepository<IngredientType, Long> {
    IngredientType findById(long id);
}
