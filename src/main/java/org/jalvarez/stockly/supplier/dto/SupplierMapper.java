package org.jalvarez.stockly.supplier.dto;

import org.jalvarez.stockly.supplier.model.LocationSupplier;
import org.jalvarez.stockly.supplier.model.Supplier;

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
    public static SupplierDto toSupplierDto(Supplier supplier) {
        if (supplier == null) return null;

        return new SupplierDto(
                supplier.getId(),
                supplier.getName(),
                supplier.getEmail(),
                supplier.getPhoneNumber()
        );
    }
}
