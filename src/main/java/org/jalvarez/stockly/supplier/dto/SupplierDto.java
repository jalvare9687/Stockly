package org.jalvarez.stockly.supplier.dto;

public record SupplierDto(
        Long id,
        String name,
        String email,
        String phoneNumber
) {
}
