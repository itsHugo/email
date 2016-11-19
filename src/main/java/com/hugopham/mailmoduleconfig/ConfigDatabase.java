 package com.hugopham.mailmoduleconfig;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Contains the various properties for configuring the database.
 * 
 * @author Hugo Pham
 * @version 1.0.0
 * @since 1.8
 */
public class ConfigDatabase {
    private final StringProperty protocol;
    private final StringProperty driver;
    private final StringProperty url;
    private final StringProperty database;
    private final StringProperty user;
    private final StringProperty password;
    
    /**
     * Default constructor.
     * Sets the protocol to jdbc and the driver to mysql as default.
     */
    public ConfigDatabase (){
        super();
        this.protocol = new SimpleStringProperty("jdbc");
        this.driver = new SimpleStringProperty("mysql");
        this.url = new SimpleStringProperty("");
        this.database = new SimpleStringProperty("");
        this.user = new SimpleStringProperty("");
        this.password = new SimpleStringProperty("");
    }
    
    /**
     * Constructor with parameters.
     * @param protocol
     * @param driver
     * @param url
     * @param database
     * @param user
     * @param password 
     */
    public ConfigDatabase(String protocol, String driver, String url,
            String database, String user, String password){
        this.protocol = new SimpleStringProperty(protocol);
        this.driver = new SimpleStringProperty(driver);
        this.url = new SimpleStringProperty(url);
        this.database = new SimpleStringProperty(database);
        this.user = new SimpleStringProperty(user);
        this.password = new SimpleStringProperty(password);
    }

    // Getters and setters below
    public String getDriver() {
        return driver.get();
    }

    public void setDriver(String driver) {
        this.driver.set(driver);
    }

    public String getProtocol() {
        return protocol.get();
    }

    public void setProtocol(String protocol) {
        this.protocol.set(protocol);
    }

    public String getUrl() {
        return url.get();
    }

    public void setUrl(String url) {
        this.url.set(url);
    }

    public String getDatabase() {
        return database.get();
    }

    public void setDatabase(String database) {
        this.database.set(database);
    }

    public String getUser() {
        return user.get();
    }

    public void setUser(String user) {
        this.user.set(user);
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }
    // End of methods
    
    // Methods for retrieving Properties for fxml below.
    public StringProperty protocolProperty() {
        return protocol;
    }
    
    public StringProperty driverProperty() {
        return driver;
    }
    
    public StringProperty urlProperty(){
        return url;
    }
    
    public StringProperty databaseProperty(){
        return database;
    }
    
    public StringProperty userProperty(){
        return user;
    }
    
    public StringProperty passwordProperty(){
        return password;
    }
    //End of methods
}
