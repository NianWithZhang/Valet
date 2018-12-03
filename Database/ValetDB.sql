/*
Navicat MySQL Data Transfer

Source Server         : testConn
Source Server Version : 50724
Source Host           : inspring.xyz:3306
Source Database       : ValetDB

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2018-11-29 01:15:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for UserTable
-- ----------------------------
DROP TABLE IF EXISTS `UserTable`;
CREATE TABLE `UserTable` (
  `id` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of UserTable
-- ----------------------------
INSERT INTO `UserTable` VALUES ('1', '2');
INSERT INTO `UserTable` VALUES ('hi', '123');
INSERT INTO `UserTable` VALUES ('test', 'test');

-- ----------------------------
-- Table structure for WardrobeTable
-- ----------------------------
DROP TABLE IF EXISTS `WardrobeTable`;
CREATE TABLE `WardrobeTable` (
  `user_id` varchar(20) NOT NULL,
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `User_Wardrobe` (`user_id`),
  CONSTRAINT `User_Wardrobe` FOREIGN KEY (`user_id`) REFERENCES `UserTable` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of WardrobeTable
-- ----------------------------
