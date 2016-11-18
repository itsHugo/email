package com.hugopham.mailmoduledatabase;

import com.hugopham.mailmoduleconfig.ConfigDatabase;
import com.hugopham.mailmodules.ExtendedEmail;
import com.hugopham.propertiesmanager.DatabasePropertiesManager;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
 * Implementing class. Contains CRUD methods to interact with the database to
 * get, update and retrieve data of an ExtendedEmail.
 *
 * @author Hugo Pham
 */
public class EmailDAOImpl implements EmailDAO {

    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

    private final String url;
    private final String user;
    private final String password;

    /**
     * Default constructor.
     * Sets the class variables with properties loaded from file.
     */
    public EmailDAOImpl() {
        super();
        DatabasePropertiesManager manager = new DatabasePropertiesManager();
        ConfigDatabase config = new ConfigDatabase();
        try{
            config = manager.loadTextProperties("", "DatabaseProperties");
        } catch (IOException ex){
            log.error("DatabaseProperties.properties not found.");
        }
        
        this.url = config.getProtocol() + ":" 
                + config.getDriver() + "://"
                + config.getUrl() + "/"
                + config.getDatabase();
        this.user = config.getUser();
        this.password = config.getPassword();
    }

    public EmailDAOImpl(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    /**
     * 
     * @param useremail
     * @param email
     * @return 
     * @throws SQLException 
     */
    @Override
    public int createEmail(String useremail, ExtendedEmail email) throws SQLException {
        int result = 0;
        int id = -1;
        String createQuery = "INSERT INTO EMAIL (USEREMAIL, FROMEMAIL, SUBJECT, SENDDATE, RECEIVEDATE, FOLDERNAME, FLAGS) VALUES (?,?,?,?,?,?,?)";
        // Connection is only open for the operation and then immediately closed
        try (Connection connection = DriverManager.getConnection(url, user, password);
                // Using a prepared statement to handle the conversion
                // of special characters in the SQL statement and guard against
                // SQL Injection
                PreparedStatement ps = connection.prepareStatement(createQuery, Statement.RETURN_GENERATED_KEYS);) {
            log.info("Inserting into EMAIL: " + email);
            ps.setString(1, useremail);
            ps.setString(2, email.getFrom().getEmail());
            ps.setString(3, email.getSubject());
            if (email.getSentDate() != null) {
                ps.setTimestamp(4, new java.sql.Timestamp(email.getSentDate().getTime()));//convert to SQLDate
            } else {
                ps.setNull(4, Types.TIMESTAMP);
            }
            if (email.getReceiveDate() != null) {
                ps.setTimestamp(5, new java.sql.Timestamp(email.getReceiveDate().getTime()));//convert to SQLDate
            } else {
                ps.setNull(5, Types.TIMESTAMP);
            }
            if (email.getFolder() != null) {
                ps.setString(6, email.getFolder());
            } else {
                ps.setString(6, "Draft");
            }
            if (email.getFlags() != null) {
                ps.setString(7, email.getFlags().toString());
            } else {
                ps.setNull(7, Types.VARCHAR);
            }

            result = ps.executeUpdate();

            if (result > 0) {
                try (ResultSet rs = ps.getGeneratedKeys();) {
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

    /**
     * Inserts an email message into the database.
     * 
     * @param message Email message
     * @param emailID Email object associated
     * @return int Number of records affected
     * @throws SQLException 
     */
    @Override
    public int createEmailMessage(EmailMessage message, int emailID) throws SQLException {
        String createQuery = "INSERT INTO EMAILMESSAGE(CONTENT, ENCODING, MIMETYPE, EMAILID) VALUES (?,?,?,?)";
        int result = 0;

        // Connection is only open for the operation and then immediately closed
        try (Connection connection = DriverManager.getConnection(url, user, password);
                // Using a prepared statement to handle the conversion
                // of special characters in the SQL statement and guard against
                // SQL Injection
                PreparedStatement ps = connection.prepareStatement(createQuery);) {
            log.info("Inserting into EMAILMESSAGE: " + message);
            ps.setString(1, message.getContent());
            ps.setString(2, message.getEncoding());
            ps.setString(3, message.getMimeType());
            ps.setInt(4, emailID);

            result = ps.executeUpdate();
        }
        log.info("# of records created : " + result);
        return result;
    }

    /**
     * Inserts a To email address into the database.
     * 
     * @param emailAddress To email address
     * @param emailID Email object associated
     * @return 
     * @throws SQLException 
     */
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

    /**
     * Inserts a Cc email address into the database.
     * 
     * @param emailAddress CC email address
     * @param emailID Email object associated
     * @return int Number of rows affected
     * @throws SQLException 
     */
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

    /**
     * Inserts a Bcc email address into the database.
     * 
     * @param emailAddress
     * @param emailID
     * @return
     * @throws SQLException 
     */
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

    /**
     * Inserts a an attachment into the database.
     * 
     * @param attachment
     * @param emailID
     * @return
     * @throws SQLException 
     */
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
            log.info("Inserting into ATTACHMENT:\n\t" + attachment.getName()
                    + "\n\t" + attachment.getContentId()
                    + "\n\t" + attachment.getEncodedName()
                    + "\n\t" + attachment.getSize());
            ps.setBlob(1, new ByteArrayInputStream(attachment.toByteArray()));
            ps.setInt(2, emailID);

            result = ps.executeUpdate();
        }
        log.info("# of records created : " + result);
        return result;
    }

    /**
     * Retrieves all the e-mails in the database.
     * 
     * @return ArrayList of e-mails
     * @throws SQLException 
     */
    @Override
    public ArrayList<ExtendedEmail> findAll() throws SQLException {
        ArrayList<ExtendedEmail> emailList = new ArrayList<>();
        String readQuery = "SELECT ID, USEREMAIL, FROMEMAIL, SUBJECT, SENDDATE, RECEIVEDATE, FOLDERNAME, FLAGS "
                + "FROM EMAIL";

        // Connection is only open for the operation and then immediately closed
        try (Connection connection = DriverManager.getConnection(url, user, password);
                // Using a prepared statement to handle the conversion
                // of special characters in the SQL statement and guard against
                // SQL Injection
                PreparedStatement ps = connection.prepareStatement(readQuery);) {
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    emailList.add(createExtendedEmail(resultSet));
                }
            }
        }
        return emailList;
    }

    @Override
    public ExtendedEmail findEmailByID(int id) throws SQLException {
        ExtendedEmail email = new ExtendedEmail();
        String readQuery = "SELECT ID, USEREMAIL, FROMEMAIL, SUBJECT, SENDDATE, RECEIVEDATE, FOLDERNAME, FLAGS "
                + "FROM EMAIL WHERE ID = ?";

        // Connection is only open for the operation and then immediately closed
        try (Connection connection = DriverManager.getConnection(url, user, password);
                // Using a prepared statement to handle the conversion
                // of special characters in the SQL statement and guard against
                // SQL Injection
                PreparedStatement ps = connection.prepareStatement(readQuery);) {
            ps.setInt(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    email = (createExtendedEmail(resultSet));
                }
            }
        }
        return email;
    }

    /**
     * Retrieves all the e-mails with the matching username in the database.
     * 
     * @return ArrayList of e-mails
     * @throws SQLException 
     */
    @Override
    public ArrayList<ExtendedEmail> findAllEmailsFor(String useremail) throws SQLException {
        ArrayList<ExtendedEmail> emailList = new ArrayList<>();
        String readQuery = "SELECT ID, USEREMAIL, FROMEMAIL, SUBJECT, SENDDATE, RECEIVEDATE, FOLDERNAME, FLAGS "
                + "FROM EMAIL WHERE USEREMAIL = ?";

        // Connection is only open for the operation and then immediately closed
        try (Connection connection = DriverManager.getConnection(url, user, password);
                // Using a prepared statement to handle the conversion
                // of special characters in the SQL statement and guard against
                // SQL Injection
                PreparedStatement ps = connection.prepareStatement(readQuery);) {
            ps.setString(1, useremail);
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    emailList.add(createExtendedEmail(resultSet));
                }
            }
        }
        return emailList;
    }

    /**
     * Retrieves all the e-mails with the matching from field.
     * 
     * @param email 
     * @return ArrayList of e-mails
     * @throws SQLException 
     */
    @Override
    public ArrayList<ExtendedEmail> findEmailsFrom(String email) throws SQLException {
        ArrayList<ExtendedEmail> emailList = new ArrayList<>();
        String readQuery = "SELECT ID, USEREMAIL, FROMEMAIL, SUBJECT, SENDDATE, RECEIVEDATE, FOLDERNAME, FLAGS "
                + "FROM EMAIL WHERE FROMEMAIL = ?";

        // Connection is only open for the operation and then immediately closed
        try (Connection connection = DriverManager.getConnection(url, user, password);
                // Using a prepared statement to handle the conversion
                // of special characters in the SQL statement and guard against
                // SQL Injection
                PreparedStatement ps = connection.prepareStatement(readQuery);) {
            ps.setString(1, email);
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    emailList.add(createExtendedEmail(resultSet));
                }
            }
        }
        return emailList;
    }

    /**
     * Retrieves all the "To" e-mail addresses for an e-mail.
     * 
     * @param ID
     * @return Array of e-mails
     * @throws SQLException 
     */
    @Override
    public String[] findTosFor(int ID) throws SQLException {
        ArrayList<String> list = new ArrayList<>();
        String findQuery = "SELECT EMAILADDRESS FROM TOEMAIL WHERE EMAILID = ?";

        // Connection is only open for the operation and then immediately closed
        try (Connection connection = DriverManager.getConnection(url, user, password);
                // Using a prepared statement to handle the conversion
                // of special characters in the SQL statement and guard against
                // SQL Injection
                PreparedStatement ps = connection.prepareStatement(findQuery);) {
            ps.setInt(1, ID);
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    list.add(resultSet.getString("EMAILADDRESS"));
                }
            }
        }
        return list.toArray(new String[list.size()]);
    }

    /**
     * Retrieves all the "Cc" e-mail addresses for an e-mail.
     * 
     * @param ID
     * @return Array of e-mails
     * @throws SQLException 
     */
    @Override
    public String[] findCcFor(int ID) throws SQLException {
        ArrayList<String> list = new ArrayList<>();
        String findQuery = "SELECT EMAILADDRESS FROM CCEMAIL WHERE EMAILID = ?";

        // Connection is only open for the operation and then immediately closed
        try (Connection connection = DriverManager.getConnection(url, user, password);
                // Using a prepared statement to handle the conversion
                // of special characters in the SQL statement and guard against
                // SQL Injection
                PreparedStatement ps = connection.prepareStatement(findQuery);) {
            ps.setInt(1, ID);
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    list.add(resultSet.getString("EMAILADDRESS"));
                }
            }
        }
        return list.toArray(new String[list.size()]);
    }

    /**
     * Retrieves all the "Bcc" e-mail addresses for an e-mail.
     * 
     * @param ID
     * @return Array of e-mails
     * @throws SQLException 
     */
    @Override
    public String[] findBccFor(int ID) throws SQLException {
        ArrayList<String> list = new ArrayList<>();
        String findQuery = "SELECT EMAILADDRESS FROM BCCEMAIL WHERE EMAILID = ?";

        // Connection is only open for the operation and then immediately closed
        try (Connection connection = DriverManager.getConnection(url, user, password);
                // Using a prepared statement to handle the conversion
                // of special characters in the SQL statement and guard against
                // SQL Injection
                PreparedStatement ps = connection.prepareStatement(findQuery);) {
            ps.setInt(1, ID);
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    list.add(resultSet.getString("EMAILADDRESS"));
                }
            }
        }
        return list.toArray(new String[list.size()]);
    }

    /**
     * Retrieves all the attachments for an e-mail.
     * 
     * @param ID
     * @return ArrayList of attachments
     * @throws SQLException 
     */
    @Override
    public ArrayList<EmailAttachment> findAttachmentsFor(int ID) throws SQLException {
        ArrayList<EmailAttachment> list = new ArrayList<>();
        String findQuery = "SELECT FILEDATA FROM ATTACHMENT WHERE EMAILID = ?";

        // Connection is only open for the operation and then immediately closed
        try (Connection connection = DriverManager.getConnection(url, user, password);
                // Using a prepared statement to handle the conversion
                // of special characters in the SQL statement and guard against
                // SQL Injection
                PreparedStatement ps = connection.prepareStatement(findQuery);) {
            ps.setInt(1, ID);
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    Blob blob = resultSet.getBlob("FILEDATA");
                    InputStream in = blob.getBinaryStream();
                    File file = File.createTempFile("attachment", ".", new File(""));
                    OutputStream out = new FileOutputStream(file);
                    byte[] buff = new byte[4096];
                    int len = 0;

                    while ((len = in.read(buff)) != -1) {
                        out.write(buff, 0, len);
                    }

                    in.close();
                    out.close();
                    EmailAttachment attachment = EmailAttachment.attachment()
                            .file(file).create();
                    list.add(attachment);
                    log.info("Found attachment:\n\t" + attachment.getName()
                            + "\n\t" + attachment.getContentId()
                            + "\n\t" + attachment.getEncodedName()
                            + "\n\t" + attachment.getSize());
                }
            } catch (IOException e) {
                log.error("Find attachments for error!" + e);
            }
        }
        return list;
    }

    /**
     * Retrieves all the messages for an e-mail.
     * 
     * @param ID
     * @return ArrayList of messages
     * @throws SQLException 
     */
    @Override
    public ArrayList<EmailMessage> findMessagesFor(int ID) throws SQLException {
        ArrayList<EmailMessage> list = new ArrayList<>();
        String findQuery = "SELECT CONTENT, ENCODING, MIMETYPE FROM EMAILMESSAGE WHERE EMAILID = ?";

        // Connection is only open for the operation and then immediately closed
        try (Connection connection = DriverManager.getConnection(url, user, password);
                // Using a prepared statement to handle the conversion
                // of special characters in the SQL statement and guard against
                // SQL Injection
                PreparedStatement ps = connection.prepareStatement(findQuery);) {
            ps.setInt(1, ID);
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    list.add(new EmailMessage(resultSet.getString("CONTENT"),
                            resultSet.getString("ENCODING"), resultSet.getString("MIMETYPE")));
                }
            }
        }
        return list;
    }

    /**
     * Retrieves all the folder names from the database.
     * 
     * @return ArrayList of folder names
     * @throws SQLException 
     */
    @Override
    public ArrayList<String> findAllFolders() throws SQLException {
        ArrayList<String> list = new ArrayList<>();
        String findQuery = "SELECT FOLDERNAME FROM FOLDER";
        
        // Connection is only open for the operation and then immediately closed
        try (Connection connection = DriverManager.getConnection(url, user, password);
                // Using a prepared statement to handle the conversion
                // of special characters in the SQL statement and guard against
                // SQL Injection
                PreparedStatement ps = connection.prepareStatement(findQuery);) {
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    list.add(resultSet.getString("FOLDERNAME"));
                }
            }
        }
        return list;
    }

    
    @Override
    public int getEmailID(String fromEmail, String subject, String folderName, Date sendDate, Date receiveDate) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int updateEmail(int ID) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Updates which folder an e-mail belongs to.
     * 
     * @param folder
     * @param emailID
     * @return
     * @throws SQLException 
     */
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
        log.info("# of records updated : " + result);
        return result;
    }

    /**
     * Deletes an e-mail.
     * 
     * @param ID
     * @return number of rows affected
     * @throws SQLException 
     */
    @Override
    public int deleteEmail(int ID) throws SQLException {
        String createQuery = "DELETE FROM EMAIL WHERE ID = ?";
        int result = 0;

        // Connection is only open for the operation and then immediately closed
        try (Connection connection = DriverManager.getConnection(url, user, password);
                // Using a prepared statement to handle the conversion
                // of special characters in the SQL statement and guard against
                // SQL Injection
                PreparedStatement ps = connection.prepareStatement(createQuery);) {
            log.info("Deleting email with ID:" + ID);
            ps.setInt(1, ID);

            result = ps.executeUpdate();
        }
        log.info("# of records deleted : " + result);
        return result;
    }

    /**
     * Retrieves the data from the database with the resultSet coming from the
     * EMAIL table. Sets the data to an ExtendedEmail object and returns it.
     *
     * @param resultSet
     * @return ExtendedEmail object
     * @throws SQLException
     */
    private ExtendedEmail createExtendedEmail(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("ID");
        ExtendedEmail extendedEmail = new ExtendedEmail();
        extendedEmail.from(resultSet.getString("FROMEMAIL"));
        extendedEmail.subject(resultSet.getString("SUBJECT"));
        extendedEmail.sentOn(resultSet.getTimestamp("SENDDATE"));
        extendedEmail.setReceiveDate(resultSet.getTimestamp("RECEIVEDATE"));
        extendedEmail.setFolder(resultSet.getString("FOLDERNAME"));

        if (resultSet.getString("FLAGS") != null) {
            extendedEmail.setFlags(new Flags(resultSet.getString("FLAGS")));
        }

        extendedEmail.to(findTosFor(id));
        extendedEmail.bcc(findBccFor(id));
        extendedEmail.cc(findCcFor(id));

        for (EmailMessage message : findMessagesFor(id)) {
            extendedEmail.addMessage(message);
        }

        for (EmailAttachment attachment : findAttachmentsFor(id)) {
            extendedEmail.attach(attachment);
        }

        return extendedEmail;
    }

}
