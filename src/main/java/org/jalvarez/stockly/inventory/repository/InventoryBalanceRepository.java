package org.jalvarez.stockly.inventory.repository;

import org.jalvarez.stockly.inventory.model.InventoryBalanceId;
import org.jalvarez.stockly.inventory.model.InventoryBalanceView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryBalanceRepository extends JpaRepository<InventoryBalanceView, InventoryBalanceId> {
}
