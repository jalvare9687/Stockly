
CREATE TABLE IF NOT EXISTS ingredient_type (
   id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   type VARCHAR(50) NOT NULL,
   is_perishable BOOLEAN NOT NULL DEFAULT FALSE,

   CONSTRAINT unique_ingredient_type
       UNIQUE (type)
);

CREATE TABLE IF NOT EXISTS location (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL CHECK (trim(name) <> ''),
    address VARCHAR(100) NOT NULL CHECK (trim(address)<> ''),
    city VARCHAR(50) NOT NULL CHECK (trim(city) <> ''),
    state VARCHAR(10) NOT NULL CHECK (trim(state) <> ''),
    country VARCHAR(50) NOT NULL CHECK (trim(country) <> ''),
    zip VARCHAR(50) NOT NULL CHECK (trim(zip) <> ''),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT unique_location_name
        UNIQUE (name),

    CONSTRAINT chk_location_updated_at
        CHECK ( updated_at >= created_at )

);

CREATE TABLE IF NOT EXISTS supplier (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL CHECK (trim(name) <> ''),
    email VARCHAR(50) NOT NULL CHECK (trim(email) <> ''),
    phone_number VARCHAR(50) NOT NULL CHECK (trim(phone_number) <> '') ,

    CONSTRAINT supplier_unique_info
        UNIQUE (name, email, phone_number)
);

CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    location_id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL CHECK (trim(name) <> ''),
    email VARCHAR(50) NOT NULL CHECK (trim(email) <> ''),

    CONSTRAINT fk_user_location
        FOREIGN KEY (location_id) REFERENCES location(id),
    CONSTRAINT unique_user_email
        UNIQUE (email)

);

CREATE TABLE IF NOT EXISTS ingredient (
      id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
      ingredient_type_id BIGINT NOT NULL,
      name VARCHAR(50) NOT NULL CHECK (trim(name) <> ''),
      description VARCHAR(100) NOT NULL CHECK (trim(description) <> ''),
      unit_cost NUMERIC(10,2) NOT NULL CHECK ( unit_cost >= 0),
      base_uom VARCHAR(50) NOT NULL CHECK (trim(base_uom) <> ''),
      par_level_qty NUMERIC(10,2) NOT NULL CHECK (par_level_qty >= 0),
      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

      CONSTRAINT fk_ingredients_ingredient_type_id
          FOREIGN KEY (ingredient_type_id) REFERENCES ingredient_type(id),

      CONSTRAINT chk_ingredients_updated_at
          CHECK ( updated_at >= created_at )

);

CREATE TABLE IF NOT EXISTS location_supplier (
     supplier_id BIGINT NOT NULL,
     location_id BIGINT NOT NULL,
     is_active_supplier BOOLEAN NOT NULL DEFAULT FALSE,
     default_lead_time_days BIGINT NOT NULL CHECK ( default_lead_time_days >= 0),
     min_order_amount NUMERIC(10,2) NOT NULL CHECK ( min_order_amount >= 0 ),
     delivery_days BIGINT NOT NULL CHECK ( delivery_days >= 0 ),
     description VARCHAR(50) NOT NULL CHECK (trim(description) <> ''),
     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

     PRIMARY KEY (supplier_id, location_id),

     CONSTRAINT fk_location_supplier_supplier_id
         FOREIGN KEY (supplier_id) REFERENCES supplier(id),
     CONSTRAINT fk_location_supplier_location_id
         FOREIGN KEY (location_id) REFERENCES location(id),

     CONSTRAINT chk_location_supplier_updated_at
         CHECK ( updated_at >= created_at )

);

CREATE TABLE IF NOT EXISTS supplier_item (
     id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
     supplier_id BIGINT NOT NULL,
     ingredient_id BIGINT NOT NULL,
     vendor_sku VARCHAR(50) NOT NULL CHECK (trim(vendor_sku) <> ''),
     pack_size NUMERIC(18, 6) NOT NULL CHECK ( pack_size > 0),
     pack_uom VARCHAR(50) NOT NULL CHECK (trim(pack_uom) <> ''),
     is_primary BOOLEAN NOT NULL DEFAULT FALSE,
     is_active BOOLEAN NOT NULL DEFAULT TRUE,
     default_cost NUMERIC(19,4) NOT NULL CHECK (default_cost >= 0 ),
     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

     CONSTRAINT fk_supplier_item_supplier_id
         FOREIGN KEY (supplier_id) REFERENCES supplier(id),
     CONSTRAINT fk_supplier_item_ingredient_id
         FOREIGN KEY (ingredient_id) REFERENCES ingredient(id),

     CONSTRAINT chk_supplier_item_updated_at
         CHECK ( updated_at >= created_at ),

     CONSTRAINT unique_supplier_item_supplier_and_ingredient
         UNIQUE (supplier_id, ingredient_id),
     CONSTRAINT unique_supplier_item_supplier_sku
         UNIQUE (supplier_id, vendor_sku)

);

CREATE TABLE IF NOT EXISTS menu_item (
     id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
     name VARCHAR(50) NOT NULL CHECK ( trim(name) <> ''),
     price NUMERIC(10, 2) NOT NULL CHECK(price >= 0),
     is_active BOOLEAN NOT NULL DEFAULT TRUE,
     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

     CONSTRAINT chk_menu_item_updated_at
         CHECK ( updated_at >= created_at )
);

CREATE TABLE IF NOT EXISTS purchase_orders (
       id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
       location_id BIGINT NOT NULL,
       supplier_id BIGINT NOT NULL,
       created_by_user_id BIGINT NOT NULL,
       po_status VARCHAR(50) NOT NULL CHECk (trim(po_status) <> ''),
       expected_at TIMESTAMP ,
       sent_at TIMESTAMP,
       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

       CONSTRAINT fk_po_location_id
           FOREIGN KEY (location_id) REFERENCES location(id),
       CONSTRAINT  fk_po_supplier_id
           FOREIGN KEY (supplier_id) REFERENCES supplier(id),
       CONSTRAINT fk_po_user_id
           FOREIGN KEY (created_by_user_id) REFERENCES users(id),

       CONSTRAINT chk_purchase_orders_expected_at
           CHECK (
               expected_at IS NULL OR
               expected_at >= created_at
               ),
       CONSTRAINT chk_purchase_orders_sent_at
           CHECK (
               sent_at IS NULL OR
               sent_at >= created_at
               ),
       CONSTRAINT chk_purchase_orders_updated_at
           CHECK ( updated_at >= created_at )
);

CREATE TABLE IF NOT EXISTS purchase_order_line (
       id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
       purchase_order_id BIGINT NOT NULL,
       supplier_item_id BIGINT NOT NULL,
       qty_ordered NUMERIC(10,2) NOT NULL CHECK ( qty_ordered > 0 ),
       unit_cost NUMERIC(10,2) NOT NULL CHECK ( unit_cost >= 0 ),
       unit VARCHAR(50) NOT NULL CHECK ( trim(unit) <> ''),

       CONSTRAINT fk_po_id
           FOREIGN KEY (purchase_order_id) REFERENCES purchase_orders(id)
                                               ON DELETE CASCADE,
       CONSTRAINT fk_supplier_item_id
           FOREIGN KEY (supplier_item_id) REFERENCES supplier_item(id),
       CONSTRAINT unique_purchase_order_line_po_id_and_supp_item_id
           UNIQUE (purchase_order_id, supplier_item_id)
);

CREATE TABLE IF NOT EXISTS delivery (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    purchase_order_id BIGINT NOT NULL,
    supplier_id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    expected_arrival TIMESTAMP NOT NULL,
    shipped_at TIMESTAMP,
    arrived_at TIMESTAMP,
    received_closed_at TIMESTAMP,
    delivery_status VARCHAR(50) NOT NULL CHECK ( trim(delivery_status) <> '' ),
    reference_number VARCHAR(50) NOT NULL CHECK (trim(reference_number) <> ''),

    CONSTRAINT fk_delivery_location_id
        FOREIGN KEY (location_id) REFERENCES location(id),
    CONSTRAINT fk_delivery_supplier_id
        FOREIGN KEY (supplier_id) REFERENCES supplier(id),
    CONSTRAINT fk_delivery_purchase_order_id
        FOREIGN KEY (purchase_order_id) REFERENCES purchase_orders(id),

    CONSTRAINT unique_delivery_ref_number_per_supplier
        UNIQUE (reference_number,supplier_id),

    CONSTRAINT chk_expected_arrival
        CHECK (
            shipped_at IS NULL OR
            expected_arrival  >= shipped_at),
    CONSTRAINT chk_received_closed_at
        CHECK (received_closed_at IS NULL OR
               arrived_at IS NULL OR
            received_closed_at >= arrived_at),
    CONSTRAINT chk_arrived_at
        CHECK (
            shipped_at IS NULL OR
            arrived_at IS NULL OR
            arrived_at >= shipped_at )
);

CREATE TABLE IF NOT EXISTS delivery_line (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    delivery_id BIGINT NOT NULL,
    po_line_id BIGINT NOT NULL,
    received_qty NUMERIC(10, 2) NOT NULL CHECK ( received_qty > 0),
    received_cost NUMERIC(10, 2) NOT NULL CHECK ( received_cost >= 0 ),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_delivery_line_delivery_id
        FOREIGN KEY (delivery_id) REFERENCES delivery(id) ON DELETE CASCADE,
    CONSTRAINT fk_delivery_line_po_line_id
        FOREIGN KEY (po_line_id) REFERENCES purchase_order_line(id),

    CONSTRAINT unique_delivery_line_delivery_and_po_line
        UNIQUE (delivery_id, po_line_id),

    CONSTRAINT chk_delivery_line_updated_at
        CHECK ( updated_at >= created_at )
);

CREATE TABLE IF NOT EXISTS inventory_lot (
     id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
     ingredient_id BIGINT NOT NULL,
     location_id BIGINT NOT NULL,
     cost_of_lot NUMERIC(10, 2) NOT NULL CHECK ( cost_of_lot >= 0 ),
     qty_received NUMERIC(10, 2) NOT NULL CHECK ( qty_received > 0 ),
     remaining_qty NUMERIC(10, 2) NOT NULL CHECK ( remaining_qty >= 0 AND remaining_qty <= qty_received),
     expiry_date DATE NOT NULL,
     received_date TIMESTAMP,
     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

     CONSTRAINT fk_inventory_lot_ingredient_id
         FOREIGN KEY (ingredient_id) REFERENCES ingredient(id),
     CONSTRAINT fk_inventory_lot_location_id
         FOREIGN KEY (location_id) REFERENCES location(id),
     CONSTRAINT chk_inventory_lot_received_date
         CHECK ( received_date IS NULL OR expiry_date >= received_date::date ),

     CONSTRAINT chk_inventory_lot_updated_at
         CHECK ( updated_at >= created_at )
);

CREATE TABLE IF NOT EXISTS receipt_lot (
   id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   delivery_line_id BIGINT NOT NULL,
   inventory_lot_id BIGINT NOT NULL,
   quantity NUMERIC(10, 2) NOT NULL CHECK ( quantity > 0 ),

   CONSTRAINT fk_receipt_lot_delivery_line_id
       FOREIGN KEY (delivery_line_id) REFERENCES delivery_line(id),
   CONSTRAINT fk_receipt_lot_inventory_lot_id
       FOREIGN KEY (inventory_lot_id) REFERENCES inventory_lot(id),

   CONSTRAINT unique_receipt_lot_delivery_line_inv_lot
       UNIQUE (inventory_lot_id,delivery_line_id)
);

CREATE TABLE IF NOT EXISTS sale (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    location_id BIGINT NOT NULL,
    sold_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_sale_location
        FOREIGN KEY (location_id) REFERENCES location(id)
);

CREATE TABLE IF NOT EXISTS sale_line (
     id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
     menu_item_id BIGINT NOT NULL,
     sale_id BIGINT NOT NULL,
     qty_sold NUMERIC(10, 2) NOT NULL CHECK ( qty_sold > 0 ),
     unit_price NUMERIC(10, 2) NOT NULL CHECK ( unit_price >= 0 ),

     CONSTRAINT fk_sale_line_menu_item
         FOREIGN KEY (menu_item_id) REFERENCES menu_item(id),
     CONSTRAINT fk_sale_line_sale
         FOREIGN KEY (sale_id) REFERENCES sale(id) ON DELETE CASCADE,
     CONSTRAINT unique_sale_line_menu_item_and_sale
         UNIQUE (menu_item_id, sale_id)
);

CREATE TABLE IF NOT EXISTS recipe_line (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ingredient_id BIGINT NOT NULL,
    menu_item_id BIGINT NOT NULL,
    qty_per_item NUMERIC(10, 2) NOT NULL CHECK ( qty_per_item > 0 ),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_recipe_line_ingredient_id
        FOREIGN KEY (ingredient_id) REFERENCES ingredient(id),
    CONSTRAINT fk_menu_item_id
        FOREIGN KEY (menu_item_id) REFERENCES menu_item(id),

    CONSTRAINT unique_fk_recipe_line
        UNIQUE (menu_item_id, ingredient_id),

    CONSTRAINT chk_recipe_line_updated_at
        CHECK ( updated_at >= created_at )
);

CREATE TABLE IF NOT EXISTS inventory_transaction (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    location_id BIGINT NOT NULL,
    transaction_type VARCHAR(50) NOT NULL CHECK (trim(transaction_type) <> ''),
    description TEXT NOT NULL CHECK (trim(description) <> ''),
    purchase_order_id BIGINT,
    sale_id BIGINT,
    delivery_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    occurred_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT NOT NULL,

    CONSTRAINT fk_inv_transaction_location_id
        FOREIGN KEY (location_id) REFERENCES location(id),
    CONSTRAINT fk_inv_transaction_user
        FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_inv_transaction_purchase_order_id
        FOREIGN KEY (purchase_order_id) REFERENCES purchase_orders(id),
    CONSTRAINT fk_inv_transaction_sale_id
        FOREIGN KEY (sale_id) REFERENCES sale(id),
    CONSTRAINT fk_inv_transaction_delivery_id
        FOREIGN KEY (delivery_id) REFERENCES delivery(id),

    CONSTRAINT  chk_inv_transaction_occurred_at
        CHECK ( occurred_at <= created_at ),

    CONSTRAINT chk_inv_transaction_exactly_one_source
        CHECK (
            ((purchase_order_id IS NOT NULL)::int +
             (sale_id IS NOT NULL)::int +
             (delivery_id IS NOT NULL)::int) <= 1
            )
);

CREATE TABLE IF NOT EXISTS inventory_transaction_lot (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    transaction_id BIGINT NOT NULL,
    inventory_lot_id BIGINT NOT NULL,
    qty_delta NUMERIC(10, 2) NOT NULL  CHECK ( qty_delta <> 0 ),

    CONSTRAINT fk_inv_tran_lot_transaction_id
        FOREIGN KEY (transaction_id) REFERENCES inventory_transaction(id),
    CONSTRAINT fk_inv_tran_lot_lot_id
        FOREIGN KEY (inventory_lot_id) REFERENCES inventory_lot(id),

    CONSTRAINT unique_inv_tran_lot_fks
        UNIQUE (inventory_lot_id, transaction_id)
);

CREATE OR REPLACE VIEW inventory_balance_view AS
SELECT
    il.location_id,
    il.ingredient_id,
    i.name,
    i.base_uom,
    i.par_level_qty,

    SUM(il.remaining_qty) AS on_hand_qty,
    COUNT(il.id) AS lot_count,
    MIN(il.expiry_date) AS next_expiry_date,

    SUM(
            CASE
                WHEN il.expiry_date <= CURRENT_DATE + INTERVAL '7 days'
                    AND il.expiry_date >= CURRENT_DATE
                    THEN il.remaining_qty
                ELSE 0
                END
    ) AS expiring_7d_qty,

    SUM(
            CASE
                WHEN il.expiry_date < CURRENT_DATE
                    THEN il.remaining_qty
                ELSE 0
                END
    ) AS expired_qty,

    CASE
        WHEN SUM(il.remaining_qty) < COALESCE(i.par_level_qty, 0)
            THEN TRUE
        ELSE FALSE
        END AS below_par

FROM inventory_lot il
         JOIN ingredient i
              ON i.id = il.ingredient_id
WHERE il.remaining_qty > 0
GROUP BY
    il.location_id,
    il.ingredient_id,
    i.name,
    i.base_uom,
    i.par_level_qty;


CREATE INDEX IF NOT EXISTS idx_delivery_po_id
    ON delivery (purchase_order_id);

CREATE INDEX IF NOT EXISTS idx_delivery_supplier_expected_arrival
    ON delivery (supplier_id, expected_arrival);

CREATE INDEX IF NOT EXISTS idx_delivery_location_expected_arrival
    ON delivery (location_id, expected_arrival);

CREATE INDEX IF NOT EXISTS idx_delivery_status
    ON delivery (delivery_status);

CREATE INDEX IF NOT EXISTS idx_delivery_line_po_line_id
    ON  delivery_line(po_line_id);

CREATE INDEX IF NOT EXISTS idx_delivery_line_delivery_id
    ON delivery_line(delivery_id);

CREATE INDEX IF NOT EXISTS idx_ingredients_ingredient_name
    ON ingredient(name);

CREATE INDEX IF NOT EXISTS idx_ingredients_ingredient_type_id
    ON ingredient(ingredient_type_id);

CREATE INDEX IF NOT EXISTS idx_location_city_state
    ON location(city, state);

CREATE INDEX IF NOT EXISTS idx_location_supplier_location_id
    ON location_supplier(location_id);

CREATE INDEX IF NOT EXISTS idx_location_supplier_supplier_id
    ON location_supplier(supplier_id);

CREATE INDEX IF NOT EXISTS idx_supplier_item_ingredient_id
    ON supplier_item(ingredient_id);

CREATE INDEX IF NOT EXISTS idx_supplier_item_supplier_id
    ON supplier_item(supplier_id);

CREATE INDEX IF NOT EXISTS idx_purchase_orders_supplier_created_at
    ON purchase_orders(supplier_id, created_at);

CREATE INDEX IF NOT EXISTS idx_purchase_orders_location_created_at
    ON purchase_orders(location_id, created_at);

CREATE INDEX IF NOT EXISTS idx_purchase_orders_po_status
    ON purchase_orders(po_status);

CREATE INDEX IF NOT EXISTS idx_purchase_orders_expected_at
    ON purchase_orders(expected_at);

CREATE INDEX IF NOT EXISTS idx_purchase_order_line_purchase_order
    ON purchase_order_line(purchase_order_id);

CREATE INDEX IF NOT EXISTS idx_purchase_order_line_supplier_item
    ON purchase_order_line(supplier_item_id);

CREATE INDEX IF NOT EXISTS idx_menu_item_name
    ON menu_item(name);

CREATE INDEX IF NOT EXISTS idx_recipe_line_ingredient_id
    ON recipe_line(ingredient_id);

CREATE INDEX IF NOT EXISTS idx_recipe_line_menu_item_id
    ON recipe_line(menu_item_id);

CREATE INDEX IF NOT EXISTS idx_sale
    ON sale(sold_at, location_id);

CREATE INDEX IF NOT EXISTS idx_sale_line_menu_item
    ON sale_line(menu_item_id);

CREATE INDEX IF NOT EXISTS idx_sale_line_sale
    ON sale_line(sale_id);

CREATE INDEX IF NOT EXISTS idx_delivery_supplier
    ON delivery(supplier_id);

CREATE INDEX IF NOT EXISTS idx_delivery_location
    ON delivery(location_id);

CREATE INDEX IF NOT EXISTS idx_delivery_expected_arrival
    ON delivery(expected_arrival);

CREATE INDEX IF NOT EXISTS idx_delivery_line_delivery
    ON delivery_line(delivery_id);

CREATE INDEX IF NOT EXISTS idx_receipt_lot_inv_lot
    ON receipt_lot(inventory_lot_id);

CREATE INDEX IF NOT EXISTS idx_receipt_lot_delivery_line
    ON receipt_lot(delivery_line_id);

CREATE INDEX IF NOT EXISTS idx_inventory_lot_location_ingredient_expiry_date
    ON inventory_lot( location_id, ingredient_id, expiry_date);

CREATE INDEX IF NOT EXISTS idx_inventory_lot_location_ingredient_id
    ON inventory_lot(location_id, ingredient_id);

CREATE INDEX IF NOT EXISTS idx_inventory_lot_expiry_date
    ON inventory_lot(expiry_date);