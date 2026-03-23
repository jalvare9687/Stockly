package org.jalvarez.stockly.inventory.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

//Should return what the dashboard needs-
//id, ingredientName, locationNametotalcostOfLot, remainingQty, expiryDate
public record InventoryLotDto(
     Long id,
     String ingredientName,
     String locationName,
     BigDecimal totalCostOfLot,
     BigDecimal remainingQty,
     LocalDate expiryDate
) {
}
