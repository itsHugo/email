package com.hugopham.mailmoduleconfig;

/**
 *
 * @author Hugo Pham
 */
public class ConfigDatabase {
    private String protocol;
    private String driver;
    private String url;
    private String database;
    private String user;
    private String password;
    
    public ConfigDatabase (){
        this("jdbc","mysql","","","","");
    }
    
    public ConfigDatabase(String protocol, String driver, String url,
            String database, String user, String password){
        this.protocol = protocol;
        this.driver = driver;
        this.url = url;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
}
