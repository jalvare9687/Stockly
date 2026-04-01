package org.jalvarez.stockly.sales.repository;

import org.jalvarez.stockly.sales.interfaces.MenuItemSalesProjection;
import org.jalvarez.stockly.sales.model.SaleLine;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SaleLineRepository extends JpaRepository<SaleLine, Long> {
    //find total quantity spent for an item?
    //find total revenue
    @Query("SELECT COALESCE(SUM(sl.quantitySold * sl.unitPrice), 0) FROM SaleLine sl WHERE sl.sale.location.id = :locationId")
    BigDecimal findTotalRevenueByLocation(@Param("locationId") Long locationId);

    @Query("SELECT COALESCE(SUM(sl.quantitySold * sl.unitPrice), 0) FROM SaleLine sl WHERE sl.sale.location.id = :locationId AND sl.sale.soldAt BETWEEN :from AND :end ")
    BigDecimal findTotalRevenueByLocationAndWithinDataRange(@Param("locationId") Long locationId, @Param("from") LocalDateTime from, @Param("end") LocalDateTime end);

    //find the most sold items
    @Query("SELECT sl.menuItem.name as menuItemName, SUM(sl.quantitySold * sl.unitPrice) as revenue FROM SaleLine sl WHERE sl.sale.location.id = :locationId GROUP BY sl.menuItem.name ORDER BY revenue DESC")
    List<MenuItemSalesProjection> findMostSoldByLocation(@Param("locationId") Long locationId, Pageable pageable); //Pageable.pageRequest(0, n)

    List<SaleLine> findBySaleId(Long saleId, Pageable pageable);

}

