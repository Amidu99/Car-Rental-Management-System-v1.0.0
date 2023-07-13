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
-- Table structure for table `ride_driver_details`
--

DROP TABLE IF EXISTS `ride_driver_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ride_driver_details` (
  `RideNo` varchar(10) NOT NULL,
  `DriverID` varchar(10) NOT NULL,
  `RideDate` date NOT NULL,
  KEY `RideNo` (`RideNo`),
  KEY `DriverID` (`DriverID`),
  CONSTRAINT `ride_driver_details_ibfk_1` FOREIGN KEY (`RideNo`) REFERENCES `ride` (`RideNo`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ride_driver_details_ibfk_2` FOREIGN KEY (`DriverID`) REFERENCES `driver` (`DriverID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ride_driver_details`
--

LOCK TABLES `ride_driver_details` WRITE;
/*!40000 ALTER TABLE `ride_driver_details` DISABLE KEYS */;
INSERT INTO `ride_driver_details` VALUES ('R001','D001','2023-01-05'),('R002','D003','2023-01-11'),('R003','D008','2023-01-21'),('R004','D002','2023-02-05'),('R005','D011','2023-02-07'),('R006','D004','2023-02-21'),('R007','D007','2023-02-25'),('R008','D005','2023-03-13'),('R009','D006','2023-03-22'),('R010','D009','2023-03-27'),('R011','D012','2023-03-29'),('R012','D014','2023-04-07'),('R012','D013','2023-04-07'),('R013','D015','2023-04-11'),('R013','D010','2023-04-11'),('R014','D004','2023-04-20'),('R014','D012','2023-04-20'),('R015','D009','2023-04-22'),('R016','D006','2023-04-28'),('R017','D002','2023-04-29'),('R017','D001','2023-04-29');
/*!40000 ALTER TABLE `ride_driver_details` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-07-13 11:02:25
