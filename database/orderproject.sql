/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : localhost:3306
 Source Schema         : orderproject

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 06/09/2019 09:05:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for classification
-- ----------------------------
DROP TABLE IF EXISTS `classification`;
CREATE TABLE `classification` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `shop_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of classification
-- ----------------------------
BEGIN;
INSERT INTO `classification` VALUES (30, '进口海鲜', 'root');
INSERT INTO `classification` VALUES (31, '家常菜', 'root');
INSERT INTO `classification` VALUES (32, '西餐', 'root');
COMMIT;

-- ----------------------------
-- Table structure for dining_table
-- ----------------------------
DROP TABLE IF EXISTS `dining_table`;
CREATE TABLE `dining_table` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `capacity` int(255) DEFAULT NULL,
  `open` tinyint(255) DEFAULT NULL,
  `used` tinyint(255) DEFAULT NULL,
  `order_id` int(11) DEFAULT NULL,
  `shop_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of dining_table
-- ----------------------------
BEGIN;
INSERT INTO `dining_table` VALUES (1, '1号桌', 10, 1, 0, 0, 'root');
INSERT INTO `dining_table` VALUES (2, '2号桌', 6, 1, 0, 0, 'root');
INSERT INTO `dining_table` VALUES (3, '3号桌', 6, 1, 0, 0, 'root');
INSERT INTO `dining_table` VALUES (4, '4号桌', 6, 1, 0, 0, 'root');
INSERT INTO `dining_table` VALUES (5, '5号桌', 6, 1, 0, 0, 'root');
INSERT INTO `dining_table` VALUES (6, '6号桌', 6, 1, 0, 0, 'root');
INSERT INTO `dining_table` VALUES (7, '7号桌', 6, 1, 0, NULL, 'root');
INSERT INTO `dining_table` VALUES (8, '8号桌', 6, 1, 0, NULL, 'root');
INSERT INTO `dining_table` VALUES (9, '9号桌', 6, 1, 0, NULL, 'root');
INSERT INTO `dining_table` VALUES (10, '10号桌', 6, 1, 0, NULL, 'root');
INSERT INTO `dining_table` VALUES (12, '铁王座', 1, 1, 0, 0, 'root');
INSERT INTO `dining_table` VALUES (13, '一号桌子', 2, 1, 0, NULL, 'admin');
COMMIT;

-- ----------------------------
-- Table structure for food
-- ----------------------------
DROP TABLE IF EXISTS `food`;
CREATE TABLE `food` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `food_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `food_info` text COLLATE utf8_bin,
  `price` decimal(10,2) DEFAULT NULL,
  `discount` decimal(10,2) DEFAULT NULL,
  `img_path` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `shop_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of food
-- ----------------------------
BEGIN;
INSERT INTO `food` VALUES (47, '澳洲大龙虾', '澳洲进口大龙虾', 899.90, 1.00, 'deb55c8079194257b1a954b5e7050f24.jpeg', 'root');
INSERT INTO `food` VALUES (49, '冰岛甜虾', '10只', 299.90, 1.00, '33b7bd1d5a584f8890bb095f18c5356d.jpeg', 'root');
INSERT INTO `food` VALUES (50, '澳洲帝王蟹', '499kg', 733.60, 0.90, 'd31bf88ec1994e9d872de94cfc7dc8da.jpeg', 'root');
INSERT INTO `food` VALUES (51, '人工养殖中华鲟', '真的是家养的', 999.00, 1.00, '86076607cca74357a2547ea53e7a828c.jpeg', 'root');
INSERT INTO `food` VALUES (52, '鱼香肉丝', '鱼香肉丝（Yuxiang shredded pork(sauteed in spicy garlic sauce） [1]  ，是一道汉族特色传统名菜，以鱼香味调味而得名，属于川菜。相传灵感来自泡椒肉丝，民国年间则是由四川籍厨师创制而成', 35.00, 0.80, '314b71a07a284326931164c8c42e3a5a.jpg', 'root');
INSERT INTO `food` VALUES (53, '麻婆豆腐', '是四川省传统名菜之一，属于川菜。主要原料为配料和豆腐', 18.00, 1.00, '1913fcae2f724a71a75bbda6e3211759.jpg', 'root');
INSERT INTO `food` VALUES (54, '牛排', '牛排，或称牛扒，是块状的牛肉，是西餐中最常见的食物之一。', 100.00, 1.00, '91d1ebf423d54186b12de9306738f76e.jpg', 'root');
COMMIT;

-- ----------------------------
-- Table structure for food_class
-- ----------------------------
DROP TABLE IF EXISTS `food_class`;
CREATE TABLE `food_class` (
  `food_id` int(11) DEFAULT NULL,
  `classification_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of food_class
-- ----------------------------
BEGIN;
INSERT INTO `food_class` VALUES (51, 30);
INSERT INTO `food_class` VALUES (50, 30);
INSERT INTO `food_class` VALUES (49, 30);
INSERT INTO `food_class` VALUES (47, 30);
INSERT INTO `food_class` VALUES (52, 31);
INSERT INTO `food_class` VALUES (53, 31);
INSERT INTO `food_class` VALUES (54, 32);
INSERT INTO `food_class` VALUES (54, 31);
COMMIT;

-- ----------------------------
-- Table structure for order_food
-- ----------------------------
DROP TABLE IF EXISTS `order_food`;
CREATE TABLE `order_food` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) DEFAULT NULL,
  `food_id` int(11) DEFAULT NULL,
  `food_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `single_price` decimal(10,2) DEFAULT NULL,
  `discount` decimal(10,2) DEFAULT NULL,
  `amount` int(10) DEFAULT NULL,
  `status` tinyint(2) DEFAULT NULL,
  `return_status` tinyint(2) DEFAULT NULL,
  `return_message` text COLLATE utf8_bin,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of order_food
-- ----------------------------
BEGIN;
INSERT INTO `order_food` VALUES (1, 5, 4, '阿萨德', 100.00, 1.00, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (2, 5, 5, '萨德', 100.00, 1.00, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (3, 5, 6, '萨', 100.00, 1.00, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (4, 5, 6, '萨', 100.00, 1.00, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (5, 5, 6, '萨', 100.00, 1.00, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (6, 5, 6, '萨', 100.00, 1.00, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (7, 5, 6, '萨', 100.00, 1.00, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (8, 5, 6, '萨', 100.00, 1.00, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (9, 5, 6, '萨', 100.00, 1.00, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (10, 5, 6, '萨', 100.00, 1.00, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (11, 5, 6, '萨', 100.00, 1.00, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (12, 5, 6, '萨', 100.00, 1.00, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (13, 10, 51, '人工养殖中华鲟', 999.00, 1.00, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (14, 10, 50, '澳洲帝王蟹', 733.60, 0.90, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (15, 10, 49, '冰岛甜虾', 299.90, 1.00, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (16, 11, 51, '人工养殖中华鲟', 999.00, 1.00, 1, 1, 0, NULL);
INSERT INTO `order_food` VALUES (17, 11, 50, '澳洲帝王蟹', 733.60, 0.90, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (18, 11, 49, '冰岛甜虾', 299.90, 1.00, 1, 0, 1, '不好恰');
INSERT INTO `order_food` VALUES (19, 11, 51, '人工养殖中华鲟', 999.00, 1.00, 1, 1, 0, NULL);
INSERT INTO `order_food` VALUES (20, 11, 50, '澳洲帝王蟹', 733.60, 0.90, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (21, 12, 51, '人工养殖中华鲟', 999.00, 1.00, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (22, 12, 50, '澳洲帝王蟹', 733.60, 0.90, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (23, 12, 49, '冰岛甜虾', 299.90, 1.00, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (24, 12, 47, '澳洲大龙虾', 899.90, 1.00, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (25, 13, 52, '鱼香肉丝', 35.00, 0.80, 2, 1, 0, NULL);
INSERT INTO `order_food` VALUES (26, 13, 51, '人工养殖中华鲟', 999.00, 1.00, 2, 1, 0, NULL);
INSERT INTO `order_food` VALUES (27, 13, 50, '澳洲帝王蟹', 733.60, 0.90, 1, 1, 0, NULL);
INSERT INTO `order_food` VALUES (28, 13, 49, '冰岛甜虾', 299.90, 1.00, 1, 1, 0, NULL);
INSERT INTO `order_food` VALUES (29, 13, 52, '鱼香肉丝', 35.00, 0.80, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (30, 15, 53, '麻婆豆腐', 18.00, 1.00, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (31, 15, 50, '澳洲帝王蟹', 733.60, 0.90, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (32, 16, 54, '牛排', 100.00, 1.00, 3, 0, 0, NULL);
INSERT INTO `order_food` VALUES (33, 16, 52, '鱼香肉丝', 35.00, 0.80, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (34, 16, 50, '澳洲帝王蟹', 733.60, 0.90, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (35, 16, 50, '澳洲帝王蟹', 733.60, 0.90, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (36, 16, 49, '冰岛甜虾', 299.90, 1.00, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (37, 17, 54, '牛排', 100.00, 1.00, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (38, 17, 53, '麻婆豆腐', 18.00, 1.00, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (39, 18, 52, '鱼香肉丝', 35.00, 0.80, 1, 1, 0, NULL);
INSERT INTO `order_food` VALUES (40, 18, 50, '澳洲帝王蟹', 733.60, 0.90, 1, 1, 0, NULL);
INSERT INTO `order_food` VALUES (41, 19, 54, '牛排', 100.00, 1.00, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (42, 19, 54, '牛排', 100.00, 1.00, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (43, 19, 54, '牛排', 100.00, 1.00, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (44, 19, 54, '牛排', 100.00, 1.00, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (45, 19, 54, '牛排', 100.00, 1.00, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (46, 20, 54, '牛排', 100.00, 1.00, 1, 1, 0, NULL);
INSERT INTO `order_food` VALUES (47, 20, 53, '麻婆豆腐', 18.00, 1.00, 2, 1, 0, NULL);
INSERT INTO `order_food` VALUES (48, 20, 50, '澳洲帝王蟹', 733.60, 0.90, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (49, 21, 54, '牛排', 100.00, 1.00, 1, 1, 0, NULL);
INSERT INTO `order_food` VALUES (50, 21, 53, '麻婆豆腐', 18.00, 1.00, 1, 1, 0, NULL);
INSERT INTO `order_food` VALUES (51, 21, 51, '人工养殖中华鲟', 999.00, 1.00, 1, 0, 1, '这个不行');
INSERT INTO `order_food` VALUES (52, 22, 49, '冰岛甜虾', 299.90, 1.00, 1, 1, 0, NULL);
INSERT INTO `order_food` VALUES (53, 22, 50, '澳洲帝王蟹', 733.60, 0.90, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (54, 23, 54, '牛排', 100.00, 1.00, 1, 0, 1, '这菜不行');
INSERT INTO `order_food` VALUES (55, 23, 53, '麻婆豆腐', 18.00, 1.00, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (56, 24, 54, '牛排', 100.00, 1.00, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (57, 24, 54, '牛排', 100.00, 1.00, 3, 0, 0, NULL);
INSERT INTO `order_food` VALUES (58, 24, 54, '牛排', 100.00, 1.00, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (59, 24, 54, '牛排', 100.00, 1.00, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (60, 25, 52, '鱼香肉丝', 35.00, 0.80, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (61, 25, 53, '麻婆豆腐', 18.00, 1.00, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (62, 25, 54, '牛排', 100.00, 1.00, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (63, 26, 54, '牛排', 100.00, 1.00, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (64, 27, 54, '牛排', 100.00, 1.00, 5, 0, 0, NULL);
INSERT INTO `order_food` VALUES (65, 28, 52, '鱼香肉丝', 35.00, 0.80, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (66, 28, 53, '麻婆豆腐', 18.00, 1.00, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (67, 27, 54, '牛排', 100.00, 1.00, 1, 0, 1, '多点一份');
INSERT INTO `order_food` VALUES (68, 29, 53, '麻婆豆腐', 18.00, 1.00, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (69, 29, 54, '牛排', 100.00, 1.00, 1, 0, 0, NULL);
INSERT INTO `order_food` VALUES (70, 29, 52, '鱼香肉丝', 35.00, 0.80, 1, 0, 0, NULL);
COMMIT;

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `payment_type` int(10) DEFAULT NULL,
  `payment_price` decimal(10,2) DEFAULT NULL,
  `actual_amount` decimal(10,2) DEFAULT NULL,
  `table_id` int(11) DEFAULT NULL,
  `number_of_people` int(11) DEFAULT NULL,
  `status` tinyint(2) DEFAULT NULL,
  `remarks` text COLLATE utf8_bin,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `payment_status` tinyint(2) DEFAULT NULL,
  `client_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `shop_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `online_code` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of orders
-- ----------------------------
BEGIN;
INSERT INTO `orders` VALUES (1, 1, 300.00, 0.00, 1, 7, 1, '不要放辣', '2019-08-13 10:13:54', '2019-08-14 13:59:56', 1, '客户', 'root', NULL);
INSERT INTO `orders` VALUES (2, 1, 400.00, 0.00, 2, 6, 1, '不要放辣', '2019-08-13 10:13:57', '2019-08-14 13:59:59', 1, '客户', 'root', NULL);
INSERT INTO `orders` VALUES (3, 2, 600.00, 0.00, 3, 6, 1, '多放辣', '2019-08-13 11:00:27', '2019-08-14 14:00:01', 1, '客户', 'root', NULL);
INSERT INTO `orders` VALUES (4, 2, 200.00, 0.00, 4, 6, 1, '不要放辣', '2019-08-13 11:20:04', '2019-08-14 14:00:03', 1, '客户', 'root', NULL);
INSERT INTO `orders` VALUES (5, 3, 1200.00, 1200.00, 5, 6, 1, '不要放辣', '2019-08-13 11:20:10', '2019-08-14 14:00:05', 1, '客户', 'root', NULL);
INSERT INTO `orders` VALUES (6, 3, 1000.00, 0.00, 6, 6, 1, '不要放辣', '2019-08-13 11:20:17', '2019-08-14 14:00:29', 1, '客户', 'root', NULL);
INSERT INTO `orders` VALUES (10, 2, 1959.14, 1959.14, 1, 2, 1, '', '2019-08-15 09:23:10', '2019-08-15 09:24:02', 1, '顾客', 'root', NULL);
INSERT INTO `orders` VALUES (11, NULL, NULL, NULL, 2, 2, 0, '不要芥末', '2019-08-15 09:29:36', '2019-08-15 10:56:58', 0, '顾客', 'root', NULL);
INSERT INTO `orders` VALUES (12, 1, 2859.04, 2859.04, 6, 6, 1, '随便点些什么', '2019-08-15 10:58:19', '2019-08-15 10:58:41', 1, '提莫队长', 'root', NULL);
INSERT INTO `orders` VALUES (13, 2, 3042.14, 3042.14, 1, 4, 1, 'rapper', '2019-08-15 11:01:00', '2019-08-15 12:10:07', 1, 'aka 活死人', 'root', NULL);
INSERT INTO `orders` VALUES (15, NULL, NULL, NULL, 12, 2, 0, 'hello', '2019-08-15 11:58:28', '2019-08-15 11:59:54', 0, 'aka 周杰伦', 'root', NULL);
INSERT INTO `orders` VALUES (16, 2, 1948.38, 1948.38, 5, 2, 1, '。。。', '2019-08-15 13:40:39', '2019-08-15 14:06:30', 1, '顾客', 'root', NULL);
INSERT INTO `orders` VALUES (17, 1, 118.00, 118.00, 1, 2, 1, '', '2019-08-15 14:54:03', '2019-08-15 16:48:49', 1, '顾客', 'root', NULL);
INSERT INTO `orders` VALUES (18, 1, 688.24, 688.24, 1, 2, 1, '', '2019-08-21 13:33:25', '2019-08-21 14:10:44', 1, '顾客', 'root', '2019082122001472351002455916');
INSERT INTO `orders` VALUES (19, 3, 500.00, 500.00, 1, 2, 1, '', '2019-08-21 14:28:03', '2019-08-22 09:21:49', 1, '顾客', 'root', NULL);
INSERT INTO `orders` VALUES (20, 3, 796.24, 796.24, 1, 8, 1, '这是一个备注', '2019-08-24 10:42:28', '2019-08-27 09:27:41', 1, '顾客', 'root', NULL);
INSERT INTO `orders` VALUES (21, NULL, NULL, 118.00, 2, 2, 0, '', '2019-08-27 09:27:56', '2019-08-27 09:50:52', 0, '顾客', 'root', NULL);
INSERT INTO `orders` VALUES (22, 3, 960.14, 960.14, 3, 2, 1, '', '2019-08-27 09:28:08', '2019-08-27 09:49:38', 1, '顾客', 'root', NULL);
INSERT INTO `orders` VALUES (23, 3, 18.00, 18.00, 4, 2, 1, '', '2019-08-27 09:29:28', '2019-08-27 16:18:37', 1, '顾客', 'root', NULL);
INSERT INTO `orders` VALUES (24, 3, 600.00, 600.00, 2, 2, 1, '不要辣', '2019-08-27 16:19:01', '2019-09-02 09:19:26', 1, '顾客', 'root', NULL);
INSERT INTO `orders` VALUES (25, 3, 146.00, 146.00, 3, 2, 1, '', '2019-08-27 16:19:25', '2019-09-02 09:19:23', 1, '带带大师兄', 'root', NULL);
INSERT INTO `orders` VALUES (26, 3, 100.00, 100.00, 4, 2, 1, '', '2019-08-27 16:30:34', '2019-09-02 09:19:19', 1, '孙笑川的亲生爸爸', 'root', NULL);
INSERT INTO `orders` VALUES (27, 3, 500.00, 500.00, 1, 2, 1, '', '2019-09-02 10:10:59', '2019-09-02 14:43:45', 1, '顾客', 'root', NULL);
INSERT INTO `orders` VALUES (28, 3, 46.00, 46.00, 2, 2, 1, '', '2019-09-02 11:33:29', '2019-09-02 14:38:13', 1, '顾客', 'root', NULL);
INSERT INTO `orders` VALUES (29, 3, 146.00, 146.00, 1, 2, 1, '', '2019-09-02 14:54:49', '2019-09-02 14:54:57', 1, '顾客', 'root', NULL);
COMMIT;

-- ----------------------------
-- Table structure for reservation_info
-- ----------------------------
DROP TABLE IF EXISTS `reservation_info`;
CREATE TABLE `reservation_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `number_of_people` int(11) DEFAULT NULL,
  `table_id` int(11) DEFAULT NULL,
  `client_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `client_tel` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `status` int(255) DEFAULT NULL,
  `shop_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of reservation_info
-- ----------------------------
BEGIN;
INSERT INTO `reservation_info` VALUES (2, '2019-08-13 14:30:00', '2019-08-13 15:30:00', 7, 1, '琪琪国王', '15758130392', 3, 'root');
INSERT INTO `reservation_info` VALUES (3, '2019-08-13 14:30:00', '2019-08-13 15:30:00', 7, 1, '琪琪国王', '15758130392', 3, 'root');
INSERT INTO `reservation_info` VALUES (4, '2019-08-13 14:30:00', '2019-08-13 15:30:00', 7, 1, '琪琪国王', '15758130392', 3, 'root');
INSERT INTO `reservation_info` VALUES (5, '2019-08-14 14:30:00', '2019-08-14 15:30:00', 7, 1, '琪琪国王', '15758130392', 3, 'root');
INSERT INTO `reservation_info` VALUES (6, '2019-08-14 15:30:00', '2019-08-14 16:30:00', 7, 1, '琪琪国王', '15758130392', 3, 'root');
INSERT INTO `reservation_info` VALUES (7, '2019-08-13 11:00:00', '2019-08-13 12:30:00', 7, 1, '琪琪国王', '15758130392', 3, 'root');
INSERT INTO `reservation_info` VALUES (8, '2019-08-14 14:30:00', '2019-08-14 16:30:00', 2, 7, '张键', '15388902316', 3, 'root');
INSERT INTO `reservation_info` VALUES (9, '2019-08-14 16:30:00', '2019-08-14 17:30:00', 2, 7, '顾客', '123123', 2, 'root');
INSERT INTO `reservation_info` VALUES (10, '2019-08-16 14:30:00', '2019-08-16 15:30:00', 2, 2, '卢本伟', '1232131', 2, 'root');
INSERT INTO `reservation_info` VALUES (11, '2019-08-20 14:30:00', '2019-08-20 15:30:00', 2, 1, '顾客', '123123', 3, 'root');
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `password` longtext COLLATE utf8_bin,
  `telephone` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `email` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `code` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `shop_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `type` int(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES (6, 'admin', '$2a$10$lerBTAfH0DtMWP39wZTGpOk7jCxPbb97nSAcr0UVCVMH.i3q1iV.q', '15558190392', 'admin@admin.com', '651174', 'admin', 0);
INSERT INTO `user` VALUES (8, 'waiter001', '$2a$10$rj6I/dljFTuRdWIQOvTIxuoOp2OxfX5WdtzXwHYeEoZuuhBiPTq1u', '15558190333', 'waiter001@qq.com', NULL, 'root', 2);
INSERT INTO `user` VALUES (9, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `user` VALUES (10, 'root', '$2a$10$dn8KDqOFclq8V6vFj78GQ.V3YPi67KC2xc.SFBftAn.K8bfV0jc82', '15558190331', 'root@qq.com', NULL, 'root', 0);
INSERT INTO `user` VALUES (11, 'chef001', '$2a$10$jGCHw1VLegGJpQqfF02kN.H/3HscvQOwPBtcx5BEIMrz8Z20orlRe', '15558190322', 'chef001@qq.com', NULL, 'root', 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
