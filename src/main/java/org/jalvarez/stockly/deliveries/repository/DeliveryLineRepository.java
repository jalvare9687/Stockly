package org.jalvarez.stockly.deliveries.repository;

import org.jalvarez.stockly.deliveries.model.DeliveryLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryLineRepository extends JpaRepository<DeliveryLine, Long> {
}
