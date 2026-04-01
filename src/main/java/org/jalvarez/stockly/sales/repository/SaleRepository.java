package org.jalvarez.stockly.sales.repository;

import org.jalvarez.stockly.sales.model.Sale;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    //find sales by location
    //find sales by date
    List<Sale> findSaleByLocationIdOrderBySoldAtDesc(Long locationId, Pageable pageable);
    List<Sale> findSaleByLocationIdAndSoldAtBetween(Long locationId, LocalDateTime start, LocalDateTime end, Pageable pageable);
}
