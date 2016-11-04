/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hugopham.propertiesmanager;

import com.hugopham.mailmoduleconfig.EmailPropertiesManager;
import com.hugopham.mailmoduleconfig.ConfigEmail;
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
public class PropertiesManagerTest {
    private EmailPropertiesManager pm;
    
    public PropertiesManagerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        pm = new EmailPropertiesManager();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of writeTextProperties method, of class PropertiesManager.
     */
    @Ignore
    @Test
    public void testWriteTextProperties() throws Exception {
        ConfigEmail mailConfig1 = new ConfigEmail("hugo.sender.not.a.bot@gmail.com",
                "JAVAbean517!", "imap.gmail.com" , "smtp.gmail.com" );
        pm.writeTextProperties("", "EmailProperties", mailConfig1);

        //ConfigEmail mailConfig2 = pm.loadTextProperties("", "EmailProperties");

        //assertEquals("The two beans do not match", mailConfig1, mailConfig2);
    }

    /**
     * Test of writeXmlProperties method, of class PropertiesManager.
     */
    @Ignore
    @Test
    public void testWriteXmlProperties() throws Exception {
        ConfigEmail mailConfig1 = new ConfigEmail("hugo.sender.not.a.bot@gmail.com",
                "JAVAbean517!", "imap.gmail.com" , "smtp.gmail.com" );
        pm.writeXmlProperties("", "EmailProperties", mailConfig1);

        //ConfigEmail mailConfig2 = pm.loadXmlProperties("", "EmailProperties");
        
        //assertEquals("The two beans do not match", mailConfig1, mailConfig2);
    }

    /**
     * Test of loadJarTextProperties method, of class PropertiesManager.
     */
    @Test
    @Ignore
    public void testLoadJarTextProperties() throws Exception {
        ConfigEmail mailConfig1 = new ConfigEmail("hugo.sender.not.a.bot@gmail.com",
                "JAVAbean517!", "imap.gmail.com" , "smtp.gmail.com" );
        
        //ConfigEmail mailConfig2 = pm.loadJarTextProperties("jar.properties");
        
        //assertEquals("The two beans do not match", mailConfig1, mailConfig2);
    }
    
}
