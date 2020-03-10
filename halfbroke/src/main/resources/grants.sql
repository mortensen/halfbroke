#drop database halfbroke;
#drop user 'duke'@'localhost';
#flush privileges;

CREATE DATABASE `halfbroke`;
CREATE USER 'duke'@'localhost' IDENTIFIED BY 'test';
GRANT ALL PRIVILEGES ON halfbroke.* TO 'duke'@'localhost';
CREATE TABLE `halfbroke`.`Person` (`id` INT UNSIGNED NOT NULL AUTO_INCREMENT, `firstName` VARCHAR(50), `lastName` VARCHAR(50), PRIMARY KEY (`id`) );