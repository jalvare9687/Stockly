package org.jalvarez.stockly.location.repository;

import org.jalvarez.stockly.location.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Location findById(long id);
}
