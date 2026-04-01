package org.jalvarez.stockly.sales.controller;

import org.jalvarez.stockly.sales.dto.MenuItemProjDto;
import org.jalvarez.stockly.sales.dto.SaleDto;
import org.jalvarez.stockly.sales.dto.SaleLineDto;
import org.jalvarez.stockly.sales.service.SalesService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SalesController {
    private final SalesService salesService;

    public SalesController(SalesService salesService) {
        this.salesService = salesService;
    }

    @GetMapping("by-location")
    public List<SaleDto> getSalesByLocation(@RequestParam Long locationId,  Pageable pageable) {
        return salesService.getSalesByLocation(locationId, pageable);
    }

    @GetMapping("by-location-and-date")
    public List<SaleDto> getSalesByLocationAndDate(@RequestParam Long locationId, @RequestParam LocalDateTime start, @RequestParam LocalDateTime end, Pageable pageable) {
        return salesService.getSalesByLocationAndWithinDataRange(locationId, start, end, pageable);
    }

    @GetMapping("sale-line-by-sale-id")
    public List<SaleLineDto> getBySaleId(@RequestParam Long saleId, Pageable pageable) {
        return salesService.getBySaleId(saleId, pageable);
    }

    @GetMapping("most-sold-by-location")
    public List<MenuItemProjDto> getMostSoldByLocation(@RequestParam Long locationId, Pageable pageable) {
        return salesService.getMostSoldByLocation(locationId, pageable);
    }

    @GetMapping("total-revenue-by-location")
    public BigDecimal getTotalRevenueByLocation(@RequestParam Long locationId) {
        return salesService.getTotalRevenueByLocation(locationId);
    }

    @GetMapping("total-revenue-by-location-by-date")
    public BigDecimal getTotalRevenueByLocationAndDate(@RequestParam Long locationId, @RequestParam LocalDateTime start, @RequestParam LocalDateTime end) {
        return salesService.getTotalRevenueByLocationAndWithinDateRange(locationId, start, end);
    }
}
