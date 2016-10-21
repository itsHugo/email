/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hugopham.mailmoduledatabase;

import com.hugopham.mailmodules.ExtendedEmail;
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
    
    EmailDAO emailDAO;
    
    public EmailDAOImplTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    /**
     * This routine recreates the database for every test. This makes sure that
     * a destructive test will not interfere with any other test.
     *
     * This routine is courtesy of Bartosz Majsak, an Arquillian developer at
     * JBoss.
     */
    @Before
    public void setUp() {
        final String seedDataScript = loadAsString("createEmailDBMySQL.sql");
        try (Connection connection = DriverManager.getConnection(url, user, password);) {
            for (String statement : splitStatements(new StringReader(seedDataScript), ";")) {
                connection.prepareStatement(statement).execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed seeding database", e);
        }
        emailDAO = new EmailDAOImpl();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of createEmailAccount method, of class EmailDAOImpl.
     */
    @Ignore
    @Test
    public void testCreateEmailAccount() throws Exception {
        System.out.println("createEmailAccount");
        String email = "hugo.sender.not.a.bot@gmail.com";
        String userpassword = "JAVAbean517!";
        int expResult = 1;
        
        int result = emailDAO.createEmailAccount(email, userpassword);
        assertEquals(expResult, result);
    }

    /**
     * Test of createEmail method, of class EmailDAOImpl.
     */
    @Ignore
    @Test
    public void testCreateEmail() throws Exception {
        // Create email account in order to link email
        testCreateEmailAccount();
        System.out.println("createEmail");
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
        int expResult = 1;
        int result = emailDAO.createEmail(useremail, email);
        assertEquals(expResult, result);
    }


    /**
     * Test of findAll method, of class EmailDAOImpl.
     */
    @Ignore
    @Test
    public void testFindAll() throws Exception {
        System.out.println("findAll");
        EmailDAOImpl instance = new EmailDAOImpl();
        ArrayList<ExtendedEmail> expResult = null;
        ArrayList<ExtendedEmail> result = instance.findAll();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findAllEmailsFor method, of class EmailDAOImpl.
     */
    @Test
    public void testFindAllEmailsFor() throws Exception {
        // Create email to get
        testCreateEmail();
        System.out.println("findAllEmailsFor");
        String email = "hugo.sender.not.a.bot@gmail.com";
        int expResult = 1;
        int result = emailDAO.findAllEmailsFor(email).size();
        assertEquals(expResult, result);
    }

    /**
     * Test of findEmailsFrom method, of class EmailDAOImpl.
     */
    @Ignore
    @Test
    public void testFindEmailsFrom() throws Exception {
        System.out.println("findEmailsFrom");
        String email = "";
        EmailDAOImpl instance = new EmailDAOImpl();
        ArrayList<ExtendedEmail> expResult = null;
        ArrayList<ExtendedEmail> result = instance.findEmailsFrom(email);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
    @Ignore
    @Test
    public void testFindAttachmentsFor() throws Exception {
        //Create email in order to get attachments
        testCreateEmail();
        System.out.println("findAttachmentsFor");
        int ID = 1;
        ArrayList<EmailAttachment> list = emailDAO.findAttachmentsFor(1);
        for(EmailAttachment attachment : list){
            System.out.println(attachment);
        }
        int expected = 2;
        assertEquals(expected, list.size());
    }

    /**
     * Test of getEmailID method, of class EmailDAOImpl.
     */
    @Ignore
    @Test
    public void testGetEmailID() throws Exception {
        System.out.println("getEmailID");
        String fromEmail = "";
        String subject = "";
        String folderName = "";
        Date sendDate = null;
        Date receiveDate = null;
        EmailDAOImpl instance = new EmailDAOImpl();
        int expResult = 0;
        int result = instance.getEmailID(fromEmail, subject, folderName, sendDate, receiveDate);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateEmail method, of class EmailDAOImpl.
     */
    @Ignore
    @Test
    public void testUpdateEmail() throws Exception {
        System.out.println("updateEmail");
        int ID = 0;
        EmailDAOImpl instance = new EmailDAOImpl();
        int expResult = 0;
        int result = instance.updateEmail(ID);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateFolder method, of class EmailDAOImpl.
     */
    @Ignore
    @Test
    public void testUpdateFolder() throws Exception {
        System.out.println("updateFolder");
        String folder = "";
        int emailID = 0;
        EmailDAOImpl instance = new EmailDAOImpl();
        int expResult = 0;
        int result = instance.updateFolder(folder, emailID);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteEmail method, of class EmailDAOImpl.
     */
    @Ignore
    @Test
    public void testDeleteEmail() throws Exception {
        System.out.println("deleteEmail");
        int ID = 0;
        EmailDAOImpl instance = new EmailDAOImpl();
        int expResult = 0;
        int result = instance.deleteEmail(ID);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
    /**
     * The following methods support the seedDatabse method
     */
    private String loadAsString(final String path) {
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
                Scanner scanner = new Scanner(inputStream)) {
            return scanner.useDelimiter("\\A").next();
        } catch (IOException e) {
            throw new RuntimeException("Unable to close input stream.", e);
        }
    }

    private List<String> splitStatements(Reader reader, String statementDelimiter) {
        final BufferedReader bufferedReader = new BufferedReader(reader);
        final StringBuilder sqlStatement = new StringBuilder();
        final List<String> statements = new LinkedList<String>();
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

    private boolean isComment(final String line) {
        return line.startsWith("--") || line.startsWith("//") || line.startsWith("/*");
    }
    
}
