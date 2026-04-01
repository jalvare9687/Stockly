package org.jalvarez.stockly.menu.repository;

import org.jalvarez.stockly.menu.model.MenuItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    MenuItem findByName(String name);
    List<MenuItem> findAllByIsActive(boolean isActive, Pageable pageable);
}
