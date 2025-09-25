/*
 Navicat Premium Data Transfer

 Source Server         : demo
 Source Server Type    : MySQL
 Source Server Version : 80034 (8.0.34)
 Source Host           : localhost:3306
 Source Schema         : tai_exam

 Target Server Type    : MySQL
 Target Server Version : 80034 (8.0.34)
 File Encoding         : 65001

 Date: 25/09/2025 09:39:40
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
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of admindepartmentmapping
-- ----------------------------
INSERT INTO `admindepartmentmapping` VALUES (33, 1, 1, '2025-04-21 14:22:19');
INSERT INTO `admindepartmentmapping` VALUES (34, 1, 4, '2025-04-21 14:22:50');
INSERT INTO `admindepartmentmapping` VALUES (35, 1, 8, '2025-04-21 14:22:58');
INSERT INTO `admindepartmentmapping` VALUES (36, 1, 11, '2025-04-21 14:23:08');
INSERT INTO `admindepartmentmapping` VALUES (37, 1, 26, '2025-04-21 14:23:15');

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '分类id',
  `category_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  `parent_category_id` int NULL DEFAULT NULL COMMENT '上级分类id',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '分类描述',
  `category_level` tinyint NOT NULL COMMENT '分类等级',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更改时间',
  `created_by` int NULL DEFAULT NULL,
  `sort_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `parent_category_id`(`parent_category_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 40 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1, '教育', NULL, '<p>与教育相关的分类</p>', 1, '2024-11-20 19:06:45', '2025-04-20 10:36:28', 1, '1');
INSERT INTO `category` VALUES (2, '课程', 1, '课程相关的分类', 2, '2024-11-20 19:06:45', '2025-04-20 10:36:29', 1, '1');
INSERT INTO `category` VALUES (3, '活动', 1, '教育活动相关的分类', 2, '2024-11-20 19:06:45', '2025-04-20 10:36:29', 1, '1');
INSERT INTO `category` VALUES (4, '健康', NULL, '与健康相关的分类', 1, '2024-11-20 19:06:45', '2025-04-20 10:36:29', 1, '1');
INSERT INTO `category` VALUES (5, '饮食', 4, '饮食相关的分类', 2, '2024-11-20 19:06:45', '2025-04-20 10:36:30', 1, '1');
INSERT INTO `category` VALUES (6, '锻炼', 4, '锻炼相关的分类', 2, '2024-11-20 19:06:45', '2025-04-20 10:36:30', 1, '1');
INSERT INTO `category` VALUES (29, '基本信息', NULL, 'sadas', 1, '2025-04-20 11:44:51', '2025-06-26 08:33:21', 1, '1');
INSERT INTO `category` VALUES (30, '课程与学习内容', NULL, '', 1, '2025-04-20 11:44:55', '2025-06-26 08:33:21', 1, '2');
INSERT INTO `category` VALUES (31, '课程满意度与建议', NULL, '', 1, '2025-04-20 11:45:00', '2025-06-26 08:33:21', 1, '3');
INSERT INTO `category` VALUES (32, '个人发展与未来规划', NULL, '', 1, '2025-04-20 11:45:05', '2025-06-26 08:33:21', 1, '4');
INSERT INTO `category` VALUES (33, '其他建议与感受', NULL, '', 1, '2025-04-20 11:45:10', '2025-06-26 08:33:21', 1, '5');

-- ----------------------------
-- Table structure for department_exam
-- ----------------------------
DROP TABLE IF EXISTS `department_exam`;
CREATE TABLE `department_exam`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `department_id` int NULL DEFAULT NULL,
  `exam_id` int NULL DEFAULT NULL,
  `department_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `assign_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of department_exam
-- ----------------------------
INSERT INTO `department_exam` VALUES (17, 1, 102, '新注册用户聚集地', '2025-04-21 14:47:20');
INSERT INTO `department_exam` VALUES (18, 1, 1, '新注册用户聚集地', '2025-04-22 16:34:39');
INSERT INTO `department_exam` VALUES (19, 8, 102, '测试部', '2025-09-15 12:48:01');
INSERT INTO `department_exam` VALUES (20, 11, 1, '教学1班', '2025-09-23 19:32:19');

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
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of departments
-- ----------------------------
INSERT INTO `departments` VALUES (1, '新注册用户聚集地', '<p>新注册的用户默认部门</p>', '2024-11-20 19:23:31', '2025-04-16 20:49:48');
INSERT INTO `departments` VALUES (4, '市场部', '负责市场推广和销售支持', '2024-11-20 19:23:31', '2024-11-20 19:23:31');
INSERT INTO `departments` VALUES (8, '测试部', '测试人员', '2024-12-16 10:56:07', '2024-12-16 10:56:07');
INSERT INTO `departments` VALUES (11, '教学1班', '<p>教学1班</p>', '2024-12-19 15:28:23', '2025-03-27 20:41:19');
INSERT INTO `departments` VALUES (26, '123', '<p>21</p>', '2025-04-21 10:24:41', '2025-04-21 10:24:41');

-- ----------------------------
-- Table structure for exam
-- ----------------------------
DROP TABLE IF EXISTS `exam`;
CREATE TABLE `exam`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '试卷名称',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '试卷描述',
  `created_by` int NOT NULL COMMENT '创建者',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` int NULL DEFAULT NULL COMMENT '更新者',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `allow_view` tinyint(1) NULL DEFAULT 0 COMMENT '是否允许查看',
  `status` enum('草稿','已发布','关闭') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '草稿' COMMENT '试卷状态',
  `is_category` int NULL DEFAULT 0 COMMENT '试卷内是否按分类',
  `sort_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 117 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of exam
-- ----------------------------
INSERT INTO `exam` VALUES (1, '学生满意度调查', '<p>关于学生对课程和教师的满意度调查</p>', 1, '2024-11-20 19:05:30', NULL, '2025-09-11 16:46:01', 1, '草稿', 0, NULL);
INSERT INTO `exam` VALUES (6, '恋爱观调查问卷', '该问卷旨在调查大学生恋爱观', 1, '2024-11-22 22:31:32', 1, '2024-12-08 15:30:09', 1, '草稿', 0, NULL);
INSERT INTO `exam` VALUES (14, '用户体验-功能需求调查', '了解用户在使用平台过程中的体验与需求，优化产品功能。', 2, '2024-12-08 14:50:00', 1, '2024-12-16 16:14:36', 1, '草稿', 0, NULL);
INSERT INTO `exam` VALUES (21, '《Java Web 应用开发》调查问卷', '亲爱的同学：\n你好！为了全面了解大家在《Java Web 应用开发》课程中的学习情况，以便更好地优化教学内容和教学方法，特开展本次问卷调查。你的回答将对我们的教学改进工作提供重要依据，请你认真作答。感谢你抽出宝贵的时间！\n', 1, '2024-12-24 13:15:50', 1, '2025-09-15 17:23:43', 0, '草稿', 0, NULL);
INSERT INTO `exam` VALUES (33, '用户体验与功能需求调查', '<p>了解用户在使用平台过程中的体验与需求，优化产品功能。</p>', 1, '2025-04-16 21:30:32', NULL, '2025-04-21 14:23:53', 1, '草稿', 0, NULL);
INSERT INTO `exam` VALUES (34, '移动应用使用习惯与需求调查', '<p>了解用户在使用移动应用时的行为习惯和功能需求，为产品优化提供参考。</p>', 1, '2025-04-16 22:01:38', NULL, '2025-04-21 14:23:49', 1, '草稿', 0, NULL);
INSERT INTO `exam` VALUES (102, '大学课程与就业准备情况调研问卷', '123', 1, '2025-04-20 11:44:39', NULL, '2025-06-26 08:33:21', 1, '草稿', 1, '1');
INSERT INTO `exam` VALUES (116, '12312', '123', 1, '2025-05-19 14:14:01', NULL, '2025-09-09 16:57:53', 0, '草稿', 0, '1');

-- ----------------------------
-- Table structure for options
-- ----------------------------
DROP TABLE IF EXISTS `options`;
CREATE TABLE `options`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '选项id',
  `question_id` int NOT NULL COMMENT '所属问题id',
  `is_open` int NULL DEFAULT 0 COMMENT '是否是开放选项',
  `type` enum('行选项','列选项','填空') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '行选项' COMMENT '选项类型',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '\r\n选项描述',
  `sort_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '排序键值',
  `is_skip` int NULL DEFAULT 0 COMMENT '是否是跳转选项',
  `skip_to` int NULL DEFAULT 0 COMMENT '跳转至问题id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `question_id`(`question_id` ASC) USING BTREE,
  INDEX `skip_to`(`is_skip` ASC) USING BTREE,
  INDEX `options_ibfk_3`(`is_open` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 805 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of options
-- ----------------------------
INSERT INTO `options` VALUES (1, 1, 0, '行选项', '化学', '1', 0, 0);
INSERT INTO `options` VALUES (2, 1, 0, '行选项', '科学', '2', 0, 0);
INSERT INTO `options` VALUES (3, 1, 0, '行选项', '语言艺术', '3', 0, 0);
INSERT INTO `options` VALUES (4, 2, 0, '行选项', '参观校外活动', '1', 0, 0);
INSERT INTO `options` VALUES (5, 2, 0, '行选项', '科学实验', '2', 0, 0);
INSERT INTO `options` VALUES (6, 2, 0, '行选项', '社区服务', '3', 0, 0);
INSERT INTO `options` VALUES (7, 3, 0, '行选项', '非常重要', '1', 0, 0);
INSERT INTO `options` VALUES (8, 3, 0, '行选项', '重要', '2', 0, 0);
INSERT INTO `options` VALUES (13, 13, 0, '填空', 'null', '1', 0, 0);
INSERT INTO `options` VALUES (15, 14, 0, '列选项', '1', '1', 0, 0);
INSERT INTO `options` VALUES (16, 14, 0, '列选项', '2', '1', 0, 0);
INSERT INTO `options` VALUES (17, 14, 0, '列选项', '3', '1', 0, 0);
INSERT INTO `options` VALUES (18, 14, 0, '列选项', '4', '1', 0, 0);
INSERT INTO `options` VALUES (19, 14, 0, '列选项', '5', '1', 0, 0);
INSERT INTO `options` VALUES (20, 14, 0, '行选项', '界面设计', '1', 0, 0);
INSERT INTO `options` VALUES (21, 14, 0, '行选项', '功能丰富度', '1', 0, 0);
INSERT INTO `options` VALUES (22, 14, 0, '行选项', '使用流畅性', '1', 0, 0);
INSERT INTO `options` VALUES (23, 14, 0, '行选项', '广告干扰度', '1', 0, 0);
INSERT INTO `options` VALUES (24, 14, 0, '行选项', '安全与隐私保障', '1', 0, 0);
INSERT INTO `options` VALUES (25, 15, 0, '行选项', '数学', '1', 0, 0);
INSERT INTO `options` VALUES (26, 15, 0, '行选项', '英语', '1', 0, 0);
INSERT INTO `options` VALUES (27, 15, 0, '行选项', '计算机', '1', 0, 0);
INSERT INTO `options` VALUES (28, 15, 0, '行选项', '体育', '1', 0, 0);
INSERT INTO `options` VALUES (29, 15, 0, '列选项', '内容丰富', '1', 0, 0);
INSERT INTO `options` VALUES (30, 15, 0, '列选项', '教学质量高', '1', 0, 0);
INSERT INTO `options` VALUES (32, 15, 0, '列选项', '学生互动好', '1', 0, 0);
INSERT INTO `options` VALUES (34, 1, 0, '行选项', '英语', '1', 0, 0);
INSERT INTO `options` VALUES (40, 31, 0, '行选项', '18岁以下', '1', 1, 34);
INSERT INTO `options` VALUES (41, 20, 1, '行选项', '其他（请填写）', '100', 0, 0);
INSERT INTO `options` VALUES (42, 20, 0, '行选项', '北京', '1', 0, 0);
INSERT INTO `options` VALUES (43, 20, 0, '行选项', '上海', '1', 0, 0);
INSERT INTO `options` VALUES (44, 20, 0, '行选项', '重庆', '1', 0, 0);
INSERT INTO `options` VALUES (59, 24, 0, '行选项', '搜索功能', '1', 0, 0);
INSERT INTO `options` VALUES (60, 24, 0, '行选项', '个性化推荐', '1', 0, 0);
INSERT INTO `options` VALUES (61, 24, 0, '行选项', '物流跟踪', '1', 0, 0);
INSERT INTO `options` VALUES (62, 24, 0, '行选项', '客户服务', '1', 0, 0);
INSERT INTO `options` VALUES (63, 25, 0, '行选项', '小于18岁', '100', 1, 27);
INSERT INTO `options` VALUES (64, 25, 0, '行选项', '大于18岁', '1', 0, 0);
INSERT INTO `options` VALUES (65, 26, 0, '行选项', '没谈过', '1', 0, 0);
INSERT INTO `options` VALUES (66, 26, 0, '行选项', '谈过', '1', 0, 0);
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
INSERT INTO `options` VALUES (118, 26, 0, '行选项', '谈过几次', '1', 0, 0);
INSERT INTO `options` VALUES (119, 19, 0, '行选项', '23岁', '1', 0, 0);
INSERT INTO `options` VALUES (120, 19, 0, '行选项', '27岁', '1', 0, 0);
INSERT INTO `options` VALUES (121, 19, 0, '行选项', '30岁以后', '1', 0, 0);
INSERT INTO `options` VALUES (122, 19, 1, '行选项', '其他（请填写）', '1', 0, 0);
INSERT INTO `options` VALUES (123, 16, 0, '行选项', '9点', '1', 1, 15);
INSERT INTO `options` VALUES (124, 16, 0, '行选项', '10点', '1', 0, 0);
INSERT INTO `options` VALUES (125, 16, 0, '行选项', '12点', '1', 0, 0);
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
INSERT INTO `options` VALUES (160, 65, 0, '行选项', '18岁以下', NULL, 0, 0);
INSERT INTO `options` VALUES (161, 65, 0, '行选项', '18-24岁', NULL, 0, 0);
INSERT INTO `options` VALUES (162, 65, 0, '行选项', '25-34岁', NULL, 0, 0);
INSERT INTO `options` VALUES (167, 69, 1, '行选项', '其他（请填写）', '100', 0, 0);
INSERT INTO `options` VALUES (168, 69, 0, '行选项', '手机', NULL, 0, 0);
INSERT INTO `options` VALUES (169, 69, 0, '行选项', '平板电脑', NULL, 0, 0);
INSERT INTO `options` VALUES (170, 69, 0, '行选项', '电脑', NULL, 0, 0);
INSERT INTO `options` VALUES (171, 70, 0, '行选项', '搜索功能', NULL, 0, 0);
INSERT INTO `options` VALUES (172, 70, 0, '行选项', '个性化推荐', NULL, 0, 0);
INSERT INTO `options` VALUES (173, 70, 0, '行选项', '物流跟踪', NULL, 0, 0);
INSERT INTO `options` VALUES (174, 70, 0, '行选项', '支付方式', NULL, 0, 0);
INSERT INTO `options` VALUES (175, 70, 0, '行选项', '客户服务', NULL, 0, 0);
INSERT INTO `options` VALUES (176, 71, 0, '行选项', '不经常使用', '100', 1, 78);
INSERT INTO `options` VALUES (177, 71, 0, '行选项', '每天多次', NULL, 0, 0);
INSERT INTO `options` VALUES (178, 71, 0, '行选项', '每天一次', NULL, 0, 0);
INSERT INTO `options` VALUES (179, 71, 0, '行选项', '每周几次', NULL, 0, 0);
INSERT INTO `options` VALUES (180, 71, 0, '行选项', '每月几次', NULL, 0, 0);
INSERT INTO `options` VALUES (181, 73, 1, '行选项', '其他（请填写）', '100', 0, 0);
INSERT INTO `options` VALUES (182, 73, 0, '行选项', '社交媒体', NULL, 0, 0);
INSERT INTO `options` VALUES (183, 73, 0, '行选项', '视频娱乐', NULL, 0, 0);
INSERT INTO `options` VALUES (184, 73, 0, '行选项', '购物', NULL, 0, 0);
INSERT INTO `options` VALUES (185, 73, 0, '行选项', '学习教育', NULL, 0, 0);
INSERT INTO `options` VALUES (186, 73, 0, '行选项', '工作工具', NULL, 0, 0);
INSERT INTO `options` VALUES (187, 73, 0, '行选项', '健康与健身', NULL, 0, 0);
INSERT INTO `options` VALUES (188, 74, 0, '行选项', '离线使用', NULL, 0, 0);
INSERT INTO `options` VALUES (189, 74, 0, '行选项', '实时通知', NULL, 0, 0);
INSERT INTO `options` VALUES (190, 74, 0, '行选项', '个性化推荐', NULL, 0, 0);
INSERT INTO `options` VALUES (191, 74, 0, '行选项', '用户隐私安全', NULL, 0, 0);
INSERT INTO `options` VALUES (192, 74, 0, '行选项', '应用速度和流畅度', NULL, 0, 0);
INSERT INTO `options` VALUES (193, 74, 0, '列选项', '1', NULL, 0, 0);
INSERT INTO `options` VALUES (194, 74, 0, '列选项', '2', NULL, 0, 0);
INSERT INTO `options` VALUES (195, 74, 0, '列选项', '3', NULL, 0, 0);
INSERT INTO `options` VALUES (196, 74, 0, '列选项', '4', NULL, 0, 0);
INSERT INTO `options` VALUES (197, 74, 0, '列选项', '5', NULL, 0, 0);
INSERT INTO `options` VALUES (198, 75, 1, '行选项', '其他（请填写）', '100', 0, 0);
INSERT INTO `options` VALUES (199, 75, 0, '行选项', '无法连接服务器', NULL, 0, 0);
INSERT INTO `options` VALUES (200, 75, 0, '行选项', '操作繁琐，界面复杂', NULL, 0, 0);
INSERT INTO `options` VALUES (201, 75, 0, '行选项', '应用崩溃或闪退', NULL, 0, 0);
INSERT INTO `options` VALUES (202, 75, 0, '行选项', '广告过多', NULL, 0, 0);
INSERT INTO `options` VALUES (203, 75, 0, '行选项', '个人隐私泄露风险', NULL, 0, 0);
INSERT INTO `options` VALUES (204, 75, 0, '行选项', '没有遇到问题', NULL, 0, 0);
INSERT INTO `options` VALUES (205, 76, 0, '行选项', '广告太多，影响使用体验', NULL, 0, 0);
INSERT INTO `options` VALUES (206, 76, 0, '行选项', '广告数量适中，不影响使用', NULL, 0, 0);
INSERT INTO `options` VALUES (207, 76, 0, '行选项', '希望能减少广告，但理解其存在', NULL, 0, 0);
INSERT INTO `options` VALUES (208, 76, 0, '行选项', '完全不介意广告的存在', NULL, 0, 0);
INSERT INTO `options` VALUES (209, 77, 0, '行选项', '会', NULL, 0, 0);
INSERT INTO `options` VALUES (210, 77, 0, '行选项', '可能会', NULL, 0, 0);
INSERT INTO `options` VALUES (211, 77, 0, '行选项', '不会', NULL, 0, 0);
INSERT INTO `options` VALUES (212, 79, 0, '行选项', '离线使用', NULL, 0, 0);
INSERT INTO `options` VALUES (213, 79, 0, '行选项', '实时通知', NULL, 0, 0);
INSERT INTO `options` VALUES (214, 79, 0, '行选项', '广告策略', NULL, 0, 0);
INSERT INTO `options` VALUES (215, 79, 0, '行选项', '隐私安全保障', NULL, 0, 0);
INSERT INTO `options` VALUES (216, 79, 0, '行选项', '应用速度', NULL, 0, 0);
INSERT INTO `options` VALUES (218, 80, 0, '列选项', '1', NULL, 0, 0);
INSERT INTO `options` VALUES (219, 80, 0, '列选项', '2', NULL, 0, 0);
INSERT INTO `options` VALUES (220, 80, 0, '列选项', '3', NULL, 0, 0);
INSERT INTO `options` VALUES (221, 80, 0, '列选项', '4', NULL, 0, 0);
INSERT INTO `options` VALUES (222, 80, 0, '列选项', '5', NULL, 0, 0);
INSERT INTO `options` VALUES (223, 80, 0, '行选项', '界面设计', NULL, 0, 0);
INSERT INTO `options` VALUES (224, 80, 0, '行选项', '功能丰富度', NULL, 0, 0);
INSERT INTO `options` VALUES (225, 80, 0, '行选项', '使用流畅性', NULL, 0, 0);
INSERT INTO `options` VALUES (226, 80, 0, '行选项', '广告干扰度', NULL, 0, 0);
INSERT INTO `options` VALUES (227, 80, 0, '行选项', '安全与隐私保障', NULL, 0, 0);
INSERT INTO `options` VALUES (564, 211, 0, '行选项', '大一', NULL, 0, 0);
INSERT INTO `options` VALUES (565, 211, 0, '行选项', '大二', NULL, 0, 0);
INSERT INTO `options` VALUES (566, 211, 0, '行选项', '大三', NULL, 0, 0);
INSERT INTO `options` VALUES (567, 211, 0, '行选项', '大四', NULL, 0, 0);
INSERT INTO `options` VALUES (568, 211, 0, '行选项', '研究生', NULL, 0, 0);
INSERT INTO `options` VALUES (569, 213, 0, '行选项', '是', NULL, 0, 0);
INSERT INTO `options` VALUES (570, 213, 0, '行选项', '否', '100', 1, 220);
INSERT INTO `options` VALUES (581, 218, 0, '行选项', '帮助程度', NULL, 0, 0);
INSERT INTO `options` VALUES (582, 219, 0, '行选项', '专业核心课程（如数据结构、金融学等）', NULL, 0, 0);
INSERT INTO `options` VALUES (583, 219, 0, '行选项', '通识课程（如写作、沟通、英语）', NULL, 0, 0);
INSERT INTO `options` VALUES (584, 219, 0, '行选项', '实践类课程（如实验、项目实训）', NULL, 0, 0);
INSERT INTO `options` VALUES (585, 219, 0, '行选项', '软技能类课程（如团队合作、演讲）', NULL, 0, 0);
INSERT INTO `options` VALUES (586, 219, 1, '行选项', '其他（请填写）', '100', 0, 0);
INSERT INTO `options` VALUES (587, 220, 0, '行选项', '非常合理', NULL, 0, 0);
INSERT INTO `options` VALUES (588, 220, 0, '行选项', '基本合理', NULL, 0, 0);
INSERT INTO `options` VALUES (589, 220, 0, '行选项', '一般', NULL, 0, 0);
INSERT INTO `options` VALUES (590, 220, 0, '行选项', '不太合理', NULL, 0, 0);
INSERT INTO `options` VALUES (591, 220, 0, '行选项', '非常不合理', NULL, 0, 0);
INSERT INTO `options` VALUES (592, 221, 0, '行选项', '实习实践类课程', NULL, 0, 0);
INSERT INTO `options` VALUES (593, 221, 0, '行选项', '就业指导类课程', NULL, 0, 0);
INSERT INTO `options` VALUES (594, 221, 0, '行选项', '创业类课程', NULL, 0, 0);
INSERT INTO `options` VALUES (595, 221, 0, '行选项', '行业前沿课程', NULL, 0, 0);
INSERT INTO `options` VALUES (596, 221, 0, '行选项', '通识素养提升课程', NULL, 0, 0);
INSERT INTO `options` VALUES (597, 221, 0, '行选项', '无建议', NULL, 0, 0);
INSERT INTO `options` VALUES (598, 221, 1, '行选项', '其他（请填写）', '100', 0, 0);
INSERT INTO `options` VALUES (599, 222, 0, '行选项', '专业必修课', NULL, 0, 0);
INSERT INTO `options` VALUES (600, 222, 0, '列选项', '非常无用', NULL, 0, 0);
INSERT INTO `options` VALUES (601, 223, 0, '行选项', '数据分析与应用', NULL, 0, 0);
INSERT INTO `options` VALUES (602, 223, 0, '行选项', '创业与创新思维', NULL, 0, 0);
INSERT INTO `options` VALUES (603, 223, 0, '行选项', '职业发展指导', NULL, 0, 0);
INSERT INTO `options` VALUES (604, 223, 0, '行选项', '通识课程', NULL, 0, 0);
INSERT INTO `options` VALUES (605, 223, 0, '列选项', '增加课程数量', NULL, 0, 0);
INSERT INTO `options` VALUES (606, 223, 0, '列选项', '减少课程数量', NULL, 0, 0);
INSERT INTO `options` VALUES (607, 223, 0, '列选项', '保持不变', NULL, 0, 0);
INSERT INTO `options` VALUES (608, 224, 0, '行选项', '明确：有目标岗位和行业', NULL, 0, 0);
INSERT INTO `options` VALUES (609, 224, 0, '行选项', '模糊：仅有大致方向', NULL, 0, 0);
INSERT INTO `options` VALUES (610, 224, 0, '行选项', '暂无明确规划', NULL, 0, 0);
INSERT INTO `options` VALUES (611, 225, 0, '行选项', '直接就业', NULL, 0, 0);
INSERT INTO `options` VALUES (612, 225, 0, '行选项', '考研', NULL, 0, 0);
INSERT INTO `options` VALUES (613, 225, 0, '行选项', '出国深造', NULL, 0, 0);
INSERT INTO `options` VALUES (614, 225, 0, '行选项', '创业', NULL, 0, 0);
INSERT INTO `options` VALUES (615, 225, 0, '行选项', '暂未决定', NULL, 0, 0);
INSERT INTO `options` VALUES (616, 225, 1, '行选项', '其他（请填写）', '100', 0, 0);
INSERT INTO `options` VALUES (617, 226, 0, '行选项', '所学专业是否对口', NULL, 0, 0);
INSERT INTO `options` VALUES (618, 226, 0, '行选项', '企业知名度', NULL, 0, 0);
INSERT INTO `options` VALUES (619, 226, 0, '行选项', '薪资待遇', NULL, 0, 0);
INSERT INTO `options` VALUES (620, 226, 0, '行选项', '工作稳定性', NULL, 0, 0);
INSERT INTO `options` VALUES (621, 226, 0, '行选项', '自我兴趣匹配', NULL, 0, 0);
INSERT INTO `options` VALUES (622, 226, 0, '行选项', '城市/地区', NULL, 0, 0);
INSERT INTO `options` VALUES (623, 227, 0, '行选项', '是', '100', 1, 228);
INSERT INTO `options` VALUES (624, 227, 0, '行选项', '否', '100', 1, 229);
INSERT INTO `options` VALUES (625, 228, 0, '行选项', '报培训班/考证', NULL, 0, 0);
INSERT INTO `options` VALUES (626, 228, 0, '行选项', '参加实习', NULL, 0, 0);
INSERT INTO `options` VALUES (627, 228, 0, '行选项', '自学提升技能', NULL, 0, 0);
INSERT INTO `options` VALUES (628, 228, 0, '行选项', '参与竞赛或科研', NULL, 0, 0);
INSERT INTO `options` VALUES (629, 228, 0, '行选项', '咨询学长学姐', NULL, 0, 0);
INSERT INTO `options` VALUES (630, 228, 1, '行选项', '其他（请填写）', '100', 0, 0);
INSERT INTO `options` VALUES (631, 229, 0, '行选项', '不清楚方向', NULL, 0, 0);
INSERT INTO `options` VALUES (632, 229, 0, '行选项', '学业压力大', NULL, 0, 0);
INSERT INTO `options` VALUES (633, 229, 0, '行选项', '缺乏资源和支持', NULL, 0, 0);
INSERT INTO `options` VALUES (634, 229, 0, '行选项', '拖延', NULL, 0, 0);
INSERT INTO `options` VALUES (635, 229, 1, '行选项', '其他（请填写）', '100', 0, 0);
INSERT INTO `options` VALUES (636, 231, 0, '行选项', '总体评价', NULL, 0, 0);
INSERT INTO `options` VALUES (637, 232, 0, '行选项', '满意程度', NULL, 0, 0);
INSERT INTO `options` VALUES (710, 222, 0, '行选项', '专业选修课', NULL, 0, 0);
INSERT INTO `options` VALUES (711, 222, 0, '行选项', '实践类课程', NULL, 0, 0);
INSERT INTO `options` VALUES (712, 222, 0, '行选项', '通识课程', NULL, 0, 0);
INSERT INTO `options` VALUES (713, 222, 0, '行选项', '创新创业课程', NULL, 0, 0);
INSERT INTO `options` VALUES (714, 222, 0, '列选项', '无用', NULL, 0, 0);
INSERT INTO `options` VALUES (715, 222, 0, '列选项', '一般', NULL, 0, 0);
INSERT INTO `options` VALUES (716, 222, 0, '列选项', '有用', NULL, 0, 0);
INSERT INTO `options` VALUES (717, 222, 0, '列选项', '非常有用', NULL, 0, 0);

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限名称',
  `permission_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限码',
  `comment` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建试卷',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 43 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES (2, '查看试卷', 'exam:view', '<p>查看试卷列表和详情</p>', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (3, '创建问卷', 'exam:create', '<p>创建新试卷</p>', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (4, '编辑问卷', 'exam:edit', '<p>编辑问卷内容</p>', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (5, '删除问卷', 'exam:delete', '<p>删除试卷</p>', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (6, '发布问卷', 'exam:publish', '<p>发布试卷到部门</p>', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (7, '查看问卷统计', 'exam:statistics', '<p>查看问卷统计数据</p>', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (8, '查看用户', 'user:view', '查看用户列表和详情', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (9, '创建用户', 'user:create', '创建新用户', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (10, '编辑用户', 'user:edit', '编辑用户信息', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (11, '删除用户', 'user:delete', '删除用户', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (12, '导入用户', 'user:import', '批量导入用户', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (13, '导出用户', 'user:export', '导出用户列表', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (14, '查看部门', 'department:view', '查看部门列表和详情', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (15, '创建部门', 'department:create', '创建新部门', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (16, '编辑部门', 'department:edit', '编辑部门信息', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (17, '删除部门', 'department:delete', '删除部门', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (18, '查看角色', 'role:view', '查看角色列表和详情', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (19, '创建角色', 'role:create', '创建新角色', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (20, '编辑角色', 'role:edit', '编辑角色信息', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (21, '删除角色', 'role:delete', '删除角色', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (22, '分配角色', 'role:assign', '将角色分配给用户或部门', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (23, '查看权限', 'permission:view', '查看权限列表和详情', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (24, '管理权限', 'permission:manage', '管理权限分配', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (25, '查看分类', 'category:view', '查看分类列表和详情', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (26, '创建分类', 'category:create', '创建新分类', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (27, '编辑分类', 'category:edit', '编辑分类信息', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (28, '删除分类', 'category:delete', '删除分类', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (29, '查看问题', 'question:view', '查看问题列表和详情', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (30, '创建问题', 'question:create', '创建新问题', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (31, '编辑问题', 'question:edit', '编辑问题内容', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (32, '删除问题', 'question:delete', '删除问题', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (33, '查看选项', 'option:view', '查看选项列表和详情', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (34, '创建选项', 'option:create', '创建新选项', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (35, '编辑选项', 'option:edit', '编辑选项内容', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (36, '删除选项', 'option:delete', '删除选项', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (37, '查看响应', 'response:view', '查看问卷响应数据', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (38, '导出响应', 'response:export', '导出响应数据', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (39, '删除响应', 'response:delete', '删除响应数据', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (40, '查看个人资料', 'profile:view', '查看个人资料信息', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (41, '编辑个人资料', 'profile:edit', '编辑个人资料信息', '2025-09-08 15:25:54');
INSERT INTO `permission` VALUES (42, '修改密码', 'password:change', '修改登录密码', '2025-09-08 15:25:54');

-- ----------------------------
-- Table structure for questions
-- ----------------------------
DROP TABLE IF EXISTS `questions`;
CREATE TABLE `questions`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '问题id',
  `exam_id` int NOT NULL COMMENT '所属问卷id',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '问题描述',
  `type` enum('单选','多选','排序','填空','矩阵单选','矩阵多选','评分题','文件上传题') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '问题类型',
  `display_type` enum('下拉','单选框','复选框','滑动条','五角星') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '下拉' COMMENT '展示形式',
  `is_required` tinyint(1) NULL DEFAULT 0 COMMENT '是否必答',
  `is_open` tinyint(1) NULL DEFAULT 0 COMMENT '是否有开放答案',
  `instructions` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '排序题评分题说明',
  `sort_type` enum('拖拽排序','选择排序') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '拖拽排序' COMMENT '排序题排序方式',
  `min_selections` int NULL DEFAULT 0 COMMENT '必答多选最少选几项',
  `max_selections` int NULL DEFAULT 0 COMMENT '最多选几项',
  `primary_category_id` int NULL DEFAULT NULL COMMENT '所属分类',
  `is_skip` tinyint(1) NULL DEFAULT 0 COMMENT '是否是跳转题',
  `sort_order` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '排序字段',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `sort_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 300 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of questions
-- ----------------------------
INSERT INTO `questions` VALUES (1, 1, '你最喜欢的课程是什么', '单选', '单选框', 0, 1, NULL, NULL, 0, 0, 4, 0, NULL, '2024-11-20 19:09:42', '2025-04-19 22:50:57', '12');
INSERT INTO `questions` VALUES (2, 1, '你参加过哪些活动？', '多选', '复选框', 0, 0, NULL, NULL, 0, 3, 5, 0, NULL, '2024-11-20 19:09:42', '2025-04-20 16:42:38', '11');
INSERT INTO `questions` VALUES (3, 1, '你认为健康饮食的重要性如何？', '单选', '下拉', 1, 0, NULL, NULL, 0, 0, 6, 1, NULL, '2024-11-20 19:09:42', '2025-04-19 22:50:57', '13');
INSERT INTO `questions` VALUES (13, 1, '谈一谈你1000米跑步的心得', '填空', '下拉', 1, 0, NULL, NULL, 0, 0, 1, 0, NULL, '2024-11-27 19:26:30', '2025-04-19 22:50:57', '2');
INSERT INTO `questions` VALUES (14, 1, '您对移动应用的整体体验如何？', '矩阵单选', '下拉', 1, 0, NULL, NULL, 0, 0, 1, 0, NULL, '2024-11-28 19:44:18', '2025-04-19 22:50:57', '14');
INSERT INTO `questions` VALUES (15, 1, '请对以下课程在不同维度上的表现进行评价', '矩阵单选', '下拉', 1, 0, NULL, NULL, 0, 0, 1, 0, NULL, '2024-11-28 21:31:36', '2025-04-19 22:50:57', '15');
INSERT INTO `questions` VALUES (16, 1, '你做作业一般到晚上几点？', '单选', '下拉', 1, 1, NULL, NULL, 0, 0, 1, 1, NULL, '2024-11-30 22:42:16', '2025-09-24 11:14:13', '1');
INSERT INTO `questions` VALUES (19, 1, '你打算什么岁数结婚？', '单选', '下拉', 1, 1, NULL, NULL, 0, 0, 1, 0, NULL, '2024-11-30 23:51:19', '2025-04-19 22:50:57', '3');
INSERT INTO `questions` VALUES (20, 1, '你想去什么地方？', '多选', '下拉', 1, 1, NULL, NULL, 0, 1, 1, 0, NULL, '2024-12-02 11:04:51', '2025-04-19 22:50:57', '4');
INSERT INTO `questions` VALUES (24, 1, '请为以下功能的重要性打分', '评分题', '五角星', 1, 0, '', NULL, 0, 0, 1, 0, NULL, '2024-12-04 13:41:17', '2025-04-19 22:50:57', '5');
INSERT INTO `questions` VALUES (25, 1, '你目前的年龄是？', '单选', '下拉', 1, 0, NULL, NULL, 0, 0, 1, 1, NULL, '2024-12-04 20:41:41', '2025-04-19 22:50:57', '6');
INSERT INTO `questions` VALUES (26, 1, '你谈过恋爱吗？', '多选', '下拉', 1, 0, NULL, NULL, 0, 1, 1, 0, NULL, '2024-12-04 20:44:55', '2025-04-19 22:50:57', '7');
INSERT INTO `questions` VALUES (27, 1, '请说出一个你喜欢的动画片', '填空', '下拉', 1, 0, NULL, NULL, 0, 0, 1, 0, NULL, '2024-12-04 20:46:02', '2025-04-19 22:50:57', '8');
INSERT INTO `questions` VALUES (31, 14, '您的年龄段是？', '单选', '下拉', 1, 0, NULL, NULL, 0, 0, 1, 1, NULL, '2024-12-08 14:50:49', '2025-04-18 15:34:24', '1');
INSERT INTO `questions` VALUES (32, 14, '您最常使用以下哪种类型的电子产品？', '单选', '下拉', 1, 1, NULL, NULL, 0, 0, 1, 0, NULL, '2024-12-08 14:53:38', '2025-04-18 15:34:24', '1');
INSERT INTO `questions` VALUES (33, 14, '请按照您认为的重要性对以下功能进行排序？', '评分题', '下拉', 1, 0, NULL, NULL, 0, 0, 1, 0, NULL, '2024-12-08 14:55:51', '2025-04-18 15:34:25', '1');
INSERT INTO `questions` VALUES (34, 14, '您对以下平台功能的满意度如何？', '矩阵单选', '下拉', 1, 0, NULL, NULL, 0, 0, 1, 0, NULL, '2024-12-08 14:57:03', '2025-04-18 15:34:26', '1');
INSERT INTO `questions` VALUES (35, 14, '您是否遇到过以下问题？', '多选', '下拉', 0, 1, NULL, NULL, 0, 0, 1, 0, NULL, '2024-12-08 15:00:27', '2025-04-18 15:34:26', '1');
INSERT INTO `questions` VALUES (36, 14, '您对平台推荐的准确度满意吗？', '单选', '下拉', 1, 0, NULL, NULL, 0, 0, 1, 1, NULL, '2024-12-08 15:02:02', '2025-04-18 15:34:27', '1');
INSERT INTO `questions` VALUES (37, 14, '您最希望平台优化哪些功能？', '多选', '下拉', 0, 1, NULL, NULL, 0, 0, 1, 0, NULL, '2024-12-08 15:02:57', '2025-04-18 15:34:29', '1');
INSERT INTO `questions` VALUES (38, 14, '您在使用平台过程中遇到的错误截图。', '文件上传题', '下拉', 1, 0, NULL, NULL, 0, 0, 1, 0, NULL, '2024-12-08 15:04:16', '2025-04-18 15:34:30', '1');
INSERT INTO `questions` VALUES (39, 14, '您对平台的整体体验如何？', '矩阵多选', '下拉', 1, 0, NULL, NULL, 0, 0, 1, 0, NULL, '2024-12-08 15:05:05', '2025-04-18 15:34:30', '1');
INSERT INTO `questions` VALUES (40, 14, '请列出您对平台改进的其他建议或意见', '填空', '下拉', 0, 0, NULL, NULL, 0, 0, 1, 0, NULL, '2024-12-08 15:06:56', '2025-04-18 15:34:31', '1');
INSERT INTO `questions` VALUES (47, 21, '你是否能够熟练搭建 Java Web 应用开发环境？', '单选', '下拉', 1, 0, NULL, NULL, 0, 0, 1, 0, NULL, '2024-12-24 13:16:11', '2025-09-15 17:23:43', '1');
INSERT INTO `questions` VALUES (48, 21, '对于使用 Idea 开发 Java Web 应用程序项目，你的掌握程度如何？', '单选', '下拉', 1, 0, NULL, NULL, 0, 0, 1, 0, NULL, '2024-12-24 13:20:49', '2025-09-15 17:23:43', '2');
INSERT INTO `questions` VALUES (49, 21, '你对 JavaScript 基本语法与应用的掌握情况是：', '单选', '下拉', 1, 0, NULL, NULL, 0, 0, 1, 0, NULL, '2024-12-24 14:04:14', '2025-09-15 17:23:43', '3');
INSERT INTO `questions` VALUES (52, 21, '关于 JSP 基本语法与内置对象，你：', '单选', '下拉', 1, 0, NULL, NULL, 0, 0, 1, 0, NULL, '2024-12-24 14:13:59', '2025-09-15 17:23:43', '5');
INSERT INTO `questions` VALUES (53, 21, '你在编写与应用 Servlet 方面的能力如何？', '单选', '下拉', 1, 0, NULL, NULL, 0, 0, 1, 0, NULL, '2024-12-29 17:03:00', '2025-09-15 17:23:43', '6');
INSERT INTO `questions` VALUES (59, 1, '提交任意一张图片', '文件上传题', '下拉', 1, 0, NULL, NULL, 0, 0, 1, 0, NULL, '2025-04-01 17:23:35', '2025-04-19 22:50:57', '10');
INSERT INTO `questions` VALUES (65, 33, '您的年龄段是？', '单选', '下拉', 1, 0, NULL, NULL, 0, 0, 1, 0, NULL, '2025-04-16 21:42:10', '2025-04-18 15:34:36', '1');
INSERT INTO `questions` VALUES (69, 33, '您最常使用以下哪种类型的电子设备访问本平台？', '单选', '下拉', 1, 1, NULL, NULL, 0, 0, 0, 0, NULL, '2025-04-16 21:56:56', '2025-04-18 15:34:36', '1');
INSERT INTO `questions` VALUES (70, 33, '请按照您认为的重要性对以下功能进行评分', '评分题', '下拉', 1, 0, NULL, NULL, 0, 0, 0, 0, NULL, '2025-04-16 21:59:06', '2025-04-18 15:34:39', '1');
INSERT INTO `questions` VALUES (71, 34, '您使用移动应用的频率是？', '单选', '下拉', 1, 0, NULL, NULL, 0, 0, 0, 1, NULL, '2025-04-16 22:02:03', '2025-04-18 15:34:40', '1');
INSERT INTO `questions` VALUES (73, 34, '您最常使用的应用类别是？', '单选', '下拉', 1, 1, NULL, NULL, 0, 0, 0, 0, NULL, '2025-04-16 22:03:51', '2025-04-18 15:34:41', '1');
INSERT INTO `questions` VALUES (74, 34, '以下功能对您使用移动应用的重要性如何？', '矩阵单选', '下拉', 1, 0, NULL, NULL, 0, 0, 0, 0, NULL, '2025-04-16 22:05:48', '2025-04-18 15:34:41', '1');
INSERT INTO `questions` VALUES (75, 34, '您在使用应用过程中是否遇到过以下问题？', '多选', '下拉', 1, 1, NULL, NULL, 0, 0, 0, 0, NULL, '2025-04-16 22:09:11', '2025-04-18 15:34:42', '1');
INSERT INTO `questions` VALUES (76, 34, '您对应用的广告策略有何看法？', '单选', '下拉', 1, 0, NULL, NULL, 0, 0, 0, 0, NULL, '2025-04-16 22:10:37', '2025-04-18 15:34:42', '1');
INSERT INTO `questions` VALUES (77, 34, '如果应用推出会员订阅服务以去除广告，您是否会考虑订阅？', '单选', '下拉', 1, 0, NULL, NULL, 0, 0, 0, 0, NULL, '2025-04-16 22:11:04', '2025-04-18 15:34:46', '1');
INSERT INTO `questions` VALUES (78, 34, '请上传您使用某个移动应用时遇到的特定问题截图或相关文件', '文件上传题', '下拉', 1, 0, NULL, NULL, 0, 0, 0, 0, NULL, '2025-04-16 22:11:35', '2025-04-18 15:35:02', '1');
INSERT INTO `questions` VALUES (79, 1, '请为以下功能的满意度打分', '排序', '下拉', 1, 0, '', '拖拽排序', 0, 0, 1, 0, NULL, '2025-04-16 22:11:55', '2025-04-19 22:50:57', '9');
INSERT INTO `questions` VALUES (80, 34, '您对移动应用的整体体验如何？', '矩阵单选', '下拉', 1, 0, NULL, NULL, 0, 0, 0, 0, NULL, '2025-04-16 22:13:11', '2025-04-18 15:35:03', '1');
INSERT INTO `questions` VALUES (81, 34, '您认为移动应用未来最需要加强的功能是什么？', '填空', '下拉', 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, '2025-04-16 22:14:10', '2025-04-18 15:35:03', '1');
INSERT INTO `questions` VALUES (82, 34, '您是否愿意参与更多的用户调查？', '单选', '下拉', 1, 0, NULL, NULL, 0, 0, 0, 0, NULL, '2025-04-16 22:14:20', '2025-04-18 15:35:04', '1');
INSERT INTO `questions` VALUES (211, 102, '您的年级是？', '单选', NULL, 1, 0, NULL, NULL, 0, 0, 29, 0, NULL, '2025-04-20 11:45:44', '2025-06-26 08:33:21', '1');
INSERT INTO `questions` VALUES (212, 102, '您的专业方向是？', '填空', NULL, 1, 0, NULL, NULL, 0, 0, 29, 0, NULL, '2025-04-20 11:48:38', '2025-06-26 08:33:21', '19');
INSERT INTO `questions` VALUES (213, 102, '您目前是否正在或已经参加过实习？', '单选', NULL, 1, 0, NULL, NULL, 0, 0, 29, 1, NULL, '2025-04-20 11:48:38', '2025-06-26 08:33:21', '2');
INSERT INTO `questions` VALUES (218, 102, '您认为所学课程对实习/就业的帮助程度如何？', '评分题', '五角星', 1, 0, '评分题：1分=完全没有帮助，5分=非常有帮助', NULL, 0, 0, 30, 0, NULL, '2025-04-20 16:06:51', '2025-06-26 08:33:21', '10');
INSERT INTO `questions` VALUES (219, 102, '在实习过程中，您觉得哪些课程内容最有帮助？', '多选', NULL, 1, 1, NULL, NULL, 1, 3, 30, 0, NULL, '2025-04-20 16:06:51', '2025-06-26 08:33:21', '11');
INSERT INTO `questions` VALUES (220, 102, '您认为当前课程设置是否合理？', '单选', NULL, 1, 0, NULL, NULL, 0, 0, 30, 0, NULL, '2025-04-20 16:06:51', '2025-06-26 08:33:21', '12');
INSERT INTO `questions` VALUES (221, 102, '如果您认为课程设置存在问题，您希望增设哪些类型的课程？', '多选', NULL, 1, 1, NULL, NULL, 1, 7, 30, 0, NULL, '2025-04-20 16:06:51', '2025-06-26 08:33:21', '13');
INSERT INTO `questions` VALUES (222, 102, '请评价下列课程类型对您能力提升的实际作用', '矩阵单选', NULL, 1, 0, NULL, NULL, 0, 0, 31, 0, NULL, '2025-04-20 16:10:19', '2025-06-26 08:33:21', '14');
INSERT INTO `questions` VALUES (223, 102, '对于以下课程，您是否希望学校增加/减少开设？', '矩阵多选', NULL, 1, 0, NULL, NULL, 0, 0, 31, 0, NULL, '2025-04-20 16:10:19', '2025-06-26 08:33:21', '15');
INSERT INTO `questions` VALUES (224, 102, '您目前的职业规划是？', '单选', NULL, 1, 0, NULL, NULL, 0, 0, 32, 0, NULL, '2025-04-20 16:14:40', '2025-06-26 08:33:21', '3');
INSERT INTO `questions` VALUES (225, 102, '您计划毕业后选择哪种路径？', '多选', NULL, 1, 1, NULL, NULL, 1, 6, 32, 0, NULL, '2025-04-20 16:14:40', '2025-06-26 08:33:21', '4');
INSERT INTO `questions` VALUES (226, 102, '如果您计划就业，请按您认为的重要性对以下因素排序', '排序', NULL, 1, 0, '按照重要性对以下因素排序', '拖拽排序', 0, 0, 32, 0, NULL, '2025-04-20 16:14:40', '2025-06-26 08:33:21', '5');
INSERT INTO `questions` VALUES (227, 102, '您目前是否正在为未来目标做准备？', '单选', NULL, 1, 0, NULL, NULL, 0, 0, 32, 1, NULL, '2025-04-20 16:14:40', '2025-06-26 08:33:21', '6');
INSERT INTO `questions` VALUES (228, 102, '您当前的准备措施包括哪些？', '多选', NULL, 1, 1, NULL, NULL, 1, 6, 32, 0, NULL, '2025-04-20 16:14:40', '2025-06-26 08:33:21', '7');
INSERT INTO `questions` VALUES (229, 102, '您未做准备的原因是？', '多选', NULL, 1, 1, NULL, NULL, 1, 5, 32, 0, NULL, '2025-04-20 16:14:40', '2025-06-26 08:33:21', '8');
INSERT INTO `questions` VALUES (230, 102, ' 如果您愿意，欢迎上传您的个人简历、职业规划或学习/项目总结等材料，便于进一步分析学习准备情况', '文件上传题', NULL, 0, 0, NULL, NULL, 0, 0, 32, 0, NULL, '2025-04-20 16:14:40', '2025-06-26 08:33:21', '9');
INSERT INTO `questions` VALUES (231, 102, '您对大学课程安排的总体评价是？', '评分题', '五角星', 1, 0, '1分=很差，5分=很好', NULL, 0, 0, 33, 0, NULL, '2025-04-20 16:16:09', '2025-06-26 08:33:21', '16');
INSERT INTO `questions` VALUES (232, 102, '您对当前教学模式（如线下/线上、项目制、讨论课等）的满意程度是？', '评分题', '滑动条', 1, 0, '1分=很差，5分=很好', NULL, 0, 0, 33, 0, NULL, '2025-04-20 16:16:09', '2025-06-26 08:33:21', '17');
INSERT INTO `questions` VALUES (233, 102, '请简要写出您对学校课程设置或教学改进的建议', '填空', NULL, 1, 0, NULL, NULL, 0, 0, 33, 0, NULL, '2025-04-20 16:16:09', '2025-06-26 08:33:21', '18');
INSERT INTO `questions` VALUES (294, 0, '12312312', '单选', '五角星', 1, 0, NULL, '拖拽排序', 0, 0, 33, 0, NULL, '2025-09-23 18:49:03', '2025-09-23 18:49:03', '1');
INSERT INTO `questions` VALUES (295, 0, NULL, '单选', '五角星', 1, 0, NULL, '拖拽排序', 0, 0, 0, 0, NULL, '2025-09-24 11:08:09', '2025-09-24 11:08:09', '1');
INSERT INTO `questions` VALUES (296, 0, '', '单选', '五角星', 1, 0, NULL, '拖拽排序', 0, 0, 0, 0, NULL, '2025-09-24 11:08:54', '2025-09-24 11:08:54', '1');
INSERT INTO `questions` VALUES (297, 0, NULL, '单选', '五角星', 1, 0, NULL, '拖拽排序', 0, 0, 0, 0, NULL, '2025-09-24 11:11:41', '2025-09-24 11:11:41', '1');
INSERT INTO `questions` VALUES (298, 0, NULL, '单选', '五角星', 1, 0, NULL, '拖拽排序', 0, 0, 0, 0, NULL, '2025-09-24 11:12:08', '2025-09-24 11:12:08', '1');
INSERT INTO `questions` VALUES (299, 0, NULL, '单选', '五角星', 1, 0, NULL, '拖拽排序', 0, 0, 0, 0, NULL, '2025-09-24 11:13:46', '2025-09-24 11:13:46', '1');

-- ----------------------------
-- Table structure for responses
-- ----------------------------
DROP TABLE IF EXISTS `responses`;
CREATE TABLE `responses`  (
  `response_id` int NOT NULL AUTO_INCREMENT COMMENT '答卷id',
  `exam_id` int NOT NULL COMMENT '问卷id',
  `question_id` int NULL DEFAULT NULL COMMENT '问题id',
  `option_id` int NULL DEFAULT 0 COMMENT '选项id',
  `row_id` int NULL DEFAULT 0 COMMENT '行id',
  `column_id` int NULL DEFAULT 0 COMMENT '列id',
  `user_id` int NULL DEFAULT NULL COMMENT '用户id',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'ip地址',
  `response_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '文本内容',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `is_valid` tinyint NULL DEFAULT 0 COMMENT '是否有效',
  `file_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件路径',
  `sort_order` int NULL DEFAULT 1,
  PRIMARY KEY (`response_id`) USING BTREE,
  INDEX `survey_id`(`exam_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 98362 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of responses
-- ----------------------------
INSERT INTO `responses` VALUES (98267, 102, 211, 564, 0, 0, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98268, 102, 211, 565, 0, 0, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98269, 102, 211, 566, 0, 0, 1, '127.0.0.1', '', '2025-09-24 03:23:09', 1, NULL, 0);
INSERT INTO `responses` VALUES (98270, 102, 211, 567, 0, 0, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98271, 102, 211, 568, 0, 0, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98272, 102, 218, 581, 0, 0, 1, '127.0.0.1', '4', '2025-09-24 03:23:09', 1, NULL, 0);
INSERT INTO `responses` VALUES (98273, 102, 219, 582, 0, 0, 1, '127.0.0.1', '', '2025-09-24 03:23:09', 1, NULL, 0);
INSERT INTO `responses` VALUES (98274, 102, 219, 583, 0, 0, 1, '127.0.0.1', '', '2025-09-24 03:23:09', 1, NULL, 0);
INSERT INTO `responses` VALUES (98275, 102, 219, 584, 0, 0, 1, '127.0.0.1', '', '2025-09-24 03:23:09', 1, NULL, 0);
INSERT INTO `responses` VALUES (98276, 102, 219, 585, 0, 0, 1, '127.0.0.1', '', '2025-09-24 02:20:36', 0, NULL, 0);
INSERT INTO `responses` VALUES (98277, 102, 219, 586, 0, 0, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98278, 102, 220, 587, 0, 0, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98279, 102, 220, 588, 0, 0, 1, '127.0.0.1', '', '2025-09-24 03:23:09', 1, NULL, 0);
INSERT INTO `responses` VALUES (98280, 102, 220, 589, 0, 0, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98281, 102, 220, 590, 0, 0, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98282, 102, 220, 591, 0, 0, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98283, 102, 221, 592, 0, 0, 1, '127.0.0.1', '', '2025-09-24 03:23:09', 1, NULL, 0);
INSERT INTO `responses` VALUES (98284, 102, 221, 593, 0, 0, 1, '127.0.0.1', '', '2025-09-24 03:23:09', 1, NULL, 0);
INSERT INTO `responses` VALUES (98285, 102, 221, 594, 0, 0, 1, '127.0.0.1', '', '2025-09-24 03:23:09', 1, NULL, 0);
INSERT INTO `responses` VALUES (98286, 102, 221, 595, 0, 0, 1, '127.0.0.1', '', '2025-09-24 03:23:09', 1, NULL, 0);
INSERT INTO `responses` VALUES (98287, 102, 221, 596, 0, 0, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98288, 102, 221, 597, 0, 0, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98289, 102, 221, 598, 0, 0, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98290, 102, 222, 0, 599, 600, 1, '127.0.0.1', '', '2025-09-24 02:21:48', 0, NULL, 0);
INSERT INTO `responses` VALUES (98291, 102, 222, 0, 599, 714, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98292, 102, 222, 0, 599, 715, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98293, 102, 222, 0, 599, 716, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98294, 102, 222, 0, 599, 717, 1, '127.0.0.1', '', '2025-09-24 03:23:09', 1, NULL, 0);
INSERT INTO `responses` VALUES (98295, 102, 222, 0, 710, 600, 1, '127.0.0.1', '', '2025-09-24 03:23:09', 1, NULL, 0);
INSERT INTO `responses` VALUES (98296, 102, 222, 0, 710, 714, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98297, 102, 222, 0, 710, 715, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98298, 102, 222, 0, 710, 716, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98299, 102, 222, 0, 710, 717, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98300, 102, 222, 0, 711, 600, 1, '127.0.0.1', '', '2025-09-24 02:21:48', 0, NULL, 0);
INSERT INTO `responses` VALUES (98301, 102, 222, 0, 711, 714, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98302, 102, 222, 0, 711, 715, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98303, 102, 222, 0, 711, 716, 1, '127.0.0.1', '', '2025-09-24 03:23:09', 1, NULL, 0);
INSERT INTO `responses` VALUES (98304, 102, 222, 0, 711, 717, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98305, 102, 222, 0, 712, 600, 1, '127.0.0.1', '', '2025-09-24 02:21:48', 0, NULL, 0);
INSERT INTO `responses` VALUES (98306, 102, 222, 0, 712, 714, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98307, 102, 222, 0, 712, 715, 1, '127.0.0.1', '', '2025-09-24 03:23:09', 1, NULL, 0);
INSERT INTO `responses` VALUES (98308, 102, 222, 0, 712, 716, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98309, 102, 222, 0, 712, 717, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98310, 102, 222, 0, 713, 600, 1, '127.0.0.1', '', '2025-09-24 02:21:48', 0, NULL, 0);
INSERT INTO `responses` VALUES (98311, 102, 222, 0, 713, 714, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98312, 102, 222, 0, 713, 715, 1, '127.0.0.1', '', '2025-09-24 03:23:09', 1, NULL, 0);
INSERT INTO `responses` VALUES (98313, 102, 222, 0, 713, 716, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98314, 102, 222, 0, 713, 717, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98315, 102, 223, 0, 601, 605, 1, '127.0.0.1', '', '2025-09-24 03:23:09', 1, NULL, 0);
INSERT INTO `responses` VALUES (98316, 102, 223, 0, 601, 606, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98317, 102, 223, 0, 601, 607, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98318, 102, 223, 0, 602, 605, 1, '127.0.0.1', '', '2025-09-24 02:27:54', 0, NULL, 0);
INSERT INTO `responses` VALUES (98319, 102, 223, 0, 602, 606, 1, '127.0.0.1', '', '2025-09-24 03:23:09', 1, NULL, 0);
INSERT INTO `responses` VALUES (98320, 102, 223, 0, 602, 607, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98321, 102, 223, 0, 603, 605, 1, '127.0.0.1', '', '2025-09-24 03:23:09', 1, NULL, 0);
INSERT INTO `responses` VALUES (98322, 102, 223, 0, 603, 606, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98323, 102, 223, 0, 603, 607, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98324, 102, 223, 0, 604, 605, 1, '127.0.0.1', '', '2025-09-24 02:27:54', 0, NULL, 0);
INSERT INTO `responses` VALUES (98325, 102, 223, 0, 604, 606, 1, '127.0.0.1', '', '2025-09-24 03:23:09', 1, NULL, 0);
INSERT INTO `responses` VALUES (98326, 102, 223, 0, 604, 607, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98327, 102, 231, 636, 0, 0, 1, '127.0.0.1', '5', '2025-09-24 03:23:09', 1, NULL, 0);
INSERT INTO `responses` VALUES (98328, 102, 232, 637, 0, 0, 1, '127.0.0.1', '3', '2025-09-24 03:23:09', 1, NULL, 0);
INSERT INTO `responses` VALUES (98329, 102, 233, 0, 0, 0, 1, '127.0.0.1', '1231231', '2025-09-24 03:23:09', 1, NULL, 0);
INSERT INTO `responses` VALUES (98330, 102, 212, 0, 0, 0, 1, '127.0.0.1', '123123', '2025-09-24 03:23:09', 1, NULL, 0);
INSERT INTO `responses` VALUES (98331, 102, 213, 569, 0, 0, 1, '127.0.0.1', '', '2025-09-24 03:23:09', 1, NULL, 0);
INSERT INTO `responses` VALUES (98332, 102, 213, 570, 0, 0, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98333, 102, 224, 608, 0, 0, 1, '127.0.0.1', '', '2025-09-24 03:23:09', 1, NULL, 0);
INSERT INTO `responses` VALUES (98334, 102, 224, 609, 0, 0, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98335, 102, 224, 610, 0, 0, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98336, 102, 225, 611, 0, 0, 1, '127.0.0.1', '', '2025-09-24 03:23:09', 1, NULL, 0);
INSERT INTO `responses` VALUES (98337, 102, 225, 612, 0, 0, 1, '127.0.0.1', '', '2025-09-24 03:23:09', 1, NULL, 0);
INSERT INTO `responses` VALUES (98338, 102, 225, 613, 0, 0, 1, '127.0.0.1', '', '2025-09-24 03:23:09', 1, NULL, 0);
INSERT INTO `responses` VALUES (98339, 102, 225, 614, 0, 0, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98340, 102, 225, 615, 0, 0, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98341, 102, 225, 616, 0, 0, 1, '127.0.0.1', '12312', '2025-09-24 03:23:09', 1, NULL, 0);
INSERT INTO `responses` VALUES (98342, 102, 226, 617, 0, 0, 1, '127.0.0.1', '', '2025-09-24 03:23:09', 1, NULL, 2);
INSERT INTO `responses` VALUES (98343, 102, 226, 618, 0, 0, 1, '127.0.0.1', '', '2025-09-24 03:23:09', 1, NULL, 1);
INSERT INTO `responses` VALUES (98344, 102, 226, 619, 0, 0, 1, '127.0.0.1', '', '2025-09-24 03:23:09', 1, NULL, 3);
INSERT INTO `responses` VALUES (98345, 102, 226, 620, 0, 0, 1, '127.0.0.1', '', '2025-09-24 03:23:09', 1, NULL, 4);
INSERT INTO `responses` VALUES (98346, 102, 226, 621, 0, 0, 1, '127.0.0.1', '', '2025-09-24 03:23:09', 1, NULL, 5);
INSERT INTO `responses` VALUES (98347, 102, 226, 622, 0, 0, 1, '127.0.0.1', '', '2025-09-24 03:23:09', 1, NULL, 6);
INSERT INTO `responses` VALUES (98348, 102, 227, 623, 0, 0, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98349, 102, 227, 624, 0, 0, 1, '127.0.0.1', '', '2025-09-24 03:23:09', 1, NULL, 0);
INSERT INTO `responses` VALUES (98350, 102, 228, 625, 0, 0, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98351, 102, 228, 626, 0, 0, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98352, 102, 228, 627, 0, 0, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98353, 102, 228, 628, 0, 0, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98354, 102, 228, 629, 0, 0, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98355, 102, 228, 630, 0, 0, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98356, 102, 229, 631, 0, 0, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98357, 102, 229, 632, 0, 0, 1, '127.0.0.1', '', '2025-09-24 03:23:09', 1, NULL, 0);
INSERT INTO `responses` VALUES (98358, 102, 229, 633, 0, 0, 1, '127.0.0.1', '', '2025-09-24 03:23:09', 1, NULL, 0);
INSERT INTO `responses` VALUES (98359, 102, 229, 634, 0, 0, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98360, 102, 229, 635, 0, 0, 1, '127.0.0.1', '', '2025-09-23 12:02:19', 0, NULL, 1);
INSERT INTO `responses` VALUES (98361, 102, 230, 0, 0, 0, 1, '127.0.0.1', NULL, '2025-09-24 11:23:09', 1, '/uploads/1758684189429_question_230_六级成绩.png', 1);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色名称',
  `comment` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色介绍',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, '超级管理员', '最高权限');
INSERT INTO `role` VALUES (2, '管理员', '普通管理员');
INSERT INTO `role` VALUES (3, '普通用户', '普通用户');

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` int NULL DEFAULT NULL COMMENT '角色id',
  `permission_id` int NULL DEFAULT NULL COMMENT '权限id',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 278 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色权限关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role_permission
-- ----------------------------
INSERT INTO `role_permission` VALUES (2, 1, 1, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (3, 1, 2, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (4, 1, 3, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (5, 1, 4, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (6, 1, 42, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (7, 1, 41, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (8, 1, 40, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (9, 1, 39, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (10, 1, 38, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (11, 1, 37, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (12, 1, 36, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (13, 1, 35, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (14, 1, 34, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (15, 1, 33, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (16, 1, 32, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (17, 1, 31, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (18, 1, 29, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (19, 1, 30, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (20, 1, 28, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (21, 1, 27, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (22, 1, 26, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (23, 1, 25, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (24, 1, 24, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (25, 1, 23, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (26, 1, 22, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (27, 1, 21, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (28, 1, 20, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (29, 1, 19, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (30, 1, 17, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (31, 1, 18, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (32, 1, 16, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (33, 1, 15, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (34, 1, 14, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (35, 1, 13, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (36, 1, 12, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (37, 1, 11, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (38, 1, 10, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (39, 1, 9, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (40, 1, 8, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (41, 1, 7, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (42, 1, 6, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (43, 1, 5, '2025-09-08 15:31:37');
INSERT INTO `role_permission` VALUES (137, 3, 2, '2025-09-09 09:00:24');
INSERT INTO `role_permission` VALUES (138, 3, 8, '2025-09-09 09:00:24');
INSERT INTO `role_permission` VALUES (139, 3, 9, '2025-09-09 09:00:24');
INSERT INTO `role_permission` VALUES (245, 2, 2, '2025-09-09 09:41:09');
INSERT INTO `role_permission` VALUES (246, 2, 4, '2025-09-09 09:41:09');
INSERT INTO `role_permission` VALUES (247, 2, 3, '2025-09-09 09:41:09');
INSERT INTO `role_permission` VALUES (248, 2, 5, '2025-09-09 09:41:09');
INSERT INTO `role_permission` VALUES (249, 2, 6, '2025-09-09 09:41:09');
INSERT INTO `role_permission` VALUES (250, 2, 7, '2025-09-09 09:41:09');
INSERT INTO `role_permission` VALUES (251, 2, 8, '2025-09-09 09:41:09');
INSERT INTO `role_permission` VALUES (252, 2, 9, '2025-09-09 09:41:09');
INSERT INTO `role_permission` VALUES (253, 2, 10, '2025-09-09 09:41:09');
INSERT INTO `role_permission` VALUES (254, 2, 14, '2025-09-09 09:41:09');
INSERT INTO `role_permission` VALUES (255, 2, 15, '2025-09-09 09:41:09');
INSERT INTO `role_permission` VALUES (256, 2, 16, '2025-09-09 09:41:09');
INSERT INTO `role_permission` VALUES (257, 2, 17, '2025-09-09 09:41:09');
INSERT INTO `role_permission` VALUES (258, 2, 25, '2025-09-09 09:41:09');
INSERT INTO `role_permission` VALUES (259, 2, 26, '2025-09-09 09:41:09');
INSERT INTO `role_permission` VALUES (260, 2, 27, '2025-09-09 09:41:09');
INSERT INTO `role_permission` VALUES (261, 2, 28, '2025-09-09 09:41:09');
INSERT INTO `role_permission` VALUES (262, 2, 29, '2025-09-09 09:41:09');
INSERT INTO `role_permission` VALUES (263, 2, 30, '2025-09-09 09:41:09');
INSERT INTO `role_permission` VALUES (264, 2, 31, '2025-09-09 09:41:09');
INSERT INTO `role_permission` VALUES (265, 2, 32, '2025-09-09 09:41:09');
INSERT INTO `role_permission` VALUES (266, 2, 33, '2025-09-09 09:41:09');
INSERT INTO `role_permission` VALUES (267, 2, 34, '2025-09-09 09:41:09');
INSERT INTO `role_permission` VALUES (268, 2, 35, '2025-09-09 09:41:09');
INSERT INTO `role_permission` VALUES (269, 2, 36, '2025-09-09 09:41:09');
INSERT INTO `role_permission` VALUES (270, 2, 37, '2025-09-09 09:41:09');
INSERT INTO `role_permission` VALUES (271, 2, 38, '2025-09-09 09:41:09');
INSERT INTO `role_permission` VALUES (272, 2, 39, '2025-09-09 09:41:09');
INSERT INTO `role_permission` VALUES (273, 2, 40, '2025-09-09 09:41:09');
INSERT INTO `role_permission` VALUES (274, 2, 41, '2025-09-09 09:41:09');
INSERT INTO `role_permission` VALUES (275, 2, 42, '2025-09-09 09:41:09');
INSERT INTO `role_permission` VALUES (276, 2, 18, '2025-09-09 09:41:09');
INSERT INTO `role_permission` VALUES (277, 2, 11, '2025-09-09 09:41:09');

-- ----------------------------
-- Table structure for user_exam
-- ----------------------------
DROP TABLE IF EXISTS `user_exam`;
CREATE TABLE `user_exam`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '记录id',
  `user_id` int NOT NULL COMMENT '用户id',
  `exam_id` int NOT NULL COMMENT '问卷id',
  `status` enum('未查看','未完成','保存未提交','已完成') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '未完成' COMMENT '答卷情况',
  `assigned_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  `completed_at` timestamp NULL DEFAULT NULL COMMENT '完成时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_user_survey`(`user_id` ASC, `exam_id` ASC) USING BTREE,
  INDEX `survey_id`(`exam_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1651 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_exam
-- ----------------------------
INSERT INTO `user_exam` VALUES (636, 0, 102, '已完成', '2025-04-21 14:47:20', '2025-09-15 02:50:40');
INSERT INTO `user_exam` VALUES (637, 1, 102, '保存未提交', '2025-04-21 14:47:20', '2025-09-24 03:23:09');
INSERT INTO `user_exam` VALUES (638, 2, 102, '已完成', '2025-04-21 14:47:20', '2025-09-15 09:04:04');
INSERT INTO `user_exam` VALUES (1650, 1513, 1, '未完成', '2025-09-23 19:32:19', NULL);

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int NULL DEFAULT NULL COMMENT '关联用户表',
  `role_id` int NULL DEFAULT NULL COMMENT '关联角色表',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1068 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (1, 1, 1);
INSERT INTO `user_role` VALUES (2, 2, 2);
INSERT INTO `user_role` VALUES (65, 2, 2);
INSERT INTO `user_role` VALUES (66, 4, 3);
INSERT INTO `user_role` VALUES (1067, 1513, 3);

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
  `department_id` int NULL DEFAULT NULL COMMENT '部门id',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE,
  INDEX `fk_department`(`department_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1514 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, '管理员', 'admin', '142f52c8921e4c77752f253f00885151820ae6177d374bad0f249fae44253965', 'UFXMoHqTHJFdtgi3nprvEg==', 1, '2024-11-20 19:02:49', '2025-09-25 09:39:33');
INSERT INTO `users` VALUES (2, '刚子', '刚子', '28931cc17c5579db91c9824c02141e2873ab6d085a2d238c61693d66113f2f98', 'WZgwcK3GQG27CNUAk+gWZw==', 1, '2024-11-20 19:03:16', '2025-09-15 10:32:04');
INSERT INTO `users` VALUES (1513, '匿名用户', '匿名用户', 'null', 'null', 11, '2024-12-07 22:43:40', '2025-09-23 19:20:57');

SET FOREIGN_KEY_CHECKS = 1;
