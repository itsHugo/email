package com.hugopham.fxcontrollers;

import com.hugopham.mailmoduleconfig.ConfigEmail;
import com.hugopham.mailmoduledatabase.EmailDAO;
import com.hugopham.mailmodules.ExtendedEmail;
import com.hugopham.mailmodules.SendReceiveModule;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;

import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jodd.mail.EmailAddress;
import jodd.mail.EmailAttachment;
import jodd.mail.MailAddress;
import jodd.mail.MailException;

/**
 * FXML Controller class of EmailHTMLLayout.fxml.
 *
 * Used with the RootLayoutController in order to pass in a SendReceiveModule, a
 * ConfigEmail, and a EmailDAO object. Also used by the EmailTableController to
 * display an email's information such who it's from, its subject, content, etc.
 *
 * @author Hugo Pham
 * @version 1.0.0
 * @since 1.8
 */
public class EmailHTMLController {

    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @FXML
    private SplitPane splitPane;

    @FXML
    private BorderPane recipientBorderPane;

    @FXML
    private ToolBar editorToolBar;

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
    private FileChooser fileChooser;
    private List<EmailAttachment> emailAttachments;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded. Not much to do here.
     */
    @FXML
    private void initialize() {
        this.fileChooser = new FileChooser();
        emailAttachments = new ArrayList<>();
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
     * Attaches a file to the email.
     */
    @FXML
    private void handleAttach() {
        // Displays file chooser window.
        Stage stage = (Stage) emailHTMLEditor.getScene().getWindow();
        File attachment = fileChooser.showOpenDialog(stage);

        // Adds attachment to ArrayList.
        log.info("Attaching: " + attachment.getName());
        emailAttachments.add(EmailAttachment.attachment().file(attachment).create());

        // Displays attachments in editor.
        editorToolBar.getItems().add(new Label(attachment.getName()));
    }

    /**
     * Sends and receives emails when the send button is clicked.
     */
    @FXML
    private void handleSend() {
        log.info("Send pressed.");
        if (!toTextField.getText().equals("") && !subjectTextField.getText().equals("")) {
            sendEmail();
        } else {
            log.info("To or Subject not set.");
            // Displays error message in Alert box.
            Alert dialog = new Alert(Alert.AlertType.WARNING);
            dialog.setTitle(resources.getString("error_alert"));
            dialog.setHeaderText(resources.getString("to_or_subject"));
            dialog.show();
        }
        receiveEmails();

    }

    /**
     * Saves an attachment through the file chooser.
     *
     * @param attachment
     */
    private void handleSave(EmailAttachment attachment) {
        Stage stage = (Stage) emailHTMLEditor.getScene().getWindow();

        fileChooser.setTitle(resources.getString("save_file"));
        fileChooser.setInitialFileName(attachment.getName());

        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            attachment.writeToFile(file.getAbsoluteFile());
            // Displays successful message
            Alert dialog = new Alert(Alert.AlertType.INFORMATION);
            dialog.setTitle(resources.getString("success_alert"));
            dialog.setHeaderText(resources.getString("save_file_message"));
            dialog.setContentText(file.getAbsolutePath());
            dialog.show();
        }
    }

    /**
     * Display the fields of an email in the HTML editor.
     *
     * @param email
     */
    public void displayEmailAsHTML(ExtendedEmail email) {
        clearHTMLEditor();// Clear HTML editor

        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");
        sb.append(email.getAllMessages().get(0).getContent());
        sb.append("</body></html>");

        toTextField.setText(concatenateEmails(email.getTo()));
        ccTextField.setText(concatenateEmails(email.getCc()));
        bccTextField.setText(concatenateEmails(email.getBcc()));
        subjectTextField.setText(email.getSubject());
        emailHTMLEditor.setHtmlText(sb.toString());

        emailAttachments = email.getAttachments();
        // Displays attachments if list is not null.
        if (emailAttachments != null) {
            for (EmailAttachment attachment : emailAttachments) {
                if (!attachment.isEmbeddedInto(email.getAllMessages().get(0))) {
                    Button button = new Button(attachment.getName());
                    // Sets an action on the button.
                    // When fired, the application will ask the user
                    // where to save the attachment.
                    button.setOnAction((ActionEvent event) -> {
                        handleSave(attachment);
                    });
                    // Adds button to the tool bar for display.
                    editorToolBar.getItems().add(button);
                }
            }

        }

        // Disables components so that they are not editable or sendable.
        editorToolBar.getItems().get(0).setDisable(true);
        recipientBorderPane.setDisable(true);
        emailHTMLEditor.setDisable(true);
    }

    /**
     * Displays html that contains an embedded image. Only used for testing.
     */
    public void displayOtherHTML() {
        emailHTMLEditor.setHtmlText(replaceCID());
    }

    /**
     * Clears the email fields and text for the HTML editor. Also clears the
     * List of EmailAttachment.
     */
    public void clearHTMLEditor() {
        toTextField.setText("");
        ccTextField.setText("");
        bccTextField.setText("");
        subjectTextField.setText("");
        emailHTMLEditor.setHtmlText("<html><head></head><body contenteditable=\"true\"></body></html>");
        editorToolBar.getItems().retainAll(editorToolBar.getItems().get(0));
        emailAttachments = new ArrayList<>(); // Clears attachments

        // Enables components necessary to write a new email.
        editorToolBar.getItems().get(0).setDisable(false);
        recipientBorderPane.setDisable(false);
        emailHTMLEditor.setDisable(false);

    }

    /**
     * Routine for sending an email with set fields from the HTML editor and
     * storing the email into the database.
     */
    private void sendEmail() {
        email = new ExtendedEmail();
        // Sets the fields.
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

        if (!emailAttachments.isEmpty()) {
            for (EmailAttachment attachment : emailAttachments) {
                email.attach(attachment);
            }
        }

        // Sends the email and stores it in the database.
        try {
            email = sendReceiveModule.sendEmail(email);
            emailDAO.createEmail(configEmail.getEmailSend(), email);
            clearHTMLEditor();

            // Displays successful email sent message in Alert box.
            Alert dialog = new Alert(Alert.AlertType.INFORMATION);
            dialog.setTitle(resources.getString("success_alert"));
            dialog.setHeaderText(resources.getString("email_sent"));
            dialog.show();
        } catch (MailException | SQLException ex) {
            // Displays successful email sent message in Alert box.
            Alert dialog = new Alert(Alert.AlertType.WARNING);
            dialog.setTitle(resources.getString("error_alert"));
            dialog.setHeaderText(resources.getString("send_error"));
            dialog.show();
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
                    java.util.logging.Logger.getLogger(EmailHTMLController.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
            // Displays successful email sent message in Alert box.
            Alert dialog = new Alert(Alert.AlertType.INFORMATION);
            dialog.setTitle(resources.getString("success_alert"));
            dialog.setHeaderText(resources.getString("emails_received"));
            dialog.show();
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

    /**
     * Only used for testing.
     *
     * This is a hard coed message that only uses the byte[] from the database
     * What has to happen next is that the message must be analyzed and all
     * references to images such as <img src='cid:FreeFall.jpg'> must be
     * replaced with
     * <img src=\"data:image/jpg;base64, [Base64 encoded byte[]]\"/>
     *
     * @param bean
     * @return
     */
    private String replaceCID() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>\n"
                + "<head>\n"
                + "<title>Insert title here</title>\n"
                + "</head>\n"
                + "<body>\n"
                + "	<h1>Here is my photograph embedded in this email.</h1>\n");
//                + "	<img src='cid:FreeFall.jpg'>\n"
        sb.append("<img src=\"data:image/;base64,")
                // Encode a byte array to a Base64 string
                .append(Base64.getMimeEncoder().encodeToString(EmailAttachment.attachment().file("test.png").create().toByteArray()))
                .append("\"/>");
        sb.append("	<h2>I'm flying!</h2>\n"
                + "</body>\n"
                + "</html>");
        return sb.toString();
    }
}
