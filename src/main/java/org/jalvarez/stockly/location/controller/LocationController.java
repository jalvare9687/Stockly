package org.jalvarez.stockly.location.controller;

import org.jalvarez.stockly.location.dto.LocationDto;
import org.jalvarez.stockly.location.service.LocationService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("all-locations")
    public List<LocationDto> getAllLocations(Pageable pageable) {
        return locationService.getAllLocations(pageable);
    }

    @GetMapping("locations-by-city")
    public List<LocationDto> getLocationsByCity(@RequestParam String city, Pageable pageable) {
        return locationService.getLocationByCity(city, pageable);
    }

    @GetMapping("locations-by-state")
    public List<LocationDto> getLocationsByState(@RequestParam String state, Pageable pageable) {
        return locationService.getLocationByState(state, pageable);
    }

    @GetMapping("location-by-name")
    public LocationDto getLocationsByName(@RequestParam String name) {
        return locationService.getLocationByName(name);
    }

    @GetMapping("/{id}")
    public LocationDto getLocationById(@PathVariable Long id) {
        return locationService.getLocationById(id);
    }


}
