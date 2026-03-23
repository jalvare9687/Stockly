package org.jalvarez.stockly.inventory.service;


import org.jalvarez.stockly.ingredient.model.Ingredient;
import org.jalvarez.stockly.inventory.dto.InventoryLotDto;
import org.jalvarez.stockly.inventory.dto.InventoryLotMapper;
import org.jalvarez.stockly.inventory.repository.InventoryLotRepository;
import org.jalvarez.stockly.location.Location;
import org.jalvarez.stockly.util.EntityLookupService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class InventoryLotService {
    private final InventoryLotRepository inventoryLotRepository;
    private final EntityLookupService entityLookupService;

    public InventoryLotService(InventoryLotRepository inventoryLotRepository, EntityLookupService entityLookupService) {
        this.inventoryLotRepository = inventoryLotRepository;
        this.entityLookupService = entityLookupService;
    }

    public List<InventoryLotDto> getByLocationAndIngredient(Long locationId, Long ingredientId, Pageable pageable) {
        Location location = entityLookupService.findLocation(locationId);
        Ingredient ingredient = entityLookupService.findIngredient(ingredientId);
        return inventoryLotRepository.findByLocationAndIngredient(location, ingredient, pageable).stream().map(InventoryLotMapper::toInventoryLotDto).toList();
    }

    public List<InventoryLotDto> getByLocation(Long locationId, Pageable pageable) {
        Location location = entityLookupService.findLocation(locationId);
        return inventoryLotRepository.findByLocationAndRemainingQtyGreaterThan(location, BigDecimal.ZERO, pageable).stream().map(InventoryLotMapper::toInventoryLotDto).toList();
    }

    public List<InventoryLotDto> getExpiredLotsByLocation(Long locationId, Pageable pageable) {
        Location location = entityLookupService.findLocation(locationId);
        return inventoryLotRepository.findByLocationAndExpiryDateLessThanAndRemainingQtyGreaterThan(location, LocalDate.now(), BigDecimal.ZERO, pageable).stream().map(InventoryLotMapper::toInventoryLotDto).toList();
    }

    public List<InventoryLotDto> getLotsExpiringByLocation(Long locationId, int days ,Pageable pageable) {
        Location location = entityLookupService.findLocation(locationId);
        return inventoryLotRepository.findByLocationAndExpiryDateBetween(location, LocalDate.now(), LocalDate.now().plusDays(days), pageable).stream().map(InventoryLotMapper::toInventoryLotDto).toList();
    }

    public BigDecimal getTotalRemainingQtyByLocation(Long locationId) {
        return inventoryLotRepository.getTotalRemainingQty(locationId);
    }
    public BigDecimal getTotalCostOfLotByLocation(Long locationId) {
        return inventoryLotRepository.getTotalCostOfLot(locationId);
    }

}
