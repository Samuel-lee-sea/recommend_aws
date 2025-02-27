CREATE TABLE `ratings` (
`userId` int NOT NULL,
`movieId` int NOT NULL,
`rating` decimal(2,1) DEFAULT NULL,
`timestamp` int DEFAULT NULL,
PRIMARY KEY (`userId`,`movieId`)
) ;