CREATE TABLE `movies_genres` (
`movieId` int NOT NULL,
`genreId` int NOT NULL,
PRIMARY KEY (`movieId`,`genreId`),
KEY `genreId` (`genreId`),
CONSTRAINT `movies_genres_ibfk_1` FOREIGN KEY (`movieId`) REFERENCES `movies`
(`movieId`),
CONSTRAINT `movies_genres_ibfk_2` FOREIGN KEY (`genreId`) REFERENCES `genres`
(`genreId`)
) ;