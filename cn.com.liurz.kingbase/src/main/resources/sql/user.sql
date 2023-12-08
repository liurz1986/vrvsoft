/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.120.201
 Source Server Type    : MySQL
 Source Server Version : 100522
 Source Host           : 192.168.120.201:3306
 Source Schema         : ajb_vap

 Target Server Type    : MySQL
 Target Server Version : 100522
 File Encoding         : 65001

 Date: 13/11/2023 20:45:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `cty_id` int(11) DEFAULT NULL,
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '用户名称',
  `account` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '用户用到登录的帐号名',
  `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '密码',
  `role_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '3',
  `idcard` varchar(18) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `phone` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `email` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `status` tinyint(2) NOT NULL DEFAULT 0,
  `org_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `org_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '',
  `province` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '',
  `city` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '',
  `is_leader` tinyint(4) DEFAULT 0,
  `lastpwdupdatetime` datetime(0) DEFAULT NULL COMMENT '最后一次密码修改时间',
  `lastlogintime` datetime(0) DEFAULT NULL COMMENT '最后一次登陆时间',
  `logintimes` int(11) DEFAULT 0 COMMENT '尝试登陆次数',
  `creator` int(11) DEFAULT NULL COMMENT '创建人',
  `domain_code` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `domain_name` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `authority_type` tinyint(4) DEFAULT NULL COMMENT '安全域数据权限控制 0 关闭，1开启',
  `pwd_status` tinyint(4) DEFAULT 0 COMMENT '0 默认密码未修改，1 表示已修改',
  `salt` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '密码盐',
  `guid` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `person_id` int(11) DEFAULT NULL COMMENT '人员id',
  `org_id` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '用户管理区域编码',
  `ip_login` tinyint(4) DEFAULT 0 COMMENT '是否开启ip登录 0:否，1:是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 120000 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (31, NULL, '安全保密管理员', 'secadm', 'f8705cb0cb84bd6277c1499d0eae4d74ae2db39c2af9851bdfe4f12206d61ba5', '5', '000000000000000000', '18888888888', '', 0, '000000000000', '**保密办', '01', '0100', 0, '2023-11-13 09:22:41', '2021-08-17 06:51:45', 1, 33, 'bc19b8c2-034e-4e8f-8480-2eb7ffb518bd', '安全单位', 0, 1, 'salt', '', NULL, NULL, 0);
INSERT INTO `user` VALUES (34, NULL, '系统管理员', 'sysadm', 'f8705cb0cb84bd6277c1499d0eae4d74ae2db39c2af9851bdfe4f12206d61ba5', '3', '000000000000000000', '18888888888', '', 0, '000000000000', '**保密办', '01', '0100', 0, '2023-11-13 09:22:51', '2019-10-28 16:03:33', 0, 33, 'bc19b8c2-034e-4e8f-8480-2eb7ffb518bd', '安全单位', 0, 1, 'salt', '', NULL, NULL, 0);
INSERT INTO `user` VALUES (52, NULL, '安全审计员', 'auditadm', 'f8705cb0cb84bd6277c1499d0eae4d74ae2db39c2af9851bdfe4f12206d61ba5', '4', '000000000000000000', '18888888888', '', 0, '000000000000', '**保密办', '01', '0100', 0, '2023-11-13 09:22:58', '2019-10-28 16:03:33', 1, 33, 'bc19b8c2-034e-4e8f-8480-2eb7ffb518bd', '安全单位', 0, 1, 'salt', '', NULL, NULL, 0);
INSERT INTO `user` VALUES (53, NULL, '保密主管', 'secretmgr', 'f8705cb0cb84bd6277c1499d0eae4d74ae2db39c2af9851bdfe4f12206d61ba5', '6', '000000000000000000', '', '', 0, '000000000000', '**保密办', '', '', 0, '2023-11-13 09:23:12', '2021-01-05 11:08:35', 0, 31, '', '', 0, 1, 'salt', NULL, NULL, NULL, 0);
INSERT INTO `user` VALUES (55, NULL, '信息化主管', 'operationmgr', 'f8705cb0cb84bd6277c1499d0eae4d74ae2db39c2af9851bdfe4f12206d61ba5', '8', '000000000000000000', '', '', 0, '000000000000', '**保密办', '', '', 0, '2023-11-13 09:23:05', '2021-01-05 11:08:35', 0, 31, '', '', 0, 1, 'salt', NULL, NULL, NULL, 0);

SET FOREIGN_KEY_CHECKS = 1;
