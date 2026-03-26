-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: laptop_wms_4
-- ------------------------------------------------------
-- Server version	9.5.0


create database laptop_wms_4;
use laptop_wms_4;

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- Table structure for table `brands`
--

DROP TABLE IF EXISTS `brands`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `brands` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `brands`
--

LOCK TABLES `brands` WRITE;
/*!40000 ALTER TABLE `brands` DISABLE KEYS */;
INSERT INTO `brands` VALUES (1,'Apple','Apple description',0,'2025-02-28 19:40:08','2026-03-25 03:43:21'),(2,'Samsung','Samsung description',0,'2024-01-17 05:22:22','2026-03-25 03:43:27'),(3,'Dell','Dell description',1,'2025-09-10 12:11:00','2024-01-04 14:49:31'),(4,'HP','HP description',1,'2025-11-05 02:55:48','2024-12-30 20:09:39'),(5,'Sony','Sony description',0,'2024-10-25 12:05:22','2026-03-25 03:42:00'),(6,'LG','LG description',0,'2024-11-24 20:08:32','2026-03-25 03:41:57'),(7,'Asus','Asus description',1,'2024-11-06 22:32:10','2024-04-18 10:04:51'),(8,'Acer','Acer description',1,'2025-12-15 12:49:12','2024-10-11 22:30:06'),(9,'Lenovo','Lenovo description',1,'2024-07-11 01:59:56','2025-10-04 09:27:14'),(10,'MSI','MSI description',1,'2025-01-29 13:47:52','2024-09-02 18:35:58'),(11,'TUF','Gaming',1,'2026-03-25 04:33:35',NULL),(12,'ROG','Gaming/Entertainment',1,'2026-03-25 04:33:35',NULL),(13,'Apple','Soft',1,'2026-03-25 04:40:04',NULL);
/*!40000 ALTER TABLE `brands` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'Gaming','Gaming category',1,'2024-09-10 02:21:41','2024-09-08 22:55:44'),(2,'Office','Office category',1,'2024-06-16 04:11:27','2024-02-11 09:40:59'),(3,'Ultrabook','Ultrabook category',1,'2024-09-27 00:29:24','2025-05-03 08:46:56'),(7,'Macbook','Macbook',1,'2026-03-25 04:39:09',NULL);
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chips`
--

DROP TABLE IF EXISTS `chips`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chips` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chips`
--

LOCK TABLES `chips` WRITE;
/*!40000 ALTER TABLE `chips` DISABLE KEYS */;
INSERT INTO `chips` VALUES (1,'i5-12450H',1),(2,'i7-12450H',1),(3,'Ryzen 5 5600H',1),(4,'Ultra 5 235U',1),(5,'i5-1334U',1),(6,'Ultra 7 258V',1),(7,'Ryzen AI 9',1),(8,' i5-13420H',1),(9,'Ryzen 5 150',1),(10,'Ultra 5 225U',1),(11,'Core 7 240H',1),(12,'Ultra 9 285HX',1),(13,'Ryzen™ 9-8940HX',1),(14,'6 CPU | 5 GPU',1),(15,'18 CPU | 40 GPU',1);
/*!40000 ALTER TABLE `chips` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `goods_receipt_items`
--

DROP TABLE IF EXISTS `goods_receipt_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `goods_receipt_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `goods_receipt_id` bigint DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  `actual_quantity` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_gri_receipt` (`goods_receipt_id`),
  KEY `fk_gri_product` (`product_id`),
  CONSTRAINT `fk_gri_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `fk_gri_receipt` FOREIGN KEY (`goods_receipt_id`) REFERENCES `goods_receipts` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goods_receipt_items`
--

LOCK TABLES `goods_receipt_items` WRITE;
/*!40000 ALTER TABLE `goods_receipt_items` DISABLE KEYS */;
INSERT INTO `goods_receipt_items` VALUES (1,1,2,3),(2,1,5,4),(3,2,9,3),(4,2,11,5),(5,3,5,7),(6,4,6,6),(7,4,10,5);
/*!40000 ALTER TABLE `goods_receipt_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `goods_receipts`
--

DROP TABLE IF EXISTS `goods_receipts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `goods_receipts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `purchase_request_id` bigint DEFAULT NULL,
  `warehouse_id` bigint DEFAULT NULL,
  `received_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `supplier` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_gr_request` (`purchase_request_id`),
  KEY `fk_gr_staff` (`warehouse_id`),
  CONSTRAINT `fk_gr_request` FOREIGN KEY (`purchase_request_id`) REFERENCES `purchase_requests` (`id`),
  CONSTRAINT `fk_gr_staff` FOREIGN KEY (`warehouse_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goods_receipts`
--

LOCK TABLES `goods_receipts` WRITE;
/*!40000 ALTER TABLE `goods_receipts` DISABLE KEYS */;
INSERT INTO `goods_receipts` VALUES (1,1,27,'2026-03-25 16:20:50','Công ty CP LaptopWorld'),(2,2,27,'2026-03-25 16:50:45','Laptop Hanoi Company'),(3,3,29,'2026-03-25 17:26:30','HCMC Laptop '),(4,4,25,'2026-03-25 17:46:42','Arakham Company');
/*!40000 ALTER TABLE `goods_receipts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory_audit_items`
--

DROP TABLE IF EXISTS `inventory_audit_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory_audit_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `audit_id` bigint DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  `system_quantity` bigint DEFAULT NULL,
  `physical_quantity` bigint DEFAULT NULL,
  `discrepancy` bigint DEFAULT NULL,
  `reason` text,
  PRIMARY KEY (`id`),
  KEY `fk_iai_audit` (`audit_id`),
  KEY `fk_iai_product` (`product_id`),
  CONSTRAINT `fk_iai_audit` FOREIGN KEY (`audit_id`) REFERENCES `inventory_audits` (`id`),
  CONSTRAINT `fk_iai_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory_audit_items`
--

LOCK TABLES `inventory_audit_items` WRITE;
/*!40000 ALTER TABLE `inventory_audit_items` DISABLE KEYS */;
INSERT INTO `inventory_audit_items` VALUES (1,1,17,0,20,-20,''),(2,2,21,0,NULL,NULL,NULL);
/*!40000 ALTER TABLE `inventory_audit_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory_audits`
--

DROP TABLE IF EXISTS `inventory_audits`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory_audits` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `audit_code` varchar(255) DEFAULT NULL,
  `created_by` bigint DEFAULT NULL,
  `status` enum('PENDING','COMPLETED','CANCELLED') DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `audit_code` (`audit_code`),
  KEY `fk_audit_creator` (`created_by`),
  CONSTRAINT `fk_audit_creator` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory_audits`
--

LOCK TABLES `inventory_audits` WRITE;
/*!40000 ALTER TABLE `inventory_audits` DISABLE KEYS */;
INSERT INTO `inventory_audits` VALUES (1,'AUD-260313',21,'COMPLETED','2026-03-13 14:38:18','2026-03-13 14:38:48'),(2,'AUD-260315',27,'PENDING','2026-03-15 02:24:49','2026-03-15 02:24:49');
/*!40000 ALTER TABLE `inventory_audits` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `models`
--

DROP TABLE IF EXISTS `models`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `models` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `brand_id` bigint NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `fk_model_brand` (`brand_id`),
  CONSTRAINT `fk_model_brand` FOREIGN KEY (`brand_id`) REFERENCES `brands` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `models`
--

LOCK TABLES `models` WRITE;
/*!40000 ALTER TABLE `models` DISABLE KEYS */;
INSERT INTO `models` VALUES (1,'Inspiron',3,1),(2,'Vostro',3,1),(3,'ThinkPad',9,1),(4,'Latitude',3,1),(5,'XPS',3,1),(6,'Zenbook',7,1),(7,'Vivobook',7,1),(8,'Aspire',8,1),(9,'Lite',8,1),(10,'Swift',8,1),(11,'Expertbook',7,1),(12,'V Series',9,1),(13,'Spectre Series',4,1),(14,'Envy Series',4,1),(15,'Pavilon Series',4,1),(16,'Probook Series',4,1),(17,'Venture',10,1),(18,'Mordern',10,1),(19,'Prestige',10,1),(20,'Pro',3,1),(21,'Thinkbook',9,1),(22,'Thinkbook',9,0),(23,'Titan',10,1),(24,'Strix',12,1),(25,'Neo',13,1),(26,'Air',13,1);
/*!40000 ALTER TABLE `models` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_item_product_items`
--

DROP TABLE IF EXISTS `order_item_product_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_item_product_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_item_id` bigint DEFAULT NULL,
  `product_item_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_item_id` (`order_item_id`,`product_item_id`),
  KEY `fk_oipi_prod_item` (`product_item_id`),
  CONSTRAINT `fk_oipi_item` FOREIGN KEY (`order_item_id`) REFERENCES `order_items` (`id`),
  CONSTRAINT `fk_oipi_prod_item` FOREIGN KEY (`product_item_id`) REFERENCES `product_items` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_item_product_items`
--

LOCK TABLES `order_item_product_items` WRITE;
/*!40000 ALTER TABLE `order_item_product_items` DISABLE KEYS */;
INSERT INTO `order_item_product_items` VALUES (1,1,1),(2,1,2),(3,2,28),(4,2,29);
/*!40000 ALTER TABLE `order_item_product_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  `quantity` bigint NOT NULL,
  `price_at_purchase` decimal(15,2) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_oi_order` (`order_id`),
  KEY `fk_oi_product` (`product_id`),
  CONSTRAINT `fk_oi_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `fk_oi_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_items`
--

LOCK TABLES `order_items` WRITE;
/*!40000 ALTER TABLE `order_items` DISABLE KEYS */;
INSERT INTO `order_items` VALUES (1,1,2,2,6666666.00,'2026-03-25 19:31:08'),(2,1,6,2,3333333.00,'2026-03-25 19:31:08');
/*!40000 ALTER TABLE `order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_code` varchar(255) DEFAULT NULL,
  `customer_name` varchar(255) DEFAULT NULL,
  `customer_phone` varchar(255) DEFAULT NULL,
  `status` enum('DRAFT','SUBMITTED','PROCESSING','COMPLETED','CANCELLED') DEFAULT 'DRAFT',
  `total_price` double DEFAULT NULL,
  `discount_amount` decimal(15,2) DEFAULT '0.00',
  `final_total` decimal(15,2) DEFAULT '0.00',
  `note` text,
  `order_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `processed_at` datetime DEFAULT NULL,
  `created_by` bigint DEFAULT NULL,
  `processed_by` bigint DEFAULT NULL,
  `coupon_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_code` (`order_code`),
  KEY `fk_order_creator` (`created_by`),
  KEY `fk_order_processor` (`processed_by`),
  KEY `fk_orders_coupon` (`coupon_id`),
  CONSTRAINT `fk_order_creator` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`),
  CONSTRAINT `fk_order_processor` FOREIGN KEY (`processed_by`) REFERENCES `users` (`id`),
  CONSTRAINT `fk_orders_coupon` FOREIGN KEY (`coupon_id`) REFERENCES `coupons` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,'ORD-f53010f9-0a1c-4c48-b7e1-ea924eff6b0e','Walk-in Customer','','COMPLETED',19999998,0.00,19999998.00,NULL,'2026-03-26 02:31:08','2026-03-26 02:33:05',11,25,NULL);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `password_resets`
--

DROP TABLE IF EXISTS `password_resets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `password_resets` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `status` enum('PENDING','APPROVED','REJECTED') DEFAULT 'PENDING',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_pr_user` (`user_id`),
  CONSTRAINT `fk_pr_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `password_resets`
--

LOCK TABLES `password_resets` WRITE;
/*!40000 ALTER TABLE `password_resets` DISABLE KEYS */;
INSERT INTO `password_resets` VALUES (1,1,'PENDING','2026-02-20 08:30:00','2026-02-20 08:30:00'),(2,2,'APPROVED','2026-02-21 09:15:00','2026-02-21 09:15:00'),(3,3,'REJECTED','2026-02-21 10:00:00','2026-02-21 10:00:00'),(4,4,'PENDING','2026-02-22 11:45:00','2026-02-22 11:45:00'),(5,5,'APPROVED','2026-02-22 14:20:00','2026-02-22 14:20:00'),(6,6,'PENDING','2026-02-23 07:00:00','2026-02-23 07:00:00'),(7,7,'REJECTED','2026-02-23 16:40:00','2026-02-23 16:40:00'),(8,8,'PENDING','2026-02-24 13:10:00','2026-02-24 13:10:00'),(9,9,'APPROVED','2026-02-24 15:00:00','2026-02-24 15:00:00'),(10,10,'PENDING','2026-02-25 09:00:00','2026-02-25 09:00:00'),(11,11,'REJECTED','2026-02-25 10:30:00','2026-02-25 10:30:00'),(12,12,'PENDING','2026-02-25 22:00:00','2026-02-25 22:00:00'),(13,13,'APPROVED','2026-02-26 01:20:00','2026-02-26 01:20:00'),(14,14,'PENDING','2026-02-26 05:00:00','2026-02-26 05:00:00'),(15,15,'REJECTED','2026-02-26 06:15:00','2026-02-26 06:15:00');
/*!40000 ALTER TABLE `password_resets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permissions`
--

DROP TABLE IF EXISTS `permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permissions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permissions`
--

LOCK TABLES `permissions` WRITE;
/*!40000 ALTER TABLE `permissions` DISABLE KEYS */;
INSERT INTO `permissions` VALUES (1,'CREATE_USER','Quyền tạo người dùng mới'),(2,'READ_USER','Quyền xem người dùng'),(3,'UPDATE_USER','Quyền chỉnh sửa người dùng'),(4,'DELETE_USER','Quyền xoá người dùng'),(5,'CREATE_ORDER','Quyền tạo đơn hàng'),(6,'READ_ORDER','Quyền xem đơn hàng'),(7,'UPDATE_ORDER','Quyền cập nhật đơn hàng'),(8,'DELETE_ORDER','Quyền xoá đơn hàng'),(9,'READ_ROLE','Quyền xem vai trò'),(10,'CREATE_ROLE','Quyền tạo vai trò'),(11,'UPDATE_ROLE','Quyền cập nhật vai trò'),(12,'DELETE_ROLE','Quyền xoá vai trò'),(13,'IMPORT_PRODUCT','Quyền nhập kho'),(14,'EXPORT_PRODUCT','Quyền xuất kho'),(15,'CREATE_PURCHASE_REQUEST','Quyền tạo đơn yêu cầu nhập hàng'),(16,'UPDATE_PURCHASE_REQUEST','Quyền cập nhật đơn yêu cầu nhập hàng'),(17,'READ_PURCHASE_REQUEST','Quyền đọc đơn yêu cầu nhập hàng'),(18,'READ_PASSWORD_RESET_REQUEST','Quyền xem yêu cầu cấp lại mật khẩu'),(19,'UPDATE_PASSWORD_RESET_REQUEST','Quyền phê duyệt hoặc từ chối yêu cầu cấp lại mật khẩu'),(20,'READ_PRODUCT','Quyền xem sản phẩm'),(21,'UPDATE_PRODUCT','Quyền chỉnh sửa sản phẩm'),(22,'CREATE_PRODUCT','Quyền thêm sản phẩm mới'),(23,'UPDATE_PRODUCT_ITEM','Quyền chỉnh sửa item thuộc sản phẩm'),(24,'READ_AUDIT','Quyền xem kiểm kê'),(25,'CREATE_AUDIT','Quyền tạo kiểm kê'),(26,'PERFORM_AUDIT','Quyền thực hiện kiểm kê'),(27,'CANCEL_AUDIT','Quyền huỷ kiểm kê'),(28,'READ_REPORT','Quyền xem báo cáo');
/*!40000 ALTER TABLE `permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_items`
--

DROP TABLE IF EXISTS `product_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `serial` varchar(100) DEFAULT NULL,
  `imported_price` decimal(15,2) DEFAULT NULL,
  `current_price` decimal(15,2) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `imported_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `product_id` bigint DEFAULT NULL,
  `goods_receipt_item_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `serial` (`serial`),
  KEY `fk_pi_receipt_item` (`goods_receipt_item_id`),
  KEY `fk_pi_product` (`product_id`),
  CONSTRAINT `fk_pi_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `fk_pi_receipt_item` FOREIGN KEY (`goods_receipt_item_id`) REFERENCES `goods_receipt_items` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_items`
--

LOCK TABLES `product_items` WRITE;
/*!40000 ALTER TABLE `product_items` DISABLE KEYS */;
INSERT INTO `product_items` VALUES (1,'abc120',6666666.00,6666666.00,0,'2026-03-25 16:20:50','2026-03-26 02:33:05',2,1),(2,'abc121',6666666.00,6666666.00,0,'2026-03-25 16:20:50','2026-03-26 02:33:05',2,1),(3,'abc122',6666666.00,6666666.00,1,'2026-03-25 16:20:50','2026-03-25 16:20:50',2,1),(4,'abc123',3333333.00,8888888.00,1,'2026-03-25 16:20:50','2026-03-25 17:26:30',5,2),(5,'abc124',3333333.00,8888888.00,1,'2026-03-25 16:20:50','2026-03-25 17:26:30',5,2),(6,'abc125',3333333.00,8888888.00,1,'2026-03-25 16:20:50','2026-03-25 17:26:30',5,2),(7,'abc126',3333333.00,8888888.00,1,'2026-03-25 16:20:50','2026-03-25 17:26:30',5,2),(8,'xyz145',12222224.00,12222224.00,1,'2026-03-25 16:50:45','2026-03-25 16:50:45',9,3),(9,'xyz146',12222224.00,12222224.00,1,'2026-03-25 16:50:45','2026-03-25 16:50:45',9,3),(10,'xyz143',12222224.00,12222224.00,1,'2026-03-25 16:50:45','2026-03-25 16:50:45',9,3),(11,'buc148',63333333.00,63333333.00,1,'2026-03-25 16:50:45','2026-03-25 16:50:45',11,4),(12,'buc149',63333333.00,63333333.00,1,'2026-03-25 16:50:45','2026-03-25 16:50:45',11,4),(13,'buc142',63333333.00,63333333.00,1,'2026-03-25 16:50:45','2026-03-25 16:50:45',11,4),(14,'buc141',63333333.00,63333333.00,1,'2026-03-25 16:50:45','2026-03-25 16:50:45',11,4),(15,'buc147',63333333.00,63333333.00,1,'2026-03-25 16:50:45','2026-03-25 16:50:45',11,4),(16,'zenAi9001',8888888.00,8888888.00,1,'2026-03-25 17:26:30','2026-03-25 17:26:30',5,5),(17,'zenAi9002',8888888.00,8888888.00,1,'2026-03-25 17:26:30','2026-03-25 17:26:30',5,5),(18,'zenAi9003',8888888.00,8888888.00,1,'2026-03-25 17:26:30','2026-03-25 17:26:30',5,5),(19,'zenAi9004',8888888.00,8888888.00,1,'2026-03-25 17:26:30','2026-03-25 17:26:30',5,5),(20,'zenAi9005',8888888.00,8888888.00,1,'2026-03-25 17:26:30','2026-03-25 17:26:30',5,5),(21,'zenAi9006',8888888.00,8888888.00,1,'2026-03-25 17:26:30','2026-03-25 17:26:30',5,5),(22,'zenAi9007',8888888.00,8888888.00,1,'2026-03-25 17:26:30','2026-03-25 17:26:30',5,5),(23,'titan9134',8888888.00,8888888.00,1,'2026-03-25 17:46:42','2026-03-25 17:46:42',10,7),(24,'titan9135',8888888.00,8888888.00,1,'2026-03-25 17:46:42','2026-03-25 17:46:42',10,7),(25,'titan9136',8888888.00,8888888.00,1,'2026-03-25 17:46:42','2026-03-25 17:46:42',10,7),(26,'titan9137',8888888.00,8888888.00,1,'2026-03-25 17:46:42','2026-03-25 17:46:42',10,7),(27,'titan9138',8888888.00,8888888.00,1,'2026-03-25 17:46:42','2026-03-25 17:46:42',10,7),(28,'asi567',3333333.00,3333333.00,0,'2026-03-25 17:46:42','2026-03-26 02:33:05',6,6),(29,'asi568',3333333.00,3333333.00,0,'2026-03-25 17:46:42','2026-03-26 02:33:05',6,6),(30,'asi569',3333333.00,3333333.00,1,'2026-03-25 17:46:42','2026-03-25 17:46:42',6,6),(31,'asi570',3333333.00,3333333.00,1,'2026-03-25 17:46:42','2026-03-25 17:46:42',6,6),(32,'asi571',3333333.00,3333333.00,1,'2026-03-25 17:46:42','2026-03-25 17:46:42',6,6),(33,'asi572',3333333.00,3333333.00,1,'2026-03-25 17:46:42','2026-03-25 17:46:42',6,6);
/*!40000 ALTER TABLE `product_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` text,
  `img_url` varchar(255) DEFAULT NULL,
  `total_quantity` int DEFAULT '0',
  `is_active` tinyint(1) DEFAULT '1',
  `brand_id` bigint DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  `unit_id` bigint DEFAULT NULL,
  `model_id` bigint DEFAULT NULL,
  `chip_id` bigint DEFAULT NULL,
  `ram_id` bigint DEFAULT NULL,
  `storage_id` bigint DEFAULT NULL,
  `size_id` bigint DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_prod_brand` (`brand_id`),
  KEY `fk_prod_cat` (`category_id`),
  KEY `fk_prod_unit` (`unit_id`),
  KEY `fk_prod_model` (`model_id`),
  KEY `fk_prod_chip` (`chip_id`),
  KEY `fk_prod_ram` (`ram_id`),
  KEY `fk_prod_storage` (`storage_id`),
  KEY `fk_prod_size` (`size_id`),
  CONSTRAINT `fk_prod_brand` FOREIGN KEY (`brand_id`) REFERENCES `brands` (`id`),
  CONSTRAINT `fk_prod_cat` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`),
  CONSTRAINT `fk_prod_chip` FOREIGN KEY (`chip_id`) REFERENCES `chips` (`id`),
  CONSTRAINT `fk_prod_model` FOREIGN KEY (`model_id`) REFERENCES `models` (`id`),
  CONSTRAINT `fk_prod_ram` FOREIGN KEY (`ram_id`) REFERENCES `rams` (`id`),
  CONSTRAINT `fk_prod_size` FOREIGN KEY (`size_id`) REFERENCES `sizes` (`id`),
  CONSTRAINT `fk_prod_storage` FOREIGN KEY (`storage_id`) REFERENCES `storages` (`id`),
  CONSTRAINT `fk_prod_unit` FOREIGN KEY (`unit_id`) REFERENCES `units` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'Dell Pro Ultra 5 235U 16GB 512GB 14 inch','CPU: Intel Core™ Ultra 5 235U vPro (2.00GHz up to 4.90GHz, 12MB Cache)\r\nRam: 16GB DDR5 (1x16GB)\r\nStorage: 512GB PCIe NVMe SSD\r\nVGA: Intel Graphics\r\nDisplay: 14 inch FHD+ (1920 x 1200) IPS, 60Hz, 300 nit, 45% NTSC, Anti-Glare, Non-Touch','/static/images/products/1774385745363_25451_24448_30287_laptop_dell_pro_14_pc14250_pc14250_255u_32512w.jpg',0,1,3,2,1,20,4,2,2,1,'2026-03-25 03:55:45','2026-03-25 03:55:45'),(2,'Dell Vostro Core i5-1334U 8GB 512GB 15.6 inch','CPU: Intel Core i5-1334U (1.30GHz up to 4.60GHz, 12MB Cache)\r\nRam: 8GB DDR4 3200MHz\r\nHard drive: 512GB M.2 PCIe SSD\r\nVGA: Intel Iris Xe Graphics\r\nDisplay: 15.6 inches FHD (1920 x 1080) 120Hz','/static/images/products/1774386115008_20998_dell_vostro_3530___en__5.jpg',3,1,3,2,1,2,5,1,2,2,'2026-03-25 04:01:55','2026-03-25 16:20:50'),(3,'Dell Latitude Core i5-1334U 8GB 512GB 14 inch','CPU: Intel Core i5-1335U (1.3GHz up to 4.60GHz, 12MB Cache)\r\nRam: 8GB DDR5 5200MHz (1x8GB)\r\nHard drive: 512GB PCIe Gen 4 NVMe (KYHD)\r\nVGA: Intel UHD Graphics\r\nDisplay: 14 inch FHD (1920 x 1080) WVA, 60Hz, 250 nit, 45% NTSC','/static/images/products/1774386237040_24372_30424_laptop_dell_latitude_3450_l3450_1335u_16512wn_bl__1_.jpg',0,1,3,2,1,4,5,1,2,1,'2026-03-25 04:03:57','2026-03-25 04:03:57'),(4,'Dell XPS Ultra 7 258V 16GB 1TB 13.4 inch','CPU: Intel® Core™ Ultra 7 258V (2.20GHz up to 4.80GHz, 12MB Cache)\r\nRam: 16GB LPDDR5X 8533MHz\r\nHard drive: 1TB M.2 PCIe NVMe SSD\r\nVGA: Intel® Arc™ graphics\r\nDisplay: 13.4inch QHD+(2560x1600), 120Hz, Anti-Glare, 500nit, Eyesafe®, InfinityEdge, Touch','/static/images/products/1774386514163_25355_21467_dell_xps_13_9350_x__m__7.jpg',0,1,3,2,1,5,6,2,3,4,'2026-03-25 04:08:34','2026-03-25 04:08:34'),(5,'Asus Zenbook Ryzen AI 9 32GB 1TB 16 inch','CPU: AMD Ryzen™ AI 9 465 (2.0GHz up to 5.0GHz, 34MB Cache)\r\nAMD XDNA™ NPU up to 50TOPS\r\nRam: 32GB LPDDR5X Onboard\r\nHard drive: 1TB M.2 NVMe PCIe 4.0\r\nVGA: AMD Radeon™ Graphics\r\nDisplay: 16.0inch 3K (2880 x 1800) OLED, 16:10, 120Hz, 0.2ms, 500nits, 1000nits HDR peak brightness, 100% DCI-P3, 1,000,000:1, 1.07 billion colors, Glossy display, TÜV Rheinland-certified, PANTONE Validated','/static/images/products/1774386757077_24674_30698_laptop_asus_zenbook_s_16_um5606ga_ss384ws_1.jpg',11,1,7,3,1,6,7,3,3,3,'2026-03-25 04:12:37','2026-03-25 17:26:30'),(6,'Asus Vivobook i5-13420H 16GB 512GB 14 inch','CPU: Intel® Core™ i5-13420H (2.10GHz up to 4.60GHz, 12MB Cache)\r\nRam: 16GB DDR4 on board\r\nHard drive: 512GB M.2 NVMe PCIe 3.0 SSD\r\nVGA: Intel UHD Graphics\r\nDisplay: 14.0 inch WUXGA (1920 x 1200) 16:10 aspect ratio, IPS-level Panel, LED Backlit, 60Hz refresh rate, 300nits, 45% NTSC color gamut, Glossy display, TÜV Rheinland-certified, Touch screen - Touch screen','/static/images/products/1774386918828_20775_asus_vivobook_s_14_flip_tp3402_b___c__logo.jpg',6,1,7,3,1,7,8,2,2,1,'2026-03-25 04:15:18','2026-03-26 02:32:50'),(7,'Asus Expertbook Ryzen 5 150 16GB 512GB 14 inch','CPU: AMD Ryzen 5 150 (3.30GHz up to 4.55GHz, 16MB Cache)\r\nRam: 16GB DDR5\r\nHard drive: 512GB M.2 2280 NVMe™ PCIe® 4.0 SSD\r\nVGA: AMD Radeon™ Graphics\r\nDisplay: 14.0inch FHD (1920 x 1080) 16:9 Anti-glare display','/static/images/products/1774387033687_25471_23778_t___i_xu___ng.jpg',0,1,7,3,1,11,9,2,2,1,'2026-03-25 04:17:13','2026-03-25 04:17:13'),(8,'Lenovo ThinkPad Ultra 5 225U 16GB 1TB 14 inch','CPU: Intel Core™ Ultra 5 225U (1.50GHz up to 4.80GHz, 12MB Cache)\r\nRam: 16GB DDR5-5600 SODIMM (1x16GB)\r\nHard drive: 1TB SSD M.2 2242 PCIe 4.0x4 NVMe Opal 2.0\r\nVGA: Intel® Graphics\r\nDisplay: 14.0inch WUXGA (1920x1200) IPS, 300nits, Anti-glare, 45% NTSC, 60Hz','/static/images/products/1774387116781_25448_22059_lenovo_thinkpad_e14_gen_7__3.jpg',0,1,9,3,1,3,10,2,3,1,'2026-03-25 04:18:36','2026-03-25 04:18:36'),(9,'Lenovo Thinkbook Core 7 240H 16GB 512GB 14 inch','CPU: Intel® Core™ 7 240H (2.50GHz up to 5.20GHz, 24MB Cache)\r\nRam: 16GB (1x 16GB) SODIMM DDR5-5600MHz (2slots, upto 64GB)\r\nHard drive: 512GB SSD M.2 2242 PCIe® 4.0x4 NVMe®\r\nVGA: Intel® Graphics\r\nDisplay: 14.0 inch WUXGA (1920x1200) IPS, 400nits, Anti-glare, 45% NTSC','/static/images/products/1774387302876_25395_laptop_lenovo_thinkbook_14_g9_irl__7.jpg',3,1,9,3,1,21,11,2,2,1,'2026-03-25 04:21:42','2026-03-25 16:50:45'),(10,'MSI Titan Ultra 9 285HX 64GB 6TB 18 inch','CPU: Intel® Core™ Ultra 9 285HX (2.80GHz up to 5.50GHz, 36MB Cache)\r\nRam: 64GB(32GBx2) DDR5 6400MHz\r\nHard drive: 6TB (2TB NVMe PCIe Gen5x4 SSD w/o DRAM + 2 x 2TB NVMe PCIe Gen4x4 SSD)\r\nVGA: NVIDIA® GeForce RTX 5090 24GB GDDR7\r\nDisplay: 18inch 16:10 UHD+ (3840x2400), MiniLED, 120Hz, 100% DCI-P3, IPS-Level panel','/static/images/products/1774387847120_21253_msi_titan_18_hx_ai_a2xw__4.jpg',5,1,10,1,1,23,12,4,5,5,'2026-03-25 04:30:47','2026-03-25 17:46:42'),(11,'ROG Strix Ryzen™ 9-8940HX 16GB 1TB 16 inch','CPU: AMD Ryzen 9 8940HX (2.40Hz up to 5.3GHz, 64MB Cache)\r\nRam: 16GB DDR5 5200MHz SO-DIMM (2 slots, up to 64GB)\r\nHard drive: 1TB PCIe® 4.0 NVMe™ M.2 SSD\r\nVGA: NVIDIA® GeForce RTX™ 5060 8GB GDDR7\r\nDisplay: 16.0 inch 2.5K (2560 x 1600, WQXGA) 16:10, 240Hz, 3ms, 100% DCI-P3, 500nits, G-Sync, Pantone Validated, anti-glare','/static/images/products/1774388121086_24620_04_g_16_l.jpg',5,1,12,1,1,24,13,2,3,3,'2026-03-25 04:35:21','2026-03-25 16:50:45'),(12,'Apple Neo 6 CPU | 5 GPU 8GB 512GB 14 inch','CPU: Apple A18 Pro chip with 6-core CPU and 5-core GPU\r\nRAM: 8GB\r\nStorage: 512GB SSD\r\nDisplay: 13-inch Liquid Retina (2408 × 1506) with 219 pixel density, IPS, 500 nits, 1 color gamut support, sRGB color\r\nTouch ID','/static/images/products/1774388553318_25246_macbook_neo_13inch_a18_pro_h___ng_ph___t__1.jpg',0,1,13,7,1,25,14,1,2,1,'2026-03-25 04:42:33','2026-03-25 04:46:39'),(13,'Apple Air 18 CPU | 40 GPU 32GB 1TB 15.6 inch','CPU: Apple M5 MAX chip with 18-core CPU and 40-core GPU\r\nRAM: 48GB\r\nStorage: 2TB SSD\r\nDisplay: 16.2-inch Liquid Retina XDR (3456 × 2234) True Tone technology, 1 billion colors, Wide Color Gamut (P3), Brightness up to 1000 nits (outdoor), ProMotion 120Hz','/static/images/products/1774388705383_25330_screenshot_1773037762.jpg',0,1,13,7,1,26,15,3,4,2,'2026-03-25 04:45:05','2026-03-25 04:45:05');
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase_request_items`
--

DROP TABLE IF EXISTS `purchase_request_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase_request_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `purchase_request_id` bigint DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  `quantity` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_pri_request` (`purchase_request_id`),
  KEY `fk_pri_product` (`product_id`),
  CONSTRAINT `fk_pri_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `fk_pri_request` FOREIGN KEY (`purchase_request_id`) REFERENCES `purchase_requests` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_request_items`
--

LOCK TABLES `purchase_request_items` WRITE;
/*!40000 ALTER TABLE `purchase_request_items` DISABLE KEYS */;
INSERT INTO `purchase_request_items` VALUES (1,1,2,3),(2,1,5,4),(3,2,9,3),(4,2,11,5),(5,3,5,7),(6,4,10,5),(7,4,6,6);
/*!40000 ALTER TABLE `purchase_request_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase_requests`
--

DROP TABLE IF EXISTS `purchase_requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase_requests` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `request_code` varchar(255) DEFAULT NULL,
  `created_by` bigint DEFAULT NULL,
  `approved_by` bigint DEFAULT NULL,
  `status` enum('PENDING','APPROVED','REJECTED','COMPLETED') DEFAULT NULL,
  `note` text,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `request_code` (`request_code`),
  KEY `fk_pr_creator` (`created_by`),
  KEY `fk_pr_approver` (`approved_by`),
  CONSTRAINT `fk_pr_approver` FOREIGN KEY (`approved_by`) REFERENCES `users` (`id`),
  CONSTRAINT `fk_pr_creator` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_requests`
--

LOCK TABLES `purchase_requests` WRITE;
/*!40000 ALTER TABLE `purchase_requests` DISABLE KEYS */;
INSERT INTO `purchase_requests` VALUES (1,'PR-1774425888205',6,37,'COMPLETED','Hàng này đang thiếu a nhé ','2026-03-25 15:04:48','2026-03-25 16:20:50'),(2,'PR-1774432046695',6,38,'COMPLETED','','2026-03-25 16:47:26','2026-03-25 16:50:45'),(3,'PR-1774432506192',11,39,'COMPLETED','','2026-03-25 16:55:06','2026-03-25 17:26:29'),(4,'PR-1774434940565',10,40,'COMPLETED','','2026-03-25 17:35:40','2026-03-25 17:46:42');
/*!40000 ALTER TABLE `purchase_requests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rams`
--

DROP TABLE IF EXISTS `rams`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rams` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `size` varchar(100) NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rams`
--

LOCK TABLES `rams` WRITE;
/*!40000 ALTER TABLE `rams` DISABLE KEYS */;
INSERT INTO `rams` VALUES (1,'8GB',1),(2,'16GB',1),(3,'32GB',1),(4,'64GB',1),(5,'128GB',1),(6,'512GB',1);
/*!40000 ALTER TABLE `rams` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_permissions`
--

DROP TABLE IF EXISTS `role_permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role_permissions` (
  `role_id` bigint NOT NULL,
  `permission_id` bigint NOT NULL,
  PRIMARY KEY (`role_id`,`permission_id`),
  KEY `fk_rp_permission` (`permission_id`),
  CONSTRAINT `fk_rp_permission` FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`),
  CONSTRAINT `fk_rp_role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_permissions`
--

LOCK TABLES `role_permissions` WRITE;
/*!40000 ALTER TABLE `role_permissions` DISABLE KEYS */;
INSERT INTO `role_permissions` VALUES (1,1),(1,2),(1,3),(1,4),(2,5),(2,6),(3,6),(2,7),(3,7),(2,8),(1,9),(1,10),(1,11),(1,12),(3,13),(3,14),(2,15),(2,16),(3,16),(4,16),(2,17),(3,17),(4,17),(1,18),(1,19),(2,20),(3,20),(2,21),(3,21),(2,22),(3,22),(2,23),(3,23),(3,24),(4,24),(4,25),(3,26),(4,27),(4,28);
/*!40000 ALTER TABLE `role_permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'Admin','Role Admin',1,'2025-11-02 11:25:38','2025-04-28 20:21:04'),(2,'Salesman','Role Salesman',1,'2024-12-19 12:59:36','2025-07-07 17:26:48'),(3,'Warehouse','Role Warehouse',1,'2025-08-29 10:07:30','2024-02-05 19:30:17'),(4,'Manager','Role Manager',1,'2024-10-15 09:03:03','2025-10-21 23:18:46');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sizes`
--

DROP TABLE IF EXISTS `sizes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sizes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `size` varchar(100) NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sizes`
--

LOCK TABLES `sizes` WRITE;
/*!40000 ALTER TABLE `sizes` DISABLE KEYS */;
INSERT INTO `sizes` VALUES (1,'14 inch',1),(2,'15.6 inch',1),(3,'16 inch',1),(4,'13.4 inch',1),(5,'18 inch',1);
/*!40000 ALTER TABLE `sizes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stock_movements`
--

DROP TABLE IF EXISTS `stock_movements`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stock_movements` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint DEFAULT NULL,
  `quantity` bigint DEFAULT NULL,
  `type` enum('IMPORT','EXPORT','ADJUSTMENT','RETURN') DEFAULT NULL,
  `reference_type` enum('ORDER','GOODS_RECEIPT','AUDIT_ADJUSTMENT') DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `reference_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_sm_product` (`product_id`),
  CONSTRAINT `fk_sm_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stock_movements`
--

LOCK TABLES `stock_movements` WRITE;
/*!40000 ALTER TABLE `stock_movements` DISABLE KEYS */;
INSERT INTO `stock_movements` VALUES (1,2,3,'IMPORT','GOODS_RECEIPT','2026-03-25 16:20:50',1),(2,5,4,'IMPORT','GOODS_RECEIPT','2026-03-25 16:20:50',1),(3,9,3,'IMPORT','GOODS_RECEIPT','2026-03-25 16:50:45',2),(4,11,5,'IMPORT','GOODS_RECEIPT','2026-03-25 16:50:45',2),(5,5,7,'IMPORT','GOODS_RECEIPT','2026-03-25 17:26:30',3),(6,6,6,'IMPORT','GOODS_RECEIPT','2026-03-25 17:46:42',4),(7,10,5,'IMPORT','GOODS_RECEIPT','2026-03-25 17:46:42',4),(8,1,2,'EXPORT','ORDER','2026-03-26 02:33:05',1),(9,2,2,'EXPORT','ORDER','2026-03-26 02:33:05',1);
/*!40000 ALTER TABLE `stock_movements` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `storages`
--

DROP TABLE IF EXISTS `storages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `storages` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `size` varchar(100) NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `storages`
--

LOCK TABLES `storages` WRITE;
/*!40000 ALTER TABLE `storages` DISABLE KEYS */;
INSERT INTO `storages` VALUES (1,'256GB',1),(2,'512GB',1),(3,'128GB',1),(4,'1TB',1),(5,'6TB',1);
/*!40000 ALTER TABLE `storages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `units`
--

DROP TABLE IF EXISTS `units`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `units` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `units`
--

LOCK TABLES `units` WRITE;
/*!40000 ALTER TABLE `units` DISABLE KEYS */;
INSERT INTO `units` VALUES (1,'Piece');
/*!40000 ALTER TABLE `units` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_activity_log`
--

DROP TABLE IF EXISTS `user_activity_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_activity_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `activity` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_log_user` (`user_id`),
  CONSTRAINT `fk_log_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=365 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_activity_log`
--

LOCK TABLES `user_activity_log` WRITE;
/*!40000 ALTER TABLE `user_activity_log` DISABLE KEYS */;
INSERT INTO `user_activity_log` VALUES (1,6,'Login','2026-03-11 16:30:10'),(2,6,'Login','2026-03-11 16:31:16'),(3,36,'Login','2026-03-11 16:31:19'),(4,36,'Login','2026-03-11 16:32:28'),(5,21,'Login','2026-03-11 16:32:31'),(6,21,'Login','2026-03-11 17:09:25'),(7,21,'Login','2026-03-11 17:29:25'),(8,21,'Login','2026-03-11 17:37:10'),(9,21,'Login','2026-03-11 18:21:01'),(10,21,'Login','2026-03-12 01:33:36'),(11,21,'Login','2026-03-12 02:06:06'),(12,21,'Login','2026-03-12 02:08:20'),(13,21,'Login','2026-03-12 02:39:36'),(14,21,'Login','2026-03-12 02:43:06'),(15,21,'Login','2026-03-12 03:02:26'),(16,21,'Login','2026-03-12 03:16:18'),(17,21,'Login','2026-03-12 03:29:16'),(18,21,'Login','2026-03-12 03:33:12'),(19,21,'Login','2026-03-12 03:40:59'),(20,21,'Login','2026-03-12 03:43:07'),(21,21,'Login','2026-03-12 03:50:16'),(22,21,'Login','2026-03-12 03:54:19'),(23,6,'Login','2026-03-12 03:54:23'),(24,6,'Login','2026-03-12 03:55:43'),(25,21,'Login','2026-03-12 03:55:47'),(26,21,'Login','2026-03-12 04:05:17'),(27,6,'Login','2026-03-12 04:05:23'),(28,6,'Login','2026-03-12 04:05:54'),(29,21,'Login','2026-03-12 04:05:57'),(30,21,'Login','2026-03-12 13:15:43'),(31,21,'Login','2026-03-12 13:17:30'),(32,6,'Login','2026-03-12 13:33:37'),(33,6,'Login','2026-03-12 13:34:03'),(34,21,'Login','2026-03-12 13:34:07'),(35,21,'Login','2026-03-12 13:36:57'),(36,21,'Login','2026-03-13 01:38:02'),(37,21,'Login','2026-03-13 01:39:26'),(38,21,'Login','2026-03-13 01:41:50'),(39,21,'Login','2026-03-13 01:44:00'),(40,21,'Login','2026-03-13 02:08:07'),(41,21,'Login','2026-03-13 02:10:32'),(42,21,'Login','2026-03-13 02:20:09'),(43,21,'Login','2026-03-13 02:49:11'),(44,21,'Login','2026-03-13 02:50:53'),(45,21,'Login','2026-03-13 02:51:52'),(46,21,'Login','2026-03-13 02:56:09'),(47,21,'Login','2026-03-13 11:03:14'),(48,21,'Login','2026-03-13 11:06:29'),(49,21,'Login','2026-03-13 11:09:11'),(50,21,'Login','2026-03-13 11:23:28'),(51,21,'Login','2026-03-13 11:24:59'),(52,21,'Login','2026-03-13 11:26:26'),(53,21,'Login','2026-03-13 11:26:56'),(54,21,'Login','2026-03-13 11:28:53'),(55,21,'Login','2026-03-13 11:29:15'),(56,6,'Login','2026-03-13 11:29:18'),(57,6,'Login','2026-03-13 11:30:04'),(58,27,'Login','2026-03-13 11:30:31'),(59,21,'Login','2026-03-13 12:21:22'),(60,6,'Login','2026-03-13 12:35:54'),(61,6,'Login','2026-03-13 12:37:42'),(62,21,'Login','2026-03-13 12:37:47'),(63,21,'Login','2026-03-13 12:41:30'),(64,36,'Login','2026-03-13 12:41:33'),(65,36,'Login','2026-03-13 12:41:44'),(66,1,'Login','2026-03-13 12:41:47'),(67,1,'Login','2026-03-13 12:42:01'),(68,36,'Login','2026-03-13 12:42:07'),(69,36,'Login','2026-03-13 12:42:13'),(70,6,'Login','2026-03-13 12:42:16'),(71,6,'Login','2026-03-13 12:44:20'),(72,1,'Login','2026-03-13 12:44:27'),(73,1,'Login','2026-03-13 12:44:41'),(74,21,'Login','2026-03-13 12:44:44'),(75,21,'Login','2026-03-13 12:44:51'),(76,1,'Login','2026-03-13 12:44:57'),(77,1,'Login','2026-03-13 12:48:06'),(78,36,'Login','2026-03-13 12:48:17'),(79,6,'Login','2026-03-13 13:34:21'),(80,6,'Login','2026-03-13 13:34:44'),(81,21,'Login','2026-03-13 13:34:48'),(82,21,'Login','2026-03-13 13:34:54'),(83,36,'Login','2026-03-13 13:34:57'),(84,36,'Login','2026-03-13 13:35:03'),(85,21,'Login','2026-03-13 13:35:05'),(86,21,'Login','2026-03-13 13:43:18'),(87,21,'Login','2026-03-13 13:43:24'),(88,36,'Login','2026-03-13 13:43:27'),(89,36,'Login','2026-03-13 13:48:45'),(90,6,'Login','2026-03-13 13:48:49'),(91,21,'Login','2026-03-13 14:04:06'),(92,21,'Login','2026-03-13 14:10:21'),(93,36,'Login','2026-03-13 14:17:21'),(94,21,'Login','2026-03-13 14:17:39'),(95,21,'Login','2026-03-13 14:17:47'),(96,6,'Login','2026-03-13 14:17:49'),(97,21,'Login','2026-03-13 14:35:39'),(98,36,'Login','2026-03-13 14:35:43'),(99,36,'Login','2026-03-13 14:37:48'),(100,21,'Login','2026-03-13 14:37:54'),(101,21,'Login','2026-03-13 14:49:20'),(102,21,'Login','2026-03-15 01:57:40'),(103,21,'Login','2026-03-15 02:00:06'),(104,6,'Login','2026-03-15 02:00:11'),(105,6,'Login','2026-03-15 02:19:02'),(106,21,'Login','2026-03-15 02:19:08'),(107,21,'Login','2026-03-15 02:19:33'),(108,36,'Login','2026-03-15 02:19:37'),(109,36,'Login','2026-03-15 02:24:34'),(110,27,'Login','2026-03-15 02:24:41'),(111,6,'Login','2026-03-17 02:57:19'),(112,6,'Create purchase request','2026-03-17 02:58:07'),(113,6,'Login','2026-03-17 02:58:09'),(114,36,'Login','2026-03-17 02:58:12'),(115,36,'Approve purchase request','2026-03-17 02:58:19'),(116,36,'Login','2026-03-17 02:58:20'),(117,27,'Login','2026-03-17 02:58:23'),(118,27,'Import product','2026-03-17 02:58:28'),(119,6,'Login','2026-03-17 03:00:55'),(120,6,'Create purchase request','2026-03-17 03:01:24'),(121,6,'Login','2026-03-17 03:01:25'),(122,37,'Login','2026-03-17 03:01:34'),(123,37,'Approve purchase request','2026-03-17 03:01:40'),(124,37,'Login','2026-03-17 03:01:43'),(125,27,'Login','2026-03-17 03:01:46'),(126,27,'Import product','2026-03-17 03:01:53'),(127,6,'Login','2026-03-17 03:03:48'),(128,6,'Create purchase request','2026-03-17 03:04:08'),(129,6,'Login','2026-03-17 03:04:10'),(130,37,'Login','2026-03-17 03:04:13'),(131,37,'Approve purchase request','2026-03-17 03:04:22'),(132,37,'Login','2026-03-17 03:04:24'),(133,27,'Login','2026-03-17 03:04:26'),(134,27,'Import product','2026-03-17 03:04:31'),(135,27,'Login','2026-03-18 01:48:50'),(136,27,'Login','2026-03-18 01:50:50'),(137,36,'Login','2026-03-18 01:50:53'),(138,36,'Login','2026-03-18 01:51:53'),(139,21,'Login','2026-03-18 01:51:56'),(140,37,'Login','2026-03-18 01:54:47'),(141,37,'Login','2026-03-18 01:55:28'),(142,36,'Login','2026-03-18 01:56:10'),(143,37,'Login','2026-03-18 02:24:43'),(144,36,'Login','2026-03-18 02:25:51'),(145,36,'Login','2026-03-18 02:33:34'),(146,41,'Login','2026-03-18 02:42:38'),(147,36,'Login','2026-03-18 02:44:22'),(148,37,'Login','2026-03-18 02:47:38'),(149,36,'Login','2026-03-18 14:41:42'),(150,36,'Login','2026-03-18 16:18:46'),(151,36,'Login','2026-03-18 16:23:09'),(152,36,'Login','2026-03-18 16:25:13'),(153,36,'Login','2026-03-18 16:29:38'),(154,37,'Login','2026-03-18 16:34:01'),(155,37,'Login','2026-03-18 16:38:43'),(156,36,'Login','2026-03-18 16:40:03'),(157,21,'Login','2026-03-18 16:42:40'),(158,21,'Login','2026-03-18 16:42:45'),(159,37,'Login','2026-03-18 16:42:48'),(160,36,'Login','2026-03-18 16:45:25'),(161,36,'Login','2026-03-18 22:39:07'),(162,36,'Login','2026-03-18 22:40:30'),(163,36,'Login','2026-03-18 22:41:20'),(164,37,'Login','2026-03-18 22:54:27'),(165,36,'Login','2026-03-18 22:57:55'),(166,36,'Login','2026-03-18 22:58:42'),(167,36,'Login','2026-03-18 23:11:05'),(168,37,'Login','2026-03-19 00:05:09'),(169,36,'Login','2026-03-19 00:06:18'),(170,37,'Login','2026-03-19 00:09:44'),(171,36,'Login','2026-03-19 00:10:55'),(172,36,'Login','2026-03-19 00:11:58'),(173,36,'Login','2026-03-19 00:12:59'),(174,37,'Login','2026-03-20 01:33:15'),(175,27,'Login','2026-03-25 03:38:24'),(176,27,'Update brand','2026-03-25 03:41:57'),(177,27,'Update brand','2026-03-25 03:42:00'),(178,27,'Update brand','2026-03-25 03:43:21'),(179,27,'Update brand','2026-03-25 03:43:27'),(180,27,'Crete model','2026-03-25 03:45:05'),(181,27,'Crete model','2026-03-25 03:45:30'),(182,27,'Crete model','2026-03-25 03:45:51'),(183,27,'Crete model','2026-03-25 03:45:58'),(184,27,'Crete model','2026-03-25 03:46:22'),(185,27,'Crete model','2026-03-25 03:46:29'),(186,27,'Crete model','2026-03-25 03:46:39'),(187,27,'Crete model','2026-03-25 03:46:58'),(188,27,'Crete model','2026-03-25 03:47:27'),(189,27,'Crete model','2026-03-25 03:48:12'),(190,27,'Crete model','2026-03-25 03:48:24'),(191,27,'Crete model','2026-03-25 03:48:41'),(192,27,'Crete model','2026-03-25 03:48:51'),(193,27,'Crete model','2026-03-25 03:49:12'),(194,27,'Crete model','2026-03-25 03:49:21'),(195,27,'Crete model','2026-03-25 03:49:35'),(196,27,'Crete model','2026-03-25 03:52:25'),(197,27,'Create new chip','2026-03-25 03:53:28'),(198,27,'Create product','2026-03-25 03:55:45'),(199,27,'Create new chip','2026-03-25 03:57:10'),(200,27,'Create product','2026-03-25 04:01:55'),(201,27,'Create product','2026-03-25 04:03:57'),(202,27,'Create new chip','2026-03-25 04:04:46'),(203,27,'Create product','2026-03-25 04:08:34'),(204,27,'Create new chip','2026-03-25 04:09:14'),(205,27,'Update Category','2026-03-25 04:10:05'),(206,27,'Update Category','2026-03-25 04:10:59'),(207,27,'Create product','2026-03-25 04:12:37'),(208,27,'Create new chip','2026-03-25 04:13:40'),(209,27,'Create product','2026-03-25 04:15:18'),(210,27,'Create new chip','2026-03-25 04:16:10'),(211,27,'Create product','2026-03-25 04:17:13'),(212,27,'Create new chip','2026-03-25 04:17:47'),(213,27,'Create product','2026-03-25 04:18:36'),(214,27,'Crete model','2026-03-25 04:19:23'),(215,27,'Crete model','2026-03-25 04:19:36'),(216,27,'Deactive model','2026-03-25 04:20:16'),(217,27,'Create new chip','2026-03-25 04:20:56'),(218,27,'Create product','2026-03-25 04:21:42'),(219,27,'Crete model','2026-03-25 04:22:58'),(220,27,'Create new chip','2026-03-25 04:23:16'),(221,27,'Crete ram','2026-03-25 04:24:24'),(222,27,'Deactive ram','2026-03-25 04:24:29'),(223,27,'Create size','2026-03-25 04:24:41'),(224,27,'Crete ram','2026-03-25 04:25:27'),(225,27,'Create storage','2026-03-25 04:25:58'),(226,27,'Active ram','2026-03-25 04:29:35'),(227,27,'Create product','2026-03-25 04:30:47'),(228,27,'Crete model','2026-03-25 04:33:57'),(229,27,'Create new chip','2026-03-25 04:34:18'),(230,27,'Create product','2026-03-25 04:35:21'),(231,27,'Create Category','2026-03-25 04:39:09'),(232,27,'Crete model','2026-03-25 04:40:27'),(233,27,'Crete model','2026-03-25 04:40:32'),(234,27,'Create new chip','2026-03-25 04:41:35'),(235,27,'Create product','2026-03-25 04:42:33'),(236,27,'Create new chip','2026-03-25 04:43:46'),(237,27,'Create product','2026-03-25 04:45:05'),(238,27,'Login','2026-03-25 14:54:33'),(239,27,'Login','2026-03-25 15:02:38'),(240,6,'Login','2026-03-25 15:02:42'),(241,6,'Create purchase request','2026-03-25 15:04:48'),(242,6,'Login','2026-03-25 15:04:50'),(243,37,'Login','2026-03-25 15:04:54'),(244,37,'Approve purchase request','2026-03-25 15:10:01'),(245,37,'Login','2026-03-25 15:10:14'),(246,27,'Login','2026-03-25 15:10:19'),(247,27,'Import product','2026-03-25 15:10:38'),(248,27,'Import product','2026-03-25 15:18:47'),(249,27,'Login','2026-03-25 15:26:52'),(250,27,'Login','2026-03-25 15:27:43'),(251,27,'Login','2026-03-25 15:28:48'),(252,27,'Import product','2026-03-25 15:28:55'),(253,27,'Login','2026-03-25 15:31:50'),(254,27,'Import product','2026-03-25 15:31:55'),(255,27,'Login','2026-03-25 15:33:44'),(256,27,'Import product','2026-03-25 15:33:52'),(257,29,'Login','2026-03-25 15:34:36'),(258,29,'Import product','2026-03-25 15:34:43'),(259,27,'Login','2026-03-25 15:35:58'),(260,27,'Import product','2026-03-25 15:36:07'),(261,27,'Login','2026-03-25 15:36:41'),(262,27,'Login','2026-03-25 15:36:45'),(263,27,'Login','2026-03-25 15:36:48'),(264,29,'Login','2026-03-25 15:36:51'),(265,29,'Import product','2026-03-25 15:36:56'),(266,21,'Login','2026-03-25 15:39:50'),(267,21,'Import product','2026-03-25 15:40:10'),(268,27,'Login','2026-03-25 15:53:17'),(269,27,'Import product','2026-03-25 15:53:26'),(270,27,'Import product','2026-03-25 15:57:24'),(271,27,'Import product','2026-03-25 15:58:36'),(272,27,'Import product','2026-03-25 16:00:59'),(273,27,'Import product','2026-03-25 16:04:11'),(274,27,'Import product','2026-03-25 16:09:47'),(275,29,'Login','2026-03-25 16:11:54'),(276,29,'Import product','2026-03-25 16:12:01'),(277,27,'Login','2026-03-25 16:13:11'),(278,27,'Import product','2026-03-25 16:13:17'),(279,27,'Login','2026-03-25 16:14:50'),(280,27,'Import product','2026-03-25 16:15:02'),(281,27,'Login','2026-03-25 16:39:02'),(282,27,'Login','2026-03-25 16:43:08'),(283,27,'Login','2026-03-25 16:43:33'),(284,43,'Login','2026-03-25 16:43:36'),(285,43,'Login','2026-03-25 16:44:00'),(286,6,'Login','2026-03-25 16:46:49'),(287,6,'Create purchase request','2026-03-25 16:47:26'),(288,6,'Login','2026-03-25 16:47:28'),(289,38,'Login','2026-03-25 16:47:35'),(290,38,'Approve purchase request','2026-03-25 16:47:44'),(291,38,'Login','2026-03-25 16:47:45'),(292,27,'Login','2026-03-25 16:47:49'),(293,27,'Import product','2026-03-25 16:47:54'),(294,27,'Import product','2026-03-25 16:49:12'),(295,21,'Login','2026-03-25 16:54:32'),(296,21,'Login','2026-03-25 16:54:44'),(297,11,'Login','2026-03-25 16:54:52'),(298,11,'Create purchase request','2026-03-25 16:55:06'),(299,11,'Login','2026-03-25 16:55:07'),(300,39,'Login','2026-03-25 16:55:16'),(301,39,'Approve purchase request','2026-03-25 16:57:48'),(302,39,'Login','2026-03-25 16:58:14'),(303,40,'Login','2026-03-25 16:58:28'),(304,40,'Login','2026-03-25 16:58:32'),(305,25,'Login','2026-03-25 16:58:42'),(306,25,'Import product','2026-03-25 16:58:51'),(307,27,'Login','2026-03-25 17:01:05'),(308,27,'Import product','2026-03-25 17:01:12'),(309,21,'Login','2026-03-25 17:03:03'),(310,21,'Import product','2026-03-25 17:03:10'),(311,27,'Login','2026-03-25 17:06:58'),(312,27,'Import product','2026-03-25 17:07:09'),(313,27,'Login','2026-03-25 17:11:16'),(314,27,'Import product','2026-03-25 17:11:27'),(315,27,'Login','2026-03-25 17:13:56'),(316,27,'Import product','2026-03-25 17:14:02'),(317,27,'Login','2026-03-25 17:16:01'),(318,27,'Import product','2026-03-25 17:16:07'),(319,29,'Login','2026-03-25 17:19:09'),(320,29,'Import product','2026-03-25 17:19:14'),(321,29,'Import product','2026-03-25 17:23:31'),(322,27,'Login','2026-03-25 17:32:30'),(323,13,'Login','2026-03-25 17:34:34'),(324,13,'Login','2026-03-25 17:34:38'),(325,41,'Login','2026-03-25 17:34:45'),(326,41,'Login','2026-03-25 17:35:01'),(327,11,'Login','2026-03-25 17:35:05'),(328,11,'Login','2026-03-25 17:35:11'),(329,10,'Login','2026-03-25 17:35:15'),(330,10,'Create purchase request','2026-03-25 17:35:40'),(331,10,'Login','2026-03-25 17:35:42'),(332,39,'Login','2026-03-25 17:35:45'),(333,39,'Login','2026-03-25 17:35:48'),(334,40,'Login','2026-03-25 17:35:53'),(335,40,'Approve purchase request','2026-03-25 17:36:01'),(336,40,'Login','2026-03-25 17:36:03'),(337,22,'Login','2026-03-25 17:36:08'),(338,22,'Import product','2026-03-25 17:36:15'),(339,25,'Login','2026-03-25 17:38:09'),(340,25,'Import product','2026-03-25 17:38:18'),(341,25,'Login','2026-03-25 17:42:26'),(342,25,'Import product','2026-03-25 17:42:38'),(343,25,'Login','2026-03-25 17:43:05'),(344,25,'Import product','2026-03-25 17:43:10'),(345,27,'Login','2026-03-25 17:53:23'),(346,29,'Login','2026-03-25 17:55:07'),(347,29,'Login','2026-03-25 17:55:48'),(348,29,'Login','2026-03-25 17:57:17'),(349,27,'Login','2026-03-25 17:58:03'),(350,27,'Login','2026-03-25 17:59:02'),(351,29,'Login','2026-03-25 17:59:54'),(352,29,'Login','2026-03-25 18:02:45'),(353,37,'Login','2026-03-25 18:02:49'),(354,43,'Login','2026-03-26 02:22:07'),(355,21,'Login','2026-03-26 02:22:10'),(356,21,'Login','2026-03-26 02:25:07'),(357,43,'Login','2026-03-26 02:25:10'),(358,11,'Login','2026-03-26 02:30:59'),(359,11,'Create order','2026-03-26 02:31:08'),(360,11,'Submit order','2026-03-26 02:31:13'),(361,11,'Login','2026-03-26 02:31:16'),(362,25,'Login','2026-03-26 02:31:18'),(363,25,'Login','2026-03-26 02:33:27'),(364,37,'Login','2026-03-26 02:33:32');
/*!40000 ALTER TABLE `user_activity_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fullname` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `role_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `fk_user_role` (`role_id`),
  CONSTRAINT `fk_user_role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Đỗ Hải Cường','admin1@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',1),(2,'Hoàng Minh Nam','admin2@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',1),(3,'Phạm Đức Cường','admin3@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',1),(4,'Bùi Bình Minh','admin4@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',1),(5,'Nguyễn Xuân Hiếu','admin5@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',1),(6,'Lê Tuấn Khanh','salesman1@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',2),(7,'Hoàng Thị Chinh','salesman2@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',2),(8,'Nguyễn Văn Dũng','salesman3@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',2),(9,'Phạm Ngọc Hùng','salesman4@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',2),(10,'Nguyễn Minh Đăng','salesman5@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',2),(11,'Phạm Hữu Phúc','salesman6@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',2),(12,'Đặng Bình Minh','salesman7@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',2),(13,'Lê Hải An','salesman8@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',2),(14,'Trần Đức Anh','salesman9@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',2),(15,'Vũ Thị Loan','salesman10@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',2),(16,'Trương Hữu Nam','salesman11@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',2),(17,'Nguyễn Thị Linh','salesman12@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',2),(18,'Cao Minh Hào','salesman13@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',2),(19,'Lê Thị Lý','salesman14@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',2),(20,'Phạm Thị Bưởi','salesman15@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',2),(21,'Trần Văn Minh','warehouse1@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',3),(22,'Đặng Tiểu Bình','warehouse2@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',3),(23,'Đinh Văn Tiến','warehouse3@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',3),(24,'Hồ Ngọc Hà','warehouse4@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',3),(25,'Phan Văn Nhàn','warehouse5@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',3),(26,'Trần Ngọc Sáng','warehouse6@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',3),(27,'Mai Thị Giang','warehouse7@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',3),(28,'Trần Văn Thụ','warehouse8@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',3),(29,'Đào Ngọc Linh','warehouse9@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',3),(30,'Phạm Thị Phương','warehouse10@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',3),(31,'Lê Văn Đông','warehouse11@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',3),(32,'Nguyễn Văn Lịch','warehouse12@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',3),(33,'Trương Văn Mạnh','warehouse13@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',3),(34,'Vũ Thị Nhài','warehouse14@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',3),(35,'Nguyễn Văn Bảy','warehouse15@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',3),(36,'Vũ Tấn Tới','manager1@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',4),(37,'Mao Văn Bình','manager2@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',4),(38,'Hoàng Thị Lời','manager3@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',4),(39,'Nguyễn Văn Kiến','manager4@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',4),(40,'Phạm Quang Dũng','manager5@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',4),(41,'Bùi Tuấn Toàn','manager6@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',4),(42,'Đặng Văn Lịch','manager7@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',4),(43,'Hoa Hải Đường','manager8@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',4),(44,'Bùi Xuân Ca','manager9@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',4),(45,'Nguyễn Tiến Thủ','manager10@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',4),(46,'Trần Thủ Môn','manager11@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',4),(47,'Nguyễn Tiền Quang','manager12@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',4),(48,'Lương Thanh Tùng','manager13@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',4),(49,'Trịnh Hữu Hạn','manager14@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',4),(50,'Trần Thị Ngân','manager15@gmail.com','$2a$10$0F26z94gU/PtFcF.Lqi1WulBQLr7ddPEC432g94N9NygMP9Sdivty',1,'2026-03-11 16:29:50','2026-03-25 18:10:47',4);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-03-26  2:39:20
