package com.hugopham.fxcontrollers;

import com.hugopham.mailmoduledatabase.EmailDAO;
import com.hugopham.mailmodules.ExtendedEmail;
import java.sql.SQLException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class of EmailTableLayout.fxml.
 * 
 * Used with the RootLayoutController in order to pass functionality to the
 * EmailHTMLController. Also used by the FolderTreeController.
 *
 * @author Hugo Pham
 * @version 1.0.0
 */
public class EmailTableController {

    @FXML
    private AnchorPane emailTable;

    @FXML
    private TableView<ExtendedEmail> emailDataTable;

    @FXML
    private TableColumn<ExtendedEmail, String> senderColumn;

    @FXML
    private TableColumn<ExtendedEmail, String> subjectColumn;

    @FXML
    private TableColumn<ExtendedEmail, String> receiveDate;

    private EmailDAO emailDAO;
    
    private EmailHTMLController controller;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     * 
     * @throws java.sql.SQLException
     */
    @FXML
    public void initialize() throws SQLException {

        // Connects the property in the ExtendedEmail object to the column in the
        // table
        senderColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().
                getFrom().getEmail()));
        subjectColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()
                .getSubject()));
        // Check if date is null, displays empty string if true
        receiveDate.setCellValueFactory(cellData -> new SimpleStringProperty((cellData.getValue()
                .getReceiveDate()) == null ? "" : cellData.getValue().getReceiveDate().toString() ));

        adjustColumnWidths();

        // Listen for selection changes and show the fishData details when
        // changed.
        emailDataTable
                .getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable, oldValue, newValue) -> showEmailDetails(newValue));
        
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
     * EmailHTMLController from which it can request a reference to the
     * HTMLEditor. With the HTMLEditor reference it can display an email's information
     * in the editor.
     *
     * @param controller
     */
    public void setHTMLController(EmailHTMLController controller){
        this.controller = controller;
    }
    
    /**
     * The table displays all the emails.
     *
     * @throws SQLException
     */
    public void displayTheTable() throws SQLException {
        ObservableList<ExtendedEmail> observableList = 
                FXCollections.observableList(emailDAO.findAll());

        // Add observable list data to the table
        emailDataTable.setItems(observableList);
    }
    
    /**
     * The table displays all the emails in the matching folder.
     *
     * @param folder 
     * @throws SQLException
     */
    public void displayTheTable(String folder) throws SQLException {
        ObservableList<ExtendedEmail> observableList = 
                FXCollections.observableList(emailDAO.findAllEmailsInFolder("hugo.sender.not.a.bot@gmail.com",folder));

        // Add observable list data to the table
        emailDataTable.setItems(observableList);
    }

    
    /**
     * Sets the width of the columns based on a percentage of the overall width
     *
     * This needs to enhanced so that it uses the width of the anchor pane it is
     * in and then changes the width as the table grows.
     */
    private void adjustColumnWidths() {
        // Get the current width of the table
        double width = emailTable.getPrefWidth();
        // Set width of each column
        senderColumn.setPrefWidth(width * .33);
        subjectColumn.setPrefWidth(width * .33);
        receiveDate.setPrefWidth(width * .33);
    }

    /**
     * Displays the ExtendedEmail object in the HTML editor.
     *
     * @param email
     */
    private void showEmailDetails(ExtendedEmail email) {
        controller.displayEmailAsHTML(email);   
    }
}
