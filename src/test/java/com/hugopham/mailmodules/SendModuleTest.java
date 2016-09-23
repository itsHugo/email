/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hugopham.mailmodules;

import jodd.mail.EmailAddress;
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
public class SendModuleTest {
    ExtendedEmail e;
    ConfigEmail c;
    SendModule s;
    public SendModuleTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        c = new ConfigEmail("smtp.gmail.com","imap.gmail.com",
                new EmailAddress("hugo.sender.not.a.bot@gmail.com"),
                "JAVAbean517!");
        s = new SendModule(c);
        e = new ExtendedEmail();
        e.from(c.getEmailSend());
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of sendEmail method, of class SendModule.
     */
    @Test
    public void testSendEmail_0args() {
        System.out.println("sendEmail");
        SendModule instance = new SendModule();
        ExtendedEmail expResult = null;
        ExtendedEmail result = instance.sendEmail();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of sendEmail method, of class SendModule.
     */
    @Test
    public void testSendEmail_ExtendedEmail() {
        System.out.println("sendEmail");
        ExtendedEmail mail = null;
        SendModule instance = new SendModule();
        ExtendedEmail expResult = null;
        ExtendedEmail result = instance.sendEmail(mail);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of receiveEmail method, of class SendModule.
     */
    @Test
    public void testReceiveEmail() {
        System.out.println("receiveEmail");
        SendModule instance = new SendModule();
        ExtendedEmail[] expResult = null;
        ExtendedEmail[] result = instance.receiveEmail();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setConfigEmail method, of class SendModule.
     */
    @Test
    public void testSetConfigEmail() {
        System.out.println("setConfigEmail");
        ConfigEmail c = null;
        SendModule instance = new SendModule();
        instance.setConfigEmail(c);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getConfigEmail method, of class SendModule.
     */
    @Test
    public void testGetConfigEmail() {
        System.out.println("getConfigEmail");
        SendModule instance = new SendModule();
        ConfigEmail expResult = null;
        ConfigEmail result = instance.getConfigEmail();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
