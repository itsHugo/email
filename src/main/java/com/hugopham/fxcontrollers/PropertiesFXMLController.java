package com.hugopham.fxcontrollers;

import com.hugopham.mailmoduleconfig.ConfigEmail;
import com.hugopham.mailmoduleconfig.EmailPropertiesManager;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * This is the controller for Scene.fxml
 *
 * @author Ken Fogel
 */
public class PropertiesFXMLController {

    // Injected fields
    @FXML
    private TextField name;

    @FXML
    private TextField emailAddress;

    @FXML
    private PasswordField password;

    // Resources are injected
    @FXML
    private ResourceBundle resources;

    Scene scene;
    Stage stage;
    RestOfProgramController rpc;

    private ConfigEmail mcp;

    /**
     * Default constructor creates an instance of ConfigEmail that can be bound to
     * the form
     */
    public PropertiesFXMLController() {
        super();
        mcp = new ConfigEmail();
    }

    /**
     * Without the ability to pass values thru a constructor we need a set
     * method for any variables required in this class
     *
     * @param scene
     * @param stage
     * @param rpc
     */
    public void setSceneStageController(Scene scene, Stage stage, RestOfProgramController rpc) {
        this.scene = scene;
        this.stage = stage;
        this.rpc = rpc;
    }

    /**
     * The initialize method is used to handle bindings
     */
    @FXML
    private void initialize() {
        /*
        Bindings.bindBidirectional(name.textProperty(), mcp.userNameProperty());
        Bindings.bindBidirectional(emailAddress.textProperty(), mcp.emailAddressProperty());
        Bindings.bindBidirectional(password.textProperty(), mcp.passwordProperty());
        */
    }

    /**
     * Event handler for Cancel button Exit the program
     */
    @FXML
    void onCancel(ActionEvent event) {
        Platform.exit();
    }

    /**
     * Event handler for Clear button
     *
     * @param event
     */
    @FXML
    void onClear(ActionEvent event) {
        /*
        mcp.setUserName("");
        mcp.setUserEmailAddress("");
        mcp.setPassword("");
        */
    }

    /**
     * Event handler for Save button
     *
     * @param event
     */
    @FXML
    void onSave(ActionEvent event) {
        EmailPropertiesManager pm = new EmailPropertiesManager();
        try {
            pm.writeTextProperties("", "MailConfig", mcp);
            // Display properties in TextArea
            rpc.displayPropertiesInTextArea();
            // Change the scene on the stage
            stage.setScene(scene);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            // Need to do more than just log the error
        }
    }
}
