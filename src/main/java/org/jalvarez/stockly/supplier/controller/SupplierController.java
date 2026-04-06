package org.jalvarez.stockly.supplier.controller;

import org.jalvarez.stockly.supplier.dto.LocationSupplierDto;
import org.jalvarez.stockly.supplier.dto.SupplierDto;
import org.jalvarez.stockly.supplier.service.SupplierService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping("location-supplier-by-location")
    public List<LocationSupplierDto> getLocationSupplierByLocation(@RequestParam long locationId, Pageable pageable) {
        return supplierService.getLocationSupplierByLocationId(locationId, pageable);
    }

    @GetMapping("location-supplier-by-location-activity")
    public List<LocationSupplierDto> getLocationSupplierByLocationActivity(@RequestParam long locationId, @RequestParam  Boolean activity, Pageable pageable) {
        return supplierService.getLocationSupplierByLocationIdAndActivity(locationId, activity, pageable);
    }

    @GetMapping("all")
    public List<SupplierDto> getAllSuppliers(Pageable pageable) {
        return supplierService.getAllSuppliers(pageable);
    }

    @GetMapping("{id}")
    public SupplierDto getSupplier(@PathVariable Long id) {
        return supplierService.getSupplierById(id);
    }

    @GetMapping("supplier-by-name")
    public SupplierDto getSupplierByName(@RequestParam String name) {
        return supplierService.getSupplierByName(name);
    }

    @GetMapping("supplier-by-email")
    public SupplierDto getSupplierByEmail(@RequestParam String email) {
        return supplierService.getSupplierByEmail(email);
    }

    @GetMapping("supplier-by-phone")
    public SupplierDto getSupplierByPhone(@RequestParam String phone) {
        return supplierService.getSupplierByPhone(phone);
    }

}
