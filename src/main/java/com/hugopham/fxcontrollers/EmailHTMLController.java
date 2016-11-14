package com.hugopham.fxcontrollers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kenfogel.javafxmulticontainerv4.beans.FishData;
import com.kenfogel.javafxmulticontainerv4.persistence.FishDAO;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.web.HTMLEditor;

/**
 * This controller began its life as part of a standalone display of a container
 * with a menu, tool bar and HTML editor. It is now part of another container.
 * Nothing was changed when added to the new program.
 *
 * i18n added
 *
 * Added drag and drop
 *
 * @author Ken Fogel
 * @version 1.2
 *
 */
public class EmailHTMLController {

    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

    // We will need this to read from the database
    private FishDAO fishDAO;

    @FXML
    private HTMLEditor fishFXHTMLEditor;

    // Resource bundle is injected when controller is loaded
    @FXML
    private ResourceBundle resources;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded. Not much to do here.
     */
    @FXML
    private void initialize() {
    }

    /**
     * This method prevents dropping the value on anything buts the
     * FXHTMLEditor,
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
        if (event.getGestureSource() != fishFXHTMLEditor && event.getDragboard().hasString()) {
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
            fishFXHTMLEditor.setHtmlText(db.getString());
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
     * This just displays the contents of the HTMLEditor
     */
    @FXML
    private void handleSave() {
        System.out.println(fishFXHTMLEditor.getHtmlText());
    }

    /**
     * Opens an about dialog.
     */
    @FXML
    private void handleAbout() {
        // Modal dialog box
        // JavaFX dialog coming in 8u40
        Alert dialog = new Alert(AlertType.INFORMATION);
        dialog.setTitle(resources.getString("HTMLDemo"));
        dialog.setHeaderText(resources.getString("About"));
        dialog.setContentText(resources.getString("Demo"));
        dialog.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }

    /**
     * The RootLayoutController calls this method to provide a reference to the
     * FishDAO object.
     *
     * @param fishDAO
     * @throws SQLException
     */
    public void setFishDAO(FishDAO fishDAO) {
        this.fishDAO = fishDAO;
    }

    /**
     * Convert the first three fields from the first three records into HTML.
     */
    public void displayFishAsHTML() {
        ArrayList<FishData> data = null;
        try {
            data = fishDAO.findAll();
        } catch (SQLException e) {
            log.error("Error retrieving records: ", e.getCause());
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<html><body contenteditable='false'>");
        for (int x = 0; x < 3; ++x) {
            sb.append(data.get(x).getId()).append("</br>");
            sb.append(data.get(x).getCommonName()).append("</br>");
            sb.append(data.get(x).getLatin()).append("</br></br>");
        }
        sb.append("</body></html>");

        fishFXHTMLEditor.setHtmlText(sb.toString());

    }

    /**
     * Displays html that contains an embedded image
     */
    public void displayOtherHTML() {
        String other = "<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">"
                + "<body><h1>Here is my photograph embedded in this email.</h1><img src=\""
                + getClass().getResource("/FreeFall.jpg") + "\"><h2>I'm flying!</h2></body></html>";

        fishFXHTMLEditor.setHtmlText(other);
    }
}
