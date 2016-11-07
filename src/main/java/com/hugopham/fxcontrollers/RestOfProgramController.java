package com.hugopham.fxcontrollers;

import com.hugopham.mailmoduleconfig.ConfigDatabase;
import com.hugopham.mailmoduleconfig.ConfigEmail;
import com.hugopham.mailmoduleconfig.DatabasePropertiesManager;
import com.hugopham.mailmoduleconfig.EmailPropertiesManager;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class RestOfProgramController {

    @FXML
    private TextArea textArea;

    @FXML
    public void initialize() {
    }

    @FXML
    void onExit(ActionEvent event) {
        Platform.exit();
    }

    public void displayPropertiesInTextArea() {
        
        DatabasePropertiesManager dpm = new DatabasePropertiesManager();
        EmailPropertiesManager epm = new EmailPropertiesManager();
        ConfigDatabase configDatabase = new ConfigDatabase();
        ConfigEmail configEmail = new ConfigEmail();
        try {
            if (epm.loadTextProperties( "", "MailConfig")) {
                textArea.setText(configEmail.toString());
            }
        } catch (IOException ex) {
            Logger.getLogger(RestOfProgramController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
