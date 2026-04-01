package org.jalvarez.stockly;

import org.jalvarez.stockly.util.enums.UnitEnum;
import org.jalvarez.stockly.ingredient.model.Ingredient;
import org.jalvarez.stockly.ingredient.model.IngredientType;
import org.jalvarez.stockly.ingredient.repository.IngredientRepository;
import org.jalvarez.stockly.ingredient.repository.IngredientTypeRepository;
import org.jalvarez.stockly.inventory.model.InventoryLot;
import org.jalvarez.stockly.inventory.repository.InventoryLotRepository;
import org.jalvarez.stockly.location.model.Location;
import org.jalvarez.stockly.location.repository.LocationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ActiveProfiles("test")
@Transactional
public class InventoryLotRepositoryTest {

    private static final DockerImageName myImage = DockerImageName.parse("postgres:16-alpine").asCompatibleSubstituteFor("postgres");
    private static final UUID UUID = new UUID(0L, 0L);

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer(myImage);

    @Autowired
    InventoryLotRepository inventoryLotRepository;
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    IngredientRepository ingredientRepository;
    @Autowired
    IngredientTypeRepository ingredientTypeRepository;

    private Ingredient testIngredient;
    private Location testLocation;

    @BeforeEach
    void setUp() {
        String unique = UUID.randomUUID().toString().substring(0, 8);

        IngredientType ingredientType = new IngredientType();
        ingredientType.setIsPerishable(Boolean.TRUE);
        ingredientType.setType("Dairy" + unique);
        ingredientTypeRepository.save(ingredientType);

        testIngredient = new Ingredient();
        testIngredient.setIngredientType(ingredientType);
        testIngredient.setName("Whole Milk");
        testIngredient.setDescription("Fresh whole milk");
        testIngredient.setUnitCost(new BigDecimal("2.50"));
        testIngredient.setBaseUom(UnitEnum.GALLONS);
        testIngredient.setParLevelQty(new BigDecimal("20.00"));
        ingredientRepository.save(testIngredient);

        testLocation = new Location();
        testLocation.setName("Main Warehouse");
        testLocation.setAddress("123 Main St");
        testLocation.setCity("Atlanta");
        testLocation.setState("GA");
        testLocation.setCountry("US");
        testLocation.setZip("30043");
        locationRepository.save(testLocation);

    }

    private InventoryLot buildLot(Location testLocation, Ingredient testIngredient, BigDecimal remainingQty, LocalDate expiryDate) {
        InventoryLot lot = new InventoryLot();
        lot.setLocation(testLocation);
        lot.setIngredient(testIngredient);
        lot.setRemainingQty(remainingQty);
        lot.setQuantityReceived(new BigDecimal("20.00"));
        lot.setTotalCostOfLot(remainingQty.multiply(new BigDecimal("2.50")));
        lot.setExpiryDate(expiryDate);
        return lot;
    }

    @Test
    void findByLocationAndRemainingQtyGreaterThanZeroShouldOnlyReturnNonEmptyLots() {
        InventoryLot activeLot = buildLot(testLocation, testIngredient, new BigDecimal("10.00"), LocalDate.now().plusDays(30));
        InventoryLot emptyLot = buildLot(testLocation, testIngredient, BigDecimal.ZERO, LocalDate.now().plusDays(30));

        inventoryLotRepository.save(activeLot);
        inventoryLotRepository.save(emptyLot);

        List<InventoryLot> result = inventoryLotRepository.findByLocationAndRemainingQtyGreaterThan(testLocation, BigDecimal.ZERO, Pageable.unpaged());

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(activeLot, result.get(0));
    }

    @Test
    void findByLocationAndIngredientShouldOnlyReturnMatchingIngredientLots() { //make sure received qty is greater than 0
        Ingredient otherIngredient = new Ingredient();
        otherIngredient.setIngredientType(testIngredient.getIngredientType());
        otherIngredient.setName("Skim Milk");
        otherIngredient.setDescription("Fresh skim milk");
        otherIngredient.setUnitCost(new BigDecimal("2.00"));
        otherIngredient.setBaseUom(UnitEnum.GALLONS);
        otherIngredient.setParLevelQty(new BigDecimal("15.00"));
        ingredientRepository.save(otherIngredient);

        InventoryLot correctLot = buildLot(testLocation, testIngredient, new BigDecimal("15.00"), LocalDate.now().plusDays(30));
        InventoryLot otherLot = buildLot(testLocation, otherIngredient, new BigDecimal("15.00"), LocalDate.now().plusDays(30));

        inventoryLotRepository.save(correctLot);
        inventoryLotRepository.save(otherLot);

        List<InventoryLot> result = inventoryLotRepository.findByLocationAndIngredient(testLocation, testIngredient, Pageable.unpaged());
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(correctLot, result.get(0));

    }

    @Test
    void findByLocationAndExpiryDateLessThanAndRemainingQtyGreaterThanShouldReturnNonEmptyLots() {
        InventoryLot expiredLot = buildLot(testLocation, testIngredient, new BigDecimal("10.00"), LocalDate.now().minusDays(10));
        InventoryLot expiredEmptyLot = buildLot(testLocation, testIngredient, BigDecimal.ZERO, LocalDate.now().minusDays(10));
        InventoryLot activeLot = buildLot(testLocation, testIngredient, new BigDecimal("10.00"), LocalDate.now().plusDays(30));

        inventoryLotRepository.save(expiredLot);
        inventoryLotRepository.save(expiredEmptyLot);
        inventoryLotRepository.save(activeLot);

        List<InventoryLot> result = inventoryLotRepository.findByLocationAndExpiryDateLessThanAndRemainingQtyGreaterThan(testLocation, LocalDate.now(),BigDecimal.ZERO, Pageable.unpaged());
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(expiredLot, result.get(0));
    }

    @Test
    void findByLocationAndExpiryDateBetweenShouldReturnLotsOnlyInRange() {
        InventoryLot expiringLot = buildLot(testLocation, testIngredient, new BigDecimal("10.00"), LocalDate.now().plusDays(5));
        InventoryLot anotherExpiringLot = buildLot(testLocation, testIngredient, new BigDecimal("20.00"), LocalDate.now().plusDays(6));
        InventoryLot safeLot = buildLot(testLocation, testIngredient, new BigDecimal("10.00"), LocalDate.now().plusDays(30));

        inventoryLotRepository.save(expiringLot);
        inventoryLotRepository.save(anotherExpiringLot);
        inventoryLotRepository.save(safeLot);

        List<InventoryLot> result = inventoryLotRepository.findByLocationAndExpiryDateBetween(testLocation, LocalDate.now(), LocalDate.now().plusDays(10), Pageable.unpaged());

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(expiringLot, result.get(0));
        Assertions.assertEquals(anotherExpiringLot, result.get(1));
    }

    @Test
    void totalRemainingQtyShouldSumOnlyRemainingStock() {
        InventoryLot lotA = buildLot(testLocation, testIngredient, new BigDecimal("10.00"), LocalDate.now().plusDays(30));
        InventoryLot lotB = buildLot(testLocation, testIngredient, new BigDecimal("5.00"), LocalDate.now().plusDays(30));
        InventoryLot emptyLot = buildLot(testLocation, testIngredient, BigDecimal.ZERO, LocalDate.now().plusDays(30));
        inventoryLotRepository.saveAll(List.of(lotA, lotB, emptyLot));

        BigDecimal result = inventoryLotRepository.getTotalRemainingQty(testLocation.getId());
        Assertions.assertEquals(15.00 , result.doubleValue());
    }

    @Test
    void totalCostOfLotShouldSumOnlyRemainingStock() {
        InventoryLot lotA = buildLot(testLocation, testIngredient, new BigDecimal("10.00"), LocalDate.now().plusDays(30));
        InventoryLot lotB = buildLot(testLocation, testIngredient, new BigDecimal("5.00"), LocalDate.now().plusDays(30));
        InventoryLot emptyLot = buildLot(testLocation, testIngredient, BigDecimal.ZERO, LocalDate.now().plusDays(30));
        inventoryLotRepository.saveAll(List.of(lotA, lotB, emptyLot));

        BigDecimal result = inventoryLotRepository.getTotalCostOfLot(testLocation.getId());
        Assertions.assertEquals(37.50 , result.doubleValue());
    }
}
