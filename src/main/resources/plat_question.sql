/*
 Navicat Premium Data Transfer

 Source Server         : demo
 Source Server Type    : MySQL
 Source Server Version : 80034 (8.0.34)
 Source Host           : localhost:3306
 Source Schema         : plat_question

 Target Server Type    : MySQL
 Target Server Version : 80034 (8.0.34)
 File Encoding         : 65001

 Date: 16/04/2025 21:13:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admindepartmentmapping
-- ----------------------------
DROP TABLE IF EXISTS `admindepartmentmapping`;
CREATE TABLE `admindepartmentmapping`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '记录id',
  `user_id` int NOT NULL COMMENT '用户id',
  `department_id` int NOT NULL COMMENT '部门id',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '授权时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `department_id`(`department_id` ASC) USING BTREE,
  CONSTRAINT `admindepartmentmapping_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `admindepartmentmapping_ibfk_2` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admindepartmentmapping
-- ----------------------------
INSERT INTO `admindepartmentmapping` VALUES (1, 2, 1, '2024-12-08 23:07:11');
INSERT INTO `admindepartmentmapping` VALUES (7, 1, 1, '2024-12-08 23:29:28');
INSERT INTO `admindepartmentmapping` VALUES (10, 1, 4, '2024-12-08 23:29:28');
INSERT INTO `admindepartmentmapping` VALUES (14, 1, 8, '2024-12-16 10:56:07');
INSERT INTO `admindepartmentmapping` VALUES (18, 1, 11, '2024-12-19 15:48:07');
INSERT INTO `admindepartmentmapping` VALUES (19, 2, 4, '2024-12-19 16:40:25');
INSERT INTO `admindepartmentmapping` VALUES (21, 2, 8, '2024-12-19 16:40:25');

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `category_id` int NOT NULL AUTO_INCREMENT COMMENT '分类id',
  `category_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  `parent_category_id` int NULL DEFAULT NULL COMMENT '上级分类id',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '分类描述',
  `category_level` tinyint NOT NULL COMMENT '分类等级',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更改时间',
  PRIMARY KEY (`category_id`) USING BTREE,
  INDEX `parent_category_id`(`parent_category_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1, '教育', NULL, '<p>与教育相关的分类</p>', 1, '2024-11-20 19:06:45', '2025-03-28 08:20:33');
INSERT INTO `category` VALUES (2, '课程', 1, '课程相关的分类', 2, '2024-11-20 19:06:45', '2024-11-20 19:06:45');
INSERT INTO `category` VALUES (3, '活动', 1, '教育活动相关的分类', 2, '2024-11-20 19:06:45', '2024-11-20 19:06:45');
INSERT INTO `category` VALUES (4, '健康', NULL, '与健康相关的分类', 1, '2024-11-20 19:06:45', '2024-11-20 19:06:45');
INSERT INTO `category` VALUES (5, '饮食', 4, '饮食相关的分类', 2, '2024-11-20 19:06:45', '2024-11-20 19:06:45');
INSERT INTO `category` VALUES (6, '锻炼', 4, '锻炼相关的分类', 2, '2024-11-20 19:06:45', '2024-12-05 13:36:18');

-- ----------------------------
-- Table structure for department_survey
-- ----------------------------
DROP TABLE IF EXISTS `department_survey`;
CREATE TABLE `department_survey`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `department_id` int NULL DEFAULT NULL,
  `survey_id` int NULL DEFAULT NULL,
  `department_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `assign_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of department_survey
-- ----------------------------

-- ----------------------------
-- Table structure for departments
-- ----------------------------
DROP TABLE IF EXISTS `departments`;
CREATE TABLE `departments`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '部门名称',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '部门描述',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of departments
-- ----------------------------
INSERT INTO `departments` VALUES (1, '新注册用户聚集地', '<p>新注册的用户默认部门</p>', '2024-11-20 19:23:31', '2025-04-16 20:49:48');
INSERT INTO `departments` VALUES (4, '市场部', '负责市场推广和销售支持', '2024-11-20 19:23:31', '2024-11-20 19:23:31');
INSERT INTO `departments` VALUES (8, '测试部', '测试人员', '2024-12-16 10:56:07', '2024-12-16 10:56:07');
INSERT INTO `departments` VALUES (11, '教学1班', '<p>教学1班</p>', '2024-12-19 15:28:23', '2025-03-27 20:41:19');

-- ----------------------------
-- Table structure for operationlog
-- ----------------------------
DROP TABLE IF EXISTS `operationlog`;
CREATE TABLE `operationlog`  (
  `log_id` int NOT NULL AUTO_INCREMENT,
  `operation_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `question_id` int NULL DEFAULT NULL,
  `category_id` int NULL DEFAULT NULL,
  `operator_id` int NOT NULL,
  `operation_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `operation_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `question_id`(`question_id` ASC) USING BTREE,
  INDEX `category_id`(`category_id` ASC) USING BTREE,
  INDEX `operator_id`(`operator_id` ASC) USING BTREE,
  CONSTRAINT `operationlog_ibfk_1` FOREIGN KEY (`question_id`) REFERENCES `questions` (`question_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `operationlog_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `operationlog_ibfk_3` FOREIGN KEY (`operator_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of operationlog
-- ----------------------------

-- ----------------------------
-- Table structure for options
-- ----------------------------
DROP TABLE IF EXISTS `options`;
CREATE TABLE `options`  (
  `option_id` int NOT NULL AUTO_INCREMENT COMMENT '选项id',
  `question_id` int NOT NULL COMMENT '所属问题id',
  `is_open` int NULL DEFAULT 0 COMMENT '是否是开放选项',
  `type` enum('行选项','列选项','填空') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '行选项' COMMENT '选项类型',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '\r\n选项描述',
  `sort_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '排序键值',
  `is_skip` int NULL DEFAULT 0 COMMENT '是否是跳转选项',
  `skip_to` int NULL DEFAULT 0 COMMENT '跳转至问题id',
  PRIMARY KEY (`option_id`) USING BTREE,
  INDEX `question_id`(`question_id` ASC) USING BTREE,
  INDEX `skip_to`(`is_skip` ASC) USING BTREE,
  INDEX `options_ibfk_3`(`is_open` ASC) USING BTREE,
  CONSTRAINT `options_ibfk_1` FOREIGN KEY (`question_id`) REFERENCES `questions` (`question_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 160 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of options
-- ----------------------------
INSERT INTO `options` VALUES (1, 1, 0, '行选项', '化学', '1', 0, 1);
INSERT INTO `options` VALUES (2, 1, 0, '行选项', '科学', '2', 0, NULL);
INSERT INTO `options` VALUES (3, 1, 0, '行选项', '语言艺术', '3', 0, 0);
INSERT INTO `options` VALUES (4, 2, 0, '行选项', '参观校外活动', '1', 0, NULL);
INSERT INTO `options` VALUES (5, 2, 0, '行选项', '科学实验', '2', 0, NULL);
INSERT INTO `options` VALUES (6, 2, 0, '行选项', '社区服务', '3', 0, NULL);
INSERT INTO `options` VALUES (7, 3, 0, '列选项', '非常重要', '1', 0, NULL);
INSERT INTO `options` VALUES (8, 3, 0, '列选项', '重要', '2', 0, NULL);
INSERT INTO `options` VALUES (9, 4, 0, '行选项', '30分钟', '1', 0, NULL);
INSERT INTO `options` VALUES (10, 4, 0, '行选项', '1小时', '2', 0, NULL);
INSERT INTO `options` VALUES (11, 4, 0, '行选项', '超过1小时', '3', 0, NULL);
INSERT INTO `options` VALUES (12, 11, 0, '行选项', '喜欢', '1', 0, NULL);
INSERT INTO `options` VALUES (13, 13, 0, '填空', 'null', '1', 0, NULL);
INSERT INTO `options` VALUES (14, 9, 0, '填空', 'null', '1', 0, NULL);
INSERT INTO `options` VALUES (15, 14, 0, '列选项', '1', '1', 0, NULL);
INSERT INTO `options` VALUES (16, 14, 0, '列选项', '2', '1', 0, NULL);
INSERT INTO `options` VALUES (17, 14, 0, '列选项', '3', '1', 0, NULL);
INSERT INTO `options` VALUES (18, 14, 0, '列选项', '4', '1', 0, NULL);
INSERT INTO `options` VALUES (19, 14, 0, '列选项', '5', '1', 0, NULL);
INSERT INTO `options` VALUES (20, 14, 0, '行选项', '界面设计', '1', 0, NULL);
INSERT INTO `options` VALUES (21, 14, 0, '行选项', '功能丰富度', '1', 0, NULL);
INSERT INTO `options` VALUES (22, 14, 0, '行选项', '使用流畅性', '1', 0, NULL);
INSERT INTO `options` VALUES (23, 14, 0, '行选项', '广告干扰度', '1', 0, NULL);
INSERT INTO `options` VALUES (24, 14, 0, '行选项', '安全与隐私保障', '1', 0, NULL);
INSERT INTO `options` VALUES (25, 15, 0, '行选项', '数学', '1', 0, NULL);
INSERT INTO `options` VALUES (26, 15, 0, '行选项', '英语', '1', 0, NULL);
INSERT INTO `options` VALUES (27, 15, 0, '行选项', '计算机', '1', 0, NULL);
INSERT INTO `options` VALUES (28, 15, 0, '行选项', '体育', '1', 0, NULL);
INSERT INTO `options` VALUES (29, 15, 0, '列选项', '内容丰富', '1', 0, NULL);
INSERT INTO `options` VALUES (30, 15, 0, '列选项', '教学质量高', '1', 0, NULL);
INSERT INTO `options` VALUES (32, 15, 0, '列选项', '学生互动好', '1', 0, NULL);
INSERT INTO `options` VALUES (34, 1, 0, '行选项', '英语', '1', 0, 0);
INSERT INTO `options` VALUES (40, 31, 0, '行选项', '18岁以下', '1', 1, 34);
INSERT INTO `options` VALUES (41, 20, 1, '行选项', '其他（请填写）', '100', 0, NULL);
INSERT INTO `options` VALUES (42, 20, 0, '行选项', '北京', '1', 0, NULL);
INSERT INTO `options` VALUES (43, 20, 0, '行选项', '上海', '1', 0, NULL);
INSERT INTO `options` VALUES (44, 20, 0, '行选项', '重庆', '1', 0, NULL);
INSERT INTO `options` VALUES (45, 21, 1, '行选项', '其他（请填写）', '100', 0, NULL);
INSERT INTO `options` VALUES (46, 21, 0, '行选项', '散步', '1', 0, NULL);
INSERT INTO `options` VALUES (47, 21, 0, '行选项', '看电影', '1', 0, NULL);
INSERT INTO `options` VALUES (48, 21, 0, '行选项', '旅游', '1', 0, NULL);
INSERT INTO `options` VALUES (51, 23, 0, '行选项', '宫保鸡丁', '1', 0, NULL);
INSERT INTO `options` VALUES (52, 23, 0, '行选项', '糖醋里脊', '1', 0, NULL);
INSERT INTO `options` VALUES (53, 23, 0, '行选项', '炒豆角', '1', 0, NULL);
INSERT INTO `options` VALUES (54, 23, 0, '行选项', '螺蛳粉', '1', 0, NULL);
INSERT INTO `options` VALUES (55, 23, 0, '列选项', '价格', '1', 0, NULL);
INSERT INTO `options` VALUES (56, 23, 0, '列选项', '味道', '1', 0, NULL);
INSERT INTO `options` VALUES (57, 23, 0, '列选项', '十分推荐', '1', 0, NULL);
INSERT INTO `options` VALUES (58, 23, 0, '列选项', '不推荐', '1', 0, NULL);
INSERT INTO `options` VALUES (59, 24, 0, '填空', '搜索功能', '1', 0, NULL);
INSERT INTO `options` VALUES (60, 24, 0, '填空', '个性化推荐', '1', 0, NULL);
INSERT INTO `options` VALUES (61, 24, 0, '填空', '物流跟踪', '1', 0, NULL);
INSERT INTO `options` VALUES (62, 24, 0, '填空', '客户服务', '1', 0, NULL);
INSERT INTO `options` VALUES (63, 25, 0, '行选项', '小于18岁', '100', 1, 27);
INSERT INTO `options` VALUES (64, 25, 0, '行选项', '大于18岁', '1', 0, NULL);
INSERT INTO `options` VALUES (65, 26, 0, '行选项', '没谈过', '1', 0, NULL);
INSERT INTO `options` VALUES (66, 26, 0, '列选项', '谈过', '1', 0, NULL);
INSERT INTO `options` VALUES (69, 31, 0, '行选项', '18-24岁', '1', 0, NULL);
INSERT INTO `options` VALUES (70, 31, 0, '行选项', '25-34岁', '1', 1, 37);
INSERT INTO `options` VALUES (71, 31, 0, '行选项', '45岁以上', '1', 0, NULL);
INSERT INTO `options` VALUES (72, 32, 1, '行选项', '其他（请填写）', '100', 0, NULL);
INSERT INTO `options` VALUES (73, 32, 0, '行选项', '手机', '1', 0, NULL);
INSERT INTO `options` VALUES (74, 32, 0, '行选项', '平板电脑', '1', 0, NULL);
INSERT INTO `options` VALUES (75, 32, 0, '行选项', '电脑', '1', 0, NULL);
INSERT INTO `options` VALUES (76, 32, 0, '行选项', '智能手表', '1', 0, NULL);
INSERT INTO `options` VALUES (77, 33, 0, '行选项', '搜索功能', '1', 0, NULL);
INSERT INTO `options` VALUES (78, 33, 0, '行选项', '个性化推荐', '1', 0, NULL);
INSERT INTO `options` VALUES (79, 33, 0, '行选项', '物流跟踪', '1', 0, NULL);
INSERT INTO `options` VALUES (80, 33, 0, '行选项', '支付方式', '1', 0, NULL);
INSERT INTO `options` VALUES (81, 33, 0, '行选项', '客户服务', '1', 0, NULL);
INSERT INTO `options` VALUES (82, 34, 0, '行选项', '搜索功能', '1', 0, NULL);
INSERT INTO `options` VALUES (83, 34, 0, '行选项', '产品推荐', '1', 0, NULL);
INSERT INTO `options` VALUES (84, 34, 0, '行选项', '支付方式', '1', 0, NULL);
INSERT INTO `options` VALUES (85, 34, 0, '行选项', '客户服务', '1', 0, NULL);
INSERT INTO `options` VALUES (86, 34, 0, '行选项', '物流跟踪', '1', 0, NULL);
INSERT INTO `options` VALUES (87, 34, 0, '列选项', '1', '1', 0, NULL);
INSERT INTO `options` VALUES (88, 34, 0, '列选项', '2', '1', 0, NULL);
INSERT INTO `options` VALUES (89, 34, 0, '列选项', '3', '1', 0, NULL);
INSERT INTO `options` VALUES (90, 34, 0, '列选项', '4', '1', 0, NULL);
INSERT INTO `options` VALUES (91, 34, 0, '列选项', '5', '1', 0, NULL);
INSERT INTO `options` VALUES (92, 35, 1, '行选项', '其他（请填写）', '100', 0, NULL);
INSERT INTO `options` VALUES (93, 35, 0, '行选项', '搜索结果不准确', '1', 0, NULL);
INSERT INTO `options` VALUES (94, 35, 0, '行选项', '支付失败或支付过程复杂', '1', 0, NULL);
INSERT INTO `options` VALUES (95, 35, 0, '行选项', '物流信息不更新', '1', 0, NULL);
INSERT INTO `options` VALUES (96, 35, 0, '行选项', '客服响应缓慢', '1', 0, NULL);
INSERT INTO `options` VALUES (97, 35, 0, '行选项', '没有遇到问题', '1', 0, NULL);
INSERT INTO `options` VALUES (98, 36, 0, '行选项', '非常满意', '100', 1, 39);
INSERT INTO `options` VALUES (99, 36, 0, '行选项', '较满意', '1', 0, NULL);
INSERT INTO `options` VALUES (100, 36, 0, '行选项', '一般', '1', 0, NULL);
INSERT INTO `options` VALUES (101, 36, 0, '行选项', '不满意', '1', 0, NULL);
INSERT INTO `options` VALUES (102, 37, 1, '行选项', '其他（请填写）', '100', 0, NULL);
INSERT INTO `options` VALUES (103, 37, 0, '行选项', '搜索功能', '1', 0, NULL);
INSERT INTO `options` VALUES (104, 37, 0, '行选项', '个性化推荐', '1', 0, NULL);
INSERT INTO `options` VALUES (105, 37, 0, '行选项', '产品详情页', '1', 0, NULL);
INSERT INTO `options` VALUES (106, 37, 0, '行选项', '物流跟踪', '1', 0, NULL);
INSERT INTO `options` VALUES (107, 37, 0, '行选项', '客户服务', '1', 0, NULL);
INSERT INTO `options` VALUES (108, 39, 0, '行选项', '整体界面设计', '1', 0, NULL);
INSERT INTO `options` VALUES (109, 39, 0, '行选项', '功能流畅性', '1', 0, NULL);
INSERT INTO `options` VALUES (110, 39, 0, '行选项', '客服服务效率', '1', 0, NULL);
INSERT INTO `options` VALUES (111, 39, 0, '行选项', '产品推荐准确性', '1', 0, NULL);
INSERT INTO `options` VALUES (112, 39, 0, '行选项', '购物流程简易性', '1', 0, NULL);
INSERT INTO `options` VALUES (113, 39, 0, '列选项', '1', '1', 0, NULL);
INSERT INTO `options` VALUES (114, 39, 0, '列选项', '2', '1', 0, NULL);
INSERT INTO `options` VALUES (115, 39, 0, '列选项', '3', '1', 0, NULL);
INSERT INTO `options` VALUES (116, 39, 0, '列选项', '4', '1', 0, NULL);
INSERT INTO `options` VALUES (117, 39, 0, '列选项', '5', '1', 0, NULL);
INSERT INTO `options` VALUES (118, 26, 0, '行选项', '谈过几次', '1', 0, NULL);
INSERT INTO `options` VALUES (119, 19, 0, '行选项', '23岁', '1', 0, 0);
INSERT INTO `options` VALUES (120, 19, 0, '行选项', '27岁', '1', 0, 0);
INSERT INTO `options` VALUES (121, 19, 0, '行选项', '30岁以后', '1', 0, 0);
INSERT INTO `options` VALUES (122, 19, 1, '行选项', '其他（请填写）', '1', 0, 0);
INSERT INTO `options` VALUES (123, 16, 0, '行选项', '9点', '1', 0, 0);
INSERT INTO `options` VALUES (124, 16, 0, '行选项', '10点', '1', 0, 0);
INSERT INTO `options` VALUES (125, 16, 0, '行选项', '12点', '1', 0, 0);
INSERT INTO `options` VALUES (126, 43, 0, '行选项', '222', '1', 0, 0);
INSERT INTO `options` VALUES (129, 47, 0, '行选项', '完全可以，毫无困难', '1', 0, 0);
INSERT INTO `options` VALUES (130, 47, 0, '行选项', '基本可以，但偶尔会遇到一些小问题', '1', 0, 0);
INSERT INTO `options` VALUES (131, 47, 0, '行选项', '只能在他人帮助或参考详细教程下完成', '1', 0, 0);
INSERT INTO `options` VALUES (132, 47, 0, '行选项', '完全不熟悉搭建过程', '1', 0, 0);
INSERT INTO `options` VALUES (133, 48, 0, '行选项', '非常熟练，能够独立高效地完成项目开发', '1', 0, 0);
INSERT INTO `options` VALUES (134, 48, 0, '行选项', '比较熟练，能顺利进行常规项目开发', '1', 0, 0);
INSERT INTO `options` VALUES (135, 48, 0, '行选项', '仅了解基本操作，开发复杂项目有困难', '1', 0, 0);
INSERT INTO `options` VALUES (136, 48, 0, '行选项', '不太会使用，需要进一步学习', '1', 0, 0);
INSERT INTO `options` VALUES (137, 49, 0, '行选项', '精通，能灵活运用解决各种前端交互问题', '1', 0, 0);
INSERT INTO `options` VALUES (138, 49, 0, '行选项', '掌握较好，可完成常见的前端功能开发', '1', 0, 0);
INSERT INTO `options` VALUES (139, 49, 0, '行选项', '了解一些基本语法，实际应用较困难', '1', 0, 0);
INSERT INTO `options` VALUES (140, 49, 0, '行选项', '只知道概念，无法进行代码编写', '1', 0, 0);
INSERT INTO `options` VALUES (142, 52, 0, '行选项', '完全掌握并能熟练运用', '1', 0, 0);
INSERT INTO `options` VALUES (143, 52, 0, '行选项', '掌握大部分内容，少数细节不太清楚', '1', 0, 0);
INSERT INTO `options` VALUES (144, 52, 0, '行选项', '仅记住基本语法，在实际项目中使用不熟练', '1', 0, 0);
INSERT INTO `options` VALUES (146, 52, 0, '行选项', '不太熟悉', '1', 0, 0);
INSERT INTO `options` VALUES (147, 53, 0, '行选项', '能够熟练编写复杂的 Servlet 并处理各种业务逻辑', '1', 0, 0);
INSERT INTO `options` VALUES (148, 53, 0, '行选项', '可以编写简单的 Servlet，应对一般业务场景', '1', 0, 0);
INSERT INTO `options` VALUES (149, 53, 0, '行选项', '了解 Servlet 编写的基本流程，但实践较少', '1', 0, 0);
INSERT INTO `options` VALUES (150, 53, 0, '行选项', '对 Servlet 概念模糊，不知道如何编写', '1', 0, 0);
INSERT INTO `options` VALUES (159, 15, 0, '列选项', '幽默有趣', NULL, 0, 0);

-- ----------------------------
-- Table structure for questions
-- ----------------------------
DROP TABLE IF EXISTS `questions`;
CREATE TABLE `questions`  (
  `question_id` int NOT NULL AUTO_INCREMENT COMMENT '问题id',
  `survey_id` int NOT NULL COMMENT '所属问卷id',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '问题描述',
  `type` enum('单选','多选','排序','填空','矩阵单选','矩阵多选','评分题','文件上传题') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '问题类型',
  `display_type` enum('下拉','单选框','复选框','滑动条','五角星') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '展示形式',
  `is_required` tinyint(1) NULL DEFAULT 0 COMMENT '是否必答',
  `is_open` tinyint(1) NULL DEFAULT 0 COMMENT '是否有开放答案',
  `max_selections` int NULL DEFAULT NULL COMMENT '最多选几项',
  `primary_category_id` int NULL DEFAULT NULL COMMENT '所属分类',
  `is_skip` tinyint(1) NULL DEFAULT 0 COMMENT '是否是跳转题',
  `sort_order` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '排序字段',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`question_id`) USING BTREE,
  INDEX `survey_id`(`survey_id` ASC) USING BTREE,
  INDEX `primary_category_id`(`primary_category_id` ASC) USING BTREE,
  CONSTRAINT `questions_ibfk_1` FOREIGN KEY (`survey_id`) REFERENCES `surveys` (`survey_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `questions_ibfk_2` FOREIGN KEY (`primary_category_id`) REFERENCES `category` (`category_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 62 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of questions
-- ----------------------------
INSERT INTO `questions` VALUES (1, 1, '你最喜欢的课程是什么', '单选', '单选框', 0, 1, NULL, 2, 0, NULL, '2024-11-20 19:09:42', '2025-03-28 17:50:12');
INSERT INTO `questions` VALUES (2, 1, '你参加过哪些活动？', '多选', '复选框', 0, 0, 3, 3, 1, NULL, '2024-11-20 19:09:42', '2024-12-02 23:30:51');
INSERT INTO `questions` VALUES (3, 1, '你认为健康饮食的重要性如何？', '单选', '下拉', 1, 0, NULL, 5, 1, NULL, '2024-11-20 19:09:42', '2024-12-03 00:07:45');
INSERT INTO `questions` VALUES (4, 2, '你每天锻炼多久？', '单选', '下拉', 1, 0, NULL, 6, 0, NULL, '2024-11-20 19:09:42', '2024-12-03 00:08:01');
INSERT INTO `questions` VALUES (5, 2, '你喜欢锻炼吗？', '单选', '下拉', 0, 0, NULL, 6, 0, NULL, '2024-11-21 20:30:35', '2024-12-03 00:02:17');
INSERT INTO `questions` VALUES (9, 2, '你每天几点睡觉？', '填空', '下拉', 0, 0, NULL, 4, 0, NULL, '2024-11-24 13:17:10', '2024-12-03 00:02:22');
INSERT INTO `questions` VALUES (11, 2, '你爱跳舞吗？', '单选', '下拉', 1, 0, NULL, 3, 0, NULL, '2024-11-25 10:36:06', '2024-12-03 00:02:24');
INSERT INTO `questions` VALUES (13, 1, '谈一谈你1000米跑步的心得', '填空', '下拉', 1, 0, NULL, 4, 0, NULL, '2024-11-27 19:26:30', '2024-12-03 00:02:25');
INSERT INTO `questions` VALUES (14, 1, '您对移动应用的整体体验如何？', '矩阵单选', '下拉', 1, 0, NULL, 3, 0, NULL, '2024-11-28 19:44:18', '2024-12-03 00:02:26');
INSERT INTO `questions` VALUES (15, 1, '请对以下课程在不同维度上的表现进行评价', '矩阵多选', '下拉', 1, 0, NULL, 2, 0, NULL, '2024-11-28 21:31:36', '2024-12-03 00:02:27');
INSERT INTO `questions` VALUES (16, 1, '你做作业一般到晚上几点？', '单选', '下拉', 1, 1, NULL, 1, 0, NULL, '2024-11-30 22:42:16', '2024-12-03 00:02:27');
INSERT INTO `questions` VALUES (19, 1, '你打算什么岁数结婚？', '单选', '下拉', 1, 1, NULL, 1, 0, NULL, '2024-11-30 23:51:19', '2024-12-03 00:02:29');
INSERT INTO `questions` VALUES (20, 1, '你想去什么地方？', '多选', '下拉', 1, 1, NULL, 1, 0, NULL, '2024-12-02 11:04:51', '2024-12-03 00:02:29');
INSERT INTO `questions` VALUES (21, 6, '你想跟你的伴侣做什么？', '多选', '下拉', 1, 1, NULL, 1, 0, NULL, '2024-12-02 17:45:51', '2024-12-03 00:02:30');
INSERT INTO `questions` VALUES (23, 5, '请对学校食堂的早餐进行评分', '矩阵多选', '下拉', 1, 0, NULL, 5, 0, NULL, '2024-12-03 22:47:35', '2024-12-03 22:47:57');
INSERT INTO `questions` VALUES (24, 1, '请为以下功能的重要性打分', '评分题', '下拉', 1, 0, NULL, 3, 0, NULL, '2024-12-04 13:41:17', '2024-12-04 14:00:44');
INSERT INTO `questions` VALUES (25, 1, '你目前的年龄是？', '单选', '下拉', 1, 0, NULL, 1, 1, NULL, '2024-12-04 20:41:41', '2024-12-04 20:53:19');
INSERT INTO `questions` VALUES (26, 1, '你谈过恋爱吗？', '多选', '下拉', 1, 0, NULL, 1, 0, NULL, '2024-12-04 20:44:55', '2025-03-31 20:29:22');
INSERT INTO `questions` VALUES (27, 1, '请说出一个你喜欢的动画片', '填空', '下拉', 1, 0, NULL, 1, 0, NULL, '2024-12-04 20:46:02', '2024-12-04 20:46:02');
INSERT INTO `questions` VALUES (30, 8, '请上传你和伴侣的照片', '文件上传题', '下拉', 1, 0, NULL, 1, 0, NULL, '2024-12-08 13:08:28', '2024-12-24 11:16:48');
INSERT INTO `questions` VALUES (31, 14, '您的年龄段是？', '单选', '下拉', 1, 0, NULL, 1, 1, NULL, '2024-12-08 14:50:49', '2024-12-09 11:34:09');
INSERT INTO `questions` VALUES (32, 14, '您最常使用以下哪种类型的电子产品？', '单选', '下拉', 1, 1, NULL, 1, 0, NULL, '2024-12-08 14:53:38', '2024-12-08 14:53:38');
INSERT INTO `questions` VALUES (33, 14, '请按照您认为的重要性对以下功能进行排序？', '评分题', '下拉', 1, 0, NULL, 1, 0, NULL, '2024-12-08 14:55:51', '2024-12-08 14:55:51');
INSERT INTO `questions` VALUES (34, 14, '您对以下平台功能的满意度如何？', '矩阵单选', '下拉', 1, 0, NULL, 1, 0, NULL, '2024-12-08 14:57:03', '2024-12-08 14:57:03');
INSERT INTO `questions` VALUES (35, 14, '您是否遇到过以下问题？', '多选', '下拉', 0, 1, NULL, 1, 0, NULL, '2024-12-08 15:00:27', '2024-12-09 11:33:39');
INSERT INTO `questions` VALUES (36, 14, '您对平台推荐的准确度满意吗？', '单选', '下拉', 1, 0, NULL, 1, 1, NULL, '2024-12-08 15:02:02', '2024-12-08 15:10:23');
INSERT INTO `questions` VALUES (37, 14, '您最希望平台优化哪些功能？', '多选', '下拉', 0, 1, NULL, 1, 0, NULL, '2024-12-08 15:02:57', '2024-12-08 15:02:57');
INSERT INTO `questions` VALUES (38, 14, '您在使用平台过程中遇到的错误截图。', '文件上传题', '下拉', 1, 0, NULL, 1, 0, NULL, '2024-12-08 15:04:16', '2024-12-09 11:33:24');
INSERT INTO `questions` VALUES (39, 14, '您对平台的整体体验如何？', '矩阵多选', '下拉', 1, 0, NULL, 1, 0, NULL, '2024-12-08 15:05:05', '2024-12-09 11:32:36');
INSERT INTO `questions` VALUES (40, 14, '请列出您对平台改进的其他建议或意见', '填空', '下拉', 0, 0, NULL, 1, 0, NULL, '2024-12-08 15:06:56', '2024-12-08 15:06:56');
INSERT INTO `questions` VALUES (41, 6, '你喜欢你的另一半吗？', '填空', '下拉', 1, 0, NULL, 1, 0, NULL, '2024-12-08 15:30:08', '2024-12-08 15:30:08');
INSERT INTO `questions` VALUES (43, 8, '11111', '单选', '下拉', 0, 0, NULL, 1, 0, NULL, '2024-12-20 23:59:42', '2024-12-20 23:59:42');
INSERT INTO `questions` VALUES (47, 21, '你是否能够熟练搭建 Java Web 应用开发环境？', '单选', '下拉', 1, 0, NULL, 1, 0, NULL, '2024-12-24 13:16:11', '2024-12-24 13:17:19');
INSERT INTO `questions` VALUES (48, 21, '对于使用 Idea 开发 Java Web 应用程序项目，你的掌握程度如何？', '单选', '下拉', 1, 0, NULL, 1, 0, NULL, '2024-12-24 13:20:49', '2024-12-24 13:20:49');
INSERT INTO `questions` VALUES (49, 21, '你对 JavaScript 基本语法与应用的掌握情况是：', '单选', '下拉', 1, 0, NULL, 1, 0, NULL, '2024-12-24 14:04:14', '2024-12-24 14:04:14');
INSERT INTO `questions` VALUES (52, 21, '关于 JSP 基本语法与内置对象，你：', '单选', '下拉', 1, 0, NULL, 1, 0, NULL, '2024-12-24 14:13:59', '2024-12-24 14:13:59');
INSERT INTO `questions` VALUES (53, 21, '你在编写与应用 Servlet 方面的能力如何？', '单选', '下拉', 1, 0, NULL, 1, 0, NULL, '2024-12-29 17:03:00', '2024-12-29 17:03:00');
INSERT INTO `questions` VALUES (55, 24, '12312', '单选', '下拉', 0, 1, NULL, 2, 1, NULL, '2025-03-09 22:04:23', '2025-03-28 19:00:35');
INSERT INTO `questions` VALUES (57, 2, '1231', '单选', '下拉', 0, 1, NULL, 2, 1, NULL, '2025-03-09 22:06:30', '2025-03-09 22:06:30');
INSERT INTO `questions` VALUES (59, 1, '提交任意一张图片', '文件上传题', '下拉', 1, 0, NULL, 5, 0, NULL, '2025-04-01 17:23:35', '2025-04-01 17:23:35');

-- ----------------------------
-- Table structure for responses
-- ----------------------------
DROP TABLE IF EXISTS `responses`;
CREATE TABLE `responses`  (
  `response_id` int NOT NULL AUTO_INCREMENT COMMENT '答卷id',
  `survey_id` int NOT NULL COMMENT '问卷id',
  `question_id` int NULL DEFAULT NULL COMMENT '问题id',
  `option_id` int NULL DEFAULT NULL COMMENT '选项id',
  `row_id` int NULL DEFAULT NULL COMMENT '行id',
  `column_id` int NULL DEFAULT NULL COMMENT '列id',
  `user_id` int NULL DEFAULT NULL COMMENT '用户id',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'ip地址',
  `response_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '文本内容',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `is_valid` tinyint NULL DEFAULT 0 COMMENT '是否有效',
  `file_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件路径',
  PRIMARY KEY (`response_id`) USING BTREE,
  INDEX `survey_id`(`survey_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2849 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of responses
-- ----------------------------

-- ----------------------------
-- Table structure for surveys
-- ----------------------------
DROP TABLE IF EXISTS `surveys`;
CREATE TABLE `surveys`  (
  `survey_id` int NOT NULL AUTO_INCREMENT COMMENT '问卷id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '问卷名称',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '问卷描述',
  `created_by` int NOT NULL COMMENT '创建者',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建试卷',
  `updated_by` int NULL DEFAULT NULL COMMENT '更新者',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `allow_view` tinyint(1) NULL DEFAULT 0 COMMENT '是否允许查看',
  `status` enum('草稿','发布','关闭') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '草稿' COMMENT '问卷状态',
  PRIMARY KEY (`survey_id`) USING BTREE,
  INDEX `created_by`(`created_by` ASC) USING BTREE,
  INDEX `updated_by`(`updated_by` ASC) USING BTREE,
  CONSTRAINT `surveys_ibfk_1` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `surveys_ibfk_2` FOREIGN KEY (`updated_by`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of surveys
-- ----------------------------
INSERT INTO `surveys` VALUES (1, '学生满意度调查', '<p>关于学生对课程和教师的满意度调查</p>', 1, '2024-11-20 19:05:30', 1, '2025-04-15 21:36:25', 1, '发布');
INSERT INTO `surveys` VALUES (2, '健康生活方式调查', '了解学生的健康生活方式及其对课程的影响', 1, '2024-11-20 19:05:30', NULL, '2025-03-09 22:06:31', 0, '草稿');
INSERT INTO `surveys` VALUES (5, '早餐意见收集问卷', '该问卷旨在了解学生对食堂早餐样式的意见', 1, '2024-11-22 22:29:32', 1, '2024-12-04 19:55:45', 1, '草稿');
INSERT INTO `surveys` VALUES (6, '恋爱观调查问卷', '该问卷旨在调查大学生恋爱观', 1, '2024-11-22 22:31:32', 1, '2024-12-08 15:30:09', 1, '草稿');
INSERT INTO `surveys` VALUES (8, '想去的地方调查问卷', '该问卷旨在调查大学生假期想去哪玩', 1, '2024-11-22 23:01:41', 1, '2024-12-23 23:42:53', 0, '草稿');
INSERT INTO `surveys` VALUES (14, '用户体验-功能需求调查', '了解用户在使用平台过程中的体验与需求，优化产品功能。', 2, '2024-12-08 14:50:00', 1, '2024-12-16 16:14:36', 1, '草稿');
INSERT INTO `surveys` VALUES (21, '《Java Web 应用开发》调查问卷', '<p>亲爱的同学：\n你好！为了全面了解大家在《Java Web 应用开发》课程中的学习情况，以便更好地优化教学内容和教学方法，特开展本次问卷调查。你的回答将对我们的教学改进工作提供重要依据，请你认真作答。感谢你抽出宝贵的时间！\n</p>', 1, '2024-12-24 13:15:50', 1, '2025-01-09 10:54:29', 0, '草稿');
INSERT INTO `surveys` VALUES (24, '作息安排调查问卷', '<p>作息安排111</p>', 1, '2025-01-09 11:01:18', NULL, '2025-03-09 22:05:12', 1, '草稿');
INSERT INTO `surveys` VALUES (30, '123', '<p>123</p>', 0, '2025-03-26 18:14:33', NULL, '2025-03-26 18:14:33', 1, '草稿');

-- ----------------------------
-- Table structure for user_survey
-- ----------------------------
DROP TABLE IF EXISTS `user_survey`;
CREATE TABLE `user_survey`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '记录id',
  `user_id` int NOT NULL COMMENT '用户id',
  `survey_id` int NOT NULL COMMENT '问卷id',
  `status` enum('未查看','未完成','保存未提交','已完成') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '未完成' COMMENT '答卷情况',
  `assigned_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  `completed_at` timestamp NULL DEFAULT NULL COMMENT '完成时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_user_survey`(`user_id` ASC, `survey_id` ASC) USING BTREE,
  INDEX `survey_id`(`survey_id` ASC) USING BTREE,
  CONSTRAINT `user_survey_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `user_survey_ibfk_2` FOREIGN KEY (`survey_id`) REFERENCES `surveys` (`survey_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 598 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_survey
-- ----------------------------

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `salt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '加密盐值',
  `role` enum('超级管理员','普通管理员','普通用户') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '普通用户' COMMENT '用户角色',
  `department_id` int NULL DEFAULT NULL COMMENT '部门id',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE,
  INDEX `fk_department`(`department_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 488 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (0, '匿名用户', '匿名用户', 'null', 'null', '普通用户', 1, '2024-12-07 22:43:40', '2025-03-27 20:02:33');
INSERT INTO `users` VALUES (1, '小帅', '帅子', '142f52c8921e4c77752f253f00885151820ae6177d374bad0f249fae44253965', 'UFXMoHqTHJFdtgi3nprvEg==', '超级管理员', 1, '2024-11-20 19:02:49', '2025-03-27 22:00:09');
INSERT INTO `users` VALUES (2, '刚子', '刚子', '28931cc17c5579db91c9824c02141e2873ab6d085a2d238c61693d66113f2f98', 'WZgwcK3GQG27CNUAk+gWZw==', '普通管理员', 1, '2024-11-20 19:03:16', '2025-01-09 16:38:07');
INSERT INTO `users` VALUES (3, '图图11', '图图', '521b247d42b096c8c0b7aa98f90c2a49392979a561f891df3357fecc17a845c6', 'hgPGEj3ZoClzOnCgrpkm5A==', '普通用户', 4, '2024-11-20 19:03:26', '2025-01-09 16:44:42');
INSERT INTO `users` VALUES (4, '毛毛', '毛毛', 'c5401efbb569206c357607df47994f2ac2efbabdb5b57fb4d0c527bd3960e09a', '+ITiu4uCgrU0stRYL6Os5Q==', '普通用户', 1, '2024-11-20 19:03:35', '2025-04-16 20:50:32');
INSERT INTO `users` VALUES (5, '冬冬', '冬冬', '842dd9988b43ed67ff901a220043f7a0193591ce57dade9a5b7673982946dad9', 'I6ovMptUIVirMS1Y9gQRfg==', '普通用户', 4, '2024-11-25 11:31:38', '2024-12-19 13:01:22');
INSERT INTO `users` VALUES (7, '强强', '强强', 'd9f08194d78dfe0c2441ea0c1f290f65e5d558fe25a88e445e9fd4e36999c9d4', 'mzJJpd62zp1KU3HEiqIXEg==', '普通用户', 4, '2024-12-07 11:26:29', '2025-01-08 20:36:15');
INSERT INTO `users` VALUES (10, '测试', '测试', 'f7ce270f7cb6d26e88f41e3eb5efb5daff14b0fe165ebc3b31edbd5fb435f7b9', 'MCHVXCWo5IMJjRxeUHalnA==', '普通用户', 11, '2024-12-19 10:38:27', '2025-04-16 20:37:19');
INSERT INTO `users` VALUES (419, '申泽森', '20221819402001', 'da675e78797172c7e185e955459e19d0661bddc73f5ba1fd17f9fc43cb769909', 'Q08lnq6+nTn/wm0TzW86wA==', '普通用户', 11, '2025-04-16 20:48:16', '2025-04-16 20:48:16');
INSERT INTO `users` VALUES (420, '郭文墨', '20221819402002', '63be4423052a152b0f311fe4582ecfef7d25b48f0eae38232dea809d98819696', 'spf/NJkx6BwMToOATPI6ag==', '普通用户', 11, '2025-04-16 20:48:16', '2025-04-16 20:48:16');
INSERT INTO `users` VALUES (421, '马一鸣', '20221819402003', '120cfbae382532f842909087641cac8ef4f51468ca252455d023fa318292b50c', '1PlhC+3mK6wVGcMUvBbNog==', '普通用户', 11, '2025-04-16 20:48:16', '2025-04-16 20:48:16');
INSERT INTO `users` VALUES (422, '王志凯', '20221819402004', '26180bfb5262abae12acb42e9c6f2bd12b7aeb53f3ac3503a00b45c3b501e88e', 'JSoFfEw+9W5/41EuL60jxw==', '普通用户', 11, '2025-04-16 20:48:16', '2025-04-16 20:48:16');
INSERT INTO `users` VALUES (423, '席家昊', '20221819402005', 'bf2659342b00cacb97ef8134e3e9406aef19b42c8d9b28a3244ae3d85526d633', 'Vqx4Z/EZXvCKHprq2AlWBQ==', '普通用户', 11, '2025-04-16 20:50:59', '2025-04-16 20:50:59');
INSERT INTO `users` VALUES (424, '吕天宇', '20221819402006', 'bffe87855f8732551aa7189b07b5bed2b23301cea5592a0b99807ba54288ec04', 'SJXQAlbcDUEgWHgXQXO1Rw==', '普通用户', 11, '2025-04-16 20:50:59', '2025-04-16 20:50:59');
INSERT INTO `users` VALUES (425, '刘艺豪', '20221819402008', '140ff66d7257f5da7b85f81ade8ed46b52cf8b033a2ec3cabeb2b80002df8077', 'QK6kTJgdKrPoJzaSg12oig==', '普通用户', 11, '2025-04-16 20:50:59', '2025-04-16 20:50:59');
INSERT INTO `users` VALUES (426, '李正阳', '20221819402009', '49c8c08e3bd5cb947d389990c668111b56163334df92c1befcfac2950c3cc946', 'nQntNqi8SCevVcCBPjF6tA==', '普通用户', 11, '2025-04-16 20:50:59', '2025-04-16 20:50:59');
INSERT INTO `users` VALUES (427, '邵嘉奇', '20221819402010', '48d2e1bb40509c1b92f8e1ca2be40ad303d64fe904a5bd4a1c3e0bc339b96fbc', 'jqn6Zfra7T9T44VzoeDOrQ==', '普通用户', 11, '2025-04-16 20:50:59', '2025-04-16 20:50:59');
INSERT INTO `users` VALUES (428, '李章阳', '20221819402011', 'cc8fd7befa40b217c1cefceb014c7ff8fdf1d90a1869ce34421ddcfd53b4879a', '1o9MR/ZYEaEKPqXZ0hKkfg==', '普通用户', 11, '2025-04-16 20:50:59', '2025-04-16 20:50:59');
INSERT INTO `users` VALUES (429, '胡景涛', '20221819402012', '5520b24108340f5079feb63db98b925f17be3103e1e8ad3dc7898d69e0799903', 'OHKLe2x/fD0aPFDnb/MsOg==', '普通用户', 11, '2025-04-16 20:50:59', '2025-04-16 20:50:59');
INSERT INTO `users` VALUES (430, '刘鑫', '20221819402013', '1871a986ee6c386544756270e1b49febde6f8e3899656f5a992c9d3f0ace967d', 'pim8Co1Vi4RY76Qrafu8pA==', '普通用户', 11, '2025-04-16 20:50:59', '2025-04-16 20:50:59');
INSERT INTO `users` VALUES (431, '崔庆阳', '20221819402014', 'ca799f6b740c75d7a66693cebbac5229d89f6792dad74ace0e604c2dbd71c9ca', '4ZPmB3MNQ+zHZY7xdUYYTQ==', '普通用户', 11, '2025-04-16 20:50:59', '2025-04-16 20:50:59');
INSERT INTO `users` VALUES (432, '杨昕宇', '20221819402015', 'aabd1355313cab5ef045d4cb893aceebd8866afa1ea45817e29868b1fb5f1a90', 'Cp6ydX3j05enSHsenITWsw==', '普通用户', 11, '2025-04-16 20:50:59', '2025-04-16 20:50:59');
INSERT INTO `users` VALUES (433, '鲁宝铎', '20221819402016', 'ca5a2a380311b4f8fb9b33b9ed3885c4fd56271d0515ed4ad61de4f0de5e2d72', 'HR/vQqWZGzqU+6Z0njlslw==', '普通用户', 11, '2025-04-16 20:50:59', '2025-04-16 20:50:59');
INSERT INTO `users` VALUES (434, '张建渠', '20221819402017', 'b89a7e6bd5eca2d2c0473b90fc3a44901a6c82e1508b137addde7af4d0abee9b', 'yT28IFfNujVdquEtpnyOwQ==', '普通用户', 11, '2025-04-16 20:50:59', '2025-04-16 20:50:59');
INSERT INTO `users` VALUES (435, '赵子尧', '20221819402018', '9902e844d08ad2302a1ba252437173f1c7f4ff00bd460c87fadeff76f00e39ac', 'IIX16KW8UHVk4UX4NGR3ZA==', '普通用户', 11, '2025-04-16 20:50:59', '2025-04-16 20:50:59');
INSERT INTO `users` VALUES (436, '徐安岐', '20221819402019', '0ac46c3b49ff49865b220a1912db22ed273ce9cc4e3104fb1916b436da2fd636', 'DYd+3qewls1+uyxBRMbN8w==', '普通用户', 11, '2025-04-16 20:50:59', '2025-04-16 20:50:59');
INSERT INTO `users` VALUES (437, '申涵', '20221819402020', '5a12157f58fe4692e1d3f9cb4ff4b0432e2c39aa7e354c85e6b6828a99234c94', 'aru743rW97sLod6G1xmRKA==', '普通用户', 11, '2025-04-16 20:50:59', '2025-04-16 20:50:59');
INSERT INTO `users` VALUES (438, '高凯利', '20221819402021', '882aafb111ec290dfd6eab348c4b30129a1c327f5019b75034f6b6a10a51b607', '0KQ804lautris5NCC0ZV+w==', '普通用户', 11, '2025-04-16 20:50:59', '2025-04-16 20:50:59');
INSERT INTO `users` VALUES (439, '周文洋', '20221819402022', '6b206804e6ccc29675df2982fc00e6b88585fd957355ee83eb57397e7ed281f2', 'yvXatEf3Go8LJ+Zk1BJ6Vg==', '普通用户', 11, '2025-04-16 20:50:59', '2025-04-16 20:50:59');
INSERT INTO `users` VALUES (440, '丁思源', '20221819402023', '8bc76b631b4a9f913f66e4d00b967f1a492bc1258cf6a7d0331a021fd701672c', '9TY2ukJbbLE8IRiisQD+lg==', '普通用户', 11, '2025-04-16 20:50:59', '2025-04-16 20:50:59');
INSERT INTO `users` VALUES (441, '刘浩楠', '20221819402024', '39f6160820e26511d5ec03f5dc960b936474476295def5ff3adaaf1a657af14a', 'IvYV9E1t6oZaLc9hG5yjUw==', '普通用户', 11, '2025-04-16 20:50:59', '2025-04-16 20:50:59');
INSERT INTO `users` VALUES (442, '王佳垚', '20221819402025', '283e476bf8e38c06b8025ebb83cfde6f47c9e8a5f11eb42af952c2851c166f9e', 'b/mTifitzfF31IE4okUdBg==', '普通用户', 11, '2025-04-16 20:50:59', '2025-04-16 20:50:59');
INSERT INTO `users` VALUES (443, '王阳', '20221819402026', 'b3d0c4cf73c90bfae739b52be33eaab60aa51e10ac817a259114b5ece1a0f0f5', 'gaELRKAxdmznWzsg1wTojA==', '普通用户', 11, '2025-04-16 20:50:59', '2025-04-16 20:50:59');
INSERT INTO `users` VALUES (444, '陈子涵', '20221819402027', '3289434ed3e5f66ee72b9dd4af6f85ed2d9d50933d76eb5f822ffc3cdeeb80cc', 'GWnT2/nW4DOBiLOdhlLt8A==', '普通用户', 11, '2025-04-16 20:50:59', '2025-04-16 20:50:59');
INSERT INTO `users` VALUES (445, '王克锐', '20221819402028', '41185e366107763e1127280919b0803ec0bd9435846f212898e6616ad7c4fd0e', 'SCZaNqkarBpknGwf/UfAZg==', '普通用户', 11, '2025-04-16 20:50:59', '2025-04-16 20:50:59');
INSERT INTO `users` VALUES (446, '张照轩', '20221819402030', '24d6ec51d13064fd0c175a08e32abb78ce4050fdff632c75c6505406987b593b', 'Ct046jC3fx/J2yyTjPLJfQ==', '普通用户', 11, '2025-04-16 20:50:59', '2025-04-16 20:50:59');
INSERT INTO `users` VALUES (447, '李天琪', '20221819402031', 'be227f9f27855433899cd305150a5424655f5b130ed981ce8a1876d0ef1508ee', 'Vm5RMZJJzlh1IscYlQCVDw==', '普通用户', 11, '2025-04-16 20:50:59', '2025-04-16 20:50:59');
INSERT INTO `users` VALUES (448, '李佰桐', '20221819402032', '91142a19f947b4bbccc68ee69d8066071caa3c888403eb6ec11cb233d2e03695', 'tzkmMP67DO92zBhMM/qfwQ==', '普通用户', 11, '2025-04-16 20:50:59', '2025-04-16 20:50:59');
INSERT INTO `users` VALUES (449, '秦淑可', '20221819402033', 'd4b4caa8135e7bc3ef9a141f13c8ed28b4d286b7ad6622ca485066b7132a9737', 'rGX027oxXwYEcRlWE8GiNg==', '普通用户', 11, '2025-04-16 20:50:59', '2025-04-16 20:50:59');
INSERT INTO `users` VALUES (450, '邸美凤', '20221819402034', 'f163d2f6684d3942bf5dfecb489ba0dd9c6e94b83955434d957cef09f5ea9e30', 'q728FYuhp9neonRSlJxNMA==', '普通用户', 11, '2025-04-16 20:50:59', '2025-04-16 20:50:59');
INSERT INTO `users` VALUES (451, '许丽琪', '20221819402035', '9095746e2d4237bba7dd324479c1c68629dcdefb3e3a8889f71d87d67901f353', '2rpJawLP33flyu7W0WYGHA==', '普通用户', 11, '2025-04-16 20:50:59', '2025-04-16 20:50:59');
INSERT INTO `users` VALUES (469, '张玉哲', '20221819401001', '8a5e99536c7fe8286205fc8bd8fb0dcdf1478be0948aba6eed7e7a7d301d99e8', 'Vm7HAnarCVAG8pzgGw8vnw==', '普通用户', 11, '2025-04-16 20:53:54', '2025-04-16 20:53:54');
INSERT INTO `users` VALUES (470, '董天宇', '20221819401002', '7d5856304c3ddaaa602d3fa01727cd087b752049126395ec62682c2fc470ddde', '6CMY426Obrkp5SHDYsJ8AQ==', '普通用户', 11, '2025-04-16 20:53:54', '2025-04-16 20:53:54');
INSERT INTO `users` VALUES (471, '何天晔', '20221819401003', 'f9b45577028766558ba11d33f3e5c5c91e08a450c0502b42eebbe914f64b90b9', '2fQ1VFrYg6O7mP7hJIpCRA==', '普通用户', 11, '2025-04-16 20:53:54', '2025-04-16 20:53:54');
INSERT INTO `users` VALUES (472, '张慧', '20221819401005', '446750da9c5eba57f3fa8fadf8d8c1dc9bcb6c7ef5e993779e6539531faa0e45', 'mdrZ34u5c9sKcPklAcMjXg==', '普通用户', 11, '2025-04-16 20:53:54', '2025-04-16 20:53:54');
INSERT INTO `users` VALUES (473, '郭依林', '20221819401006', '2ab97f77216d049b9f3b6973de6add0df9b732ffbad19efdd5b9b07d366725dd', 'D+SqOOb0wtYDhm6gt811WQ==', '普通用户', 11, '2025-04-16 20:53:54', '2025-04-16 20:53:54');
INSERT INTO `users` VALUES (474, '刘航', '20221819401007', 'ca762f624d1d3a1c6c396f18c47c2b60e15d1a6032ea92891e0c74e778f85368', 'jnzQK126dcMmPARz530E+w==', '普通用户', 11, '2025-04-16 20:53:54', '2025-04-16 20:53:54');
INSERT INTO `users` VALUES (475, '刘宇康', '20221819401008', 'd8105875f55d79933f0e7e54e725021ee0439f83cc83a7111680122ffb8386ce', 'YZAJOBu4MQzl6yIn47T6AA==', '普通用户', 11, '2025-04-16 20:53:54', '2025-04-16 20:53:54');
INSERT INTO `users` VALUES (476, '张振宗', '20221819401009', '4a0fe1016d651fee55a51c2950d31286a17c0b50b13c0959539ac1b0bbafca47', '3Oi3L4gd1QOiGxpZQuZ+uA==', '普通用户', 11, '2025-04-16 20:53:54', '2025-04-16 20:53:54');
INSERT INTO `users` VALUES (477, '李豪', '20221819401010', 'c4251eb744c902e8d6ea7650f267c01ff20f3284546fd389693a75c9c2feb0cb', '9E+Ts5r52QPOLE4vooB6Kg==', '普通用户', 11, '2025-04-16 20:53:54', '2025-04-16 20:53:54');
INSERT INTO `users` VALUES (478, '黄宗杰', '20221819401011', '5576d88790ced22e3713d9932b0e07c0c4a82f634389bdde6fca80bb4447d403', 'gsXxJBwIAJg5WAzBsewNbA==', '普通用户', 11, '2025-04-16 20:53:54', '2025-04-16 20:53:54');
INSERT INTO `users` VALUES (479, '任佳旭', '20221819401012', 'aa68618771eacad0bf2d38936f25f2fd7d83bb78a3548c51a8ed98c772b3b73a', 'hUUNuqYlSIZe1BGk1XjHwQ==', '普通用户', 11, '2025-04-16 20:53:54', '2025-04-16 20:53:54');
INSERT INTO `users` VALUES (480, '戈壮', '20221819401013', 'f89945238537ab6bc3c8eabd0416a35a56514ddaa9e2779594dd1cd209f9013c', 'tr5P2SKE2WEg6suLFaOfCg==', '普通用户', 11, '2025-04-16 20:53:54', '2025-04-16 20:53:54');
INSERT INTO `users` VALUES (481, '康明天', '20221819401014', '49f7e096531c02876bb575b480c152848dc7a716c74f92f7a9da50fc435d9f79', 'pNetvJyDlk9hj1tkcFBLvg==', '普通用户', 11, '2025-04-16 20:53:54', '2025-04-16 20:53:54');
INSERT INTO `users` VALUES (482, '叶宁硕', '20221819401015', 'af894ad56a6eeb76fbe81e34e9a7cbcf33c00f37df5cdb7c32ec4ec14e43700d', 'xNRdYu1dVyst39PyR6BcrA==', '普通用户', 11, '2025-04-16 20:53:54', '2025-04-16 20:53:54');
INSERT INTO `users` VALUES (483, '陈宇鹏', '20221819401016', '9d8aa633e8f2f4d2c6d25117d52de4d5748107bc268887a9cf4118786dcfdf85', 'EocNLdqxeMZD/2h8J0si/Q==', '普通用户', 11, '2025-04-16 20:53:54', '2025-04-16 20:53:54');
INSERT INTO `users` VALUES (484, '杜国杰', '20221819401017', '62cba8e4d07f6a7723c47859bd30ae4c7828de6fd39d4df81bc5090df2c54900', 'ksHB1JZPDttpzl9DtmCFwQ==', '普通用户', 11, '2025-04-16 20:53:54', '2025-04-16 20:53:54');
INSERT INTO `users` VALUES (485, '李子政', '20221819401018', '9ad0115b6f0de0a76676b277b271bb4734580c10a2a41caa42ad228a0d659ccd', 'leLzKhYWcegH/cc1jqxMiw==', '普通用户', 11, '2025-04-16 20:53:54', '2025-04-16 20:53:54');
INSERT INTO `users` VALUES (487, '刘赛康', '20221819401019', 'd98d5c3ae1b1f11de7d03e115283145a0c368cdce9f93d8ceb6e4e4a9c2bb880', 'bweSB01YGE/wZRHWx+Vo/Q==', '普通用户', 11, '2025-04-16 21:11:49', '2025-04-16 21:11:49');

SET FOREIGN_KEY_CHECKS = 1;
