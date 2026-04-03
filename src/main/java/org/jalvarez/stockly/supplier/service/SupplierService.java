package org.jalvarez.stockly.supplier.service;

import org.jalvarez.stockly.supplier.dto.LocationSupplierDto;
import org.jalvarez.stockly.supplier.dto.SupplierMapper;
import org.jalvarez.stockly.supplier.repository.LocationSupplierRepository;
import org.jalvarez.stockly.util.EntityLookupService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SupplierService {
    private final LocationSupplierRepository locationSupplierRepository;
    private EntityLookupService entityLookupService;
    public SupplierService(LocationSupplierRepository locationSupplierRepository) {
        this.locationSupplierRepository = locationSupplierRepository;
    }

    public List<LocationSupplierDto> getLocationSupplierByLocationId(Long locationId, Pageable pageable) {
        entityLookupService.findLocation(locationId);
        return locationSupplierRepository.findByIdLocationId(locationId, pageable).stream().map(SupplierMapper::toSupplierDto).toList();
    }

    public List<LocationSupplierDto> getLocationSupplierByLocationIdAndActivity(Long locationId, Boolean activity, Pageable pageable) {
        entityLookupService.findLocation(locationId);
        return locationSupplierRepository.findByIdLocationIdAndIsActiveSupplier(locationId, activity, pageable).stream().map(SupplierMapper::toSupplierDto).toList();
    }
}
