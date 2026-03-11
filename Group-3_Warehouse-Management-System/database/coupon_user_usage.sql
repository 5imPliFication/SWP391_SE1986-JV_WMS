-- Create coupon_user_usage table for tracking customer's coupon usage
-- This table is required to track which coupons have been used by which customers
-- Purpose: Prevent the same customer from using the same coupon multiple times
-- A staff member can apply the same coupon to different customers, but each customer can only use it once

CREATE TABLE IF NOT EXISTS coupon_user_usage (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Unique identifier for the usage record',
    customer_name VARCHAR(255) NOT NULL COMMENT 'Customer name (from orders table) who used the coupon',
    coupon_id BIGINT NOT NULL COMMENT 'Reference to the coupon that was used',
    used_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Timestamp when the coupon was used',
    
    -- Unique constraint to prevent the same customer from using the same coupon twice
    UNIQUE KEY uk_customer_coupon (customer_name, coupon_id),
    
    -- Foreign key to coupons table
    CONSTRAINT fk_coupon_user_usage_coupon FOREIGN KEY (coupon_id) 
        REFERENCES coupons (id) ON DELETE CASCADE ON UPDATE CASCADE,
    
    -- Indexes for faster queries
    KEY idx_customer_name (customer_name),
    KEY idx_coupon_id (coupon_id),
    KEY idx_used_at (used_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci 
COMMENT='Tracks which coupons have been used by which customers (identified by customer name) to prevent duplicate usage per customer';
