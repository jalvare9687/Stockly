package org.jalvarez.stockly.supplier.repository;

import org.jalvarez.stockly.supplier.model.SupplierItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierItemRepository extends JpaRepository<SupplierItem, Long> {
    /*
    Active supplier items for a supplier,
    Primary supplier for an ingredient,
    Supplier items by ingredient
     */

    List<SupplierItem> getAllByActiveAndSupplier_Id(Boolean active, Long supplierId, Pageable pageable);
    List<SupplierItem> getAllByPrimaryAndActiveAndSupplier_Id(Boolean primary, Boolean active, Long supplierId, Pageable pageable);
    List<SupplierItem> getAllByIngredient_Id(Long ingredientId, Pageable pageable);
    List<SupplierItem> getAllByIngredient_IdAndActiveAndPrimary(Long ingredientId, Boolean active, Boolean primary, Pageable pageable);



}
