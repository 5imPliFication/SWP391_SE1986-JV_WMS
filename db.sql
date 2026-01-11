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

-- 2. Tạo bảng Quyền hạn (Permission)
CREATE TABLE permissions
(
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT
);

-- 3. Tạo bảng Người dùng (User)
CREATE TABLE users
(
    id INT PRIMARY KEY,
    fullname VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role_id VARCHAR(50),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- 4. Tạo bảng trung gian Phân quyền cho Vai trò (Role_Permissions)
-- Đây là bảng liên kết n-n giữa Roles và Permission
CREATE TABLE role_permissions
(
    role_id INT,
    permission_id INT,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permission(id) ON DELETE CASCADE
);

-- Nhập Vai trò
INSERT INTO roles
    (id, name, description)
VALUES
    ('1', 'Admin', 'Toàn quyền hệ thống'),
    ('2', 'Staff', 'Chỉnh sửa nội dung');

-- Nhập Quyền hạn
INSERT INTO permissions
    (id, name, description)
VALUES
    ('1', 'CREATE_USER', 'Quyền tạo người dùng mới'),
    ('2', 'READ_USER', 'Quyền xem danh sách và chi tiết người dùng'),
    ('3', 'UPDATE_USER', 'Quyền chỉnh sửa thông tin người dùng'),
    ('4', 'DELETE_USER', 'Quyền xóa người dùng khỏi hệ thống'),

    -- Nhóm Order (CRUD)
    ('5', 'CREATE_ORDER', 'Quyền tạo đơn hàng mới'),
    ('6', 'READ_ORDER', 'Quyền xem danh sách và chi tiết đơn hàng'),
    ('7', 'UPDATE_ORDER', 'Quyền cập nhật trạng thái đơn hàng'),
    ('8', 'DELETE_ORDER', 'Quyền hủy hoặc xóa đơn hàng');

-- Gán quyền cho vai trò (Role_Permissions)
INSERT INTO role_permissions
    (role_id, permission_id)
VALUES
    ('1', '1'),
    -- Admin có quyền tạo user
    ('1', '2'),
    -- Admin có quyền sửa bài
    ('2', '2');
-- Editor có quyền sửa bài

-- Nhập Người dùng
INSERT INTO users
    (id, fullname, email, password_hash, role_id)
VALUES
    ('1', 'admin', 'admin@gmail.com', 'admin', 'R01'),
    ('2', 'staff', 'staff@gmail.com', 'staff', 'R02');