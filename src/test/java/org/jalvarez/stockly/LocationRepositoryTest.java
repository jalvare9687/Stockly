package org.jalvarez.stockly;

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

import java.util.List;
import java.util.UUID;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ActiveProfiles("test")
@Transactional
public class LocationRepositoryTest {

    private static final DockerImageName myImage = DockerImageName.parse("postgres:16-alpine").asCompatibleSubstituteFor("postgres");
    private static final UUID UUID = new UUID(0L, 0L);

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer(myImage);

    @Autowired
    private LocationRepository locationRepository;

    private Location testLocation;

    @BeforeEach
    public void setUp() {
        testLocation = new Location();
        testLocation.setName("Main Warehouse");
        testLocation.setAddress("123 Main St");
        testLocation.setCity("Atlanta");
        testLocation.setState("GA");
        testLocation.setCountry("US");
        testLocation.setZip("30043");
        locationRepository.save(testLocation);
    }

    @Test
    public void findByNameShouldReturnMainWarehouse() {
        Location wrongLocation = new Location();
        wrongLocation.setName("South Warehouse");
        wrongLocation.setAddress("123 Main St");
        wrongLocation.setCity("Atlanta");
        wrongLocation.setState("GA");
        wrongLocation.setCountry("US");
        wrongLocation.setZip("30043");
        locationRepository.save(testLocation);

        Location result = locationRepository.findByName(testLocation.getName());

        Assertions.assertEquals(testLocation, result);

    }

    @Test
    public void findByNameShouldReturnNothingWithWrongName() {
        Location result = locationRepository.findByName("Random name");
        Assertions.assertNull(result);
    }

    @Test
    public void findByCityShouldReturnMainWarehouse() {
        Location wrongLocation = new Location();
        wrongLocation.setName("South Warehouse");
        wrongLocation.setAddress("123 Main St");
        wrongLocation.setCity("Milledgeville");
        wrongLocation.setState("GA");
        wrongLocation.setCountry("US");
        wrongLocation.setZip("30043");
        locationRepository.save(wrongLocation);

        List<Location> result = locationRepository.findByCity(testLocation.getCity(), Pageable.unpaged());

        Assertions.assertEquals(testLocation, result.get(0));

    }

    @Test
    public void findByCityShouldReturnNothingWithWrongCity() {
        List<Location> result = locationRepository.findByCity("Barksville", Pageable.unpaged());

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findByStateShouldReturnMainWarehouse() {
        Location wrongLocation = new Location();
        wrongLocation.setName("South Warehouse");
        wrongLocation.setAddress("123 Main St");
        wrongLocation.setCity("Milledgeville");
        wrongLocation.setState("AL");
        wrongLocation.setCountry("US");
        wrongLocation.setZip("30043");
        locationRepository.save(wrongLocation);

        List<Location> result = locationRepository.findByState(testLocation.getState(), Pageable.unpaged());

        Assertions.assertEquals(testLocation, result.get(0));
    }

}
