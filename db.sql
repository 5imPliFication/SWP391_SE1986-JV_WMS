DROP DATABASE IF EXISTS laptop_wms;
CREATE DATABASE laptop_wms;
USE laptop_wms;

-- 1. Tạo bảng Vai trò (Roles)
CREATE TABLE roles
(
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Tạo bảng Quyền hạn (Permissions)
CREATE TABLE permissions
(
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT
);

-- 3. Tạo bảng Người dùng (Users)
CREATE TABLE users
(
    id INT PRIMARY KEY,
    fullname VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role_id INT,
    -- Sửa từ VARCHAR thành INT để khớp với bảng roles
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- 4. Tạo bảng trung gian (Role_Permissions)
CREATE TABLE role_permissions
(
    role_id INT,
    permission_id INT,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions(id) ON DELETE CASCADE
    -- Sửa tên bảng permissions
);

-- Nhập Vai trò
INSERT INTO roles
    (id, name, description)
VALUES
    (1, 'Admin', 'Toàn quyền hệ thống'),
    (2, 'Staff', 'Chỉnh sửa nội dung');

-- Nhập Quyền hạn
INSERT INTO permissions
    (id, name, description)
VALUES
    (1, 'CREATE_USER', 'Quyền tạo người dùng mới'),
    (2, 'READ_USER', 'Quyền xem danh sách và chi tiết người dùng'),
    (3, 'UPDATE_USER', 'Quyền chỉnh sửa thông tin người dùng'),
    (4, 'DELETE_USER', 'Quyền xóa người dùng khỏi hệ thống'),
    (5, 'CREATE_ORDER', 'Quyền tạo đơn hàng mới'),
    (6, 'READ_ORDER', 'Quyền xem danh sách và chi tiết đơn hàng'),
    (7, 'UPDATE_ORDER', 'Quyền cập nhật trạng thái đơn hàng'),
    (8, 'DELETE_ORDER', 'Quyền hủy hoặc xóa đơn hàng'),
    (9, 'READ_ROLE', 'Quyền xem danh sách vai trò'),
    (10, 'CREATE_ROLE', 'Quyền tạo mới vai trò'),
    (11, 'UPDATE_ROLE', 'Quyền chỉnh sửa vai trò'),
    (12, 'DELETE_ROLE', 'Quyền xóa vai trò');

-- Gán quyền cho vai trò
INSERT INTO role_permissions
    (role_id, permission_id)
VALUES
    (1, 1),
    (1, 2),
    (2, 2);

-- Nhập Người dùng
INSERT INTO users
    (id, fullname, email, password_hash, role_id)
VALUES
    (1, 'admin', 'admin@gmail.com', 'admin', 1),
    (2, 'staff', 'staff@gmail.com', 'staff', 2);