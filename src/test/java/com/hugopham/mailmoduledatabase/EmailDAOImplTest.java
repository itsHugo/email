/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hugopham.mailmoduledatabase;

import com.hugopham.mailmodules.ExtendedEmail;
import com.hugopham.mailmodules.SendReceiveModule;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import jodd.mail.EmailAttachment;
import jodd.mail.EmailMessage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Hugo
 */
public class EmailDAOImplTest {
    private final String url = "jdbc:mysql://waldo2.dawsoncollege.qc.ca/CS1334944";
    private final String user = "CS1334944";
    private final String password = "uvillien";
    
    static EmailDAO emailDAO;
    static SendReceiveModule sendReceive;
    
    public EmailDAOImplTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        emailDAO = new EmailDAOImpl();
        sendReceive = new SendReceiveModule();
    }
    
    /**
     * Create email entries in the database after testing.
     * 
     * Creates a sent email and a received email.
     * 
     * @throws SQLException 
     */
    @AfterClass
    public static void tearDownClass() throws SQLException {
        // Create an email entry for the database.
        String useremail = "hugo.sender.not.a.bot@gmail.com";
        ExtendedEmail email = new ExtendedEmail();
        email.from("hugo.sender.not.a.bot@gmail.com").subject("Test Sending")
            .to("hugo.sender.not.a.bot@gmail.com")
            .cc("hugo.pham@hotmail.com")
            .bcc("z0mg_a_hugz@hotmail.com")
            .addHtml("<html><META http-equiv=Content-Type "
                        + "content=\"text/html; charset=utf-8\">"
                        + "<body><h1>Here is my photograph embedded in "
                        + "this email.</h1><img src='cid:car.png'>"
                        + "<h2>THIS IS A TEST</h2></body></html>")
            .attach(EmailAttachment.attachment().file("test.png"))
            .embed(EmailAttachment.attachment().bytes(new File("car.png")));
        
        
        // Insert sent email
        emailDAO.createEmail(useremail, sendReceive.sendEmail(email));
        // Insert received email
        emailDAO.createEmail(useremail, sendReceive.receiveEmail()[0]);
    }
    
    
    @Before
    public void setUp() {
        seedDatabase();
    }
    
    /**
     * This routine recreates the database for every test. This makes sure that
     * a destructive test will not interfere with any other test.
     *
     * This routine is courtesy of Bartosz Majsak, an Arquillian developer at
     * JBoss.
     */
    private void seedDatabase(){
        final String seedDataScript = loadAsString("createEmailDBMySQL.sql");
        try (Connection connection = DriverManager.getConnection(url, user, password);) {
            for (String statement : splitStatements(new StringReader(seedDataScript), ";")) {
                connection.prepareStatement(statement).execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed seeding database", e);
        }
    }
    /**
     * The following methods support the setUp method
     */
    private String loadAsString(final String path) {
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
                Scanner scanner = new Scanner(inputStream)) {
            return scanner.useDelimiter("\\A").next();
        } catch (IOException e) {
            throw new RuntimeException("Unable to close input stream.", e);
        }
    }
    
    // Split squl statements
    private List<String> splitStatements(Reader reader, String statementDelimiter) {
        final BufferedReader bufferedReader = new BufferedReader(reader);
        final StringBuilder sqlStatement = new StringBuilder();
        final List<String> statements = new LinkedList<>();
        try {
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || isComment(line)) {
                    continue;
                }
                sqlStatement.append(line);
                if (line.endsWith(statementDelimiter)) {
                    statements.add(sqlStatement.toString());
                    sqlStatement.setLength(0);
                }
            }
            return statements;
        } catch (IOException e) {
            throw new RuntimeException("Failed parsing sql", e);
        }
    }
    
    // Detects comments
    private boolean isComment(final String line) {
        return line.startsWith("--") || line.startsWith("//") || line.startsWith("/*");
    }
    // End of supporting methods
    
    @After
    public void tearDown() {
        // Recreates the database.
        seedDatabase();
    }

    /**
     * Test of createEmail method, of class EmailDAOImpl.
     */
    @Test
    public void testCreateEmail() throws Exception {
        // Create email account in order to link email
        // Setup
        System.out.println("createEmail");
        String useremail = "hugo.sender.not.a.bot@gmail.com";
        ExtendedEmail email = new ExtendedEmail();
        email.folder("Sent").from("hugo.sender.not.a.bot@gmail.com").subject("Test Sending")
            .to("hugo.sender.not.a.bot@gmail.com")
            .cc("hugo.pham@hotmail.com")
            .bcc("z0mg_a_hugz@hotmail.com")
            .addHtml("<html><META http-equiv=Content-Type "
                        + "content=\"text/html; charset=utf-8\">"
                        + "<body><h1>Here is my photograph embedded in "
                        + "this email.</h1><img src='cid:car.png'>"
                        + "<h2>THIS IS A TEST</h2></body></html>")
            .attach(EmailAttachment.attachment().file("test.png"))
            .embed(EmailAttachment.attachment().bytes(new File("car.png")));
        
        // Test
        email = sendReceive.sendEmail(email);
        int expResult = 1;
        
        int result = emailDAO.createEmail(useremail, email);
        
        // Assert
        assertEquals(expResult, result);
    }
    
    /**
     * Test of createEmail method, of class EmailDAOImpl.
     */
    @Test
    public void testCreateReceivedEmail() throws Exception {
        // Create email account in order to link email
        System.out.println("createEmail");
        // Setup
        String useremail = "hugo.sender.not.a.bot@gmail.com";
        ExtendedEmail email = new ExtendedEmail();
        email.folder("Sent").from("hugo.sender.not.a.bot@gmail.com").subject("Test Sending")
            .to("hugo.sender.not.a.bot@gmail.com")
            .cc("hugo.pham@hotmail.com")
            .bcc("z0mg_a_hugz@hotmail.com")
            .addHtml("<html><META http-equiv=Content-Type "
                        + "content=\"text/html; charset=utf-8\">"
                        + "<body><h1>Here is my photograph embedded in "
                        + "this email.</h1><img src='cid:car.png'>"
                        + "<h2>THIS IS A TEST</h2></body></html>")
            .attach(EmailAttachment.attachment().file("test.png"))
            .embed(EmailAttachment.attachment().bytes(new File("car.png")));
        
        // Test
        // Send email
        sendReceive.sendEmail(email);
        
        // Receive email
        email = sendReceive.receiveEmail()[0];
        int expResult = 1;
        
        int result = emailDAO.createEmail(useremail, email);
        
        // Assert
        assertEquals(expResult, result);
    }
    
    /**
     * Test find all emails in database.
     * 
     * @throws Exception 
     */
    @Test
    public void testFindAll() throws Exception {
        System.out.println("findAll");
        // Setup
        String useremail = "hugo.sender.not.a.bot@gmail.com";
        ExtendedEmail email = new ExtendedEmail();
        email.folder("Sent").from("hugo.sender.not.a.bot@gmail.com").subject("Test Sending")
            .to("hugo.sender.not.a.bot@gmail.com")
            .cc("hugo.pham@hotmail.com")
            .bcc("z0mg_a_hugz@hotmail.com")
            .addHtml("<html><META http-equiv=Content-Type "
                        + "content=\"text/html; charset=utf-8\">"
                        + "<body><h1>Here is my photograph embedded in "
                        + "this email.</h1><img src='cid:car.png'>"
                        + "<h2>THIS IS A TEST</h2></body></html>")
            .attach(EmailAttachment.attachment().file("test.png"))
            .embed(EmailAttachment.attachment().bytes(new File("car.png")));
        
        // Test
        emailDAO.createEmail(useremail, email);
        ArrayList<ExtendedEmail> list = emailDAO.findAll();
        // 2 because there is already an email created in the database seeding process
        int expected = 2;
        
        // Assert
        assertEquals(expected, list.size());
    }
}
