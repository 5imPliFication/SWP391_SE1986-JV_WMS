DROP DATABASE IF EXISTS laptop_wms;
CREATE DATABASE laptop_wms;
USE laptop_wms;

-- 1. Bảng Roles
CREATE TABLE roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Bảng Permissions
CREATE TABLE permissions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT
);

-- 3. Bảng Users
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fullname VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role_id INT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_edited_at TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- 4. Bảng Role_Permissions (n-n)
CREATE TABLE role_permissions (
    role_id INT,
    permission_id INT,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions(id) ON DELETE CASCADE
);

-- ===== DỮ LIỆU MẪU =====

-- Roles
INSERT INTO roles (name, description) VALUES 
('Admin', 'Toàn quyền hệ thống'),
('Staff', 'Chỉnh sửa nội dung');

-- Permissions
INSERT INTO permissions (name, description) VALUES 
('CREATE_USER', 'Quyền tạo người dùng mới'),
('EDIT_POST', 'Quyền chỉnh sửa bài viết'),
('VIEW_REPORT', 'Quyền xem báo cáo');

-- Gán quyền cho roles
INSERT INTO role_permissions (role_id, permission_id) VALUES 
(1, 1),
(1, 2),
(1, 3),
(2, 2);

-- Users
INSERT INTO users (fullname, email, password_hash, role_id) VALUES 
('admin', 'admin@gmail.com', 'admin', 1),
('staff', 'staff@gmail.com', 'staff', 2);
