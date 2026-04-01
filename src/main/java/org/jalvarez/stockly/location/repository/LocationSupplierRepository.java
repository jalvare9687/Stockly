package org.jalvarez.stockly.location.repository;

import org.jalvarez.stockly.location.model.LocationSupplier;
import org.jalvarez.stockly.location.model.LocationSupplierId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationSupplierRepository extends JpaRepository<LocationSupplier, LocationSupplierId> {
    List<LocationSupplier> findByLocationId(Long locationId, Pageable pageable);
    List<LocationSupplier> findByLocationIdAndIsActiveSupplier(Long locationId, Boolean isActive, Pageable pageable);
}
