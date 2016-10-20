/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hugopham.mailmoduledatabase;

import com.hugopham.mailmodules.ExtendedEmail;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import jodd.mail.EmailAttachment;
import jodd.mail.EmailMessage;
import jodd.mail.MailAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author 1334944
 */
public class EmailDAOImpl implements EmailDAO {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());
    
    private final String url = "jdbc:mysql://waldo2.dawsoncollege.qc.ca/Aquarium";
    private final String user = "fish";
    private final String password = "fish";
    @Override
    public int createEmailAccount(String email, String password) throws SQLException {
        String createQuery = "INSERT INTO EMAILACCOUNT(USEREMAIL, PASSWORD) VALUES(?,?)";
        int result = 0;

        // Connection is only open for the operation and then immediately closed
        try (Connection connection = DriverManager.getConnection(url, user, password);
                // Using a prepared statement to handle the conversion
                // of special characters in the SQL statement and guard against
                // SQL Injection
                PreparedStatement ps = connection.prepareStatement(createQuery);) {
            log.info("Inserting into EMAILACCOUNT: "+ email);
            ps.setString(1, email);
            ps.setString(2, password);

            result = ps.executeUpdate();
        }
        log.info("# of records created : " + result);
        return result;
    }

    @Override
    public int createEmail(String useremail,ExtendedEmail email) throws SQLException {
        int id = -1;
        String createQuery = "INSERT INTO EMAIL(USEREMAIL, FROMEMAIL, SUBJECT, SENDDATE, RECEIVEDATE, FOLDERNAME, FLAGS)"
                + "VALUES(?,?,?,?,?,?)";
        // Connection is only open for the operation and then immediately closed
        try (Connection connection = DriverManager.getConnection(url, user, password);
                // Using a prepared statement to handle the conversion
                // of special characters in the SQL statement and guard against
                // SQL Injection
                PreparedStatement ps = connection.prepareStatement(createQuery);) {
            log.info("Inserting into EMAILACCOUNT: "+ email);
            ps.setString(1, useremail);
            ps.setString(2, email.getFrom().getEmail());
            ps.setString(3,email.getSubject());
            if(email.getSentDate() != null)
                ps.setTimestamp(4, new java.sql.Timestamp(email.getSentDate().getTime()));//convert to SQLDate
            if(email.getReceiveDate() != null)
                ps.setTimestamp(5, new java.sql.Timestamp(email.getReceiveDate().getTime()));//convert to SQLDate
            ps.setString(6, email.getFolder());
            if(email.getFlags() != null)
                ps.setString(7,email.getFlags().toString());
            
            
            
            id = ps.executeUpdate(createQuery, Statement.RETURN_GENERATED_KEYS);
        }
 
        for (MailAddress toAddress : email.getTo()) {
            createToEmail(toAddress.getEmail(), id);
        }

        if (email.getCc() != null)
            for (MailAddress ccAddress : email.getCc()) {
                createCcEmail(ccAddress.getEmail(), id);
            }
        
        if (email.getBcc() != null)
            for (MailAddress bccAddress : email.getBcc()) {
                createBccEmail(bccAddress.getEmail(), id);
            }

        for (EmailMessage message : email.getAllMessages()) {
            createEmailMessage(message, id);
        }
        
        if(email.getAttachments() != null)
            for (EmailAttachment attachment : email.getAttachments()){
                createAttachment(attachment, id);
            }


        return id;
    }

    @Override
    public int createEmailMessage(EmailMessage message, int emailID) throws SQLException {
        String createQuery = "INSERT INTO EMAILMESSAGE(CONTENT, EMAILID) VALUES (?,?)";
        int result = 0;

        // Connection is only open for the operation and then immediately closed
        try (Connection connection = DriverManager.getConnection(url, user, password);
                // Using a prepared statement to handle the conversion
                // of special characters in the SQL statement and guard against
                // SQL Injection
                PreparedStatement ps = connection.prepareStatement(createQuery);) {
            log.info("Inserting into EMAILMESSAGE:");
            ps.setString(1, message.getContent());
            ps.setInt(2, emailID);

            result = ps.executeUpdate();
        }
        log.info("# of records created : " + result);
        return result;
    }

    @Override
    public int createToEmail(String emailAddress, int emailID) throws SQLException {
        String createQuery = "INSERT INTO TOEMAIL(EMAILADDRESS, EMAILID) VALUES (?,?)";
        int result = 0;

        // Connection is only open for the operation and then immediately closed
        try (Connection connection = DriverManager.getConnection(url, user, password);
                // Using a prepared statement to handle the conversion
                // of special characters in the SQL statement and guard against
                // SQL Injection
                PreparedStatement ps = connection.prepareStatement(createQuery);) {
            log.info("Inserting into TOEMAIL:" + emailAddress);
            ps.setString(1, emailAddress);
            ps.setInt(2, emailID);

            result = ps.executeUpdate();
        }
        log.info("# of records created : " + result);
        return result;
    }

    @Override
    public int createCcEmail(String emailAddress, int emailID) throws SQLException {
        String createQuery = "INSERT INTO CCEMAIL(EMAILADDRESS, EMAILID) VALUES (?,?)";
        int result = 0;

        // Connection is only open for the operation and then immediately closed
        try (Connection connection = DriverManager.getConnection(url, user, password);
                // Using a prepared statement to handle the conversion
                // of special characters in the SQL statement and guard against
                // SQL Injection
                PreparedStatement ps = connection.prepareStatement(createQuery);) {
            log.info("Inserting into CCEMAIL:" + emailAddress);
            ps.setString(1, emailAddress);
            ps.setInt(2, emailID);

            result = ps.executeUpdate();
        }
        log.info("# of records created : " + result);
        return result;
    }

    @Override
    public int createBccEmail(String emailAddress, int emailID) throws SQLException {
        String createQuery = "INSERT INTO BCCEMAIL(EMAILADDRESS, EMAILID) VALUES (?,?)";
        int result = 0;

        // Connection is only open for the operation and then immediately closed
        try (Connection connection = DriverManager.getConnection(url, user, password);
                // Using a prepared statement to handle the conversion
                // of special characters in the SQL statement and guard against
                // SQL Injection
                PreparedStatement ps = connection.prepareStatement(createQuery);) {
            log.info("Inserting into TOEMAIL:" + emailAddress);
            ps.setString(1, emailAddress);
            ps.setInt(2, emailID);

            result = ps.executeUpdate();
        }
        log.info("# of records created : " + result);
        return result;
    }

    @Override
    public int createAttachment(EmailAttachment attachment, int emailID) throws SQLException {
        String createQuery = "INSERT INTO ATTACHMENT(FILEDATA, EMAILID) VALUES (?,?)";
        int result = 0;

        // Connection is only open for the operation and then immediately closed
        try (Connection connection = DriverManager.getConnection(url, user, password);
                // Using a prepared statement to handle the conversion
                // of special characters in the SQL statement and guard against
                // SQL Injection
                PreparedStatement ps = connection.prepareStatement(createQuery);) {
            log.info("Inserting into ATTACHMENT:");
            ps.setBlob(1, new ByteArrayInputStream(attachment.toByteArray()));
            ps.setInt(2, emailID);

            result = ps.executeUpdate();
        }
        log.info("# of records created : " + result);
        return result;
    }

    @Override
    public ArrayList<ExtendedEmail> findAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ExtendedEmail findEmailByID(int id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<ExtendedEmail> findAllEmailsFor(String email) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<ExtendedEmail> findEmailsFrom(String email) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ExtendedEmail getEmailID(String fromEmail, String subject, String folderName, Date sendDate, Date receiveDate) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int updateEmail(int ID) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int updateEmailMessage(int ID, int emailID) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int updateFolder(ExtendedEmail email, String folder) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int deleteEmail(int ID) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private ExtendedEmail createExtendedEmail(ResultSet resultSet) throws SQLException {
        ExtendedEmail extendedEmail = new ExtendedEmail();
        
        return extendedEmail;
    }

    
}
