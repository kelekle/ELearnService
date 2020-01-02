/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50724
Source Host           : localhost:3306
Source Database       : elearndemo

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2019-12-05 12:28:39
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `course`
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
  `id` varchar(20) NOT NULL,
  `name` varchar(50) NOT NULL,
  `code` varchar(50) NOT NULL,
  `category_id` varchar(20) DEFAULT NULL,
  `description` varchar(50) DEFAULT NULL,
  `price` varchar(50) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `open_date` date DEFAULT NULL,
  `last_update_on` datetime DEFAULT NULL,
  `level` varchar(20) DEFAULT NULL,
  `shared` varchar(20) DEFAULT NULL,
  `shared_url` varchar(50) DEFAULT NULL,
  `avatar` varchar(100) DEFAULT NULL,
  `big_avatar` varchar(100) DEFAULT NULL,
  `certification` varchar(50) DEFAULT NULL,
  `certification_duration` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO course VALUES ('001', 'Android Development', '2019SSE_001', 'ICT_SW_001', 'A course for basic android appication development', '0', 'Open', '2019-09-01', '2019-11-25 09:01:20', 'basic', '0', null, '001\\av001.jpg', null, 'BJTU', 'Forever');
INSERT INTO course VALUES ('002', 'Web Development', '2019SSE_002', 'ICT_SW_002', 'HTML, CSS, JS and basic Web Server technologies.', '0', 'Open', '2019-09-06', '2019-11-26 10:11:42', 'basic', '0', null, '002\\av002.jpg', null, 'BJTU SSE', 'Forever');

-- ----------------------------
-- Table structure for `material`
-- ----------------------------
DROP TABLE IF EXISTS `material`;
CREATE TABLE `material` (
  `id` varchar(20) NOT NULL,
  `course_id` varchar(20) NOT NULL,
  `mediatype` varchar(20) NOT NULL,
  `material_type` varchar(50) DEFAULT NULL,
  `material_url` varchar(100) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `description` varchar(50) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of material
-- ----------------------------
INSERT INTO material VALUES ('1', '001', 'video', 'lecture video', '001\\vtest.mp4', '2019-12-04 22:06:49', null, '1');
INSERT INTO material VALUES ('2', '001', 'image', 'image', '001\\boat.jpg', '2019-12-04 22:07:49', null, '1');
INSERT INTO material VALUES ('3', '001', 'audio', 'lecture audio', '001\\joy.mp3', '2019-12-04 22:08:26', null, '1');
INSERT INTO material VALUES ('4', '001', 'pdf', 'slide pdf', '001\\python.pdf', '2019-12-04 22:08:57', null, '1');

-- ----------------------------
-- Table structure for `teacher`
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher` (
  `userid` varchar(20) NOT NULL,
  `course_id` varchar(20) NOT NULL,
  `name` varchar(50) NOT NULL,
  `photo` varchar(100) DEFAULT NULL,
  `telephone` varchar(20) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `description` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO teacher VALUES ('1', '001', 'Wang Hong', '001\\wang.jpg', '01051690273', 'wang@bjtu.edu', 'android teacher');
INSERT INTO teacher VALUES ('2', '002', '张亮', '002\\zhang.jpg', '01051680732', 'zhang@bjtu.edu', 'web development teacher');
