-- ============================================================
-- FOODCHAIN PLATFORM — PGADMIN SETUP & DATA MIGRATION SCRIPT
-- Database: foodchain_db
-- Differentiates:
--   1. USER (Farmer / Producer): Produces food, manages inventory & fulfills orders
--   2. CUSTOMER (Buyer / Consumer): Browses produce, places orders & tracks delivery
-- ============================================================

-- Table: farmers (User / Farmer domain)
CREATE TABLE IF NOT EXISTS farmers (
    id BIGSERIAL PRIMARY KEY,
    farmer_code VARCHAR(50) UNIQUE,
    name VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Table: customers (Customer / Buyer domain)
CREATE TABLE IF NOT EXISTS customers (
    id BIGSERIAL PRIMARY KEY,
    customer_code VARCHAR(50) UNIQUE,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- View: users (Provides pgAdmin and SQL queries seamless access to Farmer/User records)
CREATE OR REPLACE VIEW users AS
SELECT 
    id,
    farmer_code AS user_code,
    name,
    city,
    'FARMER' AS role,
    created_at,
    updated_at
FROM farmers;

-- Table: products
CREATE TABLE IF NOT EXISTS products (
    id BIGSERIAL PRIMARY KEY,
    product_code INTEGER UNIQUE NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    description TEXT,
    quantity INTEGER NOT NULL CHECK (quantity >= 0),
    unit VARCHAR(20) DEFAULT 'kg',
    price DOUBLE PRECISION DEFAULT 0.0 CHECK (price >= 0),
    farmer_id BIGINT REFERENCES farmers(id) ON DELETE SET NULL,
    farmer_name VARCHAR(255),
    warehouse VARCHAR(255) NOT NULL,
    zone VARCHAR(100) DEFAULT 'Krishna',
    category VARCHAR(100) DEFAULT 'Vegetables',
    image VARCHAR(500),
    status VARCHAR(50) DEFAULT 'Available',
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Table: orders
CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY,
    order_id VARCHAR(50) UNIQUE NOT NULL,
    customer_id BIGINT REFERENCES customers(id) ON DELETE SET NULL,
    product_id BIGINT REFERENCES products(id) ON DELETE SET NULL,
    product_name VARCHAR(255) NOT NULL,
    product_code INTEGER NOT NULL,
    farmer_name VARCHAR(255),
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    unit_price_at_order DOUBLE PRECISION NOT NULL,
    total_price DOUBLE PRECISION NOT NULL,
    warehouse VARCHAR(255) NOT NULL,
    destination_city VARCHAR(100) NOT NULL,
    zone VARCHAR(100) DEFAULT 'Krishna',
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Table: tracking_history
CREATE TABLE IF NOT EXISTS tracking_history (
    id BIGSERIAL PRIMARY KEY,
    order_id VARCHAR(50) NOT NULL REFERENCES orders(order_id) ON DELETE CASCADE,
    status VARCHAR(50) NOT NULL,
    description TEXT,
    checkpoint_time TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create Indexes
CREATE INDEX IF NOT EXISTS idx_products_name ON products(LOWER(product_name));
CREATE INDEX IF NOT EXISTS idx_products_farmer ON products(farmer_id);
CREATE INDEX IF NOT EXISTS idx_orders_customer ON orders(customer_id);
CREATE INDEX IF NOT EXISTS idx_orders_farmer ON orders(farmer_name);
CREATE INDEX IF NOT EXISTS idx_orders_status ON orders(status);

-- Seed Sample Customers if empty
INSERT INTO customers (customer_code, name, email)
SELECT 'CUST-1001', 'Kiran Varma', 'kiran@foodchain.local'
WHERE NOT EXISTS (SELECT 1 FROM customers WHERE customer_code = 'CUST-1001');

INSERT INTO customers (customer_code, name, email)
SELECT 'CUST-1002', 'Ananya Sharma', 'ananya@foodchain.local'
WHERE NOT EXISTS (SELECT 1 FROM customers WHERE customer_code = 'CUST-1002');
