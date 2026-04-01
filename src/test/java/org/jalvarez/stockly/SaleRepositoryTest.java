package org.jalvarez.stockly;

import org.jalvarez.stockly.location.model.Location;
import org.jalvarez.stockly.location.repository.LocationRepository;
import org.jalvarez.stockly.menu.model.MenuItem;
import org.jalvarez.stockly.menu.repository.MenuItemRepository;
import org.jalvarez.stockly.sales.interfaces.MenuItemSalesProjection;
import org.jalvarez.stockly.sales.model.Sale;
import org.jalvarez.stockly.sales.model.SaleLine;
import org.jalvarez.stockly.sales.repository.SaleLineRepository;
import org.jalvarez.stockly.sales.repository.SaleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Pageable;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class SaleRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16-alpine");

    @Autowired
    SaleRepository saleRepository;
    @Autowired
    SaleLineRepository saleLineRepository;
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    MenuItemRepository menuItemRepository;

    private Location testLocation;
    private MenuItem testMenuItem;
    private Sale recentSale;
    private Sale oldSale;

    @BeforeEach
    void setUp() {
        String unique = UUID.randomUUID().toString().substring(0, 8);

        testLocation = new Location();
        testLocation.setName("Warehouse-" + unique);
        testLocation.setAddress("123 Main St");
        testLocation.setCity("Atlanta");
        testLocation.setState("GA");
        testLocation.setCountry("US");
        testLocation.setZip("30043");
        locationRepository.save(testLocation);

        testMenuItem = new MenuItem();
        testMenuItem.setName("Burger-" + unique);
        testMenuItem.setPrice(new BigDecimal("9.99"));
        testMenuItem.setIsActive(true);
        menuItemRepository.save(testMenuItem);

        // recent sale - within last 30 days
        recentSale = new Sale();
        recentSale.setLocation(testLocation);
        saleRepository.save(recentSale);

        SaleLine recentLine = new SaleLine();
        recentLine.setSale(recentSale);
        recentLine.setMenuItem(testMenuItem);
        recentLine.setQuantitySold(new BigDecimal("2.00"));
        recentLine.setUnitPrice(new BigDecimal("9.99"));
        saleLineRepository.save(recentLine);

        // old sale - outside date range
        oldSale = new Sale();
        oldSale.setLocation(testLocation);
        oldSale.setSoldAt(LocalDateTime.now().minusMonths(6));
        saleRepository.save(oldSale);

        SaleLine oldLine = new SaleLine();
        oldLine.setSale(oldSale);
        oldLine.setMenuItem(testMenuItem);
        oldLine.setQuantitySold(new BigDecimal("1.00"));
        oldLine.setUnitPrice(new BigDecimal("9.99"));
        saleLineRepository.save(oldLine);
    }

    // --- SaleRepository tests ---

    @Test
    void findByLocationIdOrderBySoldAtDesc_shouldReturnSalesForLocation() {
        List<Sale> result = saleRepository
                .findSaleByLocationIdOrderBySoldAtDesc(testLocation.getId(), Pageable.unpaged());

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertTrue(result.stream()
                .allMatch(s -> s.getLocation().getId().equals(testLocation.getId())));
    }

    @Test
    void findByLocationIdOrderBySoldAtDesc_shouldReturnInDescendingOrder() {
        List<Sale> result = saleRepository
                .findSaleByLocationIdOrderBySoldAtDesc(testLocation.getId(), Pageable.unpaged());

        Assertions.assertTrue(result.size() >= 2);
        Assertions.assertTrue(
                result.get(0).getSoldAt().isAfter(result.get(1).getSoldAt()) ||
                        result.get(0).getSoldAt().isEqual(result.get(1).getSoldAt())
        );
    }

    //TODO: failing test
    @Test
    void findByLocationIdAndSoldAtBetween_shouldReturnOnlySalesWithinRange() {
        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to = LocalDateTime.now().plusDays(1);

        List<Sale> result = saleRepository
                .findSaleByLocationIdAndSoldAtBetween(testLocation.getId(), from, to, Pageable.unpaged());

        Assertions.assertEquals(1, result.size());
        Assertions.assertTrue(result.get(0).getSoldAt().isAfter(from));
        Assertions.assertTrue(result.get(0).getSoldAt().isBefore(to));
    }

    @Test
    void findByLocationIdAndSoldAtBetween_shouldReturnEmptyWhenNoSalesInRange() {
        LocalDateTime from = LocalDateTime.now().minusDays(10);
        LocalDateTime to = LocalDateTime.now().minusDays(5);

        List<Sale> result = saleRepository
                .findSaleByLocationIdAndSoldAtBetween(testLocation.getId(), from, to, Pageable.unpaged());

        Assertions.assertTrue(result.isEmpty());
    }

    // --- SaleLineRepository tests ---

    @Test
    void findTotalRevenueByLocation_shouldSumAllSaleLines() {
        BigDecimal result = saleLineRepository.findTotalRevenueByLocation(testLocation.getId());

        // recentLine: 2.00 * 9.99 = 19.98, oldLine: 1.00 * 9.99 = 9.99, total = 29.97
        Assertions.assertEquals(0, new BigDecimal("29.97").compareTo(result));
    }

    @Test
    void findTotalRevenueByLocationAndDateRange_shouldOnlySumWithinRange() {
        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to = LocalDateTime.now().plusDays(1);

        BigDecimal result = saleLineRepository
                .findTotalRevenueByLocationAndWithinDataRange(testLocation.getId(), from, to);

        // only recentLine: 2.00 * 9.99 = 19.98
        Assertions.assertEquals(0, new BigDecimal("19.98").compareTo(result));
    }

    @Test
    void findMostSoldByLocation_shouldReturnItemsOrderedByRevenue() {
        List<MenuItemSalesProjection> result = saleLineRepository
                .findMostSoldByLocation(testLocation.getId(), Pageable.ofSize(5));

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(testMenuItem.getName(), result.get(0).getMenuItemName());
    }

    @Test
    void findMostSoldByLocation_shouldReturnEmptyForUnknownLocation() {
        List<MenuItemSalesProjection> result = saleLineRepository
                .findMostSoldByLocation(999L, Pageable.ofSize(5));

        Assertions.assertTrue(result.isEmpty());
    }
}