package com.hugopham.fxcontrollers;

import java.sql.SQLException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

import com.hugopham.mailmoduledatabase.EmailDAO;
import java.util.logging.Level;

/**
 * FXML Controller class of FolderTreeLayout.fxml.
 * 
 * Used with the RootLayoutController in order to pass functionality to the
 * EmailTableController.
 *
 * @author Hugo Pham
 * @version 1.0.0
 */
public class FolderTreeController {

    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @FXML
    private TreeView<String> folderTreeView;

    // Resource bundle is injected when controller is loaded
    @FXML
    private ResourceBundle resources;
    
    private EmailDAO emailDAO;
    private EmailTableController controller;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // The tree will display common name so we set this for the root
        // Because we are using i18n the root name comes from the resource
        // bundle
        folderTreeView.setRoot(new TreeItem<>(resources.getString("folder_root")));

        // This cell factory is used to choose the nodes' name.
        folderTreeView.setCellFactory((e) -> new TreeCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item);
                    setGraphic(getTreeItem().getGraphic());
                } else {
                    setText("");
                    setGraphic(null);
                }
            }
        });

        // We are going to drag and drop
        folderTreeView.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                /* drag was detected, start drag-and-drop gesture */
                log.debug("onDragDetected");

                /* allow any transfer mode */
                Dragboard db = folderTreeView.startDragAndDrop(TransferMode.ANY);

                /* put a string on dragboard */
                ClipboardContent content = new ClipboardContent();
                content.putString(folderTreeView.getSelectionModel().getSelectedItem().getValue());

                db.setContent(content);

                event.consume();
            }
        });
    }
    
    /**
     * The RootLayoutController calls this method to provide a reference to the
     * Email object.
     *
     * @param emailDAO
     */
    public void setEmailDAO(EmailDAO emailDAO) {
        this.emailDAO = emailDAO;
    }
    
    /**
     * The RootLayoutController calls this method to provide a reference to the
     * EmailTableController from which it can request a reference to the
     * TreeView. With the TreeView reference it can change the selection in the
     * TableView.
     *
     * @param controller
     */
    public void setTableController(EmailTableController controller){
        this.controller = controller;
    }

    /**
     * When a drag is detected the control at the start of the drag is accessed
     * to determine what will be dragged.
     *
     * SceneBuilder writes the event as ActionEvent that you must change to the
     * proper event type that in this case is DragEvent
     *
     * @param event
     */
    @FXML
    private void dragDetected(MouseEvent event) {
        /* drag was detected, start drag-and-drop gesture */
        log.debug("onDragDetected");

        /* allow any transfer mode */
        Dragboard db = folderTreeView.startDragAndDrop(TransferMode.ANY);

        /* put a string on dragboard */
        ClipboardContent content = new ClipboardContent();
        content.putString(folderTreeView.getSelectionModel().getSelectedItem().getValue());

        db.setContent(content);

        event.consume();

    }

    /**
     * Build the tree from the database
     *
     * @throws SQLException
     */
    public void displayTree() throws SQLException {
        // Retrieve the list of folders

        // Build an item for each folder and add it to the root
        for (String folder : emailDAO.findAllFolders()) {
            log.info(folder);
            TreeItem<String> item = new TreeItem<>(folder);
            item.setGraphic(null);
            folderTreeView.getRoot().getChildren().add(item);
        }

        // Open the tree
        folderTreeView.getRoot().setExpanded(true);

        // Listen for selection changes and show the emails from the associated folder
        // in the table.
        folderTreeView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> showEmailsInTable(newValue));

    }
    
    

    /**
     * Using the reference to the EmailTableController it can change the
     * emails displayed in the table depending on which folder they are in.
     *
     * @param folder
     */
    private void showEmailsInTable(TreeItem<String> folder) {
        try {
            controller.displayTheTable(folder.getValue());
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(FolderTreeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
