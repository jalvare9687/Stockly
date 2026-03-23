package org.jalvarez.stockly.inventory.controller;

import org.jalvarez.stockly.inventory.dto.InventoryLotDto;
import org.jalvarez.stockly.inventory.model.InventoryLot;
import org.jalvarez.stockly.inventory.service.InventoryLotService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/inventory-lot")
public class InventoryLotController {
    private final InventoryLotService inventoryLotService;

    public InventoryLotController(InventoryLotService inventoryLotService) {
        this.inventoryLotService = inventoryLotService;
    }
    @GetMapping("by-ingredient")
    public List<InventoryLotDto> getInventoryLotsByLocationAndIngredients(@RequestParam Long locationId, @RequestParam Long ingredientId, Pageable pageable) {
        return inventoryLotService.getByLocationAndIngredient(locationId, ingredientId, pageable);
    }

    @GetMapping("by-location")
    public List<InventoryLotDto> getInventoryLotsByLocation(@RequestParam Long locationId, Pageable pageable) {
        return inventoryLotService.getByLocation(locationId, pageable);
    }

    @GetMapping("expired-by-location")
    public List<InventoryLotDto> getExpiredLotsByLocation(@RequestParam Long locationId, Pageable pageable) {
        return inventoryLotService.getExpiredLotsByLocation(locationId, pageable);
    }

    @GetMapping("expiring-by-location")
    public List<InventoryLotDto> getExpiringLotsByLocation(@RequestParam Long locationId, @RequestParam int days, Pageable pageable) {
        return inventoryLotService.getLotsExpiringByLocation(locationId, days, pageable);
    }

    @GetMapping("total-qty")
    public BigDecimal getTotalRemainingQtyByLocation(@RequestParam Long locationId) {
        return inventoryLotService.getTotalRemainingQtyByLocation(locationId);
    }

    @GetMapping("total-cost")
    public BigDecimal getTotalCostOfLotByLocation(@RequestParam Long locationId) {
        return inventoryLotService.getTotalCostOfLotByLocation(locationId);
    }

}
