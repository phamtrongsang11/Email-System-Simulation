-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 30, 2022 at 05:06 AM
-- Server version: 10.4.14-MariaDB
-- PHP Version: 7.4.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `email_system`
--

-- --------------------------------------------------------

--
-- Table structure for table `mail`
--

CREATE TABLE `mail` (
  `MailID` int(11) NOT NULL,
  `Title` varchar(255) NOT NULL,
  `Content` varchar(255) NOT NULL,
  `Time` datetime NOT NULL DEFAULT current_timestamp(),
  `Schedule` datetime DEFAULT NULL,
  `File` varchar(100) DEFAULT NULL,
  `Size` double NOT NULL DEFAULT 0,
  `IsCC` tinyint(1) NOT NULL DEFAULT 0,
  `FromID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `mail`
--

INSERT INTO `mail` (`MailID`, `Title`, `Content`, `Time`, `Schedule`, `File`, `Size`, `IsCC`, `FromID`) VALUES
(33, 'chao alice', '<html><u><i><font face=\".Vn3DH\" size=\"16\">ban khoe khong</font></i></u><html>', '2022-11-30 10:50:47', NULL, '2022-11-30_10-50-47_data.jpg', 0.0633, 0, 3);

-- --------------------------------------------------------

--
-- Table structure for table `mail_received`
--

CREATE TABLE `mail_received` (
  `MailID` int(11) NOT NULL,
  `ReceiverID` int(11) NOT NULL,
  `StatusID` int(11) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `mail_received`
--

INSERT INTO `mail_received` (`MailID`, `ReceiverID`, `StatusID`) VALUES
(33, 5, 2);

-- --------------------------------------------------------

--
-- Table structure for table `mail_replies`
--

CREATE TABLE `mail_replies` (
  `MailID` int(11) NOT NULL,
  `RepID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `mail_status`
--

CREATE TABLE `mail_status` (
  `StatusID` int(11) NOT NULL,
  `Name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `mail_status`
--

INSERT INTO `mail_status` (`StatusID`, `Name`) VALUES
(1, 'Inbox'),
(2, 'Read'),
(3, 'Spam'),
(4, 'Delete'),
(5, 'Schedule');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `UserID` int(11) NOT NULL,
  `FirstName` varchar(100) NOT NULL,
  `LastName` varchar(100) NOT NULL,
  `Email` varchar(100) NOT NULL,
  `Password` varchar(100) NOT NULL,
  `IsAdmin` tinyint(1) NOT NULL DEFAULT 0,
  `HadUsed` double NOT NULL DEFAULT 0,
  `Storage` double NOT NULL DEFAULT 100,
  `IsLocked` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`UserID`, `FirstName`, `LastName`, `Email`, `Password`, `IsAdmin`, `HadUsed`, `Storage`, `IsLocked`) VALUES
(3, 'John', 'Ryan', 'john@gmail.com', '202cb962ac59075b964b07152d234b70', 0, 0.23154, 50, 0),
(4, 'Leo', 'Phill', 'leo@gmail.com', '202cb962ac59075b964b07152d234b70', 0, 0.23154, 100, 0),
(5, 'Alice', 'Kaye', 'alice@gmail.com', '202cb962ac59075b964b07152d234b70', 0, 0.08096, 1, 0),
(6, 'Admin', 'Christ', 'admin@gmail.com', '202cb962ac59075b964b07152d234b70', 1, 0, 100, 0),
(7, 'Boo', 'Lee', 'boo@gmail.com', '202cb962ac59075b964b07152d234b70', 0, 0, 100, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `mail`
--
ALTER TABLE `mail`
  ADD PRIMARY KEY (`MailID`),
  ADD KEY `AuthID` (`FromID`);

--
-- Indexes for table `mail_received`
--
ALTER TABLE `mail_received`
  ADD PRIMARY KEY (`MailID`,`ReceiverID`) USING BTREE,
  ADD KEY `MailID` (`MailID`,`ReceiverID`) USING BTREE,
  ADD KEY `ReceiverID` (`ReceiverID`),
  ADD KEY `StatusID` (`StatusID`);

--
-- Indexes for table `mail_replies`
--
ALTER TABLE `mail_replies`
  ADD PRIMARY KEY (`MailID`,`RepID`) USING BTREE,
  ADD KEY `MailID` (`MailID`,`RepID`) USING BTREE,
  ADD KEY `RepID` (`RepID`);

--
-- Indexes for table `mail_status`
--
ALTER TABLE `mail_status`
  ADD PRIMARY KEY (`StatusID`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`UserID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `mail`
--
ALTER TABLE `mail`
  MODIFY `MailID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;

--
-- AUTO_INCREMENT for table `mail_received`
--
ALTER TABLE `mail_received`
  MODIFY `MailID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=71;

--
-- AUTO_INCREMENT for table `mail_status`
--
ALTER TABLE `mail_status`
  MODIFY `StatusID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `UserID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `mail`
--
ALTER TABLE `mail`
  ADD CONSTRAINT `mail_ibfk_4` FOREIGN KEY (`FromID`) REFERENCES `user` (`UserID`);

--
-- Constraints for table `mail_received`
--
ALTER TABLE `mail_received`
  ADD CONSTRAINT `mail_received_ibfk_1` FOREIGN KEY (`MailID`) REFERENCES `mail` (`MailID`),
  ADD CONSTRAINT `mail_received_ibfk_2` FOREIGN KEY (`ReceiverID`) REFERENCES `user` (`UserID`),
  ADD CONSTRAINT `mail_received_ibfk_3` FOREIGN KEY (`StatusID`) REFERENCES `mail_status` (`StatusID`);

--
-- Constraints for table `mail_replies`
--
ALTER TABLE `mail_replies`
  ADD CONSTRAINT `mail_replies_ibfk_1` FOREIGN KEY (`MailID`) REFERENCES `mail` (`MailID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `mail_replies_ibfk_2` FOREIGN KEY (`RepID`) REFERENCES `mail` (`MailID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
