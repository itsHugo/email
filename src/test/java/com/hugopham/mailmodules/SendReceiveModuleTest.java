package com.hugopham.mailmodules;

import com.hugopham.mailmoduleconfig.ConfigEmail;
import java.io.File;
import jodd.mail.EmailAddress;
import jodd.mail.EmailAttachment;
import jodd.mail.EmailAttachmentBuilder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author 1334944
 */
public class SendReceiveModuleTest {
    ExtendedEmail e;
    ConfigEmail c;
    SendReceiveModule s;
    public SendReceiveModuleTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        c = new ConfigEmail("hugo.sender.not.a.bot@gmail.com",
                "JAVAbean517!","imap.gmail.com","smtp.gmail.com");
        s = new SendReceiveModule(c);
        e = new ExtendedEmail();
        //setup email
        e.from(c.getEmailSend()).subject("Test Sending")
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
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of sendEmail method, of class SendModule.
     */
    //@Ignore("Working")
    @Test
    public void testSendEmail_ExtendedEmail() {
        System.out.println("sendEmail with ExtendedEmail param");
        
        ExtendedEmail test = s.sendEmail(e);
        
        assertNotNull("",test);
    }

    /**
     * Test of receiveEmail method, of class SendModule.
     */
    @Test
    public void testReceiveEmail() {
        System.out.println("receiveEmail");
        ExtendedEmail[] test = s.receiveEmail();
        
        assertNotNull("",test);
    }
    
    @Test
    public void testComparingSendReceive() {
        
    }
}
