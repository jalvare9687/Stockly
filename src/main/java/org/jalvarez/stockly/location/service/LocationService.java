package org.jalvarez.stockly.location.service;

import org.jalvarez.stockly.location.model.Location;
import org.jalvarez.stockly.location.model.LocationSupplier;
import org.jalvarez.stockly.location.repository.LocationRepository;
import org.jalvarez.stockly.location.repository.LocationSupplierRepository;
import org.jalvarez.stockly.util.EntityLookupService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) //TODO: create DTO's and null checks
public class LocationService {
    private final LocationSupplierRepository locationSupplierRepository;
    private final LocationRepository locationRepository;
    private final EntityLookupService entityLookupService;

    public LocationService(LocationSupplierRepository locationSupplierRepository, LocationRepository locationRepository, EntityLookupService entityLookupService) {
        this.locationSupplierRepository = locationSupplierRepository;
        this.locationRepository = locationRepository;
        this.entityLookupService = entityLookupService;
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Location getLocationByName(String locationName) {
        return locationRepository.findByName(locationName);
    }
    public Location getLocationById(Long locationId) {
        entityLookupService.findLocation(locationId);
        return locationRepository.findById(locationId).get();
    }

    public List<Location> getLocationByCity(String city, Pageable pageable) {
        return locationRepository.findByCity(city, pageable);
    }
    public List<Location> getLocationByState(String state, Pageable pageable) {
        return locationRepository.findByState(state, pageable);
    }

    public List<LocationSupplier> getLocationSupplierByLocationId(Long locationId, Pageable pageable) {
        return locationSupplierRepository.findByLocationId(locationId, pageable);
    }

    public List<LocationSupplier> getLocationSupplierByLocationIdAndActivity(Long locationId, Boolean activity, Pageable pageable) {
        return locationSupplierRepository.findByLocationIdAndIsActiveSupplier(locationId, activity, pageable);
    }

}
