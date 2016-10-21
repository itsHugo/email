/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hugopham.mailmoduledatabase;

import com.hugopham.mailmodules.ExtendedEmail;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import jodd.mail.EmailAttachment;
import jodd.mail.EmailMessage;
import jodd.mail.MailAddress;

/**
 * Interface for CRUD Methods for the Email database
 *
 * @author 1334944
 */
public interface EmailDAO {

    //Create
    public int createEmailAccount(String email,String password) throws SQLException;
    public int createEmail(String useremail, ExtendedEmail email) throws SQLException;
    public int createEmailMessage(EmailMessage message, int emailID) throws SQLException;
    public int createToEmail(String emailAddress, int emailID) throws SQLException;
    public int createCcEmail(String emailAddres, int emailID) throws SQLException;
    public int createBccEmail(String emailAddress, int emailID) throws SQLException;
    public int createAttachment(EmailAttachment attachment, int emailID) throws SQLException;
    //Read
    public ArrayList<ExtendedEmail> findAll() throws SQLException;
    public ExtendedEmail findEmailByID(int id) throws SQLException;
    public ArrayList<ExtendedEmail>
        findAllEmailsFor(String useremail) throws SQLException;
    public ArrayList<ExtendedEmail>
        findEmailsFrom(String email) throws SQLException;
    public String[] findTosFor(int ID) throws SQLException;
    public String[] findCcFor(int ID) throws SQLException;
    public String[] findBccFor(int ID) throws SQLException;
    public ArrayList<EmailAttachment> findAttachmentsFor(int ID) throws SQLException;
    public ArrayList<EmailMessage> findMessagesFor(int ID) throws SQLException;
    public int 
        getEmailID(String fromEmail, String subject, String folderName, Date sendDate, Date receiveDate) throws SQLException;
    //Update
    public int updateEmail(int emailID) throws SQLException;
    public int updateFolder(String folder, int emailID) throws SQLException;
    //Delete
    public int deleteEmail(int ID) throws SQLException;

}
