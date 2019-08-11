CREATE TABLE `podcastepisode` (
  `guid` varchar(100) NOT NULL,
  `source` varchar(250) NOT NULL,
  `title` varchar(250) NOT NULL,
  `filename` varchar(100) NOT NULL,
  `filesize` int not null,
  `podcastid` char(4) DEFAULT NULL,
  `deletefile` char(1) DEFAULT NULL,
  `fileexists` char(1) DEFAULT NULL,
  `ignore` char(1) DEFAULT NULL,
  `createdate` int(11) DEFAULT NULL,
  `updatedate` int(11) DEFAULT NULL,
  PRIMARY KEY (`guid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
