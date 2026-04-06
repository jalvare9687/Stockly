package org.jalvarez.stockly;

import org.jalvarez.stockly.location.model.Location;
import org.jalvarez.stockly.location.repository.LocationRepository;
import org.jalvarez.stockly.supplier.model.LocationSupplier;
import org.jalvarez.stockly.supplier.model.Supplier;
import org.jalvarez.stockly.supplier.model.id.LocationSupplierId;
import org.jalvarez.stockly.supplier.repository.LocationSupplierRepository;
import org.jalvarez.stockly.supplier.repository.SupplierRepository;
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
import java.util.List;
import java.util.UUID;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class SupplierRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16-alpine");

    @Autowired
    SupplierRepository supplierRepository;
    @Autowired
    LocationSupplierRepository locationSupplierRepository;
    @Autowired
    LocationRepository locationRepository;

    Supplier testSupplier;
    LocationSupplier testLocationSupplier;
    Location testLocation;
    LocationSupplier inactiveLocationSupplier;

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

        testSupplier = new Supplier();
        testSupplier.setName("Supplier-" + unique);
        testSupplier.setEmail("supplier-" + unique + "@test.com");
        testSupplier.setPhoneNumber("555-" + unique);
        supplierRepository.save(testSupplier);

        LocationSupplierId locationSupplierId = new LocationSupplierId(
                testSupplier.getId(),
                testLocation.getId()
        );

        testLocationSupplier = new LocationSupplier();
        testLocationSupplier.setId(locationSupplierId);
        testLocationSupplier.setIsActiveSupplier(true);
        testLocationSupplier.setDefaultLeadTimeDays(3L);
        testLocationSupplier.setDeliveryDays(5L);
        testLocationSupplier.setMinOrderAmount(new BigDecimal("100.00"));
        testLocationSupplier.setDescription("Primary supplier");
        locationSupplierRepository.save(testLocationSupplier);

        Supplier inactiveSupplier = new Supplier();
        inactiveSupplier.setName("Inactive-Supplier-" + unique);
        inactiveSupplier.setEmail("inactive-" + unique + "@test.com");
        inactiveSupplier.setPhoneNumber("999-" + unique);
        supplierRepository.save(inactiveSupplier);

        LocationSupplierId inactiveId = new LocationSupplierId(
                inactiveSupplier.getId(),
                testLocation.getId()
        );

        inactiveLocationSupplier = new LocationSupplier();
        inactiveLocationSupplier.setId(inactiveId);
        inactiveLocationSupplier.setIsActiveSupplier(false);
        inactiveLocationSupplier.setDefaultLeadTimeDays(7L);
        inactiveLocationSupplier.setDeliveryDays(10L);
        inactiveLocationSupplier.setMinOrderAmount(new BigDecimal("200.00"));
        inactiveLocationSupplier.setDescription("Inactive supplier");
        locationSupplierRepository.save(inactiveLocationSupplier);
    }

    @Test
    void findByIdShouldReturnTestSupplier() {
        String unique = UUID.randomUUID().toString().substring(0, 8);
        Supplier wrongSupplier = new Supplier();
        wrongSupplier.setName("Supplier-" + unique);
        wrongSupplier.setEmail("supplier-" + unique + "@test.com");
        wrongSupplier.setPhoneNumber("555-" + unique);
        supplierRepository.save(wrongSupplier);

        Supplier foundSupplier = supplierRepository.findById(testSupplier.getId()).get();

        Assertions.assertEquals(testSupplier, foundSupplier);
    }

    @Test
    void findByNameShouldReturnTestSupplier() {
        String unique = UUID.randomUUID().toString().substring(0, 8);
        Supplier wrongSupplier = new Supplier();
        wrongSupplier.setName("Supplier-" + unique);
        wrongSupplier.setEmail("supplier-" + unique + "@test.com");
        wrongSupplier.setPhoneNumber("555-" + unique);
        supplierRepository.save(wrongSupplier);

        Supplier result = supplierRepository.findByName(testSupplier.getName());
        Assertions.assertEquals(testSupplier, result);

    }

    @Test
    void findByEmailShouldReturnTestSupplier() {
        String unique = UUID.randomUUID().toString().substring(0, 8);
        Supplier wrongSupplier = new Supplier();
        wrongSupplier.setName("Supplier-" + unique);
        wrongSupplier.setEmail("supplier-" + unique + "@test.com");
        wrongSupplier.setPhoneNumber("555-" + unique);
        supplierRepository.save(wrongSupplier);

        Supplier result = supplierRepository.findByEmail(testSupplier.getEmail());
        Assertions.assertEquals(testSupplier, result);
    }

    @Test
    void findByPhoneNumberShouldReturnTestSupplier() {
        String unique = UUID.randomUUID().toString().substring(0, 8);
        Supplier wrongSupplier = new Supplier();
        wrongSupplier.setName("Supplier-" + unique);
        wrongSupplier.setEmail("supplier-" + unique + "@test.com");
        wrongSupplier.setPhoneNumber("555-" + unique);
        supplierRepository.save(wrongSupplier);

        Supplier result = supplierRepository.findByPhoneNumber(testSupplier.getPhoneNumber());
        Assertions.assertEquals(testSupplier, result);
    }

    @Test
    void findByLocationIdShouldReturnTestLocationSupplier() {
        List<LocationSupplier> result = locationSupplierRepository.findByIdLocationId(testLocationSupplier.getId().getLocationId(), Pageable.unpaged());
        Assertions.assertEquals(2, result.size());
    }

    @Test
    void findByLocationAndActivityShouldReturnTestLocationSupplier() {
        List<LocationSupplier> result = locationSupplierRepository.findByIdLocationIdAndIsActiveSupplier(testLocationSupplier.getId().getLocationId(), true, Pageable.unpaged());
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(testLocationSupplier.getId(), result.get(0).getId());
    }

}
