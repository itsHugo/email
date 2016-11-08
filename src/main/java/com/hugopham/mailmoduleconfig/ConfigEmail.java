package com.hugopham.mailmoduleconfig;

import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


/**
 * Class for configuring the server names and the sender's email credentials.
 * @author Hugo Pham
 * @version 1.0.0
 * @since 1.8
 */
public class ConfigEmail {
    private final StringProperty smtpServerName;
    private final StringProperty imapServerName;
    private final StringProperty emailSend;
    private final StringProperty emailSendPwd;
    
    // Default constructor
    public ConfigEmail(){
        super();
        this.emailSend = new SimpleStringProperty("");
        this.emailSendPwd = new SimpleStringProperty("");
        this.smtpServerName = new SimpleStringProperty("");
        this.imapServerName = new SimpleStringProperty("");
    }
    
    
    public ConfigEmail(String emailSend, String emailSendPwd,
            String imapServerName, String smtpServerName) {
        super();
        this.emailSend = new SimpleStringProperty(emailSend);
        this.emailSendPwd = new SimpleStringProperty(emailSendPwd);
        this.smtpServerName = new SimpleStringProperty(smtpServerName);
        this.imapServerName = new SimpleStringProperty(imapServerName);
    }

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

    // Property for fxml methods below.
    /**
     * Converts smtp String to StringProperty.
     * @return StringProperty smtp
     */
    public StringProperty smtpProperty() {
        return smtpServerName;
    }
    
    /**
     * Converts imap String to StringProperty.
     * @return StringProperty imap
     */
    public StringProperty imapProperty() {
        return imapServerName;
    }
    
    /**
     * Converts email address String to StringProperty.
     * @return StringProperty email address
     */
    public StringProperty emailAddressProperty() {
        return emailSend;
    }
    
    /**
     * Converts email password to StringProperty.
     * @return StringProperty password
     */
    public StringProperty emailPasswordProperty() {
        return emailSendPwd;
    }
    // End of property methods
}
