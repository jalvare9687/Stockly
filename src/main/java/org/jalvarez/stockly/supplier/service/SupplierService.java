package org.jalvarez.stockly.supplier.service;

import org.jalvarez.stockly.supplier.dto.LocationSupplierDto;
import org.jalvarez.stockly.supplier.dto.SupplierDto;
import org.jalvarez.stockly.supplier.dto.SupplierMapper;
import org.jalvarez.stockly.supplier.model.Supplier;
import org.jalvarez.stockly.supplier.repository.LocationSupplierRepository;
import org.jalvarez.stockly.supplier.repository.SupplierRepository;
import org.jalvarez.stockly.util.EntityLookupService;
import org.jalvarez.stockly.util.exception.SupplierNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SupplierService {
    private final LocationSupplierRepository locationSupplierRepository;
    private final SupplierRepository supplierRepository;
    private final EntityLookupService entityLookupService;

    public SupplierService(LocationSupplierRepository locationSupplierRepository, SupplierRepository supplierRepository, EntityLookupService entityLookupService) {
        this.locationSupplierRepository = locationSupplierRepository;
        this.supplierRepository = supplierRepository;
        this.entityLookupService = entityLookupService;
    }

    public List<LocationSupplierDto> getLocationSupplierByLocationId(Long locationId, Pageable pageable) {
        entityLookupService.findLocation(locationId);
        return locationSupplierRepository.findByIdLocationId(locationId, pageable).stream().map(SupplierMapper::toSupplierDto).toList();
    }

    public List<LocationSupplierDto> getLocationSupplierByLocationIdAndActivity(Long locationId, Boolean activity, Pageable pageable) {
        entityLookupService.findLocation(locationId);
        return locationSupplierRepository.findByIdLocationIdAndIsActiveSupplier(locationId, activity, pageable).stream().map(SupplierMapper::toSupplierDto).toList();
    }

    public List<SupplierDto> getAllSuppliers(Pageable pageable) {
        return supplierRepository.findAll(pageable).stream().map(SupplierMapper::toSupplierDto).toList();
    }

    public SupplierDto getSupplierById(Long id) {
        return SupplierMapper.toSupplierDto(entityLookupService.findSupplier(id));
    }

    public SupplierDto getSupplierByName(String name) {
        Supplier supplier = supplierRepository.findByName(name);
        if (supplier == null) {
            throw new SupplierNotFoundException("Supplier with name " + name + " not found");
        }
        return SupplierMapper.toSupplierDto(supplier);
    }

    public SupplierDto getSupplierByEmail(String email) {
        Supplier supplier = supplierRepository.findByEmail(email);
        if (supplier == null) {
            throw new SupplierNotFoundException("Supplier with email " + email + " not found");
        }
        return SupplierMapper.toSupplierDto(supplier);
    }

    public SupplierDto getSupplierByPhone(String phone) {
        Supplier supplier = supplierRepository.findByPhoneNumber(phone);
        if (supplier == null) {
            throw new SupplierNotFoundException("Supplier with phone " + phone + " not found");
        }
        return SupplierMapper.toSupplierDto(supplierRepository.findByPhoneNumber(phone));
    }


}
