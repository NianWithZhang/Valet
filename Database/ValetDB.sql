/*
Navicat MySQL Data Transfer

Source Server         : localDB
Source Server Version : 50724
Source Host           : localhost:3306
Source Database       : ValetDB

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2019-01-04 14:59:30
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
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of ClothesTable
-- ----------------------------
INSERT INTO `ClothesTable` VALUES ('14', '1', '黏黏的小鹿纹', '37', '1', '3', '2019-01-01 14:08:18', '1');
INSERT INTO `ClothesTable` VALUES ('15', '1', 'taobao', '37', '1', '1', '2019-01-01 14:08:18', '1');
INSERT INTO `ClothesTable` VALUES ('16', '1', '黏黏的bbd', '37', '2', '5', '2019-01-01 14:08:18', '1');
INSERT INTO `ClothesTable` VALUES ('17', '1', '一件新衣服1', '20', '1', '5', '2019-01-01 14:08:18', '1');
INSERT INTO `ClothesTable` VALUES ('18', '1', '一件新衣服2', '10', '0', '5', '2019-01-01 14:08:18', '1');
INSERT INTO `ClothesTable` VALUES ('19', '2', '一件新衣服3', '0', '0', '5', '2019-01-01 14:08:18', '1');
INSERT INTO `ClothesTable` VALUES ('20', '1', 'aaa', '0', '0', '5', '2019-01-01 14:08:18', '1');
INSERT INTO `ClothesTable` VALUES ('21', '1', '琪亚娜1', '0', '1', '5', '0001-01-01 00:00:00', '0');
INSERT INTO `ClothesTable` VALUES ('28', '1', 'taobaoClothes', '352', '1', '5', '0001-01-01 00:00:00', '0');
INSERT INTO `ClothesTable` VALUES ('29', '1', 'taobaoClothes', '45', '1', '5', '0001-01-01 00:00:00', '0');
INSERT INTO `ClothesTable` VALUES ('30', '1', 'taobaoClothes', '235', '1', '5', '0001-01-01 00:00:00', '0');
INSERT INTO `ClothesTable` VALUES ('32', '2', 'taobaoClothes1', '231', '1', '5', '0001-01-01 00:00:00', '0');
INSERT INTO `ClothesTable` VALUES ('34', '2', 'kumiko', '56', '1', '5', '0001-01-01 00:00:00', '0');
INSERT INTO `ClothesTable` VALUES ('35', '2', 'reina', '30', '1', '5', '0001-01-01 00:00:00', '0');
INSERT INTO `ClothesTable` VALUES ('36', '1', '电吹风', '22', '0', '5', '0001-01-01 00:00:00', '0');
INSERT INTO `ClothesTable` VALUES ('37', '1', '挥挥', '43', '0', '5', '0001-01-01 00:00:00', '0');
INSERT INTO `ClothesTable` VALUES ('38', '1', '挥挥', '43', '0', '5', '0001-01-01 00:00:00', '0');
INSERT INTO `ClothesTable` VALUES ('39', '11', 'emmm', '250', '1', '7', '0001-01-01 00:00:00', '0');
INSERT INTO `ClothesTable` VALUES ('40', '1', '呃呃', '4', '1', '2', '0001-01-01 00:00:00', '0');
INSERT INTO `ClothesTable` VALUES ('41', '1', '呃呃', '4', '1', '2', '0001-01-01 00:00:00', '0');
INSERT INTO `ClothesTable` VALUES ('42', '1', '呃呃', '4', '1', '2', '0001-01-01 00:00:00', '0');
INSERT INTO `ClothesTable` VALUES ('43', '1', '呃呃', '4', '1', '2', '0001-01-01 00:00:00', '0');
INSERT INTO `ClothesTable` VALUES ('44', '1', 'hi', '22', '0', '5', '0001-01-01 00:00:00', '0');
INSERT INTO `ClothesTable` VALUES ('45', '1', 'hi', '22', '0', '5', '0001-01-01 00:00:00', '0');
INSERT INTO `ClothesTable` VALUES ('46', '1', 'hi', '22', '0', '5', '0001-01-01 00:00:00', '0');
INSERT INTO `ClothesTable` VALUES ('47', '1', 'hi', '22', '0', '5', '0001-01-01 00:00:00', '0');
INSERT INTO `ClothesTable` VALUES ('48', '1', '马鸭', '150', '0', '7', '0001-01-01 00:00:00', '0');
INSERT INTO `ClothesTable` VALUES ('49', '1', 'qqq', '43', '5', '3', '0001-01-01 00:00:00', '0');

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
-- Records of ClothesTable_SuitTable
-- ----------------------------
INSERT INTO `ClothesTable_SuitTable` VALUES ('15', '24');
INSERT INTO `ClothesTable_SuitTable` VALUES ('16', '24');
INSERT INTO `ClothesTable_SuitTable` VALUES ('21', '24');
INSERT INTO `ClothesTable_SuitTable` VALUES ('28', '24');
INSERT INTO `ClothesTable_SuitTable` VALUES ('29', '24');
INSERT INTO `ClothesTable_SuitTable` VALUES ('30', '24');
INSERT INTO `ClothesTable_SuitTable` VALUES ('14', '31');
INSERT INTO `ClothesTable_SuitTable` VALUES ('15', '31');
INSERT INTO `ClothesTable_SuitTable` VALUES ('14', '32');
INSERT INTO `ClothesTable_SuitTable` VALUES ('15', '32');
INSERT INTO `ClothesTable_SuitTable` VALUES ('16', '32');

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
-- Records of SuitTable
-- ----------------------------
INSERT INTO `SuitTable` VALUES ('2', '1', '一套新穿搭2', '8', '0', '2000-01-01 00:00:00');
INSERT INTO `SuitTable` VALUES ('24', '1', 'emmmm', '2', '0', '0001-01-01 00:00:00');
INSERT INTO `SuitTable` VALUES ('28', '1', '很好', '25', '0', '0001-01-01 00:00:00');
INSERT INTO `SuitTable` VALUES ('29', '1', '很好', '25', '0', '0001-01-01 00:00:00');
INSERT INTO `SuitTable` VALUES ('30', '1', '很好', '25', '0', '0001-01-01 00:00:00');
INSERT INTO `SuitTable` VALUES ('31', '1', '好了', '30', '0', '0001-01-01 00:00:00');
INSERT INTO `SuitTable` VALUES ('32', '1', 'uu', '25', '0', '0001-01-01 00:00:00');

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
-- Records of UserTable
-- ----------------------------
INSERT INTO `UserTable` VALUES ('1', '2', null, null);
INSERT INTO `UserTable` VALUES ('12', '2', 'https://item.taobao.com/item.htm/?id=531554484426', '//g-search2.alicdn.com/img/bao/uploaded/i4/TB2vBYioFXXXXXDXXXXXXXXXXXX_!!54349678.jpg');
INSERT INTO `UserTable` VALUES ('123', '123123', 'https://item.taobao.com/item.htm/?id=525923826991', '//g-search3.alicdn.com/img/bao/uploaded/i4/TB11dkcLXXXXXbSaXXXYXGcGpXX_M2.SS2');
INSERT INTO `UserTable` VALUES ('1234', '123123', 'https://item.taobao.com/item.htm/?id=525923826991', '//g-search3.alicdn.com/img/bao/uploaded/i4/TB11dkcLXXXXXbSaXXXYXGcGpXX_M2.SS2');
INSERT INTO `UserTable` VALUES ('233', '123456', 'https://item.taobao.com/item.htm/?id=525923826991', '//g-search3.alicdn.com/img/bao/uploaded/i4/TB11dkcLXXXXXbSaXXXYXGcGpXX_M2.SS2');
INSERT INTO `UserTable` VALUES ('hi', '123', 'https://item.taobao.com/item.htm/?id=525923826991', '//g-search3.alicdn.com/img/bao/uploaded/i4/TB11dkcLXXXXXbSaXXXYXGcGpXX_M2.SS2');
INSERT INTO `UserTable` VALUES ('nian', '123', null, null);
INSERT INTO `UserTable` VALUES ('test', 'test', null, null);
INSERT INTO `UserTable` VALUES ('你好', '1234abcd', 'https://item.taobao.com/item.htm/?id=525923826991', '//g-search3.alicdn.com/img/bao/uploaded/i4/TB11dkcLXXXXXbSaXXXYXGcGpXX_M2.SS2');
INSERT INTO `UserTable` VALUES ('诶嘿', '1234abcd', 'https://item.taobao.com/item.htm/?id=525923826991', '//g-search3.alicdn.com/img/bao/uploaded/i4/TB11dkcLXXXXXbSaXXXYXGcGpXX_M2.SS2');

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
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of WardrobeTable
-- ----------------------------
INSERT INTO `WardrobeTable` VALUES ('1', '1', '黏黏的衣橱', '2019-01-01 14:08:18');
INSERT INTO `WardrobeTable` VALUES ('2', '1', '智障的衣橱', '2000-01-01 00:00:00');
INSERT INTO `WardrobeTable` VALUES ('3', '1', '一个新衣橱1', '2000-01-01 00:00:00');
INSERT INTO `WardrobeTable` VALUES ('4', '1', '一个新衣橱2', '2000-01-01 00:00:00');
INSERT INTO `WardrobeTable` VALUES ('5', '1', '一个新衣橱3', '2000-01-01 00:00:00');
INSERT INTO `WardrobeTable` VALUES ('6', '1', '一个新衣橱4', '2000-01-01 00:00:00');
INSERT INTO `WardrobeTable` VALUES ('7', '1', '一个新衣橱5', '2000-01-01 00:00:00');
INSERT INTO `WardrobeTable` VALUES ('8', '1', '一个新衣橱6', '2000-01-01 00:00:00');
INSERT INTO `WardrobeTable` VALUES ('9', '1', '一个新衣橱7', '2000-01-01 00:00:00');
INSERT INTO `WardrobeTable` VALUES ('11', 'nian', 'hello', '0001-01-01 00:00:00');
