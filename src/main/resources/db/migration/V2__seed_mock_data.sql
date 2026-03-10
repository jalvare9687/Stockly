BEGIN;

-- -------------------------------------------------------------------
-- V2__seed_mock_data.sql
-- Purpose:
--   Seed a fresh Stockly database with realistic v1 mock data for:
--   balances, past orders, deliveries, receipts, inventory lots,
--   transactions, menu items, recipes, and sales history.
--
-- Assumptions:
--   - V1__init.sql has already run successfully on an empty database.
--   - Table names and columns match the current schema.
--   - This script is intended for a fresh local/dev database.
-- -------------------------------------------------------------------

-- =========================
-- 1) Master reference data
-- =========================

INSERT INTO ingredient_type (type, is_perishable) VALUES
    ('Dairy', TRUE),
    ('Produce', TRUE),
    ('Protein', TRUE),
    ('Bakery', TRUE),
    ('Dry Goods', FALSE),
    ('Beverage', FALSE);

INSERT INTO location (
    name, address, city, state, country, zip, created_at, updated_at
) VALUES
    ('Downtown Cafe', '101 Main St', 'New York', 'NY', 'USA', '10001',
     TIMESTAMP '2026-03-01 08:00:00', TIMESTAMP '2026-03-01 08:00:00'),
    ('Uptown Cafe', '202 Broadway', 'New York', 'NY', 'USA', '10012',
     TIMESTAMP '2026-03-01 08:10:00', TIMESTAMP '2026-03-01 08:10:00');

INSERT INTO supplier (name, email, phone_number) VALUES
    ('FreshFarm Produce', 'orders@freshfarm.example', '212-555-0101'),
    ('Metro Dairy Co', 'sales@metrodairy.example', '212-555-0102'),
    ('Prime Foods Supply', 'hello@primefoods.example', '212-555-0103');

INSERT INTO users (location_id, name, email)
SELECT l.id, x.user_name, x.email
FROM (
    VALUES
        ('Downtown Cafe', 'Alice Manager', 'alice@stockly.example'),
        ('Downtown Cafe', 'Ben Supervisor', 'ben@stockly.example'),
        ('Uptown Cafe', 'Cara Manager', 'cara@stockly.example')
) AS x(location_name, user_name, email)
JOIN location l ON l.name = x.location_name;

INSERT INTO ingredient (
    ingredient_type_id, name, description, unit_cost, base_uom, par_level_qty,
    created_at, updated_at
)
SELECT
    it.id,
    x.ingredient_name,
    x.description,
    x.unit_cost,
    x.base_uom,
    x.par_level_qty,
    TIMESTAMP '2026-03-01 09:00:00',
    TIMESTAMP '2026-03-01 09:00:00'
FROM (
    VALUES
        ('Dairy', 'Whole Milk', 'Whole milk for coffee drinks and baking', 3.20, 'L', 20.00),
        ('Dairy', 'Cheddar Cheese', 'Sliced cheddar cheese for sandwiches', 7.50, 'kg', 5.00),
        ('Produce', 'Romaine Lettuce', 'Romaine for sandwiches and salads', 2.10, 'kg', 6.00),
        ('Produce', 'Tomato', 'Fresh tomatoes for sandwiches', 2.80, 'kg', 8.00),
        ('Protein', 'Chicken Breast', 'Boneless chicken breast', 8.90, 'kg', 10.00),
        ('Bakery', 'Burger Bun', 'Soft brioche burger bun', 0.55, 'ea', 80.00),
        ('Dry Goods', 'Coffee Beans', 'House espresso blend beans', 12.75, 'kg', 12.00),
        ('Beverage', 'Orange Juice', 'Bottled orange juice', 2.40, 'L', 10.00)
) AS x(type_name, ingredient_name, description, unit_cost, base_uom, par_level_qty)
JOIN ingredient_type it ON it.type = x.type_name;

INSERT INTO location_supplier (
    supplier_id, location_id, is_active_supplier, default_lead_time_days,
    min_order_amount, delivery_days, description, created_at, updated_at
)
SELECT
    s.id,
    l.id,
    x.is_active_supplier,
    x.default_lead_time_days,
    x.min_order_amount,
    x.delivery_days,
    x.description,
    TIMESTAMP '2026-03-01 10:00:00',
    TIMESTAMP '2026-03-01 10:00:00'
FROM (
    VALUES
        ('FreshFarm Produce', 'Downtown Cafe', TRUE, 2, 100.00, 3, 'Primary produce vendor for Downtown'),
        ('FreshFarm Produce', 'Uptown Cafe', TRUE, 2, 100.00, 3, 'Primary produce vendor for Uptown'),
        ('Metro Dairy Co', 'Downtown Cafe', TRUE, 1, 75.00, 5, 'Primary dairy vendor for Downtown'),
        ('Metro Dairy Co', 'Uptown Cafe', TRUE, 1, 75.00, 5, 'Primary dairy vendor for Uptown'),
        ('Prime Foods Supply', 'Downtown Cafe', TRUE, 3, 150.00, 2, 'General supplier for Downtown'),
        ('Prime Foods Supply', 'Uptown Cafe', TRUE, 3, 150.00, 2, 'General supplier for Uptown')
) AS x(supplier_name, location_name, is_active_supplier, default_lead_time_days, min_order_amount, delivery_days, description)
JOIN supplier s ON s.name = x.supplier_name
JOIN location l ON l.name = x.location_name;

INSERT INTO supplier_item (
    supplier_id, ingredient_id, vendor_sku, pack_size, pack_uom,
    is_primary, is_active, default_cost, created_at, updated_at
)
SELECT
    s.id,
    i.id,
    x.vendor_sku,
    x.pack_size,
    x.pack_uom,
    x.is_primary,
    x.is_active,
    x.default_cost,
    TIMESTAMP '2026-03-01 11:00:00',
    TIMESTAMP '2026-03-01 11:00:00'
FROM (
    VALUES
        ('Metro Dairy Co', 'Whole Milk', 'MD-MILK-001', 12.00, 'L', TRUE, TRUE, 36.0000),
        ('Metro Dairy Co', 'Cheddar Cheese', 'MD-CHED-005', 5.00, 'kg', TRUE, TRUE, 37.5000),
        ('FreshFarm Produce', 'Romaine Lettuce', 'FF-ROM-010', 8.00, 'kg', TRUE, TRUE, 16.8000),
        ('FreshFarm Produce', 'Tomato', 'FF-TOM-012', 10.00, 'kg', TRUE, TRUE, 28.0000),
        ('Prime Foods Supply', 'Chicken Breast', 'PF-CHK-020', 10.00, 'kg', TRUE, TRUE, 89.0000),
        ('Prime Foods Supply', 'Burger Bun', 'PF-BUN-033', 48.00, 'ea', TRUE, TRUE, 26.4000),
        ('Prime Foods Supply', 'Coffee Beans', 'PF-COF-041', 6.00, 'kg', TRUE, TRUE, 76.5000),
        ('Prime Foods Supply', 'Orange Juice', 'PF-OJ-051', 12.00, 'L', TRUE, TRUE, 28.8000)
) AS x(supplier_name, ingredient_name, vendor_sku, pack_size, pack_uom, is_primary, is_active, default_cost)
JOIN supplier s ON s.name = x.supplier_name
JOIN ingredient i ON i.name = x.ingredient_name;

INSERT INTO menu_item (
    name, price, is_active, created_at, updated_at
) VALUES
    ('Chicken Sandwich', 11.99, TRUE, TIMESTAMP '2026-03-01 12:00:00', TIMESTAMP '2026-03-01 12:00:00'),
    ('Grilled Cheese', 7.49, TRUE, TIMESTAMP '2026-03-01 12:05:00', TIMESTAMP '2026-03-01 12:05:00'),
    ('Latte', 4.50, TRUE, TIMESTAMP '2026-03-01 12:10:00', TIMESTAMP '2026-03-01 12:10:00'),
    ('Orange Juice Cup', 3.25, TRUE, TIMESTAMP '2026-03-01 12:15:00', TIMESTAMP '2026-03-01 12:15:00');

-- =========================
-- 2) Purchase orders
-- =========================

INSERT INTO purchase_orders (
    location_id, supplier_id, created_by_user_id, po_status,
    expected_at, sent_at, created_at, updated_at
)
SELECT
    l.id,
    s.id,
    u.id,
    x.po_status,
    x.expected_at,
    x.sent_at,
    x.created_at,
    x.updated_at
FROM (
    VALUES
        ('Downtown Cafe', 'Metro Dairy Co', 'Alice Manager', 'RECEIVED',
         TIMESTAMP '2026-03-04 09:00:00', TIMESTAMP '2026-03-02 10:00:00',
         TIMESTAMP '2026-03-02 09:30:00', TIMESTAMP '2026-03-04 09:10:00'),

        ('Downtown Cafe', 'FreshFarm Produce', 'Ben Supervisor', 'RECEIVED',
         TIMESTAMP '2026-03-05 08:00:00', TIMESTAMP '2026-03-03 11:00:00',
         TIMESTAMP '2026-03-03 10:15:00', TIMESTAMP '2026-03-05 08:05:00'),

        ('Downtown Cafe', 'Prime Foods Supply', 'Alice Manager', 'RECEIVED',
         TIMESTAMP '2026-03-06 12:00:00', TIMESTAMP '2026-03-04 14:00:00',
         TIMESTAMP '2026-03-04 13:20:00', TIMESTAMP '2026-03-06 12:15:00'),

        ('Uptown Cafe', 'Metro Dairy Co', 'Cara Manager', 'SENT',
         TIMESTAMP '2026-03-10 09:30:00', TIMESTAMP '2026-03-08 15:00:00',
         TIMESTAMP '2026-03-08 14:00:00', TIMESTAMP '2026-03-08 15:00:00')
) AS x(location_name, supplier_name, user_name, po_status, expected_at, sent_at, created_at, updated_at)
JOIN location l ON l.name = x.location_name
JOIN supplier s ON s.name = x.supplier_name
JOIN users u ON u.name = x.user_name;

INSERT INTO purchase_order_line (
    purchase_order_id, supplier_item_id, qty_ordered, unit_cost, unit
)
SELECT
    po.id,
    si.id,
    x.qty_ordered,
    x.unit_cost,
    x.unit
FROM (
    VALUES
        ('Downtown Cafe', 'Metro Dairy Co', TIMESTAMP '2026-03-02 09:30:00', 'Whole Milk', 2.00, 36.00, 'case'),
        ('Downtown Cafe', 'Metro Dairy Co', TIMESTAMP '2026-03-02 09:30:00', 'Cheddar Cheese', 1.00, 37.50, 'case'),
        ('Downtown Cafe', 'FreshFarm Produce', TIMESTAMP '2026-03-03 10:15:00', 'Romaine Lettuce', 1.00, 16.80, 'case'),
        ('Downtown Cafe', 'FreshFarm Produce', TIMESTAMP '2026-03-03 10:15:00', 'Tomato', 1.00, 28.00, 'case'),
        ('Downtown Cafe', 'Prime Foods Supply', TIMESTAMP '2026-03-04 13:20:00', 'Chicken Breast', 1.00, 89.00, 'case'),
        ('Downtown Cafe', 'Prime Foods Supply', TIMESTAMP '2026-03-04 13:20:00', 'Burger Bun', 2.00, 26.40, 'case'),
        ('Downtown Cafe', 'Prime Foods Supply', TIMESTAMP '2026-03-04 13:20:00', 'Coffee Beans', 1.00, 76.50, 'case'),
        ('Downtown Cafe', 'Prime Foods Supply', TIMESTAMP '2026-03-04 13:20:00', 'Orange Juice', 1.00, 28.80, 'case'),
        ('Uptown Cafe', 'Metro Dairy Co', TIMESTAMP '2026-03-08 14:00:00', 'Whole Milk', 2.00, 36.00, 'case')
) AS x(location_name, supplier_name, po_created_at, ingredient_name, qty_ordered, unit_cost, unit)
JOIN purchase_orders po
  ON po.location_id = (SELECT id FROM location WHERE name = x.location_name)
 AND po.supplier_id = (SELECT id FROM supplier WHERE name = x.supplier_name)
 AND po.created_at = x.po_created_at
JOIN supplier_item si
  ON si.supplier_id = po.supplier_id
 AND si.ingredient_id = (SELECT id FROM ingredient WHERE name = x.ingredient_name);

-- =========================
-- 3) Deliveries and receipts
-- =========================

INSERT INTO delivery (
    purchase_order_id, supplier_id, location_id,
    expected_arrival, shipped_at, arrived_at, received_closed_at,
    delivery_status, reference_number
)
SELECT
    po.id,
    s.id,
    l.id,
    x.expected_arrival,
    x.shipped_at,
    x.arrived_at,
    x.received_closed_at,
    x.delivery_status,
    x.reference_number
FROM (
    VALUES
        ('Downtown Cafe', 'Metro Dairy Co', TIMESTAMP '2026-03-02 09:30:00',
         TIMESTAMP '2026-03-04 09:00:00', TIMESTAMP '2026-03-03 18:00:00',
         TIMESTAMP '2026-03-04 08:45:00', TIMESTAMP '2026-03-04 09:10:00',
         'RECEIVED', 1001001),

        ('Downtown Cafe', 'FreshFarm Produce', TIMESTAMP '2026-03-03 10:15:00',
         TIMESTAMP '2026-03-05 08:00:00', TIMESTAMP '2026-03-04 20:00:00',
         TIMESTAMP '2026-03-05 07:50:00', TIMESTAMP '2026-03-05 08:05:00',
         'RECEIVED', 2001001),

        ('Downtown Cafe', 'Prime Foods Supply', TIMESTAMP '2026-03-04 13:20:00',
         TIMESTAMP '2026-03-06 12:00:00', TIMESTAMP '2026-03-05 19:30:00',
         TIMESTAMP '2026-03-06 11:40:00', TIMESTAMP '2026-03-06 12:15:00',
         'RECEIVED', 3001001),

        ('Uptown Cafe', 'Metro Dairy Co', TIMESTAMP '2026-03-08 14:00:00',
         TIMESTAMP '2026-03-10 09:30:00', TIMESTAMP '2026-03-09 17:00:00',
         NULL, NULL,
         'IN_TRANSIT', 1001002)
) AS x(location_name, supplier_name, po_created_at, expected_arrival, shipped_at, arrived_at, received_closed_at, delivery_status, reference_number)
JOIN purchase_orders po
  ON po.location_id = (SELECT id FROM location WHERE name = x.location_name)
 AND po.supplier_id = (SELECT id FROM supplier WHERE name = x.supplier_name)
 AND po.created_at = x.po_created_at
JOIN supplier s ON s.id = po.supplier_id
JOIN location l ON l.id = po.location_id;

INSERT INTO delivery_line (
    delivery_id, po_line_id, received_qty, received_cost, created_at, updated_at
)
SELECT
    d.id,
    pol.id,
    x.received_qty,
    x.received_cost,
    x.created_at,
    x.updated_at
FROM (
    VALUES
        ('1001001', 'Whole Milk', 2.00, 36.00, TIMESTAMP '2026-03-04 09:00:00', TIMESTAMP '2026-03-04 09:00:00'),
        ('1001001', 'Cheddar Cheese', 1.00, 37.50, TIMESTAMP '2026-03-04 09:00:00', TIMESTAMP '2026-03-04 09:00:00'),
        ('2001001', 'Romaine Lettuce', 1.00, 16.80, TIMESTAMP '2026-03-05 08:00:00', TIMESTAMP '2026-03-05 08:00:00'),
        ('2001001', 'Tomato', 1.00, 28.00, TIMESTAMP '2026-03-05 08:00:00', TIMESTAMP '2026-03-05 08:00:00'),
        ('3001001', 'Chicken Breast', 1.00, 89.00, TIMESTAMP '2026-03-06 12:00:00', TIMESTAMP '2026-03-06 12:00:00'),
        ('3001001', 'Burger Bun', 2.00, 26.40, TIMESTAMP '2026-03-06 12:00:00', TIMESTAMP '2026-03-06 12:00:00'),
        ('3001001', 'Coffee Beans', 1.00, 76.50, TIMESTAMP '2026-03-06 12:00:00', TIMESTAMP '2026-03-06 12:00:00'),
        ('3001001', 'Orange Juice', 1.00, 28.80, TIMESTAMP '2026-03-06 12:00:00', TIMESTAMP '2026-03-06 12:00:00')
) AS x(reference_number, ingredient_name, received_qty, received_cost, created_at, updated_at)
JOIN delivery d ON d.reference_number = x.reference_number
JOIN purchase_order_line pol
  ON pol.purchase_order_id = d.purchase_order_id
 AND pol.supplier_item_id = (
        SELECT si.id
        FROM supplier_item si
        JOIN ingredient i ON i.id = si.ingredient_id
        WHERE si.supplier_id = d.supplier_id
          AND i.name = x.ingredient_name
    );

INSERT INTO inventory_lot (
    ingredient_id, location_id, cost_of_lot, qty_received, remaining_qty,
    expiry_date, received_date, created_at, updated_at
)
SELECT
    i.id,
    l.id,
    x.cost_of_lot,
    x.qty_received,
    x.remaining_qty,
    x.expiry_date,
    x.received_date,
    x.created_at,
    x.updated_at
FROM (
    VALUES
        ('Downtown Cafe', 'Whole Milk', 36.00, 12.00, 7.00, TIMESTAMP '2026-03-12 00:00:00', TIMESTAMP '2026-03-04 09:00:00', TIMESTAMP '2026-03-04 09:00:00', TIMESTAMP '2026-03-08 10:00:00'),
        ('Downtown Cafe', 'Cheddar Cheese', 37.50, 5.00, 4.00, TIMESTAMP '2026-03-20 00:00:00', TIMESTAMP '2026-03-04 09:00:00', TIMESTAMP '2026-03-04 09:00:00', TIMESTAMP '2026-03-08 10:00:00'),
        ('Downtown Cafe', 'Romaine Lettuce', 16.80, 8.00, 3.50, TIMESTAMP '2026-03-10 00:00:00', TIMESTAMP '2026-03-05 08:00:00', TIMESTAMP '2026-03-05 08:00:00', TIMESTAMP '2026-03-08 10:00:00'),
        ('Downtown Cafe', 'Tomato', 28.00, 10.00, 6.00, TIMESTAMP '2026-03-11 00:00:00', TIMESTAMP '2026-03-05 08:00:00', TIMESTAMP '2026-03-05 08:00:00', TIMESTAMP '2026-03-08 10:00:00'),
        ('Downtown Cafe', 'Chicken Breast', 89.00, 10.00, 6.00, TIMESTAMP '2026-03-13 00:00:00', TIMESTAMP '2026-03-06 12:00:00', TIMESTAMP '2026-03-06 12:00:00', TIMESTAMP '2026-03-08 10:00:00'),
        ('Downtown Cafe', 'Burger Bun', 52.80, 96.00, 60.00, TIMESTAMP '2026-03-14 00:00:00', TIMESTAMP '2026-03-06 12:00:00', TIMESTAMP '2026-03-06 12:00:00', TIMESTAMP '2026-03-08 10:00:00'),
        ('Downtown Cafe', 'Coffee Beans', 76.50, 6.00, 4.68, TIMESTAMP '2026-09-01 00:00:00', TIMESTAMP '2026-03-06 12:00:00', TIMESTAMP '2026-03-06 12:00:00', TIMESTAMP '2026-03-08 10:00:00'),
        ('Downtown Cafe', 'Orange Juice', 28.80, 12.00, 9.00, TIMESTAMP '2026-03-18 00:00:00', TIMESTAMP '2026-03-06 12:00:00', TIMESTAMP '2026-03-06 12:00:00', TIMESTAMP '2026-03-08 10:00:00')
) AS x(location_name, ingredient_name, cost_of_lot, qty_received, remaining_qty, expiry_date, received_date, created_at, updated_at)
JOIN ingredient i ON i.name = x.ingredient_name
JOIN location l ON l.name = x.location_name;

INSERT INTO receipt_lot (delivery_line_id, inventory_lot_id, quantity)
SELECT
    dl.id,
    il.id,
    x.quantity
FROM (
    VALUES
        ('1001001', 'Whole Milk', 12.00),
        ('1001001', 'Cheddar Cheese', 5.00),
        ('2001001', 'Romaine Lettuce', 8.00),
        ('2001001', 'Tomato', 10.00),
        ('3001001', 'Chicken Breast', 10.00),
        ('3001001', 'Burger Bun', 96.00),
        ('3001001', 'Coffee Beans', 6.00),
        ('3001001', 'Orange Juice', 12.00)
) AS x(reference_number, ingredient_name, quantity)
JOIN delivery_line dl
  ON dl.delivery_id = (SELECT id FROM delivery WHERE reference_number = x.reference_number)
 AND dl.po_line_id = (
        SELECT pol.id
        FROM purchase_order_line pol
        JOIN supplier_item si ON si.id = pol.supplier_item_id
        JOIN ingredient i ON i.id = si.ingredient_id
        JOIN delivery d ON d.purchase_order_id = pol.purchase_order_id
        WHERE d.reference_number = x.reference_number
          AND i.name = x.ingredient_name
    )
JOIN inventory_lot il
  ON il.ingredient_id = (SELECT id FROM ingredient WHERE name = x.ingredient_name)
 AND il.location_id = (SELECT id FROM location WHERE name = 'Downtown Cafe');

-- =========================
-- 4) Sales and recipes
-- =========================

INSERT INTO sale (location_id, sold_at)
SELECT l.id, x.sold_at
FROM (
    VALUES
        ('Downtown Cafe', TIMESTAMP '2026-03-07 08:15:00'),
        ('Downtown Cafe', TIMESTAMP '2026-03-07 12:20:00'),
        ('Downtown Cafe', TIMESTAMP '2026-03-08 09:05:00'),
        ('Uptown Cafe', TIMESTAMP '2026-03-08 11:40:00')
) AS x(location_name, sold_at)
JOIN location l ON l.name = x.location_name;

INSERT INTO sale_line (menu_item_id, sale_id, qty_sold, unit_price)
SELECT
    mi.id,
    s.id,
    x.qty_sold,
    x.unit_price
FROM (
    VALUES
        ('2026-03-07 08:15:00', 'Chicken Sandwich', 2.00, 11.99),
        ('2026-03-07 08:15:00', 'Latte', 2.00, 4.50),
        ('2026-03-07 12:20:00', 'Grilled Cheese', 3.00, 7.49),
        ('2026-03-07 12:20:00', 'Orange Juice Cup', 2.00, 3.25),
        ('2026-03-08 09:05:00', 'Latte', 4.00, 4.50),
        ('2026-03-08 09:05:00', 'Chicken Sandwich', 1.00, 11.99),
        ('2026-03-08 11:40:00', 'Orange Juice Cup', 3.00, 3.25)
) AS x(sold_at_text, menu_item_name, qty_sold, unit_price)
JOIN sale s ON s.sold_at = CAST(x.sold_at_text AS TIMESTAMP)
JOIN menu_item mi ON mi.name = x.menu_item_name;

INSERT INTO recipe_line (
    ingredient_id, menu_item_id, qty_per_item, created_at, updated_at
)
SELECT
    i.id,
    mi.id,
    x.qty_per_item,
    TIMESTAMP '2026-03-01 12:30:00',
    TIMESTAMP '2026-03-01 12:30:00'
FROM (
    VALUES
        ('Chicken Sandwich', 'Chicken Breast', 0.20),
        ('Chicken Sandwich', 'Burger Bun', 1.00),
        ('Chicken Sandwich', 'Romaine Lettuce', 0.05),
        ('Chicken Sandwich', 'Tomato', 0.06),
        ('Grilled Cheese', 'Cheddar Cheese', 0.12),
        ('Grilled Cheese', 'Burger Bun', 1.00),
        ('Grilled Cheese', 'Tomato', 0.04),
        ('Latte', 'Whole Milk', 0.30),
        ('Latte', 'Coffee Beans', 0.02),
        ('Orange Juice Cup', 'Orange Juice', 0.35)
) AS x(menu_item_name, ingredient_name, qty_per_item)
JOIN menu_item mi ON mi.name = x.menu_item_name
JOIN ingredient i ON i.name = x.ingredient_name;

-- =========================
-- 5) Inventory transactions
-- =========================

INSERT INTO inventory_transaction (
    location_id, transaction_type, description,
    purchase_order_id, sale_id, delivery_id,
    created_at, occurred_at, user_id
)
SELECT
    l.id,
    x.transaction_type,
    x.description,
    NULL,
    NULL,
    d.id,
    x.created_at,
    x.occurred_at,
    u.id
FROM (
    VALUES
        ('Downtown Cafe', 'RECEIPT', 'Received Metro Dairy delivery', '1001001',
         TIMESTAMP '2026-03-04 09:10:00', TIMESTAMP '2026-03-04 09:05:00', 'Alice Manager'),
        ('Downtown Cafe', 'RECEIPT', 'Received FreshFarm delivery', '2001001',
         TIMESTAMP '2026-03-05 08:05:00', TIMESTAMP '2026-03-05 08:00:00', 'Ben Supervisor'),
        ('Downtown Cafe', 'RECEIPT', 'Received Prime Foods delivery', '3001001',
         TIMESTAMP '2026-03-06 12:15:00', TIMESTAMP '2026-03-06 12:00:00', 'Alice Manager')
) AS x(location_name, transaction_type, description, reference_number, created_at, occurred_at, user_name)
JOIN location l ON l.name = x.location_name
JOIN delivery d ON d.reference_number = x.reference_number
JOIN users u ON u.name = x.user_name;

INSERT INTO inventory_transaction (
    location_id, transaction_type, description,
    purchase_order_id, sale_id, delivery_id,
    created_at, occurred_at, user_id
)
SELECT
    l.id,
    x.transaction_type,
    x.description,
    NULL,
    s.id,
    NULL,
    x.created_at,
    x.occurred_at,
    u.id
FROM (
    VALUES
        ('Downtown Cafe', 'CONSUMPTION', 'Ingredients consumed by morning sales', TIMESTAMP '2026-03-07 08:15:00',
         TIMESTAMP '2026-03-07 08:20:00', TIMESTAMP '2026-03-07 08:15:00', 'Ben Supervisor'),
        ('Downtown Cafe', 'CONSUMPTION', 'Ingredients consumed by lunch sales', TIMESTAMP '2026-03-07 12:20:00',
         TIMESTAMP '2026-03-07 12:30:00', TIMESTAMP '2026-03-07 12:20:00', 'Ben Supervisor'),
        ('Downtown Cafe', 'CONSUMPTION', 'Ingredients consumed by weekend sales', TIMESTAMP '2026-03-08 09:05:00',
         TIMESTAMP '2026-03-08 09:10:00', TIMESTAMP '2026-03-08 09:05:00', 'Alice Manager')
) AS x(location_name, transaction_type, description, sold_at, created_at, occurred_at, user_name)
JOIN location l ON l.name = x.location_name
JOIN sale s ON s.location_id = l.id AND s.sold_at = x.sold_at
JOIN users u ON u.name = x.user_name;

INSERT INTO inventory_transaction_lot (transaction_id, inventory_lot_id, qty_delta)
SELECT
    it.id,
    il.id,
    x.qty_delta
FROM (
    VALUES
        ('RECEIPT', TIMESTAMP '2026-03-04 09:10:00', 'Whole Milk', 12.00),
        ('RECEIPT', TIMESTAMP '2026-03-04 09:10:00', 'Cheddar Cheese', 5.00),
        ('RECEIPT', TIMESTAMP '2026-03-05 08:05:00', 'Romaine Lettuce', 8.00),
        ('RECEIPT', TIMESTAMP '2026-03-05 08:05:00', 'Tomato', 10.00),
        ('RECEIPT', TIMESTAMP '2026-03-06 12:15:00', 'Chicken Breast', 10.00),
        ('RECEIPT', TIMESTAMP '2026-03-06 12:15:00', 'Burger Bun', 96.00),
        ('RECEIPT', TIMESTAMP '2026-03-06 12:15:00', 'Coffee Beans', 6.00),
        ('RECEIPT', TIMESTAMP '2026-03-06 12:15:00', 'Orange Juice', 12.00),

        ('CONSUMPTION', TIMESTAMP '2026-03-07 08:20:00', 'Chicken Breast', -0.40),
        ('CONSUMPTION', TIMESTAMP '2026-03-07 08:20:00', 'Burger Bun', -2.00),
        ('CONSUMPTION', TIMESTAMP '2026-03-07 08:20:00', 'Romaine Lettuce', -0.10),
        ('CONSUMPTION', TIMESTAMP '2026-03-07 08:20:00', 'Tomato', -0.12),
        ('CONSUMPTION', TIMESTAMP '2026-03-07 08:20:00', 'Whole Milk', -0.60),
        ('CONSUMPTION', TIMESTAMP '2026-03-07 08:20:00', 'Coffee Beans', -0.04),

        ('CONSUMPTION', TIMESTAMP '2026-03-07 12:30:00', 'Cheddar Cheese', -0.36),
        ('CONSUMPTION', TIMESTAMP '2026-03-07 12:30:00', 'Burger Bun', -3.00),
        ('CONSUMPTION', TIMESTAMP '2026-03-07 12:30:00', 'Tomato', -0.12),
        ('CONSUMPTION', TIMESTAMP '2026-03-07 12:30:00', 'Orange Juice', -0.70),

        ('CONSUMPTION', TIMESTAMP '2026-03-08 09:10:00', 'Whole Milk', -1.20),
        ('CONSUMPTION', TIMESTAMP '2026-03-08 09:10:00', 'Coffee Beans', -0.08),
        ('CONSUMPTION', TIMESTAMP '2026-03-08 09:10:00', 'Chicken Breast', -0.20),
        ('CONSUMPTION', TIMESTAMP '2026-03-08 09:10:00', 'Burger Bun', -1.00),
        ('CONSUMPTION', TIMESTAMP '2026-03-08 09:10:00', 'Romaine Lettuce', -0.05),
        ('CONSUMPTION', TIMESTAMP '2026-03-08 09:10:00', 'Tomato', -0.06)
) AS x(transaction_type, created_at, ingredient_name, qty_delta)
JOIN inventory_transaction it
  ON it.transaction_type = x.transaction_type
 AND it.created_at = x.created_at
JOIN inventory_lot il
  ON il.ingredient_id = (SELECT id FROM ingredient WHERE name = x.ingredient_name)
 AND il.location_id = (SELECT id FROM location WHERE name = 'Downtown Cafe');

COMMIT;
