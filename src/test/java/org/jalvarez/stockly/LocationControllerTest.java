package org.jalvarez.stockly;

import org.jalvarez.stockly.location.controller.LocationController;
import org.jalvarez.stockly.location.dto.LocationDto;
import org.jalvarez.stockly.location.service.LocationService;
import org.jalvarez.stockly.util.EntityLookupService;
import org.jalvarez.stockly.util.exception.LocationNotFoundException;
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

@WebMvcTest(LocationController.class)
public class LocationControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    public LocationService locationService;

    @MockitoBean
    private EntityLookupService entityLookupService;


    private LocationDto testLocationDto;

    @BeforeEach
    void setUp() {
        testLocationDto = new LocationDto(
                1L,
                "Main Warehouse",
                "123 Main St",
                "Atlanta",
                "GA",
                "US",
                "30043"
        );
    }

    @Test
    void getAllLocations_shouldReturn200WithLocations() throws Exception {
        when(locationService.getAllLocations(any(Pageable.class)))
                .thenReturn(List.of(testLocationDto));

        mvc.perform(get("/api/locations/all-locations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Main Warehouse"));
    }

    // --- GET /api/locations/locations-by-city ---

    @Test
    void getLocationsByCity_shouldReturn200WithLocations() throws Exception {
        when(locationService.getLocationByCity(eq("Atlanta"), any(Pageable.class)))
                .thenReturn(List.of(testLocationDto));

        mvc.perform(get("/api/locations/locations-by-city")
                        .param("city", "Atlanta"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].city").value("Atlanta"));
    }

    @Test
    void getLocationsByCity_missingParam_shouldReturn400() throws Exception {
        mvc.perform(get("/api/locations/locations-by-city"))
                .andExpect(status().isBadRequest());
    }

    // --- GET /api/locations/locations-by-state ---

    @Test
    void getLocationsByState_shouldReturn200WithLocations() throws Exception {
        when(locationService.getLocationByState(eq("GA"), any(Pageable.class)))
                .thenReturn(List.of(testLocationDto));

        mvc.perform(get("/api/locations/locations-by-state")
                        .param("state", "GA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].state").value("GA"));
    }

    @Test
    void getLocationsByState_missingParam_shouldReturn400() throws Exception {
        mvc.perform(get("/api/locations/locations-by-state"))
                .andExpect(status().isBadRequest());
    }

    // --- GET /api/locations/location-by-name ---

    @Test
    void getLocationByName_shouldReturn200() throws Exception {
        when(locationService.getLocationByName("Main Warehouse"))
                .thenReturn(testLocationDto);

        mvc.perform(get("/api/locations/location-by-name")
                        .param("name", "Main Warehouse"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Main Warehouse"));
    }

    @Test
    void getLocationByName_missingParam_shouldReturn400() throws Exception {
        mvc.perform(get("/api/locations/location-by-name"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getLocationByName_notFound_shouldReturn404() throws Exception {
        when(locationService.getLocationByName("nonexistent"))
                .thenThrow(new LocationNotFoundException("Location not found: nonexistent"));

        mvc.perform(get("/api/locations/location-by-name")
                        .param("name", "nonexistent"))
                .andExpect(status().isNotFound());
    }

    // --- GET /api/locations/{id} ---

    @Test
    void getLocationById_shouldReturn200() throws Exception {
        when(locationService.getLocationById(1L))
                .thenReturn(testLocationDto);

        mvc.perform(get("/api/locations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Main Warehouse"));
    }

    @Test
    void getLocationById_notFound_shouldReturn404() throws Exception {
        when(locationService.getLocationById(99L))
                .thenThrow(new LocationNotFoundException("Location not found: 99"));

        mvc.perform(get("/api/locations/99"))
                .andExpect(status().isNotFound());
    }
}

