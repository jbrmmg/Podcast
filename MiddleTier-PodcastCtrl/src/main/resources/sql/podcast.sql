CREATE TABLE `podcast` (
  `id` char(4) NOT NULL,
  `source` varchar(300) NOT NULL,
  `directory` varchar(300) NOT NULL,
  `imagefile` varchar(100) NOT NULL,
  `datefile` char(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
