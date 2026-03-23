package org.jalvarez.stockly.ingredient.service;

import org.jalvarez.stockly.ingredient.dto.IngredientDto;
import org.jalvarez.stockly.ingredient.dto.IngredientMapper;
import org.jalvarez.stockly.ingredient.model.IngredientType;
import org.jalvarez.stockly.ingredient.repository.IngredientRepository;
import org.jalvarez.stockly.util.EntityLookupService;
import org.jalvarez.stockly.util.exception.IngredientNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class IngredientService {
    private final IngredientRepository ingredientRepository;
    private final EntityLookupService entityLookupService;

    public IngredientService(IngredientRepository ingredientRepository, EntityLookupService entityLookupService) {
        this.ingredientRepository = ingredientRepository;
        this.entityLookupService = entityLookupService;
    }

    List<IngredientDto> getAllIngredients(Pageable pageable) {
        return ingredientRepository.findAll(pageable).stream().map(IngredientMapper::toDto).toList();
    }

    List<IngredientDto> getIngredientsByIngredientType(Long ingredientTypeId, Pageable pageable) {
        IngredientType ingredientType = entityLookupService.findIngredientType(ingredientTypeId);
        return ingredientRepository.findByIngredientType(ingredientType, pageable).stream().map(IngredientMapper::toDto).toList();
    }
    List<IngredientDto> getPerishableIngredients(Boolean isPerishable, Pageable pageable) {
        return ingredientRepository.findByIngredientTypeIsPerishable(isPerishable, pageable).stream().map(IngredientMapper::toDto).toList();
    }

    List<IngredientDto> getIngredientsByName(String ingredientName, Pageable pageable) {
        return ingredientRepository.findByNameContainingIgnoreCase(ingredientName, pageable).stream().map(IngredientMapper::toDto).toList();
    }

    List<IngredientDto> getIngredientsByParLevelQty(BigDecimal parLevelQty, Pageable pageable) {
        return ingredientRepository.findByParLevelQtyLessThan(parLevelQty, pageable).stream().map(IngredientMapper::toDto).toList();
    }

    IngredientDto getIngredientById(Long id) {
        return ingredientRepository.findById(id).map(IngredientMapper::toDto).orElseThrow(() -> new IngredientNotFoundException("Ingredient not found" + id));
    }
}
