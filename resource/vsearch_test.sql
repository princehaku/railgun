-- phpMyAdmin SQL Dump
-- version 3.4.6
-- http://www.phpmyadmin.net
--
-- 主机: localhost
-- 生成日期: 2012 年 05 月 15 日 11:33
-- 服务器版本: 5.5.17
-- PHP 版本: 5.3.9-ZS5.6.0

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";



/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

DROP DATABASE IF EXISTS `railgun`;
CREATE DATABASE `railgun`;
GRANT ALL PRIVILEGES ON `railgun`.* TO 'railgun'@'%' IDENTIFIED BY 'hellorailgun';

USE `railgun`;
--
-- 数据库: `railgun`
--

-- --------------------------------------------------------

--
-- 表的结构 `origin`
--

CREATE TABLE IF NOT EXISTS `origin` (
  `id` int(64) unsigned NOT NULL AUTO_INCREMENT,
  `hash` varchar(128) COLLATE utf8_bin NOT NULL,
  `source` mediumtext CHARACTER SET utf8 NOT NULL,
  `url` varchar(512) CHARACTER SET utf8 NOT NULL,
  `fetchtime` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `hash` (`hash`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- 表的结构 `videos`
--

CREATE TABLE IF NOT EXISTS `videos` (
  `id` int(64) unsigned NOT NULL AUTO_INCREMENT,
  `hash` varchar(128) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `title` varchar(256) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `surl` varchar(512) CHARACTER SET utf8 NOT NULL,
  `size` varchar(32) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '文件大小',
  `uptime` date NOT NULL DEFAULT '0000-00-00' COMMENT '上映时间',
  `resolution` varchar(16) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '解析度',
  `location` varchar(16) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '制片地',
  `catname` varchar(16) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '类目名',
  `format` varchar(8) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '文件格式',
  `links` text CHARACTER SET utf8 NOT NULL COMMENT '下载链接们',
  `intro` mediumtext CHARACTER SET utf8 NOT NULL COMMENT '介绍信息',
  `ratesum` int(3) unsigned NOT NULL DEFAULT '0' COMMENT '评分',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '入库时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `hash` (`hash`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
