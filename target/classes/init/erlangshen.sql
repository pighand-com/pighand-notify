 /*
 Navicat Premium Data Transfer

 Source Server         : 阿里云
 Source Server Type    : MySQL
 Source Server Version : 50723
 Source Host           : 123.56.228.68:3306
 Source Schema         : erlangshen

 Target Server Type    : MySQL
 Target Server Version : 50723
 File Encoding         : 65001

 Date: 19/10/2018 18:24:29
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for client
-- ----------------------------
DROP TABLE IF EXISTS `client`;
CREATE TABLE `client`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'client key',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'client名称',
  `created_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除 0未删除 1删除',
  `deleted_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_client_name`(`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '客户端表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of client
-- ----------------------------
INSERT INTO `client` VALUES ('erlangshen', '二郎神', 'erlangshen', 0, NULL);

-- ----------------------------
-- Table structure for client_mail
-- ----------------------------
DROP TABLE IF EXISTS `client_mail`;
CREATE TABLE `client_mail`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'client key',
  `client_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'client名称',
  `mail` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件',
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pwd` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户注册发送邮件密码',
  `smtp` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '注册邮件内容',
  `subject` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `text` varchar(21000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '注册短信内容',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_client_name`(`client_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '客户端表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of client_mail
-- ----------------------------
INSERT INTO `client_mail` VALUES (1, 'erlangshen', 'erlangshen@pighand.com', 'erlangshen@pighand.com', '98357E8CBE8AABC1B874FCB6A0D849F3FF0C3E0CCBFD85516F986E4B1EE9FCDE', 'smtp.qq.com', 'register', '欢迎注册二郎神', '欢迎注册二郎神用户系统，验证码为：${code}。');
INSERT INTO `client_mail` VALUES (2, 'erlangshen', 'erlangshen@pighand.com', 'erlangshen@pighand.com', '98357E8CBE8AABC1B874FCB6A0D849F3FF0C3E0CCBFD85516F986E4B1EE9FCDE', 'smtp.qq.com', 'retrieve', '二郎神找回密码', '您正在找回二郎神的密码，验证码为：${code}，请注意保持管，非本人操作请及时修改密码。');

-- ----------------------------
-- Table structure for client_phone
-- ----------------------------
DROP TABLE IF EXISTS `client_phone`;
CREATE TABLE `client_phone`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'client key',
  `client_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'client名称',
  `platform` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '短信平台',
  `ak` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sk` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户注册发送邮件密码',
  `sign` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '签名',
  `tmplate` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模板',
  `type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型',
  `text` varchar(70) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '短信内容',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_client_name`(`client_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '客户端表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of client_phone
-- ----------------------------
INSERT INTO `client_phone` VALUES (1, 'erlangshen', 'aliyun', 'LTAI88FcG9du3vXt', '6FA66B38ED088F7F5EFB61DD1CA261CC1C74E2ACAEE3F1121C2A4CF8BD20F8D7', '二郎神用户平台', 'SMS_131780161', 'retrieve', NULL);
INSERT INTO `client_phone` VALUES (2, 'erlangshen', 'aliyun', 'LTAI88FcG9du3vXt', '6FA66B38ED088F7F5EFB61DD1CA261CC1C74E2ACAEE3F1121C2A4CF8BD20F8D7', '二郎神用户平台', 'SMS_131815185', 'register', NULL);

-- ----------------------------
-- Table structure for client_security
-- ----------------------------
DROP TABLE IF EXISTS `client_security`;
CREATE TABLE `client_security`  (
  `client_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `is_check_place` tinyint(1) NULL DEFAULT 0 COMMENT '是否异地登陆检查 0不检查 1检查',
  `check_place_priority` int(1) NULL DEFAULT NULL COMMENT '通知优先级 0都通知 1手机优先 2邮件优先',
  `check_place_phone_type_id` int(11) NULL DEFAULT NULL COMMENT '异地登陆邮件通知类型',
  `check_place_mail_type_id` int(11) NULL DEFAULT NULL COMMENT '异地登陆手机通知类型',
  `is_check_platform` int(1) NULL DEFAULT 1 COMMENT '是否对登陆平台检查 0多平台多账号可同时登陆 1可以多平台登录，同一平台只能1个账号在线 2所有平台只能1个账号在线',
  `check_platform_type` int(1) NULL DEFAULT 0 COMMENT '登录冲突操作 0登出之前登陆的账号 1新登陆请求失败',
  `login_api` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录通知接口',
  PRIMARY KEY (`client_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for key
-- ----------------------------
DROP TABLE IF EXISTS `key`;
CREATE TABLE `key`  (
  `access` varbinary(255) NOT NULL,
  `secret` varbinary(255) NOT NULL,
  `client_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '1正常',
  `created_time` datetime(0) NOT NULL,
  PRIMARY KEY (`access`, `secret`) USING BTREE,
  INDEX `idx_key_ak_sk`(`access`, `secret`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for login_log
-- ----------------------------
DROP TABLE IF EXISTS `login_log`;
CREATE TABLE `login_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `ip` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录ip',
  `login_time` datetime(0) NOT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 156 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `client_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户端id',
  `pwd` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `username` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `mail` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `mail_verify` tinyint(1) NULL DEFAULT 0 COMMENT '邮箱验证 0未验证 1验证',
  `phone` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `phone_verify` tinyint(1) NULL DEFAULT 0 COMMENT '手机号码验证 0未验证 1验证',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_client_group_name`(`client_id`, `username`) USING BTREE,
  INDEX `idx_user_client_group_nikname`(`client_id`) USING BTREE,
  INDEX `idx_user_client_group_mail`(`client_id`, `mail`) USING BTREE,
  INDEX `idx_user_client_group_phone`(`client_id`, `phone`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('erlangshen', 'erlangshen', '21232F297A57A5A743894A0E4A801FC3', 'admin', 'admin', 1, NULL, NULL, 0);
INSERT INTO `user` VALUES ('guest', 'erlangshen', '84E0343A0486FF05530DF6C705C8BB4', 'guest', 'guest', 1, NULL, 0, 0);

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `source` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来源',
  `nickname` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `tel` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话',
  `qq` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'QQ',
  `weixin` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微信',
  `weibo` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '新浪微博',
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `sex` tinyint(1) NULL DEFAULT NULL COMMENT '性别 0女 1男',
  `idcard` varchar(18) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  `certification` int(1) NULL DEFAULT 0 COMMENT '实名认证 0未实名 1认证中 2认证失败 3认证成功',
  `certification_fail_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '实名认证失败原因',
  `province` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '省',
  `city` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '市',
  `area` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
  `created_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_client_group_nikname`(`nickname`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
