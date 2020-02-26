CREATE USER 'fred'@'localhost' IDENTIFIED BY 'test';
GRANT ALL PRIVILEGES ON halfbroke.* TO 'fred'@'localhost';
CREATE TABLE `halfbroke`.`Person` (`id` INT UNSIGNED NOT NULL AUTO_INCREMENT, `firstName` VARCHAR(50), `lastName` VARCHAR(50), PRIMARY KEY (`id`) );

#DROP USER 'fred'@'localhost';