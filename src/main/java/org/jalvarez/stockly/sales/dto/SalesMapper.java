package org.jalvarez.stockly.sales.dto;

import org.jalvarez.stockly.sales.interfaces.MenuItemSalesProjection;
import org.jalvarez.stockly.sales.model.Sale;
import org.jalvarez.stockly.sales.model.SaleLine;

import java.util.List;

public class SalesMapper {

    public static SaleDto toSaleDto(Sale sale) {
        if (sale == null) return null;

        List<SaleLineDto> saleLineDtos = sale.getSaleLines().stream().map(SalesMapper::toSaleLineDto).toList();

        return new SaleDto(
                sale.getId(),
                saleLineDtos,
                sale.getLocation().getName(),
                sale.getSoldAt()
        );
    }

    public static SaleLineDto toSaleLineDto(SaleLine saleLine) {
        if (saleLine == null) return null;

        return new SaleLineDto(
                saleLine.getId(),
                saleLine.getSale().getId(),
                saleLine.getMenuItem().getName(),
                saleLine.getQuantitySold(),
                saleLine.getUnitPrice()
        );
    }

    public static MenuItemProjDto toMenuItemProjDto(MenuItemSalesProjection projection) {
        if (projection == null) return null;

        return new MenuItemProjDto(
                projection.getMenuItemName(),
                projection.getRevenue()
        );
    }
}
