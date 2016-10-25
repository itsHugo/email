/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hugopham.mailmoduleconfig;

/**
 *
 * @author 1334944
 */
public class ConfigDatabase {
    private String driver;
    private String protocol;
    private String url;
    private String database;
    
    public ConfigDatabase (){
        super();
    }
    
    public ConfigDatabase(String driver, String protocol, String url, String database){
        this.driver = driver;
        this.protocol = protocol;
        this.url = url;
        this.database = database;
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
    
    
}
