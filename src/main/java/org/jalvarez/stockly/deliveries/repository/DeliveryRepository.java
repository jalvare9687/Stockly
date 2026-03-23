package org.jalvarez.stockly.deliveries.repository;

import org.jalvarez.stockly.deliveries.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
