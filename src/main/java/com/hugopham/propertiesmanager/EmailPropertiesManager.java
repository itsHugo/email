package com.hugopham.propertiesmanager;

import com.hugopham.mailmoduleconfig.ConfigEmail;
import static java.nio.file.Files.newInputStream;
import static java.nio.file.Files.newOutputStream;
import static java.nio.file.Paths.get;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Class containing methods to load and save a MailConfig object's properties.
 * Supported formats are text, xml, and jar files.
 * 
 * @author Hugo Pham
 * @version 1.0.0
 * @since 1.8
 */
public class EmailPropertiesManager {
    /**
     * Returns a MailConfig object with the contents of the properties file
     *
     * @param path Must exist, will not be created
     * @param propFileName Name of the properties file
     * @return The bean loaded with the properties
     * @throws IOException
     */
    public final ConfigEmail loadTextProperties(final String path, 
            final String propFileName) throws IOException {
        boolean found = false;
        Properties prop = new Properties();

        Path txtFile = get(path, propFileName + ".properties");

        ConfigEmail mailConfig = new ConfigEmail();
        

        // File must exist
        if (Files.exists(txtFile)) {
            try (InputStream propFileStream = newInputStream(txtFile);) {
                prop.load(propFileStream);
            }
            mailConfig.setEmailSend(prop.getProperty("userEmailAddress"));
            mailConfig.setEmailSendPwd(prop.getProperty("password"));
            mailConfig.setImapServerName(prop.getProperty("imapServerName"));
            mailConfig.setSmtpServerName(prop.getProperty("smtpServerName"));
            
            found = true;
        }
        if(found)
            return mailConfig;
        else
            throw new IOException(propFileName + " file not found.");
    }
    
    /**
     * Returns a MailConfig object with the contents of the properties file
     * that is in an XML format
     *
     * @param path Must exist, will not be created. Empty string means root of
     * program folder
     * @param propFileName Name of the properties file
     * @return The bean loaded with the properties
     * @throws IOException
     */
    public final ConfigEmail loadXmlProperties(final String path, 
            final String propFileName) throws IOException {
        boolean found = false;
        Properties prop = new Properties();

        // The path of the XML file
        Path xmlFile = get(path, propFileName + ".xml");

        ConfigEmail mailConfig = new ConfigEmail();

        // File must exist
        if (Files.exists(xmlFile)) {
            try (InputStream propFileStream = newInputStream(xmlFile);) {
                prop.loadFromXML(propFileStream);
            }
            mailConfig.setEmailSend(prop.getProperty("userEmailAddress"));
            mailConfig.setEmailSendPwd(prop.getProperty("password"));
            mailConfig.setImapServerName(prop.getProperty("imapServerName"));
            mailConfig.setSmtpServerName(prop.getProperty("smtpServerName"));
            
            found = true;
        }
        if(found)
            return mailConfig;
        else
            throw new IOException(propFileName + " file not found.");
    }
    
    /**
     * Creates a plain text properties file based on the parameters
     *
     * @param path Must exist, will not be created
     * @param propFileName Name of the properties file
     * @param mailConfig The bean to store into the properties
     * @throws IOException
     */
    public final void writeTextProperties(final String path, 
            final String propFileName, final ConfigEmail mailConfig) 
            throws IOException {

        Properties prop = new Properties();
        
        prop.setProperty("kek", "kek");

        prop.setProperty("userEmailAddress", mailConfig.getEmailSend());
        prop.setProperty("password", mailConfig.getEmailSendPwd());
        prop.setProperty("imapServerName", mailConfig.getImapServerName());
        prop.setProperty("smtpServerName", mailConfig.getSmtpServerName());

        Path txtFile = get(path, propFileName + ".properties");

        // Creates the file or if file exists it is truncated to length of zero
        // before writing
        try (OutputStream propFileStream = newOutputStream(txtFile)) {
            prop.store(propFileStream, "Email Properties");
        }
    }
    
    /**
     * Creates an XML properties file based on the parameters
     *
     * @param path Must exist, will not be created
     * @param propFileName Name of the properties file. Empty string means root
     * of program folder
     * @param mailConfig The bean to store into the properties
     * @throws IOException
     */
    public final void writeXmlProperties(final String path, 
            final String propFileName, final ConfigEmail mailConfig) 
            throws IOException {

        Properties prop = new Properties();

        prop.setProperty("userEmailAddress", mailConfig.getEmailSend());
        prop.setProperty("password", mailConfig.getEmailSendPwd());
        prop.setProperty("imapServerName", mailConfig.getImapServerName());
        prop.setProperty("smtpServerName", mailConfig.getSmtpServerName());

        Path xmlFile = get(path, propFileName + ".xml");

        // Creates the file or if file exists it is truncated to length of zero
        // before writing
        try (OutputStream propFileStream = newOutputStream(xmlFile)) {
            prop.storeToXML(propFileStream, "XML Email Properties");
        }
    }
    
    /**
     * Retrieve the properties file. This syntax rather than normal File I/O is
     * employed because the properties file is inside the jar. The technique
     * shown here will work in both Java SE and Java EE environments. A similar
     * technique is used for loading images.
     *
     * In a Maven project all configuration files, images and other files go
     * into src/main/resources. The files and folders placed there are moved to
     * the root of the project when it is built.
     *
     * @param propFileName : Name of the properties file
     * @return The bean loaded with the properties
     * @throws IOException : Error while reading the file
     * @throws NullPointerException : File not found
     */
    public final ConfigEmail loadJarTextProperties(final String propFileName) 
            throws IOException, NullPointerException {
        boolean found = false;
           
        Properties prop = new Properties();
        ConfigEmail mailConfig = new ConfigEmail();

        // There is no exists method for files in a jar so we try to get the
        // resource and if its not there it returns a null
        if (this.getClass().getResource("/" + propFileName) != null) {
            // Assumes that the properties file is in the root of the
            // project/jar.
            // Include a path from the root if required.
            try (InputStream stream = this.getClass()
                    .getResourceAsStream("/" + propFileName);) {
                prop.load(stream);
            }
            mailConfig.setEmailSend(prop.getProperty("userEmailAddress"));
            mailConfig.setEmailSendPwd(prop.getProperty("password"));
            mailConfig.setImapServerName(prop.getProperty("imapServerName"));
            mailConfig.setSmtpServerName(prop.getProperty("smtpServerName"));
            
            found = true;
        }
        
        if(found)
            return mailConfig;
        else
            throw new IOException(propFileName + " file not found.");
    }
    
}
