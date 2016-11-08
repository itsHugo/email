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
    private String smtpServerName;
    private String imapServerName;
    private String emailSend;
    private String emailSendPwd;
    
    // Default constructor
    public ConfigEmail(){
        this("","","smtp.gmail.com","imap.gmail.com");
    }
    
    
    public ConfigEmail(String emailSend, String emailSendPwd,
            String imapServerName, String smtpServerName) {
        super();
        this.emailSend = emailSend;
        this.emailSendPwd = emailSendPwd;
        this.smtpServerName = smtpServerName;
        this.imapServerName = imapServerName;
    }

    public String getSmtpServerName() {
        return smtpServerName;
    }

    public void setSmtpServerName(String smtpServerName) {
        this.smtpServerName = smtpServerName;
    }

    public String getImapServerName() {
        return imapServerName;
    }

    public void setImapServerName(String imapServerName) {
        this.imapServerName = imapServerName;
    }

    public String getEmailSend() {
        return emailSend;
    }

    public void setEmailSend(String emailSend) {
        this.emailSend = emailSend;
    }

    public String getEmailSendPwd() {
        return emailSendPwd;
    }

    public void setEmailSendPwd(String emailSendPwd) {
        this.emailSendPwd = emailSendPwd;
    }

    // Property for fxml methods below.
    /**
     * Converts smtp String to StringProperty.
     * @return StringProperty smtp
     */
    public StringProperty smtpProperty() {
        return new SimpleStringProperty(smtpServerName);
    }
    
    /**
     * Converts imap String to StringProperty.
     * @return StringProperty imap
     */
    public StringProperty imapProperty() {
        return new SimpleStringProperty(imapServerName);
    }
    
    /**
     * Converts email address String to StringProperty.
     * @return StringProperty email address
     */
    public StringProperty emailAddressProperty() {
        return new SimpleStringProperty(emailSend);
    }
    
    /**
     * Converts email password to StringProperty.
     * @return StringProperty password
     */
    public StringProperty emailPasswordProperty() {
        return new SimpleStringProperty(emailSendPwd);
    }
    // End of property methods
    
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ConfigEmail other = (ConfigEmail) obj;
        if (emailSend == null) {
            if (other.emailSend != null) {
                return false;
            }
        } else if (!emailSend.equals(other.emailSend)) {
            return false;
        }
        if (emailSendPwd == null) {
            if (other.emailSendPwd != null) {
                return false;
            }
        } else if (!emailSendPwd.equals(other.emailSendPwd)) {
            return false;
        }
        if (imapServerName == null) {
            if (other.imapServerName != null) {
                return false;
            }
        } else if (!imapServerName.equals(other.imapServerName)) {
            return false;
        }
        if (smtpServerName == null) {
            if (other.smtpServerName != null) {
                return false;
            }
        } else if (!smtpServerName.equals(other.smtpServerName)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.smtpServerName);
        hash = 83 * hash + Objects.hashCode(this.imapServerName);
        hash = 83 * hash + Objects.hashCode(this.emailSend);
        hash = 83 * hash + Objects.hashCode(this.emailSendPwd);
        return hash;
    }
    
    
    
}
