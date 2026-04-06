package org.jalvarez.stockly;

import org.jalvarez.stockly.supplier.controller.SupplierController;
import org.jalvarez.stockly.supplier.dto.LocationSupplierDto;
import org.jalvarez.stockly.supplier.dto.SupplierDto;
import org.jalvarez.stockly.supplier.model.id.LocationSupplierId;
import org.jalvarez.stockly.supplier.service.SupplierService;
import org.jalvarez.stockly.util.EntityLookupService;
import org.jalvarez.stockly.util.exception.SupplierNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SupplierController.class)
public class SupplierControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    public SupplierService supplierService;

    @MockitoBean
    private EntityLookupService entityLookupService;

    private LocationSupplierDto testLocationSupplierDto;
    private LocationSupplierDto testLocationSupplierDto2;
    private SupplierDto testSupplierDto;

    @BeforeEach
    void setUp() {
        testSupplierDto = new SupplierDto(
                1L,
                "Test Supplier",
                "supplier@test.com",
                "555-1234"
        );

        testLocationSupplierDto = new LocationSupplierDto(
                new LocationSupplierId(1L, 1L),  // locationId// supplierId
                true,
                3L,
                5L,
                "Primary supplier"
        );
        testLocationSupplierDto2 = new LocationSupplierDto(
                new LocationSupplierId(2L, 1L),  // locationId// supplierId
                false,
                3L,
                5L,
                "Secondary supplier"
        );
    }

    @Test
    void getLocationSupplierByLocationShouldReturn200OK() throws Exception {
        when(supplierService.getLocationSupplierByLocationId(eq(1L), any(Pageable.class)))
                .thenReturn(List.of(testLocationSupplierDto));

        mvc.perform(get("/api/suppliers/location-supplier-by-location")
                .param("locationId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].description").value("Primary supplier"));
    }

    @Test
    void getLocationSupplierByLocationMissingParam_shouldReturn400() throws Exception {
        mvc.perform(get("/api/suppliers/location-supplier-by-location"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getLocationSupplierByLocationAndActivityShouldReturn200OK() throws Exception {
        when(supplierService.getLocationSupplierByLocationIdAndActivity(eq(1L), eq(true), any(Pageable.class))
        ).thenReturn(List.of(testLocationSupplierDto));

        mvc.perform(
                get("/api/suppliers/location-supplier-by-location-activity")
                        .param("locationId", "1")
                        .param("activity", "true")
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].description").value("Primary supplier"));
    }

    @Test
    void getLocationSupplierByLocationAndActivityNoParam_shouldReturn400() throws Exception {
        mvc.perform(get("/api/suppliers/location-supplier-by-location-activity"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getSupplierShouldReturn200OK() throws Exception {
        when(supplierService.getSupplierById(1L)).thenReturn(testSupplierDto);

        mvc.perform(
                get("/api/suppliers/1")
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Supplier"));
    }

    @Test
    void getSupplierIdShouldReturn404WithInvalidId() throws Exception {
        when(supplierService.getSupplierById(99L))
                .thenThrow(new SupplierNotFoundException("Supplier not found: 99"));
        mvc.perform(
                get("/api/suppliers/99")
        ).andExpect(status().isNotFound());
    }

    // --- GET /api/suppliers/supplier-by-name ---

    @Test
    void getSupplierByName_shouldReturn200() throws Exception {
        when(supplierService.getSupplierByName("Test Supplier"))
                .thenReturn(testSupplierDto);

        mvc.perform(get("/api/suppliers/supplier-by-name")
                        .param("name", "Test Supplier"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Supplier"))
                .andExpect(jsonPath("$.email").value("supplier@test.com"));
    }

    @Test
    void getSupplierByName_missingParam_shouldReturn400() throws Exception {
        mvc.perform(get("/api/suppliers/supplier-by-name"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getSupplierByName_notFound_shouldReturn404() throws Exception {
        when(supplierService.getSupplierByName("nonexistent"))
                .thenThrow(new SupplierNotFoundException("Supplier not found: nonexistent"));

        mvc.perform(get("/api/suppliers/supplier-by-name")
                        .param("name", "nonexistent"))
                .andExpect(status().isNotFound());
    }

// --- GET /api/suppliers/supplier-by-email ---

    @Test
    void getSupplierByEmail_shouldReturn200() throws Exception {
        when(supplierService.getSupplierByEmail("supplier@test.com"))
                .thenReturn(testSupplierDto);

        mvc.perform(get("/api/suppliers/supplier-by-email")
                        .param("email", "supplier@test.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("supplier@test.com"));
    }

    @Test
    void getSupplierByEmail_missingParam_shouldReturn400() throws Exception {
        mvc.perform(get("/api/suppliers/supplier-by-email"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getSupplierByEmail_notFound_shouldReturn404() throws Exception {
        when(supplierService.getSupplierByEmail("notfound@test.com"))
                .thenThrow(new SupplierNotFoundException("Supplier not found: notfound@test.com"));

        mvc.perform(get("/api/suppliers/supplier-by-email")
                        .param("email", "notfound@test.com"))
                .andExpect(status().isNotFound());
    }

// --- GET /api/suppliers/supplier-by-phone ---

    @Test
    void getSupplierByPhone_shouldReturn200() throws Exception {
        when(supplierService.getSupplierByPhone("555-1234"))
                .thenReturn(testSupplierDto);

        mvc.perform(get("/api/suppliers/supplier-by-phone")
                        .param("phone", "555-1234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phoneNumber").value("555-1234"));
    }

    @Test
    void getSupplierByPhone_missingParam_shouldReturn400() throws Exception {
        mvc.perform(get("/api/suppliers/supplier-by-phone"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getSupplierByPhone_notFound_shouldReturn404() throws Exception {
        when(supplierService.getSupplierByPhone("000-0000"))
                .thenThrow(new SupplierNotFoundException("Supplier not found: 000-0000"));

        mvc.perform(get("/api/suppliers/supplier-by-phone")
                        .param("phone", "000-0000"))
                .andExpect(status().isNotFound());
    }
}
