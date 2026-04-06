package org.jalvarez.stockly.supplier.repository;

import org.jalvarez.stockly.supplier.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Supplier findByName(String name);
    Supplier findByEmail(String email);
    Supplier findByPhoneNumber(String phoneNumber);
}
