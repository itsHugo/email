package com.hugopham.fxcontrollers;

import com.hugopham.mailmoduledatabase.EmailDAO;
import com.hugopham.mailmoduledatabase.EmailDAOImpl;
import com.hugopham.mailmodules.ExtendedEmail;
import com.sun.javafx.collections.ImmutableObservableList;
import java.sql.SQLException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

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

    public EmailTableController() {
        super();
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    public void initialize() {

        emailDAO = new EmailDAOImpl();
        // Connects the property in the ExtendedEmail object to the column in the
        // table
        senderColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().
                getFrom().getEmail()));
        subjectColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()
                .getSubject()));
        // Check if date is null, displays empty string if true
        receiveDate.setCellValueFactory(cellData -> new SimpleStringProperty((cellData.getValue()
                .getReceiveDate()) == null ? "" : cellData.getValue().getReceiveDate().toString() ));

        //adjustColumnWidths();

        // Listen for selection changes and show the fishData details when
        // changed.
        emailDataTable
                .getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable, oldValue, newValue) -> showEmailDetails(newValue));
        
        try{
           displayTheTable(); 
        } catch(SQLException ex) {
            
        }
        
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
     * The table displays the email data
     *
     * @throws SQLException
     */
    public void displayTheTable() throws SQLException {
        ObservableList<ExtendedEmail> observableList = 
                FXCollections.observableList(this.emailDAO.findAll());
        
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
        senderColumn.setPrefWidth(width * .05);
        subjectColumn.setPrefWidth(width * .15);
        receiveDate.setPrefWidth(width * .15);
    }

    /**
     * To be able to test the selection handler for the table, this method
     * displays the FishData object that corresponds to the selected row.
     *
     * @param fishData
     */
    private void showEmailDetails(ExtendedEmail email) {
        System.out.println("showEmailDetails\n" + email);
    }
}
