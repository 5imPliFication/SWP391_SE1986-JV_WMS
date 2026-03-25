-- ==========================================
-- THIẾT LẬP HỆ THỐNG
-- ==========================================
DROP DATABASE IF EXISTS laptop_wms_3;
CREATE DATABASE laptop_wms_3;
USE laptop_wms_3;

-- ==========================================
-- 1. CÁC BẢNG DANH MỤC ĐỘC LẬP
-- ==========================================

CREATE TABLE roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE permissions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) UNIQUE NOT NULL,
    description VARCHAR(255)
);

CREATE TABLE brands (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE units (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE chips (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE rams (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    size VARCHAR(100) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE storages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    size VARCHAR(100) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE sizes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    size VARCHAR(100) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE
);

-- ==========================================
-- 2. NHÂN SỰ & HỆ THỐNG (PHỤ THUỘC CẤP 1)
-- ==========================================

CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    fullname VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    role_id BIGINT,
    CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE role_permissions (
    role_id BIGINT,
    permission_id BIGINT,
    PRIMARY KEY (role_id, permission_id),
    CONSTRAINT fk_rp_role FOREIGN KEY (role_id) REFERENCES roles(id),
    CONSTRAINT fk_rp_permission FOREIGN KEY (permission_id) REFERENCES permissions(id)
);

CREATE TABLE password_resets (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_pr_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE user_activity_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    activity VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_log_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- ==========================================
-- 3. SẢN PHẨM & KHO (PHỤ THUỘC CẤP 2)
-- ==========================================

CREATE TABLE models (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    brand_id BIGINT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_model_brand FOREIGN KEY (brand_id) REFERENCES brands(id)
);

CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    img_url VARCHAR(255),
    total_quantity INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    brand_id BIGINT,
    category_id BIGINT,
    unit_id BIGINT,
    model_id BIGINT,
    chip_id BIGINT,
    ram_id BIGINT,
    storage_id BIGINT,
    size_id BIGINT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_prod_brand FOREIGN KEY (brand_id) REFERENCES brands(id),
    CONSTRAINT fk_prod_cat FOREIGN KEY (category_id) REFERENCES categories(id),
    CONSTRAINT fk_prod_unit FOREIGN KEY (unit_id) REFERENCES units(id),
    CONSTRAINT fk_prod_model FOREIGN KEY (model_id) REFERENCES models(id),
    CONSTRAINT fk_prod_chip FOREIGN KEY (chip_id) REFERENCES chips(id),
    CONSTRAINT fk_prod_ram FOREIGN KEY (ram_id) REFERENCES rams(id),
    CONSTRAINT fk_prod_storage FOREIGN KEY (storage_id) REFERENCES storages(id),
    CONSTRAINT fk_prod_size FOREIGN KEY (size_id) REFERENCES sizes(id)
);

-- ==========================================
-- 4. GIAO DỊCH VÀ QUY TRÌNH (NHẬP - XUẤT - KIỂM KÊ)
-- ==========================================

-- KIỂM KÊ
CREATE TABLE inventory_audits (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    audit_code VARCHAR(255) UNIQUE,
    created_by BIGINT,
    status ENUM('PENDING', 'SUBMITTED', 'COMPLETED', 'REJECTED', 'CANCELLED'),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_audit_creator FOREIGN KEY (created_by) REFERENCES users(id)
);

CREATE TABLE inventory_audit_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    audit_id BIGINT,
    product_id BIGINT,
    system_quantity BIGINT,
    physical_quantity BIGINT,
    discrepancy BIGINT,
    reason TEXT,
    CONSTRAINT fk_iai_audit FOREIGN KEY (audit_id) REFERENCES inventory_audits(id),
    CONSTRAINT fk_iai_product FOREIGN KEY (product_id) REFERENCES products(id)
);

-- PHIẾU YÊU CẦU NHẬP HÀNG
CREATE TABLE purchase_requests (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    request_code VARCHAR(255) UNIQUE,
    created_by BIGINT,
    approved_by BIGINT,
    status ENUM('PENDING', 'APPROVED', 'REJECTED', 'COMPLETED'),
    note TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_pr_creator FOREIGN KEY (created_by) REFERENCES users(id),
    CONSTRAINT fk_pr_approver FOREIGN KEY (approved_by) REFERENCES users(id)
);

CREATE TABLE purchase_request_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    purchase_request_id BIGINT,
    product_id BIGINT,
    quantity BIGINT,
    CONSTRAINT fk_pri_request FOREIGN KEY (purchase_request_id) REFERENCES purchase_requests(id),
    CONSTRAINT fk_pri_product FOREIGN KEY (product_id) REFERENCES products(id)
);

-- NHẬP KHO (Phải có trước items)
CREATE TABLE goods_receipts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    purchase_request_id BIGINT,
    warehouse_id BIGINT,
    received_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_gr_request FOREIGN KEY (purchase_request_id) REFERENCES purchase_requests(id),
    CONSTRAINT fk_gr_staff FOREIGN KEY (warehouse_id) REFERENCES users(id)
);

CREATE TABLE goods_receipt_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    goods_receipt_id BIGINT,
    product_id BIGINT,
    actual_quantity BIGINT,
    CONSTRAINT fk_gri_receipt FOREIGN KEY (goods_receipt_id) REFERENCES goods_receipts(id),
    CONSTRAINT fk_gri_product FOREIGN KEY (product_id) REFERENCES products(id)
);

-- CHI TIẾT SERIAL SẢN PHẨM
CREATE TABLE product_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    serial VARCHAR(100) UNIQUE,
    imported_price DECIMAL(15, 2),
    current_price DECIMAL(15, 2),
    is_active BOOLEAN DEFAULT TRUE,
    imported_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME ON UPDATE CURRENT_TIMESTAMP,
    product_id BIGINT,
    goods_receipt_item_id BIGINT,
    CONSTRAINT fk_pi_receipt_item FOREIGN KEY (goods_receipt_item_id) REFERENCES goods_receipt_items(id),
    CONSTRAINT fk_pi_product FOREIGN KEY (product_id) REFERENCES products(id)
);

-- ĐƠN HÀNG XUẤT KHO
CREATE TABLE orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_code VARCHAR(255) UNIQUE,
    customer_name VARCHAR(255),
    customer_phone VARCHAR(255),
    status ENUM('DRAFT', 'SUBMITTED','PROCESSING', 'COMPLETED', 'CANCELLED') DEFAULT 'DRAFT',
    total_price DOUBLE,
    discount_amount DECIMAL(15, 2) DEFAULT 0,
    final_total DECIMAL(15, 2) DEFAULT 0,
    note TEXT,
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    processed_at DATETIME,
    created_by BIGINT,
    processed_by BIGINT,
    coupon_id BIGINT NULL,
    CONSTRAINT fk_order_creator FOREIGN KEY (created_by) REFERENCES users(id),
    CONSTRAINT fk_order_processor FOREIGN KEY (processed_by) REFERENCES users(id)
);

CREATE TABLE order_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT,
    product_id BIGINT NULL DEFAULT NULL,
    quantity BIGINT NOT NULL,
    price_at_purchase DECIMAL(15, 2) NULL DEFAULT NULL,
    created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_oi_order FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT fk_oi_product FOREIGN KEY (product_id) REFERENCES products(id)  
);

CREATE TABLE order_item_product_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_item_id BIGINT,
    product_item_id BIGINT,
    ordered_price DOUBLE,
    processed_by BIGINT,
    exported_at DATETIME,
    UNIQUE KEY (order_item_id, product_item_id),
    CONSTRAINT fk_oipi_item FOREIGN KEY (order_item_id) REFERENCES order_items(id),
    CONSTRAINT fk_oipi_prod_item FOREIGN KEY (product_item_id) REFERENCES product_items(id),
    CONSTRAINT fk_oipi_user FOREIGN KEY (processed_by) REFERENCES users(id)
);

-- LỊCH SỬ BIẾN ĐỘNG & BÁO CÁO
CREATE TABLE stock_movements (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  product_id BIGINT,
  quantity BIGINT,
  type ENUM('IMPORT', 'EXPORT', 'ADJUSTMENT', 'RETURN'),
  reference_type ENUM('ORDER', 'GOODS_RECEIPT', 'AUDIT_ADJUSTMENT'),
  created_at DATETIME,
  CONSTRAINT fk_sm_product FOREIGN KEY (product_id) REFERENCES products(id)
);

use laptop_wms_3;

INSERT INTO brands (id, name) VALUES
(1, 'Dell'),
(2, 'Apple'),
(3, 'Asus');

INSERT INTO categories (id, name) VALUES
(1, 'Laptop');

INSERT INTO units (id, name) VALUES
(1, 'Piece');

INSERT INTO chips (id, name) VALUES
(1, 'Intel i5'),
(2, 'Intel i7'),
(3, 'Apple M1'),
(4, 'Apple M2');

INSERT INTO rams (id, size) VALUES
(1, '8GB'),
(2, '16GB');

INSERT INTO storages (id, size) VALUES
(1, '256GB SSD'),
(2, '512GB SSD');

INSERT INTO sizes (id, size) VALUES
(1, '13 inch'),
(2, '15 inch');

INSERT INTO models (id, name, brand_id) VALUES
(1, 'XPS 13', 1),
(2, 'MacBook Air M1', 2),
(3, 'MacBook Air M2', 2),
(4, 'ROG Zephyrus', 3);

INSERT INTO products 
(id, name, total_quantity, brand_id, category_id, unit_id, model_id, chip_id, ram_id, storage_id, size_id)
VALUES

-- Dell XPS
(1, 'Dell XPS 13 i5 8GB 256GB', 15, 1, 1, 1, 1, 1, 1, 1, 1),
(2, 'Dell XPS 13 i7 16GB 512GB', 23, 1, 1, 1, 1, 2, 2, 2, 1),

-- MacBook
(3, 'MacBook Air M1 8GB 256GB', 40, 2, 1, 1, 2, 3, 1, 1, 1),
(4, 'MacBook Air M2 16GB 512GB', 25, 2, 1, 1, 3, 4, 2, 2, 1),

-- Asus Gaming
(5, 'Asus ROG Zephyrus i7 16GB 512GB', 13, 3, 1, 1, 4, 2, 2, 2, 2);

INSERT INTO goods_receipts (id, warehouse_id) VALUES
(1, 1);

INSERT INTO goods_receipt_items (id, goods_receipt_id, product_id, actual_quantity) VALUES
(1, 1, 1, 5),
(2, 1, 2, 3),
(3, 1, 3, 4),
(4, 1, 4, 2),
(5, 1, 5, 3);

INSERT INTO product_items (serial, imported_price, current_price, product_id, goods_receipt_item_id)
VALUES
('DX13-001', 800, 950, 1, 1),
('DX13-002', 800, 950, 1, 1),
('DX13-003', 800, 950, 1, 1),
('DX13-004', 800, 950, 1, 1),
('DX13-005', 800, 950, 1, 1);

INSERT INTO product_items VALUES
(NULL,'DX13I7-001',1000,1200,TRUE,NOW(),NULL,2,2),
(NULL,'DX13I7-002',1000,1200,TRUE,NOW(),NULL,2,2),
(NULL,'DX13I7-003',1000,1200,TRUE,NOW(),NULL,2,2);

INSERT INTO product_items VALUES
(NULL,'MBA-M1-001',700,900,TRUE,NOW(),NULL,3,3),
(NULL,'MBA-M1-002',700,900,TRUE,NOW(),NULL,3,3),
(NULL,'MBA-M1-003',700,900,TRUE,NOW(),NULL,3,3),
(NULL,'MBA-M1-004',700,900,TRUE,NOW(),NULL,3,3);

INSERT INTO product_items VALUES
(NULL,'MBA-M2-001',900,1200,TRUE,NOW(),NULL,4,4),
(NULL,'MBA-M2-002',900,1200,TRUE,NOW(),NULL,4,4);

INSERT INTO product_items VALUES
(NULL,'ROG-001',1100,1400,TRUE,NOW(),NULL,5,5),
(NULL,'ROG-002',1100,1400,TRUE,NOW(),NULL,5,5),
(NULL,'ROG-003',1100,1400,TRUE,NOW(),NULL,5,5);