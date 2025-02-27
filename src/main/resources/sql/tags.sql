CREATE TABLE `tags` (
`userId` int NOT NULL,
`movieId` int NOT NULL,
`tag` varchar(255) NOT NULL,
`timestamp` int DEFAULT NULL,
PRIMARY KEY (`userId`,`movieId`,`tag`)
) ;