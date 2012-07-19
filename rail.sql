-- phpMyAdmin SQL Dump
-- version 3.4.10.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: May 01, 2012 at 05:06 PM
-- Server version: 5.1.61
-- PHP Version: 5.3.3

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

drop database if exists `railgun`;
create database  `railgun`;
grant all on railgun.* to 'railgun'@'%' identified by 'hellorailgun';

--
-- Database: `railgun`
--

-- --------------------------------------------------------
use  `railgun`;

-- --------------------------------------------------------

--
-- ��Ľṹ `origin`
--

CREATE TABLE IF NOT EXISTS `origin` (
  `id` int(64) unsigned NOT NULL AUTO_INCREMENT,
  `hash` varchar(128) COLLATE utf8_bin NOT NULL,
  `source` mediumtext CHARACTER SET utf8 NOT NULL,
  `url` varchar(512) CHARACTER SET utf8 NOT NULL,
  `fetchtime` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `hash` (`hash`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=72025 ;

-- --------------------------------------------------------

--
-- ��Ľṹ `videos`
--

CREATE TABLE IF NOT EXISTS `videos` (
  `id` int(64) unsigned NOT NULL AUTO_INCREMENT,
  `hash` varchar(128) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `title` varchar(256) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `surl` varchar(512) CHARACTER SET utf8 NOT NULL,
  `size` varchar(32) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '�ļ���С',
  `uptime` date NOT NULL DEFAULT '0000-00-00' COMMENT '��ӳʱ��',
  `resolution` varchar(16) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '������',
  `location` varchar(16) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '��Ƭ��',
  `catname` varchar(16) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '��Ŀ��',
  `format` varchar(8) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '�ļ���ʽ',
  `links` text CHARACTER SET utf8 NOT NULL COMMENT '����������',
  `intro` mediumtext CHARACTER SET utf8 NOT NULL COMMENT '������Ϣ',
  `ratesum` int(3) unsigned NOT NULL DEFAULT '0' COMMENT '����',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '���ʱ��',
  PRIMARY KEY (`id`),
  UNIQUE KEY `hash` (`hash`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=72025 ;
