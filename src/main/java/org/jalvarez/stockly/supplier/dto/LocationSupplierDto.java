package org.jalvarez.stockly.supplier.dto;

import org.jalvarez.stockly.supplier.model.id.LocationSupplierId;

public record LocationSupplierDto(
        LocationSupplierId id,
        Boolean isActiveSupplier,
        Long defaultLeadTimeDays,
        Long deliveryDays,
        String description
) {
}
