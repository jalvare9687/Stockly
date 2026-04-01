package org.jalvarez.stockly.menu.repository;

import org.jalvarez.stockly.menu.model.RecipeLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface RecipeLineRepository extends JpaRepository<RecipeLine, Long> {
    List<RecipeLine> findByMenuItemId(Long menuItemId);
}
