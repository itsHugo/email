package com.hugopham.fxcontrollers;

import com.hugopham.mailmoduleconfig.ConfigEmail;
import com.hugopham.mailmoduleconfig.PropertiesManager;
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
        /*
        PropertiesManager pm = new PropertiesManager();
        ConfigEmail mcp = new ConfigEmail();
        try {
            if (pm.loadTextProperties(mcp, "", "MailConfig")) {
                textArea.setText(mcp.toString());
            }
        } catch (IOException ex) {
            Logger.getLogger(RestOfProgramController.class.getName()).log(Level.SEVERE, null, ex);
        }
*/

    }
}
