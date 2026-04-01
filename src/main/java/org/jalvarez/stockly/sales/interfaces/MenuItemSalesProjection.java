package org.jalvarez.stockly.sales.interfaces;

import java.math.BigDecimal;

public interface MenuItemSalesProjection {
    String getMenuItemName();
    BigDecimal getRevenue();
}