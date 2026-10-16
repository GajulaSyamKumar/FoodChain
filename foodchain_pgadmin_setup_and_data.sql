-- ============================================================
-- FOODCHAIN PLATFORM — PGADMIN SETUP & DATA MIGRATION SCRIPT
-- Database: foodchain_db
-- Differentiates:
--   1. USER (Farmer / Producer): Produces food, manages inventory & fulfills orders
--   2. CUSTOMER (Buyer / Consumer): Browses produce, places orders & tracks delivery
-- Credentials: Dynamically registered via backend and stored in PostgreSQL
-- ============================================================

-- Table: farmers (User / Farmer domain)
CREATE TABLE IF NOT EXISTS farmers (
    id BIGSERIAL PRIMARY KEY,
    farmer_code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(50),
    password VARCHAR(255) NOT NULL DEFAULT '1234',
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Table: customers (Customer / Buyer domain)
CREATE TABLE IF NOT EXISTS customers (
    id BIGSERIAL PRIMARY KEY,
    customer_code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(50),
    city VARCHAR(100) DEFAULT 'Vijayawada',
    address TEXT,
    password VARCHAR(255) NOT NULL DEFAULT '1234',
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Ensure columns exist in case tables were previously created without them
ALTER TABLE farmers ADD COLUMN IF NOT EXISTS password VARCHAR(255) DEFAULT '1234';
ALTER TABLE farmers ADD COLUMN IF NOT EXISTS phone VARCHAR(50);
ALTER TABLE farmers ADD COLUMN IF NOT EXISTS email VARCHAR(255);

ALTER TABLE customers ADD COLUMN IF NOT EXISTS password VARCHAR(255) DEFAULT '1234';
ALTER TABLE customers ADD COLUMN IF NOT EXISTS phone VARCHAR(50);
ALTER TABLE customers ADD COLUMN IF NOT EXISTS city VARCHAR(100) DEFAULT 'Vijayawada';
ALTER TABLE customers ADD COLUMN IF NOT EXISTS address TEXT;

-- View: users (Provides pgAdmin and SQL queries seamless access to Farmer/User records)
CREATE OR REPLACE VIEW users AS
SELECT 
    id,
    farmer_code AS user_code,
    name,
    city,
    email,
    phone,
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

-- Create Indexes for fast lookup
CREATE INDEX IF NOT EXISTS idx_farmers_code ON farmers(farmer_code);
CREATE INDEX IF NOT EXISTS idx_farmers_name ON farmers(LOWER(name));
CREATE INDEX IF NOT EXISTS idx_customers_code ON customers(customer_code);
CREATE INDEX IF NOT EXISTS idx_customers_email ON customers(LOWER(email));
CREATE INDEX IF NOT EXISTS idx_products_name ON products(LOWER(product_name));
CREATE INDEX IF NOT EXISTS idx_products_farmer ON products(farmer_id);
CREATE INDEX IF NOT EXISTS idx_orders_customer ON orders(customer_id);
CREATE INDEX IF NOT EXISTS idx_orders_farmer ON orders(farmer_name);
CREATE INDEX IF NOT EXISTS idx_orders_status ON orders(status);
