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
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `CustID` varchar(10) NOT NULL,
  `ID` varchar(20) NOT NULL,
  `Name` varchar(40) NOT NULL,
  `Tel` varchar(40) NOT NULL,
  `MembCode` varchar(10) NOT NULL,
  PRIMARY KEY (`CustID`),
  KEY `MembCode` (`MembCode`),
  CONSTRAINT `customer_ibfk_1` FOREIGN KEY (`MembCode`) REFERENCES `membership` (`Code`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES ('C001','991234567V','Nimal Priyantha','0785678860','M1'),('C002','987654321V','Kamal Thissa','0781234567','M2'),('C003','200005602345','Sunil Perera','0756677889','M3'),('C004','892348765V','Kusal Mendis','0762347658','M3'),('C005','971543288V','Lahiru Kumara','0704569871','M2'),('C006','967854321V','Sangeeth Silva','0724569871','M3'),('C007','990560234V','Amidu Shamika','0785255251','M3'),('C008','965670981V','Thusith Chamara','0751233210','M2'),('C009','887654321V','Kasun Rajitha','0707655674','M3'),('C010','798866123V','Wasantha Perera','0772345234','M1'),('C011','879912376V','Thilina Sandeep','0112997788','M2'),('C012','945671234V','Nipun Shantha','0726677547','M1'),('C013','925565656V','Amila Prasad','0749877891','M2'),('C014','919876543V','Thushara Ferando','0783434123','M3'),('C015','993213210V','Vidura Manthika','0717766554','M3'),('C016','943215678V','Rumesh Veerasekara','0342267432','M2'),('C017','785677654V','Nalaka Bandara','0777002345','M3'),('C018','974545656V','Manula Sanjaya','0703443554','M3'),('C019','874521351V','Sarath Nandasiri','0754321213','M3'),('C020','774974574V','Yasith Chamara','0718746549','M3'),('C021','200045245545','Akash Kithsara','0783451231','M2'),('C022','983432544V','Praveen Randika','0783543243','M1');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
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
