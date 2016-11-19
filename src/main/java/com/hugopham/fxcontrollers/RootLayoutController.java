package com.hugopham.fxcontrollers;
import com.hugopham.forms.MainApp;
import com.hugopham.mailmoduledatabase.EmailDAO;
import com.hugopham.mailmoduledatabase.EmailDAOImpl;
import com.hugopham.mailmodules.SendReceiveModule;
import com.hugopham.propertiesmanager.EmailPropertiesManager;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class of RootLayout.fxml.
 * 
 * Initializes the required components, such as the TreeView, TableView, 
 * and the HTML editor and their controllers. Passes the EmailDAO to each 
 * controller so that they have CRUD methods.
 *
 * @author Hugo Pham
 * @version 1.0.0
 */
public class RootLayoutController {

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private SplitPane splitPane;
    
    @FXML
    private ResourceBundle resources;
    
    private final EmailDAO emailDAO;
    private final SendReceiveModule sendReceiveModule;
    private FolderTreeController folderTreeController;
    private EmailTableController emailTableController;
    private EmailHTMLController emailHTMLController;
    
    public RootLayoutController() {
        super();
        emailDAO = new EmailDAOImpl();
        sendReceiveModule = new SendReceiveModule();
    }
    
    /**
     * Initializes the controller class.
     */
    @FXML
    public void initialize() {
        initFolderTree();
        initTable();
        initHTMLEditor();
        
        splitPane.setDividerPositions(0.30);
        
        setTableControllerToTree();
        setHTMLToTable();
        setConfigEmailToHTMLController();
        setSendReceiveModuleToHTMLController();
        
        
        try {
            folderTreeController.displayTree();
            emailTableController.displayTheTable("Inbox");
            emailHTMLController.displayOtherHTML();
        } catch (SQLException ex) {
            Logger.getLogger(RootLayoutController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Opens configuration menu.
     */
    @FXML
    private void handleConfiguration() {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/fxml/PropertiesFXML.fxml"));
            loader.setResources(ResourceBundle.getBundle("MessagesBundle"));
            Parent root = (BorderPane) loader.load();
            
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/styles/Styles.css");
            
            stage.setTitle("Config");
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(EmailHTMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }

    /**
     * Clears the HTML editor in order to write a new email.
     */
    @FXML
    private void handleNewEmail(){
        emailHTMLController.clearHTMLEditor();
    }
    /**
     * Creates the folder tree in the RootLayout fxml.
     */
    private void initFolderTree() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(resources);

            loader.setLocation(MainApp.class
                    .getResource("/fxml/FolderTreeLayout.fxml"));
            AnchorPane treeView = (AnchorPane) loader.load();

            // Give the controller the data object.
            folderTreeController = loader.getController();
            folderTreeController.setEmailDAO(emailDAO);

            mainBorderPane.setLeft(treeView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the email table in the RootLayout fxml.
     */
    private void initTable() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(resources);

            loader.setLocation(MainApp.class
                    .getResource("/fxml/EmailTableLayout.fxml"));
            AnchorPane tableView = (AnchorPane) loader.load();

            // Give the controller the data object.
            emailTableController = loader.getController();
            emailTableController.setEmailDAO(emailDAO);

            splitPane.getItems().add(tableView);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the HTML editor in the RootLayout fxml.
     */
    private void initHTMLEditor() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(resources);

            loader.setLocation(MainApp.class
                    .getResource("/fxml/EmailHTMLLayout.fxml"));
            SplitPane htmlView = (SplitPane) loader.load();

            // Give the controller the data object.
            emailHTMLController = loader.getController();
            emailHTMLController.setEmailDAO(emailDAO);

            splitPane.getItems().add(htmlView);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Send the reference to the EmailTableController to the
     * FolderTreeController.
     */
    private void setTableControllerToTree() {
        folderTreeController.setTableController(emailTableController);
    }
    
    /**
     * Send the reference to the EmailTableController to the
     * FolderTreeController.
     */
    private void setHTMLToTable() {
        emailTableController.setHTMLController(emailHTMLController);
    }
    
    /**
     * Sets the ConfigEmail object in EmailHTMLController.
     */
    private void setConfigEmailToHTMLController(){
        try {
            emailHTMLController.setConfigEmail(new EmailPropertiesManager().loadTextProperties("", "EmailProperties"));
        } catch (IOException ex) {
            Logger.getLogger(RootLayoutController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Sets the SendReceiveModule object in EmailHTMLController.
     */
    private void setSendReceiveModuleToHTMLController(){
        emailHTMLController.setSendReceiveModule(sendReceiveModule);
    }
}
