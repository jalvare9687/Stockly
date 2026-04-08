package org.jalvarez.stockly.supplier.service;

import org.jalvarez.stockly.supplier.dto.SupplierItemDto;
import org.jalvarez.stockly.supplier.dto.mapper.SupplierItemMapper;
import org.jalvarez.stockly.supplier.repository.SupplierItemRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SupplierItemService {
    private final SupplierItemRepository supplierItemRepository;

    public SupplierItemService(SupplierItemRepository supplierItemRepository) {
        this.supplierItemRepository = supplierItemRepository;
    }

    public List<SupplierItemDto> getAllByActivityAndSupplier(Boolean activity, Long supplierId, Pageable pageable) {
        return supplierItemRepository.getAllByActiveAndSupplier_Id(activity, supplierId, pageable).stream().map(SupplierItemMapper::toDto).toList();
    }

    public List<SupplierItemDto> getAllByPrimaryAndActiveAndSupplier(Boolean primary, Boolean active, Long supplierId, Pageable pageable) {
        return supplierItemRepository.getAllByPrimaryAndActiveAndSupplier_Id(primary, active, supplierId, pageable).stream().map(SupplierItemMapper::toDto).toList();
    }

    public List<SupplierItemDto> getAllByIngredient(Long ingredientId, Pageable pageable) {
        return supplierItemRepository.getAllByIngredient_Id(ingredientId, pageable).stream().map(SupplierItemMapper::toDto).toList();
    }

    public List<SupplierItemDto> getAllByIngredientActivityAndIsPrimary(Long ingredientId, Boolean activity, Boolean isPrimary, Pageable pageable) {
        return supplierItemRepository.getAllByIngredient_IdAndActiveAndPrimary(ingredientId, activity, isPrimary, pageable).stream().map(SupplierItemMapper::toDto).toList();
    }
}
