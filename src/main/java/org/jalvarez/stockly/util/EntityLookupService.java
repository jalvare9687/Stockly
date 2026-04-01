package org.jalvarez.stockly.util;

import org.jalvarez.stockly.ingredient.model.Ingredient;
import org.jalvarez.stockly.ingredient.model.IngredientType;
import org.jalvarez.stockly.ingredient.repository.IngredientRepository;
import org.jalvarez.stockly.ingredient.repository.IngredientTypeRepository;
import org.jalvarez.stockly.location.model.Location;
import org.jalvarez.stockly.location.repository.LocationRepository;
import org.jalvarez.stockly.sales.model.Sale;
import org.jalvarez.stockly.sales.repository.SaleRepository;
import org.jalvarez.stockly.util.exception.IngredientNotFoundException;
import org.jalvarez.stockly.util.exception.IngredientTypeNotFoundException;
import org.jalvarez.stockly.util.exception.LocationNotFoundException;
import org.jalvarez.stockly.util.exception.SaleNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EntityLookupService {
    private final LocationRepository locationRepository;
    private final IngredientRepository ingredientRepository;
    private final IngredientTypeRepository ingredientTypeRepository;
    private final SaleRepository saleRepository;

    public EntityLookupService(LocationRepository locationRepository, IngredientRepository ingredientRepository, IngredientTypeRepository ingredientTypeRepository, SaleRepository saleRepository) { //simplify this
        this.locationRepository = locationRepository;
        this.ingredientRepository = ingredientRepository;
        this.ingredientTypeRepository = ingredientTypeRepository;
        this.saleRepository = saleRepository;
    }

    public Location findLocation(Long locationId) {
        return locationRepository.findById(locationId).orElseThrow(() -> new LocationNotFoundException("Location not found: " + locationId)); // throw an error here
    }

    public Ingredient findIngredient(Long ingredientId) {
        return ingredientRepository.findById(ingredientId).orElseThrow(() -> new IngredientNotFoundException("Ingredient not found: " + ingredientId)); // throw an error here
    }

    public IngredientType findIngredientType(Long ingredientTypeId) {
        return ingredientTypeRepository.findById(ingredientTypeId).orElseThrow(() -> new IngredientTypeNotFoundException("Ingredient type not found" + ingredientTypeId));
    }

    public Sale findSale(Long saleId) {
        return saleRepository.findById(saleId).orElseThrow(() -> new SaleNotFoundException("Sale not found: " + saleId));
    }
}
