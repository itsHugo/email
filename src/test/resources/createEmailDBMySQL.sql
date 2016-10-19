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
    EMAIL VARCHAR(50) NOT NULL UNIQUE,
    PASSWORD VARCHAR(50) NOT NULL,
    PRIMARY KEY (ID)
);

DROP TABLE IF EXISTS EMAIL;
CREATE TABLE EMAIL (
    ID int(11) NOT NULL auto_increment,
    FROMEMAIL varchar(50) NOT NULL,
    SUBJECT varchar(50) NOT NULL,
    SENDDATE date,
    RECEIVEDATE date,
    FOLDERNAME varchar(40) NOT NULL,
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
    FILEDATA blob NOT NULL,
    EMAILID int(11) NOT NULL,
    PRIMARY KEY (ID)
);

DROP TABLE IF EXISTS FOLDER;
CREATE TABLE FOLDER (
    FOLDERNAME varchar(40) NOT NULL UNIQUE,
    PRIMARY KEY (FOLDERNAME)
);


ALTER TABLE EMAILMESSAGE ADD FOREIGN KEY (EMAILID) REFERENCES EMAIL(ID) ON DELETE CASCADE;
ALTER TABLE TOEMAIL ADD FOREIGN KEY (EMAILID) REFERENCES EMAIL(ID) ON DELETE CASCADE;
ALTER TABLE CCEMAIL ADD FOREIGN KEY (EMAILID) REFERENCES EMAIL(ID ON DELETE CASCADE);
ALTER TABLE ATTACHMENT ADD FOREIGN KEY (EMAILID) REFERENCES EMAIL(ID) ON DELETE CASCADE;
ALTER TABLE EMAIL ADD FOREIGN KEY (FOLDERNAME) REFERENCES FOLDER(FOLDERNAME) ON DELETE CASCADE;

INSERT INTO EMAILACCOUNT(EMAIL, PASSWORD) VALUES("hugo.sender.not.a.bot@gmail.com","JAVAbean517!");

INSERT INTO FOLDER (FOLDERNAME) VALUES("Inbox");
INSERT INTO FOLDER (FOLDERNAME) VALUES("Sent");
INSERT INTO FOLDER (FOLDERNAME) VALUES("Draft");
INSERT INTO FOLDER (FOLDERNAME) VALUES("Junk");

