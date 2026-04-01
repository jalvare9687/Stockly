package org.jalvarez.stockly.sales.dto;

import java.time.LocalDateTime;
import java.util.List;

public record SaleDto(
        Long id,
        List<SaleLineDto> saleLineDtos,
        String locationName,
        LocalDateTime soldAt
) {
}
