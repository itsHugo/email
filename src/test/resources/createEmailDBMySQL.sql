/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  Hugo
 * Created: 11-Oct-2016
 */
DROP DATABASE IF EXISTS JODDEMAIL;
CREATE DATABASE JODDEMAIL;

GRANT ALL ON JODDEMAIL.* TO;

USE JODDEMAIL;

DROP TABLE IF EXISTS EMAILACCOUNT;
CREATE TABLE EMAILACCOUNT (
    ID int(11) NOT NULL auto_increment,
    EMAIL VARCHAR(45) NOT NULL default '',
    PASSWORD VARCHAR(45) NOT NULL default ''
    PRIMARY KEY (ID)
);

DROP TABLE IF EXISTS EMAILCONTENT;
CREATE TABLE EMAILCONTENT (
    ID int(11) NOT NULL auto_increment,
    FROMEMAIL varchar(40) NOT NULL default '',
    TOEMAIL varchar(40) NOT NULL default '',
    CC varchar(255) default '',
    SUBJECT varchar(40),
    SENDDATE date,
    RECEIVEDATE date,
    FOLDER varchar(40) NOT NULL default '',
    CONTENT varchar(MAX) NOT NULL default '',
)

DROP TABLE IF EXISTS FOLDER;
CREATE TABLE FOLDER (
    ID int(11) NOT NULL auto_increment,
    FOLDERNAME varchar(40) NOT NULL default ''
)

