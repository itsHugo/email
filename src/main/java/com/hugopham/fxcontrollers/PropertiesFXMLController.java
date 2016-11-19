package com.hugopham.fxcontrollers;

import com.hugopham.mailmoduleconfig.ConfigDatabase;
import com.hugopham.mailmoduleconfig.ConfigEmail;
import com.hugopham.propertiesmanager.DatabasePropertiesManager;
import com.hugopham.propertiesmanager.EmailPropertiesManager;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * This is the controller for PropertiesFXML.
 * 
 * Configures the require properties in order to make the main application 
 * function.
 *
 * @author Hugo Pham
 * @version 1.0.0
 */
public class PropertiesFXMLController {

    // Injected fields  
    // Email fields
     @FXML
    private TextField emailAddressTxt;

    @FXML
    private PasswordField emailPasswordTxt;

    @FXML
    private TextField smtpTxt;

    @FXML
    private TextField imapTxt;

    @FXML
    private Button saveEmailBtn;

    @FXML
    private Button clearEmailBtn;
    // End email fields

    // Database fields
    @FXML
    private TextField usernameTxt;

    @FXML
    private PasswordField databasePasswordTxt;

    @FXML
    private TextField protocolTxt;

    @FXML
    private TextField driverTxt;

    @FXML
    private TextField databaseUrlTxt;

    @FXML
    private TextField databaseNameTxt;

    @FXML
    private Button clearDatabaseBtn;

    @FXML
    private Button saveDatabaseBtn;
    // End database fields


    // Resources are injected
    @FXML
    private ResourceBundle resources;

    Scene scene;
    Stage stage;

    private ConfigEmail configEmail;
    private ConfigDatabase configDatabase;
    
    private final String configEmailFilename = "EmailProperties";
    private final String configDatabaseFilename = "DatabaseProperties";

    /**
     * Default constructor creates an instance of ConfigEmail and ConfigDatabase
     * that can be bound to the form.
     */
    public PropertiesFXMLController() {
        super();
        // Load the properties to display if they exist.
        loadProperties();
    }

    /**
     * Without the ability to pass values through a constructor we need a set
     * method for any variables required in this class
     *
     * @param scene
     * @param stage
     */
    public void setSceneStageController(Scene scene, Stage stage) {
        this.scene = scene;
        this.stage = stage;
    }

    /**
     * The initialize method is used to handle bindings
     */
    @FXML
    private void initialize() {
        //Bind email config properties
        Bindings.bindBidirectional(emailAddressTxt.textProperty(), configEmail.emailAddressProperty());
        Bindings.bindBidirectional(emailPasswordTxt.textProperty(), configEmail.emailPasswordProperty());
        Bindings.bindBidirectional(smtpTxt.textProperty(), configEmail.smtpProperty());
        Bindings.bindBidirectional(imapTxt.textProperty(), configEmail.imapProperty());
        
        //Bind database config properties
        Bindings.bindBidirectional(usernameTxt.textProperty(), configDatabase.userProperty());
        Bindings.bindBidirectional(databasePasswordTxt.textProperty(), configDatabase.passwordProperty());
        Bindings.bindBidirectional(protocolTxt.textProperty(), configDatabase.protocolProperty());
        Bindings.bindBidirectional(driverTxt.textProperty(), configDatabase.driverProperty());
        Bindings.bindBidirectional(databaseUrlTxt.textProperty(), configDatabase.urlProperty());
        Bindings.bindBidirectional(databaseNameTxt.textProperty(), configDatabase.databaseProperty());
    }

    /**
     * Event handler for Cancel button Exit the program
     */
    @FXML
    void onCancel(ActionEvent event) {
        Platform.exit();
    }
    
    /**
     * Clears the fields in the database tab.
     * 
     * @param event 
     */
    @FXML
    void onClearDatabase(ActionEvent event) {
        configDatabase.setDatabase("");
        configDatabase.setDriver("");
        configDatabase.setPassword("");
        configDatabase.setProtocol("");
        configDatabase.setUrl("");
        configDatabase.setUser("");
    }

    /**
     * Clears the fields in the email tab.
     * 
     * @param event 
     */
    @FXML
    void onClearEmail(ActionEvent event) {
        emailAddressTxt.setText("");
        emailPasswordTxt.setText("");
        smtpTxt.setText("");
        imapTxt.setText("");

        configEmail.setEmailSend("");
        configEmail.setEmailSendPwd("");
        configEmail.setSmtpServerName("");
        configEmail.setImapServerName("");
    }

    /**
     * Event handler for Save button.
     * 
     * Saves the fields in the email tab to a properties file.
     *
     * @param event
     */
    @FXML
    void onSaveEmail(ActionEvent event) {
        EmailPropertiesManager epm = new EmailPropertiesManager();
        try {
            epm.writeTextProperties("", configEmailFilename, configEmail);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            // Need to do more than just log the error
        }
    }
    
    /**
     * Event handler for Save button.
     * 
     * Saves the fields in the database tab to a properties file.
     *
     * @param event
     */
    @FXML
    void onSaveDatabase(ActionEvent event) {
        DatabasePropertiesManager dpm = new DatabasePropertiesManager();
        try {
            dpm.writeTextProperties("", configDatabaseFilename, configDatabase);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            // Need to do more than just log the error
        }
    }
    
    /**
     * Display the existing properties if applicable in all the fields in
     * each tab.
     */
    private void loadProperties(){
        try{
            configEmail = new EmailPropertiesManager()
                    .loadTextProperties("", configEmailFilename);
            
            configDatabase = new DatabasePropertiesManager()
                    .loadTextProperties("", configDatabaseFilename);
        } catch(IOException ex){
            if(configEmail == null){
                configEmail = new ConfigEmail();
            }
            
            if(configDatabase == null){
                configDatabase = new ConfigDatabase();
            }
        }
    }
    
}
