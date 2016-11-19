package com.hugopham.mailmodules;

import com.hugopham.mailmoduleconfig.ConfigEmail;
import com.hugopham.mailmodulesinterfaces.Mailer;
import com.hugopham.propertiesmanager.EmailPropertiesManager;
import java.io.IOException;
import java.util.List;

import javax.mail.Flags;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jodd.mail.Email;
import jodd.mail.EmailAddress;
import jodd.mail.EmailAttachment;
import jodd.mail.EmailFilter;
import jodd.mail.EmailMessage;
import jodd.mail.ReceiveMailSession;
import jodd.mail.ReceivedEmail;
import jodd.mail.SendMailSession;
import jodd.mail.SmtpServer;
import jodd.mail.SmtpSslServer;
import jodd.mail.ImapSslServer;
import jodd.mail.MailAddress;

/**
 * Module for sending and receiving emails using the Jodd API and the
 * ExtendedEmail class that extends Email. 
 * 
 * Implements the Mailer interface.
 *
 * @author Hugo Pham
 * @version 1.0.0
 * @since 1.8
 */
public class SendReceiveModule implements Mailer {

    private final String senderEmail;
    private final String senderPwd;
    private final String smtpServerName;
    private final String imapServerName;
    
    private EmailAddress[] emailReceive;
    private String subject;
    private String html;
    
    private ConfigEmail c;
    private final Logger logger
            = LoggerFactory.getLogger(this.getClass().getName());

    private final String inboxFolder = "Inbox";
    private final String sentFolder = "Sent";

    /**
     * Default constructor.
     * Sets the class variables with properties loaded from file.
     */
    public SendReceiveModule() {
        super();
        EmailPropertiesManager manager = new EmailPropertiesManager();
        ConfigEmail config = new ConfigEmail();
        try{
            config = manager.loadTextProperties("", "EmailProperties");
        } catch(IOException ex) {
            logger.error("EmailProperties.properties not found.");
        }
        this.senderEmail = config.getEmailSend();
        this.senderPwd = config.getEmailSendPwd();
        this.smtpServerName = config.getSmtpServerName();
        this.imapServerName = config.getImapServerName();
        
        
    }

    /**
     * Constructor that loads from a ConfigEmail object.
     * Sets the class variables.
     * @param c ConfigEmail object
     */
    public SendReceiveModule(ConfigEmail c) {
        this.c = c;
        this.senderEmail = c.getEmailSend();
        this.senderPwd = c.getEmailSendPwd();
        this.smtpServerName = c.getSmtpServerName();
        this.imapServerName = c.getImapServerName();
    }

    /**
     * Sends an email
     *
     * @return
     */
    @Override
    public ExtendedEmail sendEmail() {
        // Create am SMTP server object
        SmtpServer<SmtpSslServer> smtpServer = SmtpSslServer
                .create(smtpServerName)
                .authenticateWith(senderEmail, senderPwd);

        //create Email with html message
        ExtendedEmail email = (ExtendedEmail) Email.create().from(senderEmail)
                .to(emailReceive)
                .subject(subject).addHtml(html);

        // A session is the object responsible for communicating with the server
        SendMailSession session = smtpServer.createSession();

        // Like a file we open the session, send the message and close the
        // session
        session.open();
        session.sendMail(email);
        email.setFolder(sentFolder);
        session.close();

        return email;

    }
    
    /**
     * Gets the ConfigEmail object.
     * 
     * @return 
     */
    @Override
    public ConfigEmail getConfigEmail() {
        return c;
    }
    
    /**
     * Sets the ConfigEmail object.
     * 
     * @param c 
     */
    @Override
    public void setConfigEmail(ConfigEmail c) {
        this.c = c;
    }

    /**
     * Send an email with an existing ExtendedEmail object
     *
     * @param mail Email to send
     * @return the email sent
     */
    @Override
    public ExtendedEmail sendEmail(ExtendedEmail mail) {
        // Create am SMTP server object
        SmtpServer<SmtpSslServer> smtpServer = SmtpSslServer
                .create(smtpServerName)
                .authenticateWith(senderEmail, senderPwd);

        // A session is the object responsible for communicating with the server
        SendMailSession session = smtpServer.createSession();

        // Like a file we open the session, send the message and close the
        // session
        session.open();
        session.sendMail(mail);
        mail.setFolder(sentFolder);

        session.close();
        
        //Log sent email
        logger.info("-------Sent an email: --------");
        logEmail(mail);
        return mail;
    }

    /**
     * Retrieves unread emails.
     *
     * @return array of ExtendedEmail
     */
    @Override
    public ExtendedEmail[] receiveEmail() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        // Create am IMAP server object
        ImapSslServer imapSslServer = new ImapSslServer(imapServerName,
                senderEmail, senderPwd);

        // Display the converstaion between the application and the imap server
        //imapSslServer.setProperty("mail.debug", "true");
        // A session is the object responsible for communicating with the server
        ReceiveMailSession session = imapSslServer.createSession();
        session.open();

        // We only want messages that have not be read yet.
        // Mark as seen after.
        ReceivedEmail[] emails = session.receiveEmailAndMarkSeen(EmailFilter
                .filter().flag(Flags.Flag.SEEN, false));
        session.close();

        // Converts the retrieved emails into ExtendedEmail
        return convertToExtended(emails);
    }


    

    // Converts ReceivedEmail array to ExtendedEmail array
    private ExtendedEmail[] convertToExtended(ReceivedEmail[] r) {
        ExtendedEmail[] converted = null;
        if (r != null) {
            logger.info("-------Converting ReceivedEmail to ExtendedEmail-------");
            converted = new ExtendedEmail[r.length];
            for (int i = 0; i < r.length; i++) {
                converted[i] = new ExtendedEmail();
                
                converted[i].setBcc(r[i].getBcc());
                converted[i].setCc(r[i].getCc());
                converted[i].setFrom(r[i].getFrom());
                converted[i].setSubject(r[i].getSubject());
                converted[i].setTo(r[i].getTo());
                
                for (EmailMessage message : r[i].getAllMessages()) {
                    converted[i].addMessage(message);
                }
                if(r[i].getAttachments() != null)
                    for (EmailAttachment attachment : r[i].getAttachments()) 
                        converted[i].attach(attachment);
                    
                converted[i].setSentDate(r[i].getSentDate());
                converted[i].setReceiveDate(r[i].getReceiveDate());
                
                converted[i].setMessageNumber(r[i].getMessageNumber());
                converted[i].setFlags(r[i].getFlags());
                converted[i].setFolder(inboxFolder);
                
                logEmail(converted[i]);
            }
        }
        return converted;
    }

    //Method for logging an email's details (from, to, cc, etc.)
    private void logEmail(ExtendedEmail mail) {
        // common info
        logger.info("MESSAGE #:" + mail.getMessageNumber());
        logger.info("FROM:" + mail.getFrom());
        for (MailAddress email : mail.getTo()) {
            logger.info("TO:" + email);
        }
        for (MailAddress email : mail.getCc()) {
            logger.info("CC:" + email);
        }
        for (MailAddress email : mail.getBcc()) {
            logger.info("BCC:" + email);
        }
        logger.info("SUBJECT:" + mail.getSubject());
        logger.info("PRIORITY:" + mail.getPriority());
        logger.info("SENT DATE:" + mail.getSentDate());
        logger.info("RECEIVE DATE:" + mail.getReceiveDate());

        // Messages may be multi-part so they are stored in an array
        List<EmailMessage> messages = mail.getAllMessages();
        for (EmailMessage msg : messages) {
            logger.info("------------");
            logger.info(msg.getEncoding());
            logger.info(msg.getMimeType());
            logger.info(msg.getContent());
        }

        // There may be multiple arrays so they are stored in an array
        List<EmailAttachment> attachments = mail.getAttachments();
        if (attachments != null) {
            logger.info("------------");
            for (EmailAttachment attachment : attachments) {
                logger.info("name: " + attachment.getName());
                logger.info("cid: " + attachment.getContentId());
                logger.info("size: " + attachment.getSize());
                
            }
        }
        /*
        logger.info("-----------");
        logger.info("attachedMessages");
        List<ReceivedEmail> attachedMessages = mail.getAttachedMessages();
        for (ReceivedEmail r : attachedMessages){
            logger.info("from: " + r.getFrom());
        }*/
        
        logger.info("------------");
        logger.info("FOLDER:" + mail.getFolder());
        
        logger.info("------------");
        logger.info("FLAGS:" + mail.getFlags());
        
        logger.info("-------------------------");
    }

    

}
