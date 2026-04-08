package org.jalvarez.stockly;

import org.jalvarez.stockly.supplier.controller.SupplierItemController;
import org.jalvarez.stockly.supplier.dto.SupplierItemDto;
import org.jalvarez.stockly.supplier.service.SupplierItemService;
import org.jalvarez.stockly.util.enums.PackUom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SupplierItemController.class)
public class SupplierItemControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private SupplierItemService supplierItemService;

    private SupplierItemDto testSupplierItemDto;

    @BeforeEach
    public void setup() {
        testSupplierItemDto = new SupplierItemDto(
                1L,          // id
                1L,          // supplierId
                1L,          // ingredientId
                "SKU-001",   // vendorSku
                new BigDecimal("10.00"),  // packSize
                PackUom.KG,  // packUom
                new BigDecimal("25.00"),  // defaultCost
                true,        // primary
                true         // active
        );
    }

    @Test
    void getAllByActivityAndSupplier_ShouldReturn200OK() throws Exception {
        when(supplierItemService.getAllByActivityAndSupplier(eq(true), eq(1L), any(Pageable.class)))
                .thenReturn(List.of(testSupplierItemDto));

        mvc.perform(
                get("/api/supplier-items/active-items-by-supplier")
                        .param("activity", String.valueOf(true))
                        .param("supplierId", String.valueOf(1L))
        )       .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void getAllByActivityAndSupplier_ShouldReturn400WithEmptyParam() throws Exception {
        mvc.perform(get("/api/supplier-items/active-items-by-supplier"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllActivePrimaryItemsBySupplierShouldReturn200OK() throws Exception {
        when(supplierItemService.getAllByPrimaryAndActiveAndSupplier(eq(true), eq(true), eq(1L), any(Pageable.class)))
                .thenReturn(List.of(testSupplierItemDto));

        mvc.perform(
                get("/api/supplier-items/active-and-primary-items-by-supplier")
                        .param("primary", String.valueOf(true))
                        .param("activity", String.valueOf(true))
                        .param("supplierId", String.valueOf(1L))

        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void getAllActivePrimaryItemsBySupplierShouldReturn400WithEmptyParam() throws Exception {
        mvc.perform(
                get("/api/supplier-items/active-and-primary-items-by-supplier")
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllByIngredient_ShouldReturn200OK() throws Exception {
        when(supplierItemService.getAllByIngredient(eq(1L), any(Pageable.class)))
                .thenReturn(List.of(testSupplierItemDto));

        mvc.perform(get("/api/supplier-items/items-by-ingredient")
                        .param("ingredientId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getAllByIngredient_ShouldReturn400WithMissingParam() throws Exception {
        mvc.perform(get("/api/supplier-items/items-by-ingredient"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllActivePrimaryByIngredient_ShouldReturn200OK() throws Exception {
        when(supplierItemService.getAllByIngredientActivityAndIsPrimary(eq(1L), eq(true), eq(true), any(Pageable.class)))
                .thenReturn(List.of(testSupplierItemDto));

        mvc.perform(get("/api/supplier-items/active-primary-items-by-ingredient")
                        .param("ingredientId", "1")
                        .param("activity", "true")
                        .param("isPrimary", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getAllActivePrimaryByIngredient_ShouldReturn400WithMissingParam() throws Exception {
        mvc.perform(get("/api/supplier-items/active-primary-items-by-ingredient"))
                .andExpect(status().isBadRequest());
    }
}
