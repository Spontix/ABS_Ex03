package absController;

import dataObjects.dtoBank.dtoAccount.DTOLoan;
import dataObjects.dtoBank.dtoAccount.DTOMovement;
import dataObjects.dtoCustomer.DTOCustomer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomersListController implements Initializable {

    @FXML
    protected GridPane CustomersMainGridPane;

    @FXML
    protected ListView<DTOCustomer> customersListView;

    @FXML
    protected Accordion customersAccordionInformation;

    @FXML
    protected TableView<DTOMovement> movementsTableView;

    @FXML
    private TableColumn<DTOMovement, Integer> yazColumn;

    @FXML
    private TableColumn<DTOMovement, Integer> sumColumn;

    @FXML
    private TableColumn<DTOMovement, String > actionColumn;

    @FXML
    private TableColumn<DTOMovement, Integer> sumBeforeColumn;

    @FXML
    private TableColumn<DTOMovement, Integer> sumAfterColumn;

    @FXML
    protected Label InterestAndCapitalLoanerLabel;

    @FXML
    protected Label firstStatusLoanerLabel;

    @FXML
    protected Label secondStatusLoanerLabel;

    @FXML
    protected ListView<DTOLoan> loansListLoanerView;

    @FXML
    protected Label InterestAndCapitalLenderLabel;

    @FXML
    protected Label firstStatusLenderLabel;

    @FXML
    protected Label secondStatusLenderLabel;

    @FXML
    protected ListView<DTOLoan> loansListLenderView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        yazColumn.setCellValueFactory(new PropertyValueFactory<>("toDoYazTime"));
        sumColumn.setCellValueFactory(new PropertyValueFactory<>("sum"));
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("operation"));
        sumBeforeColumn.setCellValueFactory(new PropertyValueFactory<>("sumBeforeOperation"));
        sumAfterColumn.setCellValueFactory(new PropertyValueFactory<>("sumAfterOperation"));

    }
}
