package org.jalvarez.stockly;


import org.jalvarez.stockly.inventory.controller.InventoryLotController;
import org.jalvarez.stockly.inventory.dto.InventoryLotDto;
import org.jalvarez.stockly.inventory.service.InventoryLotService;
import org.jalvarez.stockly.util.EntityLookupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InventoryLotController.class)
public class InventoryLotControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private InventoryLotService inventoryLotService;

    @MockitoBean
    private EntityLookupService entityLookupService;

    private InventoryLotDto testDto;
    private InventoryLotDto testExpiredDto;
    private InventoryLotDto testExpiringDto;

    @BeforeEach
    void setUp() {
        testDto = new InventoryLotDto(
                1L,
                "Cheese",
                "Main Warehouse",
                new BigDecimal("25.00"),
                new BigDecimal("10.00"),
                LocalDate.now().plusDays(30)
        );
        testExpiredDto = new InventoryLotDto(
                2L,
                "Cheese",
                "Main Warehouse",
                new BigDecimal("25.00"),
                new BigDecimal("10.00"),
                LocalDate.now().minusDays(10)
        );
        testExpiringDto = new InventoryLotDto(
                3L,
                "Cheese",
                "Main Warehouse",
                new BigDecimal("25.00"),
                new BigDecimal("10.00"),
                LocalDate.now().plusDays(6)

        );
    }

    @Test
    void getInventoryLotsByLocationAndIngredientsShouldReturn200WithLots() throws Exception {
        when(inventoryLotService.getByLocationAndIngredient(eq(1L),eq(1L), any(Pageable.class)))
                .thenReturn(List.of(testDto));

        mvc.perform(get("/api/inventory-lot/by-ingredient")
                        .param("locationId", "1")
                        .param("ingredientId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getInventoryLotsByLocationShouldReturn200WithLots() throws Exception {
        when(inventoryLotService.getByLocation(eq(1L), any(Pageable.class)))
            .thenReturn(List.of(testDto));

        mvc.perform(
                get("/api/inventory-lot/by-location")
                        .param("locationId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

    }

    @Test
    void getExpiredLotsByLocationShouldReturn200WithLots() throws Exception {
        when(inventoryLotService.getExpiredLotsByLocation(eq(2L), any(Pageable.class)))
            .thenReturn(List.of(testExpiredDto));

        mvc.perform(
                get("/api/inventory-lot/expired-by-location")
                        .param("locationId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getExpiringLotsByLocationShouldReturn200WithLots() throws Exception {
        when(inventoryLotService.getLotsExpiringByLocation(eq(3L), eq(10), any(Pageable.class)))
            .thenReturn(List.of(testExpiringDto));

        mvc.perform(
                get("/api/inventory-lot/expiring-by-location")
                    .param("locationId", "3")
                    .param("days", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

    }


}
