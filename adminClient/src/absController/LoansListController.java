package absController;

import dataObjects.dtoBank.dtoAccount.DTOInlay;
import dataObjects.dtoBank.dtoAccount.DTOLoan;
import dataObjects.dtoBank.dtoAccount.DTOPaymentsInfo;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class LoansListController implements Initializable{
    @FXML
    protected TableView<DTOPaymentsInfo> inRiskStatusTableView;

    @FXML
    protected TableView<DTOLoan> activeStatusTableView;

    @FXML
    protected TableView<DTOInlay> lendersTableView;

    @FXML
    protected GridPane LoansMainGridPane;

    @FXML
    private TableColumn<DTOInlay, String> nameColumn;

    @FXML
    private TableColumn<DTOInlay, Integer> investmentColumn;

    @FXML
    protected Accordion loansAccordionInformation;

    @FXML
    protected ListView<DTOLoan> LoansListView;

    @FXML
    protected Label totalPaymentsLabel;

    @FXML
    protected Label leftPaymentsLabel;

    @FXML
    private TableColumn<DTOLoan, Integer> activatedColumnAC;

    @FXML
    private TableColumn<DTOLoan, Integer> nextPaymentColumnAC;

    @FXML
    private TableColumn<DTOLoan, Integer> capitalPaidColumnAC;

    @FXML
    private TableColumn<DTOLoan, Integer> capitalLeftColumnAC;

    @FXML
    private TableColumn<DTOLoan, Integer> InterestPaidColumnAC;

    @FXML
    private TableColumn<DTOLoan, Integer> InterestLeftColumnAC;

    @FXML
    private TableColumn<DTOPaymentsInfo, Integer> yazPaymentColumnACRI;

    @FXML
    private TableColumn<DTOPaymentsInfo,Integer> capitalAmountColumnACRI;

    @FXML
    private TableColumn<DTOPaymentsInfo, Integer> interestAmountColumnACRI;

    @FXML
    private TableColumn<DTOPaymentsInfo, Integer> capitalAndInterestColumnACRI;

    @FXML
    private TableColumn<DTOPaymentsInfo, String> isPaidColumnACRI;

    @FXML
    protected Label startYazLabelFI;

    @FXML
    protected Label endYazColumnFI;

    @FXML
    protected Label delayedPaymentsColumnRI;

    @FXML
    protected Label totalDelayedColumnRI;




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        investmentColumn.setCellValueFactory(new PropertyValueFactory<>("investAmount"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        activatedColumnAC.setCellValueFactory(new PropertyValueFactory<>("startedYazInActive"));
        nextPaymentColumnAC.setCellValueFactory(new PropertyValueFactory<>("nextYazToBePaid"));
        capitalPaidColumnAC.setCellValueFactory(new PropertyValueFactory<>("totalCapitalPayTillNow"));
        capitalLeftColumnAC.setCellValueFactory(new PropertyValueFactory<>("totalCapitalPayTillEnd"));
        InterestPaidColumnAC.setCellValueFactory(new PropertyValueFactory<>("totalInterestPayTillNow"));
        InterestLeftColumnAC.setCellValueFactory(new PropertyValueFactory<>("totalInterestPayTillEnd"));
        yazPaymentColumnACRI.setCellValueFactory(new PropertyValueFactory<>("yazPayment"));
        capitalAmountColumnACRI.setCellValueFactory(new PropertyValueFactory<>("capitalAmount"));
        interestAmountColumnACRI.setCellValueFactory(new PropertyValueFactory<>("interestAmount"));
        capitalAndInterestColumnACRI.setCellValueFactory(new PropertyValueFactory<>("capitalAndInterest"));
        isPaidColumnACRI.setCellValueFactory(new PropertyValueFactory<>("isPaid"));


    }
}
