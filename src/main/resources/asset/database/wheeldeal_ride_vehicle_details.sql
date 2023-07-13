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
-- Table structure for table `ride_vehicle_details`
--

DROP TABLE IF EXISTS `ride_vehicle_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ride_vehicle_details` (
  `RideNo` varchar(10) NOT NULL,
  `VehiNo` varchar(10) NOT NULL,
  `RideDate` date NOT NULL,
  `Distance` double(6,2) DEFAULT NULL,
  KEY `RideNo` (`RideNo`),
  KEY `VehiNo` (`VehiNo`),
  CONSTRAINT `ride_vehicle_details_ibfk_1` FOREIGN KEY (`RideNo`) REFERENCES `ride` (`RideNo`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ride_vehicle_details_ibfk_2` FOREIGN KEY (`VehiNo`) REFERENCES `vehicle` (`VehiNo`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ride_vehicle_details`
--

LOCK TABLES `ride_vehicle_details` WRITE;
/*!40000 ALTER TABLE `ride_vehicle_details` DISABLE KEYS */;
INSERT INTO `ride_vehicle_details` VALUES ('R002','V003','2023-01-11',NULL),('R004','V007','2023-02-05',NULL),('R005','V004','2023-02-07',NULL),('R003','V002','2023-01-21',NULL),('R001','V001','2023-01-05',NULL),('R006','V011','2023-02-21',NULL),('R007','V007','2023-02-25',NULL),('R008','V017','2023-03-13',NULL),('R009','V009','2023-03-22',NULL),('R010','V010','2023-03-27',NULL),('R011','V003','2023-03-29',NULL),('R012','V029','2023-04-07',43.00),('R012','V016','2023-04-07',65.00),('R013','V028','2023-04-11',79.00),('R013','V024','2023-04-11',37.00),('R014','V009','2023-04-20',101.00),('R014','V026','2023-04-20',89.00),('R015','V025','2023-04-22',NULL),('R016','V030','2023-04-28',NULL),('R017','V008','2023-04-29',107.00),('R017','V014','2023-04-29',88.00);
/*!40000 ALTER TABLE `ride_vehicle_details` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-07-13 11:02:26
