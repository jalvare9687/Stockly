package org.jalvarez.stockly.location.repository;

import org.jalvarez.stockly.location.model.Location;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Location findByName(String name);
    List<Location> findByCity(String city, Pageable pageable);
    List<Location> findByState(String state, Pageable pageable);
}
