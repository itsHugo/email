/**
 * Author:  Hugo
 * Created: 11-Oct-2016
 */
--DROP DATABASE IF EXISTS JODDEMAIL;
--CREATE DATABASE JODDEMAIL;

GRANT ALL ON JODDEMAIL.* TO;

USE JODDEMAIL;

DROP TABLE IF EXISTS EMAILACCOUNT;
CREATE TABLE EMAILACCOUNT (
    ID int(11) NOT NULL auto_increment,
    EMAIL VARCHAR(50) NOT NULL,
    PASSWORD VARCHAR(50) NOT NULL,
    PRIMARY KEY (ID)
);

DROP TABLE IF EXISTS EMAIL;
CREATE TABLE EMAIL (
    ID int(11) NOT NULL auto_increment,
    FROMEMAIL varchar(50) NOT NULL,
    SUBJECT varchar(50) NOT NULL,
    SENDDATE date NOT NULL,
    RECEIVEDATE date NOT NULL,
    FOLDER
    PRIMARY KEY (ID)
);

DROP TABLE IF EXISTS EMAILMESSAGE;
CREATE TABLE EMAILMESSAGE (
    ID int(11) NOT NULL auto_increment,
    CONTENT text NOT NULL,
    EMAILID int(11) NOT NULL,
    PRIMARY KEY (ID)
);

DROP TABLE IF EXISTS TOEMAIL;
CREATE TABLE TOEMAIL (
    ID int(11) NOT NULL auto_increment,
    EMAILADDRESS varchar(50) NOT NULL,
    EMAILID int(11) NOT NULL,
    PRIMARY KEY (ID)
);

DROP TABLE IF EXISTS CCEMAIL;
CREATE TABLE CCEMAIL (
    ID int(11) NOT NULL auto_increment,
    EMAILADDRESS varchar(50) NOT NULL,
    EMAILID int(11) NOT NULL,
    PRIMARY KEY (ID)
);

DROP TABLE IF EXISTS ATTACHMENT;
CREATE TABLE ATTACHMENT (
    ID int(11) NOT NULL auto_increment,
    FILENAME varchar(40) NOT NULL,
    FILEDATA BLOB,
    EMAILID int(11)
    PRIMARY KEY (ID)
);

DROP TABLE IF EXISTS FOLDER;
CREATE TABLE FOLDER (
    ID int(2) NOT NULL auto_increment,
    FOLDERNAME varchar(40),
    EMAILID int(11),
    PRIMARY KEY (ID)
);


ALTER TABLE EMAILMESSAGE ADD FOREIGN KEY (EMAILID) REFERENCES EMAIL (ID);
ALTER TABLE TOEMAIL ADD FOREIGN KEY (EMAILID) REFERENCES EMAIL (ID);
ALTER TABLE CCEMAIL ADD FOREIGN KEY (EMAILID) REFERENCES EMAIL (ID);
ALTER TABLE ATTACHMENT ADD FOREIGN KEY (EMAILID) REFERENCES EMAIL (ID);
ALTER TABLE  ADD FOREIGN KEY (EMAILID) REFERENCES EMAIL (ID);
