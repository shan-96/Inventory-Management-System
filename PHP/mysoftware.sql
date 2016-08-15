-- phpMyAdmin SQL Dump
-- version 4.2.11
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Jul 09, 2016 at 02:26 PM
-- Server version: 5.6.21
-- PHP Version: 5.6.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `mysoftware`
--

-- --------------------------------------------------------

--
-- Table structure for table `expirydates`
--

CREATE TABLE IF NOT EXISTS `expirydates` (
`id` int(11) NOT NULL,
  `date` varchar(20) NOT NULL,
  `duration` varchar(20) NOT NULL,
  `key` varchar(100) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `expirydates`
--

INSERT INTO `expirydates` (`id`, `date`, `duration`, `key`) VALUES
(1, '08/07/2017 00:00', '1', 'c4ca4238a0b923820dcc509a6f75849b'),
(2, '06/07/2017 00:00', '1', 'c81e728d9d4c2f636f067f89cc14862c');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `expirydates`
--
ALTER TABLE `expirydates`
 ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `expirydates`
--
ALTER TABLE `expirydates`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
