package com.hugopham.mailmodules;

import java.util.ArrayList;
import java.util.List;
import jodd.mail.Email;
import jodd.mail.EmailAddress;
import jodd.mail.EmailAttachment;
import jodd.mail.EmailMessage;
import jodd.mail.MailAddress;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author 1334944
 */
public class ExtendedEmailTest {
    ExtendedEmail email = new ExtendedEmail();
    ExtendedEmail email2 = new ExtendedEmail();
    
    public ExtendedEmailTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        EmailAddress e = new EmailAddress("a@gmail.com");
        email.from(e)
            .to("b@b.com").to("c@c.com")
            .subject("test1").addHtml("<html>"
                + "<h1>Hello</h1>"
                + "</html>")
            .attach(EmailAttachment.attachment().file("test.png"));;
        
       email2.from(e.getInternetAddress().getAddress())
            .to("b@b.com").to("c@c.com")
            .subject("test1")
            .addHtml("<html>"
                + "<h1>Hello</h1>"
                + "</html>")
            .attach(EmailAttachment.attachment().file("test.png"));;
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of hashCode method, of class ExtendedEmail.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        assertEquals(email.hashCode(), email.hashCode());
        // TODO review the generated test code and remove the default call to fail.
        // fail("The test case is a prototype.");
    }


    @Test
    @Ignore
    public void testEquals() {
        System.out.println("equals");
        boolean expResult = false; //not same object
        boolean result = email.equals(email2);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testCompareMessage() {
        System.out.println("Compare message");
        assertTrue(email.compareMessage(email2));
        //fail("Message not equal");
    }
    
    @Test
    public void testCompareAttachments() {
        System.out.println("Compare attachments");
        assertTrue(email.compareAttachment(email2));
    }
    
    @Test
    public void testCompareTos() {
        System.out.println("Compare To's");
        assertTrue(email.compareTos(email2));
    }
    
    @Test
    public void testCompareCc() {
        System.out.println("Compare Cc");
        assertTrue(email.compareCc(email2));
    }
}
