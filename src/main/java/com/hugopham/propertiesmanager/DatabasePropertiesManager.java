package com.hugopham.propertiesmanager;

import com.hugopham.mailmoduleconfig.ConfigDatabase;
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
 * Class containing methods to load and save a DatabaseConfig object's properties.
 * Supported formats are text, xml, and jar files.
 * @author Hugo Pham
 */
public class DatabasePropertiesManager {
    /**
     * Returns a DatabaseConfig object with the contents of the properties file
     *
     * @param path Must exist, will not be created
     * @param propFileName Name of the properties file
     * @return The bean loaded with the properties
     * @throws IOException
     */
    public final ConfigDatabase loadTextProperties(final String path, 
            final String propFileName) throws IOException {
        boolean found = false;
        Properties prop = new Properties();

        Path txtFile = get(path, propFileName + ".properties");

        ConfigDatabase databaseConfig = new ConfigDatabase();
        

        // File must exist
        if (Files.exists(txtFile)) {
            try (InputStream propFileStream = newInputStream(txtFile);) {
                prop.load(propFileStream);
            }
            databaseConfig.setDatabase(prop.getProperty("database"));
            databaseConfig.setDriver(prop.getProperty("driver"));
            databaseConfig.setPassword(prop.getProperty("password"));
            databaseConfig.setProtocol(prop.getProperty("protocol"));
            databaseConfig.setUrl(prop.getProperty("url"));
            databaseConfig.setUser(prop.getProperty("username"));
            
            found = true;
        }
        if(found)
            return databaseConfig;
        else
            throw new IOException(propFileName + " file not found.");
    }
    
    /**
     * Returns a ConfigDatabase object with the contents of the properties file
     * that is in an XML format
     *
     * @param path Must exist, will not be created. Empty string means root of
     * program folder
     * @param propFileName Name of the properties file
     * @return The bean loaded with the properties
     * @throws IOException
     */
    public final ConfigDatabase loadXmlProperties(final String path, final String propFileName) throws IOException {
        boolean found = false;
        Properties prop = new Properties();

        // The path of the XML file
        Path xmlFile = get(path, propFileName + ".xml");

        ConfigDatabase databaseConfig = new ConfigDatabase();

        // File must exist
        if (Files.exists(xmlFile)) {
            try (InputStream propFileStream = newInputStream(xmlFile);) {
                prop.loadFromXML(propFileStream);
            }
            databaseConfig.setDatabase(prop.getProperty("database"));
            databaseConfig.setDriver(prop.getProperty("driver"));
            databaseConfig.setPassword(prop.getProperty("password"));
            databaseConfig.setProtocol(prop.getProperty("protocol"));
            databaseConfig.setUrl(prop.getProperty("url"));
            databaseConfig.setUser(prop.getProperty("username"));
            
            found = true;
        }
        if(found)
            return databaseConfig;
        else
            throw new IOException(propFileName + " file not found.");
    }
    
    /**
     * Creates a plain text properties file based on the parameters
     *
     * @param path Must exist, will not be created
     * @param propFileName Name of the properties file
     * @param databaseConfig The bean to store into the properties
     * @throws IOException
     */
    public final void writeTextProperties(final String path, final String propFileName, final ConfigDatabase databaseConfig) throws IOException {

        Properties prop = new Properties();

        prop.setProperty("database", databaseConfig.getDatabase());
        prop.setProperty("driver",databaseConfig.getDriver());
        prop.setProperty("password",databaseConfig.getPassword());
        prop.setProperty("protocol",databaseConfig.getProtocol());
        prop.setProperty("url",databaseConfig.getUrl());
        prop.setProperty("username",databaseConfig.getUser());

        Path txtFile = get(path, propFileName + ".properties");

        // Creates the file or if file exists it is truncated to length of zero
        // before writing
        try (OutputStream propFileStream = newOutputStream(txtFile)) {
            prop.store(propFileStream, "Database Properties");
        }
    }
    
    /**
     * Creates an XML properties file based on the parameters
     *
     * @param path Must exist, will not be created
     * @param propFileName Name of the properties file. Empty string means root
     * of program folder
     * @param databaseConfig The bean to store into the properties
     * @throws IOException
     */
    public final void writeXmlProperties(final String path, final String propFileName, final ConfigDatabase databaseConfig) throws IOException {

        Properties prop = new Properties();

        prop.setProperty("database", databaseConfig.getDatabase());
        prop.setProperty("driver",databaseConfig.getDriver());
        prop.setProperty("password",databaseConfig.getPassword());
        prop.setProperty("protocol",databaseConfig.getProtocol());
        prop.setProperty("url",databaseConfig.getUrl());
        prop.setProperty("username",databaseConfig.getUser());

        Path xmlFile = get(path, propFileName + ".xml");

        // Creates the file or if file exists it is truncated to length of zero
        // before writing
        try (OutputStream propFileStream = newOutputStream(xmlFile)) {
            prop.storeToXML(propFileStream, "XML Database Properties");
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
     * @param propertiesFileName : Name of the properties file
     * @return The bean loaded with the properties
     * @throws IOException : Error while reading the file
     * @throws NullPointerException : File not found
     */
    public final boolean loadJarTextProperties(final String propertiesFileName) throws IOException, NullPointerException {
        boolean found = false;
           
        Properties prop = new Properties();
        ConfigDatabase databaseConfig = new ConfigDatabase();

        // There is no exists method for files in a jar so we try to get the
        // resource and if its not there it returns a null
        if (this.getClass().getResource("/" + propertiesFileName) != null) {
            // Assumes that the properties file is in the root of the
            // project/jar.
            // Include a path from the root if required.
            try (InputStream stream = this.getClass().getResourceAsStream("/" + propertiesFileName);) {
                prop.load(stream);
            }
            databaseConfig.setDatabase(prop.getProperty("database"));
            databaseConfig.setDriver(prop.getProperty("driver"));
            databaseConfig.setPassword(prop.getProperty("password"));
            databaseConfig.setProtocol(prop.getProperty("protocol"));
            databaseConfig.setUrl(prop.getProperty("url"));
            databaseConfig.setUser(prop.getProperty("username"));
            
            found = true;
        }
        return found;
    }
    
}
