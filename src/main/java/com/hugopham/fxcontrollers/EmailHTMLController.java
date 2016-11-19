package com.hugopham.fxcontrollers;

import com.hugopham.mailmoduleconfig.ConfigEmail;
import com.hugopham.mailmoduledatabase.EmailDAO;
import com.hugopham.mailmodules.ExtendedEmail;
import com.hugopham.mailmodules.SendReceiveModule;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;

import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.web.HTMLEditor;
import jodd.mail.EmailAddress;
import jodd.mail.MailAddress;

/**
 * FXML Controller class of EmailHTMLLayout.fxml.
 * 
 * Used with the RootLayoutController in order to pass in a SendReceiveModule,
 * a ConfigEmail, and a EmailDAO object. Also used by the EmailTableController
 * to display an email's information such who it's from, its subject, content, etc.
 *
 * @author Hugo Pham
 * @version 1.0.0
 */
public class EmailHTMLController {

    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @FXML
    private TextField toTextField;

    @FXML
    private TextField ccTextField;

    @FXML
    private TextField bccTextField;

    @FXML
    private TextField subjectTextField;

    @FXML
    private HTMLEditor emailHTMLEditor;

    // Resource bundle is injected when controller is loaded
    @FXML
    private ResourceBundle resources;

    private ConfigEmail configEmail;
    private ExtendedEmail email;
    private EmailDAO emailDAO;
    private SendReceiveModule sendReceiveModule;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded. Not much to do here.
     */
    @FXML
    private void initialize() {
    }

    /**
     * The RootLayoutController calls this method to provide a reference to the
     * EmailDAO object.
     *
     * @param emailDAO
     * @throws SQLException
     */
    public void setEmailDAO(EmailDAO emailDAO) throws SQLException {
        this.emailDAO = emailDAO;
    }

    /**
     * The RootLayoutController calls this method to provide a reference to the
     * ConfigEmail object.
     *
     * @param configEmail
     */
    public void setConfigEmail(ConfigEmail configEmail) {
        this.configEmail = configEmail;
    }

    /**
     * The RootLayoutController calls this method to provide a reference to the
     * SendReceiveModule object.
     *
     * @param sendReceiveModule
     */
    public void setSendReceiveModule(SendReceiveModule sendReceiveModule) {
        this.sendReceiveModule = sendReceiveModule;
    }

    /**
     * This method prevents dropping the value on anything but the HTMLEditor,
     *
     * SceneBuilder writes the event as ActionEvent that you must change to the
     * proper event type that in this case is DragEvent
     *
     * @param event
     */
    @FXML
    private void dragOver(DragEvent event) {
        /* data is dragged over the target */
        log.debug("onDragOver");

        /*
		 * Accept it only if it is not dragged from the same control and if it
		 * has a string data
         */
        if (event.getGestureSource() != emailHTMLEditor && event.getDragboard().hasString()) {
            /*
			 * allow for both copying and moving, whatever user chooses
             */
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }

        event.consume();
    }

    /**
     * When the mouse is released over the FXHTMLEditor the value is written to
     * the editor.
     *
     * SceneBuilder writes the event as ActionEvent that you must change to the
     * proper event type that in this case is DragEvent
     *
     * @param event
     */
    @FXML
    private void dragDropped(DragEvent event) {
        log.debug("onDragDropped");
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasString()) {
            emailHTMLEditor.setHtmlText(db.getString());
            success = true;
        }
        /*
		 * let the source know whether the string was successfully transferred
		 * and used
         */
        event.setDropCompleted(success);

        event.consume();
    }

    /**
     * Sends and receives emails when the send button is clicked.
     */
    @FXML
    private void handleSend() {
        log.info("Send pressed.");
        if (!toTextField.getText().equals("") && !subjectTextField.getText().equals("")) {
            sendEmail();
            receiveEmails();
        } else {
            log.info("To or Subject not set.");
        }

    }

    /**
     * Display the fields of an email in the HTML editor.
     *
     * @param email
     */
    public void displayEmailAsHTML(ExtendedEmail email) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body contenteditable='false'>");
        sb.append(email.getAllMessages().get(0).getContent());
        sb.append("</body></html>");

        toTextField.setText(concatenateEmails(email.getTo()));
        ccTextField.setText(concatenateEmails(email.getCc()));
        bccTextField.setText(concatenateEmails(email.getBcc()));
        subjectTextField.setText(email.getSubject());
        emailHTMLEditor.setHtmlText(email.getAllMessages().get(0).getContent());

    }

    /**
     * Displays html that contains an embedded image.
     */
    public void displayOtherHTML() {
        String other = "<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">"
                + "<body contenteditable=\"false\"><h1>Here is my photograph embedded in this email.</h1><img src=\"cid:test.png\""
                + getClass().getResource("/test.png") + "><h2>TEST!</h2></body></html>";

        emailHTMLEditor.setHtmlText(other);
    }
    
    /**
     * Clears the email fields and text for the HTML editor.
     */
    public void clearHTMLEditor() {
        toTextField.setText("");
        ccTextField.setText("");
        bccTextField.setText("");
        subjectTextField.setText("");
        emailHTMLEditor.setHtmlText("<html><head></head><body contenteditable=\"true\"></body></html>");
    }

    /**
     * Routine for sending an email with set fields from the HTML editor and
     * storing the email into the database.
     */
    private void sendEmail() {
        email = new ExtendedEmail();
        email.from(configEmail.getEmailSend())
                .to(toTextField.getText())
                .subject(subjectTextField.getText());

        if (!ccTextField.getText().equals("")) {
            email.cc(parseEmails(ccTextField.getText()));
        }
        if (!bccTextField.getText().equals("")) {
            email.bcc(parseEmails(bccTextField.getText()));
        }

        email.addHtml(emailHTMLEditor.getHtmlText());

        try {
            email = sendReceiveModule.sendEmail(email);
            emailDAO.createEmail(configEmail.getEmailSend(), email);
            clearHTMLEditor();
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(EmailHTMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Routine for receiving emails and storing them into the database.
     */
    private void receiveEmails() {
        ExtendedEmail[] receivedEmails = sendReceiveModule.receiveEmail();
        if (receivedEmails != null) {
            for (ExtendedEmail receivedEmail : receivedEmails) {
                try {
                    emailDAO.createEmail(configEmail.getEmailSend(), receivedEmail);
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(EmailHTMLController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Returns a string representation of a MailAddress array.
     *
     * @param email
     * @return
     */
    private String concatenateEmails(MailAddress[] email) {
        if (email != null && email.length > 0) {
            log.info("Concatenating emails.");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < email.length; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(email[i].getEmail());
            }
            log.info("Concatenation result: " + sb.toString());
            return sb.toString();
        }
        return "";
    }

    /**
     * Parse through a string with comma-separated emails and converts them to
     * an EmailAddress array.
     *
     * @param emails
     * @return
     */
    private EmailAddress[] parseEmails(String emails) {
        log.info("Parsing emails");
        String[] emailArray = emails.split(",");
        ArrayList<EmailAddress> emailList = new ArrayList<>();
        for (String newEmail : emailArray) {
            log.info("Parsed email: " + newEmail);
            emailList.add(new EmailAddress(newEmail));
        }
        return emailList.toArray(new EmailAddress[emailList.size()]);

    }
}
