package org.jalvarez.stockly.inventory.repository;

import org.jalvarez.stockly.ingredient.model.Ingredient;
import org.jalvarez.stockly.inventory.model.InventoryLot;
import org.jalvarez.stockly.location.model.Location;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface InventoryLotRepository extends JpaRepository<InventoryLot, Long> {
    /*
    We should fetch a couple things:
        - lots that are running out of stock in a certain location belongs in view repository

     */

    //The entire lot via location - should act as the default display, with remaining_qty > 0
    List<InventoryLot> findByLocationAndRemainingQtyGreaterThan(Location location, BigDecimal qty, Pageable pageable);

    //lots by ingredient and location
    List<InventoryLot> findByLocationAndIngredient(Location location, Ingredient ingredient, Pageable pageable);

    //Lots in a location that have already expired but still have remaining qty
    List<InventoryLot> findByLocationAndExpiryDateLessThanAndRemainingQtyGreaterThan(Location location, LocalDate expiryDate, BigDecimal qty, Pageable pageable);

    //Expiring lots within a set amount of time
    List<InventoryLot> findByLocationAndExpiryDateBetween(Location location, LocalDate start, LocalDate end, Pageable pageable);

    //The total value of the lot via location
    @Query("SELECT COALESCE(SUM(il.remainingQty), 0) FROM InventoryLot il WHERE il.remainingQty > 0 AND il.location.id = :locationId")
    BigDecimal getTotalRemainingQty(@Param("locationId") Long locationId);

    //Total cost of lot
    @Query("SELECT COALESCE(SUM(il.totalCostOfLot), 0) FROM InventoryLot il WHERE il.location.id = :locationId AND il.remainingQty > 0")
    BigDecimal getTotalCostOfLot(@Param("locationId") Long locationId);
}
