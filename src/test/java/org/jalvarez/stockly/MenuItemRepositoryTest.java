package org.jalvarez.stockly;

import org.jalvarez.stockly.ingredient.repository.IngredientRepository;
import org.jalvarez.stockly.ingredient.repository.IngredientTypeRepository;
import org.jalvarez.stockly.menu.model.MenuItem;
import org.jalvarez.stockly.menu.model.RecipeLine;
import org.jalvarez.stockly.menu.repository.MenuItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ActiveProfiles("test")
@Transactional
public class MenuItemRepositoryTest {

    private static final DockerImageName myImage = DockerImageName.parse("postgres:16-alpine").asCompatibleSubstituteFor("postgres");

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer(myImage);

    @Autowired
    MenuItemRepository menuItemRepository;

    private MenuItem activeItem;
    private MenuItem inactiveItem;

    @BeforeEach
    void setUp() {
        String unique = UUID.randomUUID().toString().substring(0, 8);

        activeItem = new MenuItem();
        activeItem.setName("Burger-" + unique);
        activeItem.setPrice(new BigDecimal("9.99"));
        activeItem.setIsActive(true);
        menuItemRepository.save(activeItem);

        inactiveItem = new MenuItem();
        inactiveItem.setName("Pizza-" + unique);
        inactiveItem.setPrice(new BigDecimal("12.99"));
        inactiveItem.setIsActive(false);
        menuItemRepository.save(inactiveItem);
    }
    @Test
    void findNameShouldReturnCorrectName() {
        MenuItem result = menuItemRepository.findByName(activeItem.getName());
        Assertions.assertEquals(activeItem.getName(), result.getName());
    }

    @Test
    void findActivityShouldReturnActiveMenuItems() {
        List<MenuItem> activeResult = menuItemRepository.findAllByIsActive(true, Pageable.unpaged());
        Assertions.assertFalse(activeResult.isEmpty());
        Assertions.assertTrue(activeResult.stream().allMatch(MenuItem::getIsActive));

    }

    @Test//MenuItemRepositoryTest.findActivityShouldReturnInactiveMenuItems:82 expected: <true> but was: <false>
    void findActivityShouldReturnInactiveMenuItems() {
        List<MenuItem> inactiveResult = menuItemRepository.findAllByIsActive(false, Pageable.unpaged());
        Assertions.assertFalse(inactiveResult.isEmpty());
        Assertions.assertTrue(inactiveResult.stream().noneMatch(MenuItem::getIsActive));
    }

}
