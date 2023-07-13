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
-- Table structure for table `vehicle`
--

DROP TABLE IF EXISTS `vehicle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehicle` (
  `VehiNo` varchar(10) NOT NULL,
  `VehiType` varchar(10) NOT NULL,
  `Model` varchar(30) NOT NULL,
  `CostPerKM` double(6,2) NOT NULL,
  `Availability` varchar(20) NOT NULL,
  PRIMARY KEY (`VehiNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehicle`
--

LOCK TABLES `vehicle` WRITE;
/*!40000 ALTER TABLE `vehicle` DISABLE KEYS */;
INSERT INTO `vehicle` VALUES ('V001','Car','Toyota CHR',150.00,'Available'),('V002','Van','Toyota KDH',200.00,'Available'),('V003','Car','Toyota Axio',140.00,'Available'),('V004','Lorry','Dimo batta',120.00,'Available'),('V005','Van','Nissan Caravan',160.00,'Available'),('V006','Lorry','Isuzu Canter',200.00,'Available'),('V007','Car','Mazda RX8',250.00,'Available'),('V008','Car','LaFerrari Aperta ',250.00,'Available'),('V009','Van','Toyota Noha',150.00,'Available'),('V010','Car','Toyota Aqua',120.00,'Available'),('V011','Car','Honda Insight',110.00,'Available'),('V012','Van','Toyota Hiace',130.00,'Available'),('V013','Van','Nissan Urvan',125.00,'Available'),('V014','Van','Suzuki Every',100.00,'Available'),('V015','Van','Toyota GranAce',110.00,'Available'),('V016','Van','Ford Transit',140.00,'Available'),('V017','Lorry','Toyota Dyna',180.00,'Available'),('V018','Lorry','Isuzu ELF',190.00,'Available'),('V019','Lorry','Maxximo',160.00,'Available'),('V020','Van','Nissan Vanette',130.00,'Available'),('V021','Lorry','Tata Ex709',220.00,'Available'),('V022','Lorry','Nissan Atlas',140.00,'Available'),('V023','Van','Nissan Serena',130.00,'Available'),('V024','Lorry','Tata LPT',240.00,'Available'),('V025','Lorry','Mitsubishi Canter',210.00,'Available'),('V026','Car','Toyota Premio',220.00,'Available'),('V027','Car','Nissan Leaf',150.00,'Available'),('V028','Car','Honda Civic Ex',230.00,'Available'),('V029','Car','Honda Vezel',180.00,'Available'),('V030','Car','Mitsubishi Lancer',225.00,'Available'),('V031','Car','Porche Q777',290.00,'Available');
/*!40000 ALTER TABLE `vehicle` ENABLE KEYS */;
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
