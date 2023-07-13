-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: localhost    Database: wheeldeal
-- ------------------------------------------------------
-- Server version	8.0.22

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `returns`
--

DROP TABLE IF EXISTS `returns`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `returns` (
  `ReturnNo` varchar(10) NOT NULL,
  `RideNo` varchar(10) NOT NULL,
  `Distance` double(6,2) NOT NULL,
  `Cost` double(10,2) NOT NULL,
  `ReDate` date NOT NULL,
  PRIMARY KEY (`ReturnNo`),
  UNIQUE KEY `RideNo` (`RideNo`),
  CONSTRAINT `returns_ibfk_1` FOREIGN KEY (`RideNo`) REFERENCES `ride` (`RideNo`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `returns`
--

LOCK TABLES `returns` WRITE;
/*!40000 ALTER TABLE `returns` DISABLE KEYS */;
INSERT INTO `returns` VALUES ('H001','R001',77.00,9240.00,'2023-01-06'),('H002','R002',56.00,6664.00,'2023-01-13'),('H003','R003',44.00,7920.00,'2023-01-22'),('H004','R005',37.00,3996.00,'2023-02-08'),('H005','R004',108.00,24300.00,'2023-02-09'),('H006','R006',97.00,9069.50,'2023-02-22'),('H007','R007',178.00,40050.00,'2023-02-26'),('H008','R008',89.00,14418.00,'2023-03-13'),('H009','R009',112.00,14280.00,'2023-03-23'),('H010','R010',94.00,9024.00,'2023-03-29'),('H011','R011',69.00,7728.00,'2023-03-30'),('H012','R012',108.00,14314.00,'2023-04-08'),('H013','R013',116.00,24345.00,'2023-04-13'),('H014','R014',190.00,31257.00,'2023-04-22'),('H015','R015',57.00,10773.00,'2023-04-23'),('H016','R017',302.00,56070.00,'2023-04-30'),('H017','R016',99.00,18933.75,'2023-05-01');
/*!40000 ALTER TABLE `returns` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-07-13 11:02:27