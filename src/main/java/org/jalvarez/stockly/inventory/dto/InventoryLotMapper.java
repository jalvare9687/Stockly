package org.jalvarez.stockly.inventory.dto;

import org.jalvarez.stockly.inventory.model.InventoryLot;

public class InventoryLotMapper {

    public static InventoryLotDto toInventoryLotDto(InventoryLot inventoryLot) {
        if (inventoryLot == null) {
            return null;
        }

        return new InventoryLotDto(
                inventoryLot.getId(),
                inventoryLot.getIngredient().getName(),
                inventoryLot.getLocation().getName(),
                inventoryLot.getTotalCostOfLot(),
                inventoryLot.getRemainingQty(),
                inventoryLot.getExpiryDate()
        );
    }
}
