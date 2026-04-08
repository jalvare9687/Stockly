package org.jalvarez.stockly.supplier.controller;

import org.jalvarez.stockly.supplier.dto.SupplierItemDto;
import org.jalvarez.stockly.supplier.service.SupplierItemService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/supplier-items")
public class SupplierItemController {
    private final SupplierItemService supplierItemService;

    public SupplierItemController(SupplierItemService supplierItemService) {
        this.supplierItemService = supplierItemService;
    }

    @GetMapping("active-items-by-supplier")
    public List<SupplierItemDto> getAllByActivityAndSupplier(@RequestParam Boolean activity, @RequestParam Long supplierId, Pageable pageable) {
       return supplierItemService.getAllByActivityAndSupplier(activity, supplierId, pageable);
    }

    @GetMapping("active-and-primary-items-by-supplier")
    public List<SupplierItemDto> getAllActivePrimaryItemsBySupplier(@RequestParam Boolean primary,@RequestParam Boolean activity, @RequestParam Long supplierId, Pageable pageable) {
        return supplierItemService.getAllByPrimaryAndActiveAndSupplier(primary, activity, supplierId, pageable);
    }

    @GetMapping("items-by-ingredient")
    public List<SupplierItemDto> getAllByIngredient(@RequestParam Long ingredientId, Pageable pageable) {
        return supplierItemService.getAllByIngredient(ingredientId, pageable);
    }

    @GetMapping("active-primary-items-by-ingredient")
    public List<SupplierItemDto> getPrimaryActiveItemsByIngredient(@RequestParam Long ingredientId, @RequestParam Boolean activity, @RequestParam Boolean isPrimary, Pageable pageable) {
        return supplierItemService.getAllByIngredientActivityAndIsPrimary(ingredientId, activity, isPrimary, pageable);
    }


}
