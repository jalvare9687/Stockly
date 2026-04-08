package org.jalvarez.stockly.supplier.dto;

import org.jalvarez.stockly.util.enums.PackUom;

import java.math.BigDecimal;

public record SupplierItemDto(
        Long id,
        Long supplierId,
        Long ingredientId,
        String vendorSku,
        BigDecimal packSize,
        PackUom packUom,
        BigDecimal defaultCost,
        Boolean primary,
        Boolean active
) {
}
