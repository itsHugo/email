package com.hugopham.forms;

import com.hugopham.propertiesmanager.DatabasePropertiesManager;
import com.hugopham.propertiesmanager.EmailPropertiesManager;
import java.io.IOException;
import java.util.ResourceBundle;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        EmailPropertiesManager email = new EmailPropertiesManager();
        DatabasePropertiesManager database = new DatabasePropertiesManager();

        // Display configurations menu if no properties are found
        // else displays email application.
        if (email.loadTextProperties("", "EmailProperties") == null || database.loadTextProperties("", "DatabaseProperties") == null) {
            displayProperties(stage);
        } else {
            displayRoot(stage);
        }
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Displays the Properties menu in the main stage.
     * 
     * @param stage
     * @throws IOException 
     */
    private void displayProperties(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/fxml/PropertiesFXML.fxml"));
        loader.setResources(ResourceBundle.getBundle("MessagesBundle"));
        Parent root = (BorderPane) loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");

        stage.setTitle("Config");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Displays the RootLayout in the main stage, which is the main application.
     * 
     * @param stage
     * @throws IOException 
     */
    private void displayRoot(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/fxml/RootLayout.fxml"));
        loader.setResources(ResourceBundle.getBundle("MessagesBundle"));
        Parent root = (BorderPane) loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");

        stage.setTitle("My Email Program");
        stage.setScene(scene);
        stage.show();
    }

}
