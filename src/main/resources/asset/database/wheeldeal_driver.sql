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
-- Table structure for table `driver`
--

DROP TABLE IF EXISTS `driver`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `driver` (
  `DriverID` varchar(10) NOT NULL,
  `Name` varchar(40) NOT NULL,
  `Location` varchar(20) NOT NULL,
  `Tel` varchar(40) NOT NULL,
  `Availability` varchar(20) NOT NULL,
  PRIMARY KEY (`DriverID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `driver`
--

LOCK TABLES `driver` WRITE;
/*!40000 ALTER TABLE `driver` DISABLE KEYS */;
INSERT INTO `driver` VALUES ('D001','Ravi perera','Kaluthara','0342245678','Available'),('D002','Nizar usuf','Panadura','0781231234','Available'),('D003','Asif zasha','Kaluthara','0702234599','Available'),('D004','Sugath yapa','Colombo','0112445566','Available'),('D005','Dilan Akalanka','Gampaha','0765432189','Available'),('D006','Harsha Kumara','Galle','0779876123','Available'),('D007','Gihan Randima','Colombo','0706676767','Available'),('D008','Imanka Madushan','Neluwa','0771567432','Available'),('D009','Kavinda Irosh','Aluthgama','0775674321','Available'),('D010','Sadaru Sharuda','Agalawatta','0769912344','Available'),('D011','Pasindu Pimalka','Walipanna','0761087474','Available'),('D012','Kasun Dileepa','Wadduwa','0742132342','Available'),('D013','Hiruna Lakshan','Dodangoda','0715654454','Available'),('D014','Sameera Perea','Gampaha','0725654548','Available'),('D015','Udara Priyan','Moratuwa','0743435248','Available'),('D016','Danapala Udawththa','Colombo','0773434334','Available');
/*!40000 ALTER TABLE `driver` ENABLE KEYS */;
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
