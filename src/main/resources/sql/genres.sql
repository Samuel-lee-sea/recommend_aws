CREATE TABLE `genres` (
`genreId` int NOT NULL AUTO_INCREMENT,
`genre` varchar(255) DEFAULT NULL,
PRIMARY KEY (`genreId`),
UNIQUE KEY `genre` (`genre`)
) ;