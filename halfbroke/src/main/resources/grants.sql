CREATE USER 'fred'@'localhost' IDENTIFIED BY 'test';
GRANT ALL PRIVILEGES ON halfbroke.* TO 'fred'@'localhost';
DROP USER 'fred'@'localhost';
CREATE TABLE `halfbroke`.`person` (`id` INT UNSIGNED NOT NULL AUTO_INCREMENT, `testcolumn` VARCHAR, PRIMARY KEY (`id`));