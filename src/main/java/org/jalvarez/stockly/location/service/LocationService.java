package org.jalvarez.stockly.location.service;

import org.jalvarez.stockly.location.dto.LocationDto;
import org.jalvarez.stockly.location.dto.LocationMapper;
import org.jalvarez.stockly.location.model.Location;
import org.jalvarez.stockly.location.repository.LocationRepository;
import org.jalvarez.stockly.util.EntityLookupService;
import org.jalvarez.stockly.util.exception.LocationNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LocationService {
    private final LocationRepository locationRepository;
    private final EntityLookupService entityLookupService;

    public LocationService(LocationRepository locationRepository, EntityLookupService entityLookupService) {
        this.locationRepository = locationRepository;
        this.entityLookupService = entityLookupService;
    }

    public List<LocationDto> getAllLocations(Pageable pageable) {
        return locationRepository.findAll(pageable).stream().map(LocationMapper::toDto).toList();
    }

    public LocationDto getLocationByName(String locationName) {
        Location location = locationRepository.findByName(locationName);
        if (location == null) { throw new LocationNotFoundException(locationName);}
        return LocationMapper.toDto(location);
    }
    public LocationDto getLocationById(Long locationId) {
        return LocationMapper.toDto(entityLookupService.findLocation(locationId));
    }

    public List<LocationDto> getLocationByCity(String city, Pageable pageable) {
        return locationRepository.findByCity(city, pageable).stream().map(LocationMapper::toDto).toList();
    }
    public List<LocationDto> getLocationByState(String state, Pageable pageable) {
        return locationRepository.findByState(state, pageable).stream().map(LocationMapper::toDto).toList();
    }


}
