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
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import javax.mail.Flags;
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
    
    private final String url = "jdbc:mysql://waldo2.dawsoncollege.qc.ca/CS1334944";
    private final String user = "CS1334944";
    private final String password = "uvillien";
    @Override
    public int createEmailAccount(String email, String password) throws SQLException {
        String createQuery = "INSERT INTO EMAILACCOUNT(USEREMAIL, PASSWORD) VALUES(?,?)";
        int result = 0;

        // Connection is only open for the operation and then immediately closed
        try (Connection connection = DriverManager.getConnection(url, user, this.password);
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
        int result = 0;
        int id = -1;
        String createQuery = "INSERT INTO EMAIL (USEREMAIL, FROMEMAIL, SUBJECT, SENDDATE, RECEIVEDATE, FOLDERNAME, FLAGS) VALUES (?,?,?,?,?,?,?)";
        // Connection is only open for the operation and then immediately closed
        try (Connection connection = DriverManager.getConnection(url, user, password);
                // Using a prepared statement to handle the conversion
                // of special characters in the SQL statement and guard against
                // SQL Injection
                PreparedStatement ps = connection.prepareStatement(createQuery,Statement.RETURN_GENERATED_KEYS);) {
            log.info("Inserting into EMAIL: "+ email);
            ps.setString(1, useremail);
            ps.setString(2, email.getFrom().getEmail());
            ps.setString(3,email.getSubject());
            if(email.getSentDate() != null)
                ps.setTimestamp(4, new java.sql.Timestamp(email.getSentDate().getTime()));//convert to SQLDate
            else
                ps.setNull(4, Types.TIMESTAMP);
            if(email.getReceiveDate() != null)
                ps.setTimestamp(5, new java.sql.Timestamp(email.getReceiveDate().getTime()));//convert to SQLDate
            else
                ps.setNull(5, Types.TIMESTAMP);
            if(email.getFolder() != null)
                ps.setString(6, email.getFolder());
            else
                ps.setString(6, "Draft");
            if(email.getFlags() != null)
                ps.setString(7,email.getFlags().toString());
            else
                ps.setNull(7, Types.VARCHAR);
            
            
            
            result = ps.executeUpdate();
            
            
            if(result > 0){
                try(ResultSet rs = ps.getGeneratedKeys();){
                    rs.next();
                    id = rs.getInt(1);
                    log.info("ID of email inserted: " + id);
                }
            }
        }
        
        if (id != -1) {
            for (MailAddress toAddress : email.getTo()) {
                createToEmail(toAddress.getEmail(), id);
            }

            if (email.getCc() != null) {
                for (MailAddress ccAddress : email.getCc()) {
                    createCcEmail(ccAddress.getEmail(), id);
                }
            }

            if (email.getBcc() != null) {
                for (MailAddress bccAddress : email.getBcc()) {
                    createBccEmail(bccAddress.getEmail(), id);
                }
            }

            for (EmailMessage message : email.getAllMessages()) {
                createEmailMessage(message, id);
            }

            if (email.getAttachments() != null) {
                for (EmailAttachment attachment : email.getAttachments()) {
                    createAttachment(attachment, id);
                }
            }
        }
        
        return result;
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
            log.info("Inserting into EMAILMESSAGE: " + message.getContent());
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
            log.info("Inserting into BCCEMAIL:" + emailAddress);
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
            log.info("Inserting into ATTACHMENT:" + attachment.getName());
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
        ExtendedEmail extendedEmail = new ExtendedEmail();
        String readEmail = "SELECT ID,";
        String readTo;
        String readCc = "SELECT";
        String readBcc = "SELECT";
        return extendedEmail;
    }

    @Override
    public ArrayList<ExtendedEmail> findAllEmailsFor(String email) throws SQLException {
        int id = 0;
        ArrayList<ExtendedEmail> emailList = new ArrayList<ExtendedEmail>();
        String readQuery = "SELECT ID, USEREMAIL, FROMEMAIL, SUBJECT, SENDDATE, RECEIVEDATE, FOLDERNAME, FLAGS "
                + "FROM EMAIL WHERE USEREMAIL = ?";
        
        // Connection is only open for the operation and then immediately closed
        try (Connection connection = DriverManager.getConnection(url, user, password);
                // Using a prepared statement to handle the conversion
                // of special characters in the SQL statement and guard against
                // SQL Injection
                PreparedStatement ps = connection.prepareStatement(readQuery);) {
            ps.setString(1, email);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    emailList.add(createExtendedEmail(resultSet));
                }
            }
        }
        
        return emailList;
    }

    @Override
    public ArrayList<ExtendedEmail> findEmailsFrom(String email) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getEmailID(String fromEmail, String subject, String folderName, Date sendDate, Date receiveDate) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int updateEmail(int ID) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public int updateFolder(String folder, int emailID) throws SQLException {
        String updateQuery = "UPDATE EMAIL SET FOLDERNAME = ?";
        int result = 0;

        // Connection is only open for the operation and then immediately closed
        try (Connection connection = DriverManager.getConnection(url, user, password);
                // Using a prepared statement to handle the conversion
                // of special characters in the SQL statement and guard against
                // SQL Injection
                PreparedStatement ps = connection.prepareStatement(updateQuery);) {
            log.info("UPDATE EMAIL folder:");
            ps.setString(1, folder);
            ps.setInt(2, emailID);

            result = ps.executeUpdate();
        }
        log.info("# of records created : " + result);
        return result;
    }

    @Override
    public int deleteEmail(int ID) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private ExtendedEmail createExtendedEmail(ResultSet resultSet) throws SQLException {
        ExtendedEmail extendedEmail = new ExtendedEmail();
        extendedEmail.from(resultSet.getString("FROMEMAIL"));
        extendedEmail.subject(resultSet.getString("SUBJECT"));
        extendedEmail.sentOn(resultSet.getTimestamp("SENDDATE"));
        extendedEmail.setReceiveDate(resultSet.getTimestamp("RECEIVEDATE"));
        extendedEmail.setFolder(resultSet.getString("FOLDER"));
        extendedEmail.setFlags(new Flags(resultSet.getString("FLAGS")));
        
        
        
        return extendedEmail;
    }

    
}
