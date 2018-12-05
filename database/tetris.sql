/*
SQLyog Community v12.4.3 (64 bit)
MySQL - 5.7.19 : Database - tetris
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`tetris` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `tetris`;

/*Table structure for table `heroes` */

DROP TABLE IF EXISTS `heroes`;

CREATE TABLE `heroes` (
  `heroID` int(11) NOT NULL AUTO_INCREMENT,
  `heroName` varchar(50) NOT NULL,
  `heroAbility` varchar(300) NOT NULL,
  `heroAbilityNegative` tinyint(4) DEFAULT '1',
  `cost` int(11) NOT NULL,
  PRIMARY KEY (`heroID`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

/*Data for the table `heroes` */

insert  into `heroes`(`heroID`,`heroName`,`heroAbility`,`heroAbilityNegative`,`cost`) values 
(1,'Pac-Man','Eating 2 adjacent rows',0,100),
(2,'Donkey Kong','Throws a ton on the roughest surface of the field',1,100),
(4,'Pikachu','Reverse controls',1,100),
(5,'Sonic','\rThe next block of opponent goes directly down',1,100);

/*Table structure for table `rewards` */

DROP TABLE IF EXISTS `rewards`;

CREATE TABLE `rewards` (
  `rewardID` int(11) NOT NULL AUTO_INCREMENT,
  `countStreakDays` int(11) DEFAULT NULL,
  `rewards` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`rewardID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

/*Data for the table `rewards` */

insert  into `rewards`(`rewardID`,`countStreakDays`,`rewards`) values 
(1,1,'50 xp'),
(2,2,'scratch card'),
(3,3,'100 xp'),
(4,4,'scratch card'),
(5,5,'150 xp'),
(6,6,'mystery box'),
(7,7,'10 cubes');

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `registerDate` varchar(100) NOT NULL,
  `startStreakDate` varchar(100) DEFAULT NULL,
  `lastLoggedInDate` varchar(100) DEFAULT NULL,
  `streakDays` int(11) DEFAULT NULL,
  `alreadyLoggedInToday` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_id_UNIQUE` (`user_id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  KEY `streak_day_id_idx` (`streakDays`),
  CONSTRAINT `streak_id` FOREIGN KEY (`streakDays`) REFERENCES `rewards` (`rewardID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

/*Data for the table `user` */

insert  into `user`(`user_id`,`username`,`password`,`registerDate`,`startStreakDate`,`lastLoggedInDate`,`streakDays`,`alreadyLoggedInToday`) values 
(1,'Testid','98f6bcd4621d373cade4e832627b4f6','',NULL,NULL,NULL,0),
(2,'Bryan','7d4ef62de50874a4db33e6da3ff79f75','',NULL,NULL,NULL,0),
(3,'samsung','6bac5ceb65066fc615beeb839bb6b81','2018-11-19 04:36:34','2018-11-19 04:36:34','2018-11-20 04:36:34',1,0),
(4,'hallo','598d4c200461b81522a3328565c25f7c','2018-11-19 05:29:09','2018-11-19 05:29:09','2018-11-22 01:18:02',4,0),
(5,'test','98f6bcd4621d373cade4e832627b4f6','2018-11-21 01:19:49','2018-11-21 01:19:49','2018-11-22 01:20:40',1,0);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
