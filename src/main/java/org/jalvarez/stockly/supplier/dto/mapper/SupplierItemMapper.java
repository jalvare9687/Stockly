package org.jalvarez.stockly.supplier.dto.mapper;

import org.jalvarez.stockly.supplier.dto.SupplierItemDto;
import org.jalvarez.stockly.supplier.model.SupplierItem;

public class SupplierItemMapper {
    public static SupplierItemDto toDto(SupplierItem supplierItem) {
        if (supplierItem == null) {
            return null;
        }

        return new SupplierItemDto(
                supplierItem.getId(),
                supplierItem.getSupplier().getId(),
                supplierItem.getIngredient().getId(),
                supplierItem.getVendorSku(),
                supplierItem.getPackSize(),
                supplierItem.getPackUom(),
                supplierItem.getDefaultCost(),
                supplierItem.getPrimary(),
                supplierItem.getActive()
        );
    }
}
