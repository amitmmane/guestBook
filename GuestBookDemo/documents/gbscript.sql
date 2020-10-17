DROP DATABASE  IF EXISTS `guestbook`;
CREATE DATABASE  IF NOT EXISTS `guestbook`;
USE `guestbook`;
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`)
) ;
INSERT INTO `users` 
VALUES 
('Amit','{noop}root',1),
('Prashant','{noop}root',1),
('Rohan','{noop}root',1),
('Pravin','{noop}root',1),
('Pushkar','{noop}root',1),
('Omkar','{noop}root',1);

DROP TABLE IF EXISTS `authorities`;
CREATE TABLE `authorities` (
  `username` varchar(50) NOT NULL,
  `authority` varchar(50) NOT NULL,
  UNIQUE KEY `authorities_idx_1` (`username`,`authority`),
  CONSTRAINT `authorities_ibfk_1` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ;

INSERT INTO `authorities` 
VALUES 
('Amit','ROLE_ADMIN'),
('Prashant','ROLE_USER'),
('Rohan','ROLE_DENY'),
('Pravin','ROLE_USER'),
('Pushkar','ROLE_USER'),
('Omkar','ROLE_USER');


DROP TABLE IF EXISTS `feedbackdetails`;
CREATE TABLE `feedbackdetails` (
  `id` int(10)  NOT NULL AUTO_INCREMENT,
  `user_id` varchar(50) NULL,
  `feedback_text` varchar(100)  NULL,
  `feedback_image` longblob  NULL,
  `feedback_image_name` varchar(100)  NULL,
  `feedback_time` DateTime  NULL,
  `feedback_Approved` varchar(2) NULL,
  PRIMARY KEY (`id`)
) 
