package org.jalvarez.stockly.supplier.repository;

import org.jalvarez.stockly.supplier.model.LocationSupplier;
import org.jalvarez.stockly.supplier.model.id.LocationSupplierId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationSupplierRepository extends JpaRepository<LocationSupplier, LocationSupplierId> {
    List<LocationSupplier> findByIdLocationId(Long locationId, Pageable pageable);
    List<LocationSupplier> findByIdLocationIdAndIsActiveSupplier(Long locationId, Boolean isActive, Pageable pageable);
}
