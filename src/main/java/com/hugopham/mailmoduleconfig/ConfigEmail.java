package com.hugopham.mailmoduleconfig;

import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


/**
 * Contains the various properties for configuring the email's settings.
 * @author Hugo Pham
 * @version 1.0.0
 * @since 1.8
 */
public class ConfigEmail {
    private final StringProperty smtpServerName;
    private final StringProperty imapServerName;
    private final StringProperty emailSend;
    private final StringProperty emailSendPwd;
    
    /**
     * Default constructor;
     */
    public ConfigEmail(){
        super();
        this.emailSend = new SimpleStringProperty("");
        this.emailSendPwd = new SimpleStringProperty("");
        this.smtpServerName = new SimpleStringProperty("");
        this.imapServerName = new SimpleStringProperty("");
    }
    
    /**
     * Constructor with parameters
     * @param emailSend
     * @param emailSendPwd
     * @param imapServerName
     * @param smtpServerName 
     */
    public ConfigEmail(String emailSend, String emailSendPwd,
            String imapServerName, String smtpServerName) {
        super();
        this.emailSend = new SimpleStringProperty(emailSend);
        this.emailSendPwd = new SimpleStringProperty(emailSendPwd);
        this.smtpServerName = new SimpleStringProperty(smtpServerName);
        this.imapServerName = new SimpleStringProperty(imapServerName);
    }

    // Getters and setters below
    public String getSmtpServerName() {
        return smtpServerName.get();
    }

    public void setSmtpServerName(String smtpServerName) {
        this.smtpServerName.set(smtpServerName);
    }

    public String getImapServerName() {
        return imapServerName.get();
    }

    public void setImapServerName(String imapServerName) {
        this.imapServerName.set(imapServerName);
    }

    public String getEmailSend() {
        return emailSend.get();
    }

    public void setEmailSend(String emailSend) {
        this.emailSend.set(emailSend);
    }

    public String getEmailSendPwd() {
        return emailSendPwd.get();
    }

    public void setEmailSendPwd(String emailSendPwd) {
        this.emailSendPwd.set(emailSendPwd);
    } 
    // End of methods

    // Methods for retrieving Properties for fxml methods below.
    public StringProperty smtpProperty() {
        return smtpServerName;
    }
    
    public StringProperty imapProperty() {
        return imapServerName;
    }
    

    public StringProperty emailAddressProperty() {
        return emailSend;
    }
    

    public StringProperty emailPasswordProperty() {
        return emailSendPwd;
    }
    // End of methods
}
