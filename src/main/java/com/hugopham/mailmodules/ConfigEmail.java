/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hugopham.mailmodules;

import jodd.mail.EmailAddress;

/**
 * Class for configuring the server names and the sender's email credentials.
 * @author 1334944
 */
public class ConfigEmail {
    private String smtpServerName;
    private String imapServerName;
    private EmailAddress emailSend;
    private String emailSendPwd;
    
    // Default constructor
    public ConfigEmail(){}
    
    
    public ConfigEmail(String smtpServerName, String imapServerName, 
            EmailAddress emailSend, String emailSendPwd) {
        this.smtpServerName = smtpServerName;
        this.imapServerName = imapServerName;
        this.emailSend = emailSend;
        this.emailSendPwd = emailSendPwd;
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
        return emailSend.getInternetAddress().getAddress();
    }

    public void setEmailSend(String emailSend) {
        this.emailSend.getReturnPathAddress();
    }

    public String getEmailSendPwd() {
        return emailSendPwd;
    }

    public void setEmailSendPwd(String emailSendPwd) {
        this.emailSendPwd = emailSendPwd;
    }
    
    
    
}
