CREATE DATABASE IF NOT EXISTS laptop_wms;

USE laptop_wms;

-- 1. Tạo bảng Vai trò (Roles)
CREATE TABLE roles (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Tạo bảng Quyền hạn (Permission)
CREATE TABLE permission (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT
);

-- 3. Tạo bảng Người dùng (User)
CREATE TABLE user (
    id VARCHAR(50) PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role_id VARCHAR(50),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- 4. Tạo bảng trung gian Phân quyền cho Vai trò (Role_Permissions)
-- Đây là bảng liên kết n-n giữa Roles và Permission
CREATE TABLE role_permissions (
    role_id VARCHAR(50),
    permission_id VARCHAR(50),
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permission(id) ON DELETE CASCADE
);

-- Nhập Vai trò
INSERT INTO roles (id, name, description) VALUES 
('R01', 'Admin', 'Toàn quyền hệ thống'),
('R02', 'Staff', 'Chỉnh sửa nội dung');

-- Nhập Quyền hạn
INSERT INTO permission (id, name, description) VALUES 
('P01', 'CREATE_USER', 'Quyền tạo người dùng mới'),
('P02', 'EDIT_POST', 'Quyền chỉnh sửa bài viết'),
('P03', 'VIEW_REPORT', 'Quyền xem báo cáo');

-- Gán quyền cho vai trò (Role_Permissions)
INSERT INTO role_permissions (role_id, permission_id) VALUES 
('R01', 'P01'), -- Admin có quyền tạo user
('R01', 'P02'), -- Admin có quyền sửa bài
('R02', 'P02'); -- Editor có quyền sửa bài

-- Nhập Người dùng
INSERT INTO user (id, username, email, password_hash, role_id) VALUES 
('U01', 'admin', 'admin@gmail.com', 'admin', 'R01'),
('U02', 'staff', 'staff@gmail.com', 'staff', 'R02');