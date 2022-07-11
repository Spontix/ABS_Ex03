package absController;

import dataObjects.dtoBank.dtoAccount.DTOInlay;
import dataObjects.dtoBank.dtoAccount.DTOLoan;
import dataObjects.dtoBank.dtoAccount.DTOMovement;
import dataObjects.dtoCustomer.DTOCustomer;
import javafx.animation.*;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.dialog.ProgressDialog;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;

import static java.lang.Thread.sleep;

public class CustomerController extends HelperFunction implements Initializable {
    protected DTOCustomer dtoCustomer;
    protected ListView<DTOLoan> allInlayListView;
    protected ListView<DTOLoan> chosenInlayListView;
    protected ABSController absControllerRef;
    private Task<Boolean> workerScrambleTask;


    @FXML
    protected TextArea errorTextArea;
    @FXML
    private ImageView imageAnimation;

    @FXML
    private BorderPane loansThatShouldBePaidBorderPane;

    @FXML
    protected TableView<DTOMovement> customerMovments;

    @FXML
    private TableColumn<DTOMovement, Integer> yazColumn;

    @FXML
    private TableColumn<DTOMovement, Integer> sumColumn;

    @FXML
    private TableColumn<DTOMovement, String> actionColumn;

    @FXML
    private TableColumn<DTOMovement, Integer> sumBeforeColumn;

    @FXML
    private TableColumn<DTOMovement, Integer> sumAfterColumn;
    //
    @FXML
    private Tab informationTab;

    @FXML
    private Tab scrambleTab;

    @FXML
    protected ListView<DTOLoan> loanerLoansListView;

    @FXML
    protected ListView<DTOLoan> LenderLoansTableListView;

    @FXML
    private Button chargeButton;

    @FXML
    private Button withdrawButton;

    @FXML
    protected Button doneChosenLoanButton;

    @FXML
    protected Button unChosenLoanButton;

    @FXML
    protected Button chooseLoanButton;

    @FXML
    private BorderPane chosenInlayLoansBorderPane;

    @FXML
    private BorderPane allInlayLoansBorderPane;

    @FXML
    protected TabPane customerTablePane;

    @FXML
    protected CheckComboBox<String> categoriesList;

    @FXML
    protected Button enableInlayButton;

    @FXML
    private TextField investmentAmount;

    @FXML
    private TextField minimumInterestYaz;

    @FXML
    private TextField minimumTotalYaz;

    @FXML
    private TextField maximumLoansOpenToTheBorrower;

    @FXML
    private TextField maximumLoanOwnershipPercentage;

    @FXML
    protected ListView<String> notificationAreaListView;


    protected LoansListController loansListController;

    @FXML
    protected Button closeLoanButton;

    @FXML
    protected Button payButton;

    List<DTOLoan> loansThatShouldPay;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        yazColumn.setCellValueFactory(new PropertyValueFactory<>("toDoYazTime"));
        sumColumn.setCellValueFactory(new PropertyValueFactory<>("sum"));
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("operation"));
        sumBeforeColumn.setCellValueFactory(new PropertyValueFactory<>("sumBeforeOperation"));
        sumAfterColumn.setCellValueFactory(new PropertyValueFactory<>("sumAfterOperation"));

        loansListController = myFXMLLoader("LoansListViewer.fxml");

        loansThatShouldBePaidBorderPane.setCenter(loansListController.LoansMainGridPane);
        chosenInlayListView = new ListView<>();
        allInlayListView = new ListView<>();

        scrambleTab.setOnSelectionChanged(e -> {
            errorTextArea.setVisible(false);
        });

        informationTab.setOnSelectionChanged(e -> {

            /*if (bank != null) {

                setCurrentCustomer(bank.getCustomerByName(dtoCustomer.getCustomerName()));
                showLoanInformationInAdminAndCustomerView(loanerLoansListView, bank.getCustomerLoanersList(dtoCustomer.getCustomerName()),false);
                showLoanInformationInAdminAndCustomerView(LenderLoansTableListView, bank.getCustomerBorrowersList(dtoCustomer.getCustomerName()),false);
                customerMovments.setItems(FXCollections.observableArrayList(dtoCustomer.getMovements()));

            }
        });
        chargeButton.setOnAction(c->{
            chargeOrWithdrawAction(1);
        });

        withdrawButton.setOnAction(w->{
            chargeOrWithdrawAction(2);
     
        
             */
        });
    }
    
    

    /*private void chargeOrWithdrawAction(int indexOperation) {
        String textOperation;
        if (indexOperation == 1)
            textOperation = "charge";
        else
            textOperation = "withdraw";
        TextInputDialog paymentDialog = new TextInputDialog();
        paymentDialog.setTitle("Run an action.");
        paymentDialog.setContentText("Please enter the amount of money you would like to " + textOperation + ": ");
        paymentDialog.setHeaderText("Current Balance: " + bank.getCustomerByName(dtoCustomer.getCustomerName()).getAmount());
        paymentDialog.showAndWait();

        try {
            int amount = Integer.parseInt(paymentDialog.getResult());
            if (paymentDialog.getResult() != null && amount > 0) {
                if (indexOperation == 1) {
                    DTOMovement dtoMovement = bank.movementBuildToCustomer(dtoCustomer, amount, "+", bank.getCustomerByName(dtoCustomer.getCustomerName()).getAmount(), bank.getCustomerByName(dtoCustomer.getCustomerName()).getAmount() + amount);
                    bank.cashDeposit(bank.getRealCustomerByName(dtoCustomer.getCustomerName()), amount);
                    customerMovments.getItems().add(dtoMovement);
                } else if (indexOperation == 2 && amount <= bank.getCustomerByName(dtoCustomer.getCustomerName()).getAmount()) {
                    DTOMovement dtoMovement = bank.movementBuildToCustomer(dtoCustomer, amount, "-", bank.getCustomerByName(dtoCustomer.getCustomerName()).getAmount(), bank.getCustomerByName(dtoCustomer.getCustomerName()).getAmount() - amount);
                    bank.cashWithdrawal(bank.getRealCustomerByName(dtoCustomer.getCustomerName()), amount);
                    customerMovments.getItems().add(dtoMovement);
                } else
                    throw new NumberFormatException();
            }
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException ex){
                ex.printStackTrace();
            }
    }*/

    //public void setBankInCustomerController(UIInterfaceLogic bank){this.bank= bank;}

    @FXML
    private int MaximumLoanOwnershipPercentageActionLisener() {
        int maxLoanOwnershipPercentage = 0;
        String number = maximumLoanOwnershipPercentage.getText();
        if (Objects.equals(number, ""))
            return 0;
        else {
            maxLoanOwnershipPercentage = Integer.parseInt((number));
            if (maxLoanOwnershipPercentage < 1 || maxLoanOwnershipPercentage > 100)
                throw new NumberFormatException("In 'Maximum loan ownership percentage' - Invalid input!! Please enter a number between 1 and 100, or leave this figure empty.");
        }
        return maxLoanOwnershipPercentage;
    }

    @FXML
    private int MaximumLoansOpenToTheBorrowerActionLisener() {
        int maxLoansOpen;
        String number = maximumLoansOpenToTheBorrower.getText();
        if (Objects.equals(number, ""))
            return 0;
        else {
            maxLoansOpen = Integer.parseInt((number));
            if (maxLoansOpen <= 0)
                throw new NumberFormatException("In 'Maximum loans open to the borrower' - Invalid input!! Please enter a number greater than 0 or leave this figure empty.");
        }
        return maxLoansOpen;
    }

    //
    @FXML
    private double MinimumInterestYazActionLisener() {
        double chosenMinInterestYaz;
        String number = minimumInterestYaz.getText();
        if (Objects.equals(number, ""))
            return 0;
        else {
            chosenMinInterestYaz = Double.parseDouble((number));
            if (chosenMinInterestYaz <= 0 || chosenMinInterestYaz > 100)
                throw new NumberFormatException("In 'Minimum interest yaz' - Invalid input!! Please enter a number greater than 0 or leave this figure empty.");

        }
        return chosenMinInterestYaz;
    }

    @FXML
    private int MinimumTotalYazActionLisener() {
        int chosenMinYazTime;
        String number = minimumTotalYaz.getText();
        if (Objects.equals(number, ""))
            return 0;
        else {
            chosenMinYazTime = Integer.parseInt((number));
            if (chosenMinYazTime <= 0)
                throw new NumberFormatException("In 'Minimum total yaz' - Invalid input!! Please enter a number greater than 0 or leave this figure empty.");
        }
        return chosenMinYazTime;
    }

    @FXML
    private int investmentAmountActionListener() throws Exception {
        String amount = investmentAmount.getText();
        int amountFromUser;
        amountFromUser = Integer.parseInt(amount);
       /* if (amountFromUser <= 0 || amountFromUser > (bank.getCustomerByName(dtoCustomer.getCustomerName())).getAmount())
            throw new NumberFormatException("In 'Amount to investment' - Invalid input!! Please enter a number greater than 0 and less than " + (bank.getCustomerByName(dtoCustomer.getCustomerName())).getAmount());
*/
        return amountFromUser;
    }


    /*@FXML
    private void ClickEnableInlayButtonActionLisener(ActionEvent event) {
        mySetVisible(false);
        errorTextArea.setVisible(false);
        chosenInlayListView.getItems().clear();
        try {
            ObservableList<String> list = categoriesList.getCheckModel().getCheckedItems();
            int investAmount = investmentAmountActionListener();
            int minimumTotalYaz = MinimumTotalYazActionLisener();
            double minimumInterestYaz = MinimumInterestYazActionLisener();
            int maximumLoansOpenToTheBorrower = MaximumLoansOpenToTheBorrowerActionLisener();
            int maximumLoanOwnershipPercentage = MaximumLoanOwnershipPercentageActionLisener();
            DTOInlay dtoInlay = bank.inlayBuildForDK(dtoCustomer, investAmount,list.toString().substring(1,list.toString().length()-1), minimumInterestYaz, minimumTotalYaz, maximumLoansOpenToTheBorrower);
            ArrayList<DTOLoan> loansSupportInlay = bank.loansSustainInlayDK(dtoInlay);
            if(loansSupportInlay.isEmpty()){
                popupMessage("Failed!!!", "No loans were found for this inlay.");
            }
            else {
                mySetVisible(true);
                allInlayLoansBorderPane.setCenter(allInlayListView);
                chosenInlayLoansBorderPane.setCenter(chosenInlayListView);
                showLoanInformationInAdminAndCustomerView(allInlayListView, loansSupportInlay,false);
                popupMessage("Success!!!", "Please click on the loan you want and then click on the 'Add loan' button.");

                chooseLoanButton.setOnAction(e->{
                    chooseLoanButtonSetOnAction();
                });
                unChosenLoanButton.setOnAction(e->{
                    unChosenLoanButtonSetOnAction();
                });
               doneChosenLoanButton.setOnAction(e-> {
                    workerScrambleTask=new Task<Boolean>(){
                        @Override
                        protected Boolean call() throws Exception {
                            List<DTOMovement>  dtoMovementList = bank.addMovementPerLoanFromInlayDK(dtoInlay, new ArrayList<>(chosenInlayListView.getItems()), investAmount, maximumLoanOwnershipPercentage);
                            loansThatShouldPay = bank.yazProgressLogicDesktop();
                            sleepForSomeTime();
                            return true;
                        }
                    };
                    ProgressDialog progressDialog=new ProgressDialog(workerScrambleTask);
                    progressDialog.setContentText("Please wait...");
                    progressDialog.setTitle("Scramble");
                    progressDialog.setHeaderText("Doing the scramble");
                    new Thread(workerScrambleTask).start();
                    progressDialog.showAndWait();
                    absControllerRef.clearAllLoansPayListView();
                    absControllerRef.addTheLoansThatShouldPayToAllTheLoansPayListView(loansThatShouldPay);
                    allInlayListView.getItems().clear();
                    mySetVisible(false);
                    popupMessage("Success!", "The operation is done.");
                });
            }
        } catch (Exception e) {
            String message = e.getMessage();
            errorTextArea.setVisible(true);
            errorTextArea.setText("ERROR: " + message);
            //ShakeTransition anim = new ShakeTransition(dialog.getDialogPane(), t->dialog.close());
            //anim.playFromStart();
        }
    }
*/
    private void unChosenLoanButtonSetOnAction() {
        DTOLoan localLoan = chosenInlayListView.getSelectionModel().getSelectedItem();
        if (localLoan != null) {
            allInlayListView.getItems().add(localLoan);
            chooseLoanButton.setDisable(false);
            chosenInlayListView.getItems().removeAll(localLoan);
            if (chosenInlayListView.getItems().isEmpty())
                unChosenLoanButton.setDisable(true);
        }
    }

    private void chooseLoanButtonSetOnAction() {
        DTOLoan localLoan = allInlayListView.getSelectionModel().getSelectedItem();
        if (localLoan != null) {
            chosenInlayListView.getItems().add(localLoan);
            unChosenLoanButton.setDisable(false);
            allInlayListView.getItems().removeAll(localLoan);
            if (allInlayListView.getItems().isEmpty())
                chooseLoanButton.setDisable(true);
        }
    }

    private void mySetVisible(boolean parameter) {
        allInlayListView.setVisible(parameter);
        chosenInlayListView.setVisible(parameter);
        chooseLoanButton.setVisible(parameter);
        unChosenLoanButton.setVisible(parameter);
        doneChosenLoanButton.setVisible(parameter);
    }


    private void sleepForSomeTime() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ignored) {
        }
    }


    protected void setCurrentCustomer(DTOCustomer dtoCustomer) {
        this.dtoCustomer = dtoCustomer;
    }

    protected void setAbsControllerRef(ABSController absController) {
        this.absControllerRef = absController;
    }


    public void ClickEnableInlayButtonActionLisener(ActionEvent actionEvent) {
    }
}
