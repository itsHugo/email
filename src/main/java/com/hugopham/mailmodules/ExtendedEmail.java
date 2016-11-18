package com.hugopham.mailmodules;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.mail.Flags;
import jodd.mail.Email;

import jodd.mail.ReceivedEmail;

/**
 * Extends the Email class from Jodd.
 * Adds a field called folder in order to store the emails in an approriate folder.
 * 
 * @author Hugo Pham
 */
public class ExtendedEmail extends Email {
    private String folder;
    private int messageNumber;
    private List<ReceivedEmail> attachedMessages;
    private Date receiveDate;
    private Flags flags;
    
    // Default Constructor - Calls the Email constructor
    public ExtendedEmail() {
        super();
    }
    
    /**
     * Sets the folder for the email with fluent syntax.
     * @param folder
     * @return 
     */
    public ExtendedEmail folder(String folder) {
        setFolder(folder);
        return this;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public void setMessageNumber(int messageNumber) {
        this.messageNumber = messageNumber;
    }

    public List<ReceivedEmail> getAttachedMessages() {
        return attachedMessages;
    }

    public void setAttachedMessages(List<ReceivedEmail> attachedMessages) {
        for(ReceivedEmail r : attachedMessages){
            this.attachedMessages.add(r);
        }
    }
    
    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date recvDate) {
        this.receiveDate = recvDate;
    }

    public Flags getFlags() {
        return flags;
    }

    public void setFlags(Flags flags) {
        this.flags = flags;
    }
    
    /**
     * Gets folder name.
     * @return 
     */
    public String getFolder(){
        return folder;
    }
    
    /**
     * Sets folder name.
     * @param folder 
     */
    public void setFolder(String folder){
        this.folder = folder;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.from);
        hash = 79 * hash + Arrays.deepHashCode(this.to);
        hash = 79 * hash + Arrays.deepHashCode(this.cc);
        hash = 79 * hash + Objects.hashCode(this.subject);
        hash = 79 * hash + Objects.hashCode(this.messages);
        hash = 79 * hash + Objects.hashCode(this.attachments);
        hash = 79 * hash + Objects.hashCode(this.folder);
        return hash;
    }

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
        final ExtendedEmail other = (ExtendedEmail) obj;
        if (!Objects.equals(this.folder, other.folder)) {
            return false;
        }
        
        
        return (compareAttachment(other) && compareCc(other) 
                && compareFrom(other) && compareMessage(other) 
                && compareTos(other) && compareSubject(this));
    }
    
    /**
     * Compare the attachments between 2 ExtendedEmails. 
     * using the attachment's name.
     * @param email object to compare
     * @return true if the list of attachments is the same.
     */
    public boolean compareAttachment(ExtendedEmail email){
        boolean bool = true;
        
        if(this.attachments != null && email.attachments != null){
            if(this.attachments.size() != email.attachments.size()){
                bool = false;
            } else {
                for(int i=0; i < this.attachments.size(); i++) {
                    if(!this.attachments.get(i).getName()
                            .equals(email.attachments.get(i).getName()))
                        bool = false;
                }
            }
        }
        return bool;
    }
    
    /**
     * Compares the list of recipients in the Cc field between 2 ExtendedEmails.
     * @param email object to compare
     * @return true if contacts in both lists are the same.
     */
    public boolean compareCc(ExtendedEmail email) {
        boolean bool = true;
        
        if(this.cc.length != email.cc.length){
            bool = false;
        } else {
            for(int i=0; i < this.cc.length; i++){
                if(!this.cc[i].getEmail().equals(email.cc[i].getEmail()))
                    bool = false;
            }
        }

        return bool;
    }
    
    /**
     * Compares the sending email address between 2 ExtendedEmails.
     * @param email object to compare
     * @return true if the sender is the same.
     */
    public boolean compareFrom(ExtendedEmail email) {
        boolean bool = true;
        
        if (this.from.getEmail()
                .compareToIgnoreCase(email.from.getEmail()) != 0) {
            return false;
        }
        
        return bool;
    }
    
    /**
     * Compares the message between 2 ExtendedEmails.
     * @param email object to compare
     * @return true if the messages have the same content.
     */
    public boolean compareMessage(ExtendedEmail email){
        boolean bool = true;
        
        for(int i=0; i < this.getAllMessages().size(); i++){
            if(!this.getAllMessages().get(i).getContent()
                    .equals(email.getAllMessages().get(i).getContent())){
                bool=false;
            }
        }
        return bool;
    }
    
    /**
     * Compares the subject between 2 ExtendedEmails.
     * @param email object to compare
     * @return true if the subject line is the same.
     */
    public boolean compareSubject(ExtendedEmail email){
        boolean bool = true;
        
        if(!this.getSubject().equals(email.getSubject()))
            bool = false;
        
        return bool;
    }
    /**
     * Compares the list of recipient(s) between 2 ExtendedEmails.
     * @param email object to compare
     * @return true if contacts in both lists are the same.
     */
    public boolean compareTos(ExtendedEmail email){
        boolean bool = true;
        if(this.to.length != email.to.length){
            bool = false;
        } else {
            for(int i=0; i < this.to.length; i++){
                if(!this.to[i].getEmail().equals(email.to[i].getEmail()))
                    bool = false;
            }
        }
        return bool;
    }
    
}
