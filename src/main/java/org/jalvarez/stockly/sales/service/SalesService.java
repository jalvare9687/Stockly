package org.jalvarez.stockly.sales.service;

import org.jalvarez.stockly.sales.dto.MenuItemProjDto;
import org.jalvarez.stockly.sales.dto.SaleDto;
import org.jalvarez.stockly.sales.dto.SaleLineDto;
import org.jalvarez.stockly.sales.dto.SalesMapper;
import org.jalvarez.stockly.sales.repository.SaleLineRepository;
import org.jalvarez.stockly.sales.repository.SaleRepository;
import org.jalvarez.stockly.util.EntityLookupService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class SalesService {
    private final SaleRepository saleRepository;
    private final SaleLineRepository saleLineRepository;
    private final EntityLookupService entityLookupService;

    public SalesService(SaleRepository saleRepository, SaleLineRepository saleLineRepository, EntityLookupService entityLookupService) {
        this.saleRepository = saleRepository;
        this.saleLineRepository = saleLineRepository;
        this.entityLookupService = entityLookupService;
    }

    public List<SaleDto> getSalesByLocation(Long locationId, Pageable pageable) {
        entityLookupService.findLocation(locationId); //check
        return saleRepository.findSaleByLocationIdOrderBySoldAtDesc(locationId, pageable).stream().map(SalesMapper::toSaleDto).toList();
    }

    public List<SaleDto> getSalesByLocationAndWithinDataRange(Long locationId, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        entityLookupService.findLocation(locationId);
        return saleRepository.findSaleByLocationIdAndSoldAtBetween(locationId, start, end, pageable).stream().map(SalesMapper::toSaleDto).toList();
    }

    public List<SaleLineDto> getBySaleId(Long saleId, Pageable pageable) {
        entityLookupService.findSale(saleId);
        return saleLineRepository.findBySaleId(saleId, pageable).stream().map(SalesMapper::toSaleLineDto).toList();
    }

    public List<MenuItemProjDto> getMostSoldByLocation(Long locationId, Pageable pageable) {
        entityLookupService.findLocation(locationId);
        return saleLineRepository.findMostSoldByLocation(locationId, pageable).stream().map(SalesMapper::toMenuItemProjDto).toList();
    }

    public BigDecimal getTotalRevenueByLocation(Long locationId) {
        entityLookupService.findLocation(locationId);
        return saleLineRepository.findTotalRevenueByLocation(locationId);
    }

    public BigDecimal getTotalRevenueByLocationAndWithinDateRange(Long locationId, LocalDateTime start, LocalDateTime end) {
        entityLookupService.findLocation(locationId);
        return saleLineRepository.findTotalRevenueByLocationAndWithinDataRange(locationId, start, end);
    }

}
