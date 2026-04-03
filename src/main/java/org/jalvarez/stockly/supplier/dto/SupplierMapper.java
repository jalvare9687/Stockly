package org.jalvarez.stockly.supplier.dto;

import org.jalvarez.stockly.supplier.model.LocationSupplier;

public class SupplierMapper {
    public static LocationSupplierDto toSupplierDto(LocationSupplier locationSupplier) {
        if (locationSupplier == null) return null;

        return new LocationSupplierDto(
                locationSupplier.getId(),
                locationSupplier.getIsActiveSupplier(),
                locationSupplier.getDefaultLeadTimeDays(),
                locationSupplier.getDeliveryDays(),
                locationSupplier.getDescription()
        );
    }
}
