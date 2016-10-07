/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hugopham.mailmodulesinterfaces;
import com.hugopham.mailmoduleconfig.ConfigEmail;
import com.hugopham.mailmodules.*;

/**
 *
 * @author 1334944
 */
public interface Mailer {
    ExtendedEmail sendEmail();
    ExtendedEmail sendEmail(ExtendedEmail mail);
    ExtendedEmail[] receiveEmail();
    void setConfigEmail(ConfigEmail c);
    ConfigEmail getConfigEmail();
}
