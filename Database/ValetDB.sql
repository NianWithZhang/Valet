/*
Navicat MySQL Data Transfer

Source Server         : localDB
Source Server Version : 50724
Source Host           : 127.0.0.1:3306
Source Database       : ValetDB

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2019-01-07 15:14:41
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for ClothesTable
-- ----------------------------
DROP TABLE IF EXISTS `ClothesTable`;
CREATE TABLE `ClothesTable` (
  `id` int(15) NOT NULL AUTO_INCREMENT,
  `wardrobe_id` int(15) NOT NULL,
  `name` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '一件新衣服',
  `color` double(10,0) DEFAULT '0',
  `type` int(5) DEFAULT '0',
  `thickness` int(5) DEFAULT '5',
  `last_wearing_time` datetime DEFAULT '2000-01-01 00:00:00',
  `wearing_frequency` int(10) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `item_wardrobe` (`wardrobe_id`) USING BTREE,
  CONSTRAINT `item_wardrobe` FOREIGN KEY (`wardrobe_id`) REFERENCES `WardrobeTable` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for ClothesTable_SuitTable
-- ----------------------------
DROP TABLE IF EXISTS `ClothesTable_SuitTable`;
CREATE TABLE `ClothesTable_SuitTable` (
  `clothes_id` int(15) NOT NULL,
  `suit_id` int(15) NOT NULL,
  PRIMARY KEY (`clothes_id`,`suit_id`),
  KEY `suit` (`suit_id`),
  CONSTRAINT `clothes` FOREIGN KEY (`clothes_id`) REFERENCES `ClothesTable` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `suit` FOREIGN KEY (`suit_id`) REFERENCES `SuitTable` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for SuitTable
-- ----------------------------
DROP TABLE IF EXISTS `SuitTable`;
CREATE TABLE `SuitTable` (
  `id` int(15) NOT NULL AUTO_INCREMENT,
  `wardrobe_id` int(15) NOT NULL,
  `name` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '一套新穿搭',
  `warmth_degree` double(20,0) DEFAULT NULL,
  `wearing_frequency` int(10) DEFAULT '0',
  `last_wearing_time` datetime DEFAULT '2000-01-01 00:00:00',
  PRIMARY KEY (`id`),
  KEY `wardrobe` (`wardrobe_id`),
  CONSTRAINT `wardrobe` FOREIGN KEY (`wardrobe_id`) REFERENCES `WardrobeTable` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for UserTable
-- ----------------------------
DROP TABLE IF EXISTS `UserTable`;
CREATE TABLE `UserTable` (
  `id` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  `recommend_item_url` varchar(128) DEFAULT NULL,
  `recommend_item_pic_url` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for WardrobeTable
-- ----------------------------
DROP TABLE IF EXISTS `WardrobeTable`;
CREATE TABLE `WardrobeTable` (
  `id` int(15) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(20) NOT NULL,
  `name` varchar(20) DEFAULT '一个新衣橱',
  `last_used_time` datetime DEFAULT '2000-01-01 00:00:00',
  PRIMARY KEY (`id`),
  KEY `User_Wardrobe` (`user_id`),
  CONSTRAINT `User_Wardrobe` FOREIGN KEY (`user_id`) REFERENCES `UserTable` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
