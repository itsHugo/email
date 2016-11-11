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
import com.hugopham.mailmoduledatabase.EmailDAOImpl;

/**
 * This controller began its life as part of a standalone display of a container
 * with a menu, tool bar and HTML editor. It is now part of another container.
 *
 * A method was added to allow the RootLayoutController to pass in a reference
 * to the FishFXTableController
 *
 * i18n added
 *
 * Added drag and drop
 *
 * @author Ken Fogel
 * @version 1.2
 *
 */
public class FolderTreeController {

    private EmailDAO emailDAO;
    //private FishFXTableController fishFXTableController;

    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @FXML
    private TreeView<String> folderTreeView;

    // Resource bundle is injected when controller is loaded
    @FXML
    private ResourceBundle resources;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {

        //emailDAO = new EmailDAOImpl();
        // We need a root node for the tree and it must be the same type as all
        // nodes
        String root = "Folder";

        // The tree will display common name so we set this for the root
        // Because we are using i18n the root name comes from the resource
        // bundle
        folderTreeView.setRoot(new TreeItem<>(root));

        // This cell factory is used to choose which field in the FihsData object
        // is used for the node name
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
        
        try{
            displayTree();
        } catch(SQLException ex) {
            
        }
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
     * FishFXTableController from which it can request a reference to the
     * TreeView. With theTreeView reference it can change the selection in the
     * TableView.
     *
     * @param fishFXTableController
     */
    /* TO DO:
	public void setTableController(FishFXTableController fishFXTableController) {
		this.fishFXTableController = fishFXTableController;
	}*/
    /**
     * Build the tree from the database
     *
     * @throws SQLException
     */
    public void displayTree() throws SQLException {
        // Retrieve the list of fish
        // Change later
        emailDAO = new EmailDAOImpl();
        //ObservableList<String> folders = (ObservableList<String>) emailDAO.findAllFolders();

        // Build an item for each fish and add it to the root
        //if (folders != null) {
            for (String folder : emailDAO.findAllFolders()) {
                log.info(folder);
                TreeItem<String> item = new TreeItem<>(folder);
                //item.setGraphic(new ImageView(getClass().getResource("/images/fish.png").toExternalForm()));
                item.setGraphic(null);
                folderTreeView.getRoot().getChildren().add(item);
            //}
        }

        // Open the tree
        folderTreeView.getRoot().setExpanded(true);

        // Listen for selection changes and show the fishData details when
        // changed.
        /* TO DO:
                fishFXTreeView.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showFishDetailsTree(newValue));
         */
    }

    /**
     * Using the reference to the FishFXTableController it can change the
     * selected row in the TableView It also displays the FishData object that
     * corresponds to the selected node.
     *
     * @param fishData
     */
    /* TO DO:
	private void showFishDetailsTree(TreeItem<FishData> fishData) {

		// Select the row that contains the FishData object from the Tree
		fishFXTableController.getfishDataTable().getSelectionModel().select(fishData.getValue());
		// Get the row number
		int x = fishFXTableController.getfishDataTable().getSelectionModel().getSelectedIndex();
		// Scroll the table so that the row is at the top of the displayed table
		fishFXTableController.getfishDataTable().scrollTo(x);

		System.out.println("showFishDetailsTree\n" + fishData.getValue());
	}*/
}
