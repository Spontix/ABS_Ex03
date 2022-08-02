package absController;

import dataObjects.dtoBank.DTOBank;
import dataObjects.dtoBank.dtoAccount.*;
import dataObjects.dtoCustomer.DTOCustomer;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import okhttp3.*;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.dialog.ProgressDialog;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;

import static java.lang.Thread.sleep;
import static util.Constants.*;

public class CustomerController extends HelperFunction implements Initializable {
    protected DTOCustomer dtoCustomer = new DTOCustomer();
    protected ListView<DTOLoan> allInlayListView;
    protected ListView<DTOLoan> chosenInlayListView;
    protected ABSController absControllerRef;
    private Task<Boolean> workerScrambleTask;
    private Timer timer;
    private TimerTask listRefresher;
    private IntegerProperty currentYaz=new SimpleIntegerProperty();


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
    private ListView<DTOLoanForSale> loansForSaleListView;

    @FXML
    private Button sellLoanButton;

    @FXML
    private Button buyLoanButton;

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

    List<String> originalStringList=new ArrayList<>();

    boolean firstTime=true;

    private void onSaleButton(){
        sellLoanButton.setOnAction(e->{
            DTOLoan dtoLoan = LenderLoansTableListView.getSelectionModel().getSelectedItem();
            getLoansForSaleServlet(dtoLoan.getId());
        });
    }

    private void onBuyButton(){
        buyLoanButton.setOnAction(e->{
            DTOLoanForSale dtoLoanForSale = loansForSaleListView.getSelectionModel().getSelectedItem();
            loanToBuyServlet(dtoLoanForSale);
        });
    }

    private void loanToBuyServlet(DTOLoanForSale dtoLoanForSale){
        String finalUrl = HttpUrl
                .parse(BUY_LOAN_PAGE_CUSTOMER)
                .newBuilder()
                .build()
                .toString();
        String json = GSON_INSTANCE.toJson(dtoLoanForSale);
        HttpClientUtil.runAsyncJson(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        popupMessage("Error","Something went wrong: " + e.getMessage())//ToDo
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            popupMessage("Error","Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        String rawBody = null;
                        try {
                            rawBody = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                      popupMessage("Success",rawBody);
                    });
                }
            }
        },json);
    }

    private void updateCustomer(DTOCustomer dtoCustomer){
        Platform.runLater(()->{
            try{
                this.dtoCustomer=dtoCustomer;
                mySetDisable(dtoCustomer.getRewind());
                if(dtoCustomer.getRewind())
                    payButton.setDisable(true);

                absControllerRef.showLoanInformationInAdminAndCustomerViewServlet(Constants.AS_LOANER_PAGE_CUSTOMER,loanerLoansListView,dtoCustomer.getName(), dtoCustomer.getRewind());
                absControllerRef.showLoanInformationInAdminAndCustomerViewServlet(Constants.AS_BORROWER_PAGE_CUSTOMER,LenderLoansTableListView,dtoCustomer.getName(),dtoCustomer.getRewind());
                customerMovments.setItems(FXCollections.observableArrayList(dtoCustomer.getMovements()));
                getYazUnitServlet();
                getLoansForSaleServlet("");
                absControllerRef.showLoanInformationInAdminAndCustomerViewServlet(LOANS_TO_PAY_PAGE_CUSTOMER,loansListController.LoansListView,dtoCustomer.getName(),dtoCustomer.getRewind());

            }
            catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    private void getMassagesByYaz(int yaz){
        List<String> list=new ArrayList<>();
        //String yazHistoryPaymentYaz="Payment Yaz: " + yaz;
        String yazHistoryPaymentYaz=String.valueOf(yaz);

        String yazHistoryPaymentYazThatDidntHappened = "Payment Yaz that didnt happened : " + yaz;
        for (String string: notificationAreaListView.getItems()) {
                list.add(string);
            if(string.contains(yazHistoryPaymentYaz) || string.contains(yazHistoryPaymentYazThatDidntHappened)){
                break;
            }
        }

        notificationAreaListView.getItems().clear();
        notificationAreaListView.getItems().addAll(list);
        /*if(notificationAreaListView.getItems().size()!=list.size()) {
            firstTime = false;
            list=new ArrayList<>();
            notificationAreaListView.getItems().clear();
            notificationAreaListView.getItems().addAll(list);
        }
        else {
            originalStringList.clear();
            originalStringList.addAll(notificationAreaListView.getItems());
        }*/

    }

    private void mySetDisable(boolean flag){
        closeLoanButton.setDisable(flag);
        enableInlayButton.setDisable(flag);
        chooseLoanButton.setDisable(flag);
        unChosenLoanButton.setDisable(flag);
        doneChosenLoanButton.setDisable(flag);
        withdrawButton.setDisable(flag);
        chargeButton.setDisable(flag);
        absControllerRef.loadFileButton.setDisable(flag);

      /*  if(firstTime && flag){
            getMassagesByYaz(currentYaz);
        }
        else if(!flag && !firstTime) {
            notificationAreaListView.getItems().addAll(originalStringList);
            firstTime=true;
        }*/
    }

    private void getYazUnitServlet(){
        String finalUrl = HttpUrl
                .parse(YAZ_UNIT_PAGE_CUSTOMER)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        popupMessage("Error", "Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            popupMessage("Error", "Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        String rawBody = null;
                        try {
                            rawBody = response.body().string().trim();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        absControllerRef.currentYaz.setText("Current Yaz : "+ rawBody);
                        currentYaz.set(Integer.parseInt(rawBody));
                    });
                }
            }
        });
    }

    private void getLoansForSaleServlet(String loanID){
        String finalUrl = HttpUrl
                .parse(SELL_LOAN_PAGE_CUSTOMER)
                .newBuilder()
                .addQueryParameter("loan", loanID)
                .build()
                .toString();

        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        popupMessage("Error", "Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            popupMessage("Error", "Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        String rawBody = null;
                        try {
                            rawBody = response.body().string().trim();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        DTOLoansForSaleList dtoLoansForSaleList=GSON_INSTANCE.fromJson(rawBody, DTOLoansForSaleList.class);
                        if(loanID.equals(""))
                        showLoansForSaleInformationInCustomerView(loansForSaleListView,dtoLoansForSaleList.getDTOLoansForSale(),dtoCustomer.getRewind());
                    });
                }
            }
        });
    }

    public void setListRefresher(){
        listRefresher=new CustomerRefresher(this::updateCustomer);
        timer= new Timer();
        timer.schedule(listRefresher,REFRESH_RATE,REFRESH_RATE);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        yazColumn.setCellValueFactory(new PropertyValueFactory<>("toDoYazTime"));
        sumColumn.setCellValueFactory(new PropertyValueFactory<>("sum"));
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("operation"));
        sumBeforeColumn.setCellValueFactory(new PropertyValueFactory<>("sumBeforeOperation"));
        sumAfterColumn.setCellValueFactory(new PropertyValueFactory<>("sumAfterOperation"));

        loansListController = myFXMLLoader("LoansListViewer.fxml");

        loansThatShouldBePaidBorderPane.setCenter(loansListController.LoansMainGridPane);
        onLoanClickProperty(loansListController,this);
        onSaleButton();
        onBuyButton();
        currentYaz.addListener(((observable, oldValue, newValue) -> {
            if(oldValue.intValue()>newValue.intValue()) {
                getMassagesByYaz(newValue.intValue());
            }
            else {
                notificationAreaListView.getItems().clear();
                notificationAreaListView.getItems().addAll(originalStringList);
            }
        }));
        setListRefresher();

        chosenInlayListView = new ListView<>();
        allInlayListView = new ListView<>();
        //onLoanClickProperty(loansListController);
        scrambleTab.setOnSelectionChanged(e -> errorTextArea.setVisible(false));
        chargeButton.setOnAction(c -> chargeOrWithdrawAction(1));
        withdrawButton.setOnAction(w -> chargeOrWithdrawAction(2));


        informationTab.setOnSelectionChanged(e -> {

        });
    }

    private void onLoanClickProperty(LoansListController loansListController) {
        loansListController.LoansListView.getSelectionModel().selectedItemProperty().addListener(e -> {
            if (!loansListController.LoansListView.getItems().isEmpty()) {
                DTOLoan selectedLoan = loansListControllerHandler(loansListController);
            }
        });
    }

    private DTOLoan loansListControllerHandler(LoansListController loansListController){
        clearComponents(loansListController);
        DTOLoan selectedLoan = loansListController.LoansListView.getSelectionModel().getSelectedItem();
        if(selectedLoan!=null) {
            loansListController.loansAccordionInformation.setVisible(true);
            if (selectedLoan.getLoanStatus() != DTOLoanStatus.NEW) {
                loansListController.leftPaymentsLabel.setText("Left payments : " + String.valueOf(selectedLoan.getCapital() - selectedLoan.getCapitalSumLeftTillActive()));
                loansListController.totalPaymentsLabel.setText("Total payments : " + String.valueOf(selectedLoan.getCapitalSumLeftTillActive()));
                loansListController.lendersTableView.setItems(FXCollections.observableArrayList(selectedLoan.getListOfInlays()));
                loansListController.activeStatusTableView.setItems(FXCollections.observableArrayList(selectedLoan));
                loansListController.inRiskStatusTableView.setItems(FXCollections.observableArrayList(selectedLoan.getPaymentsInfoList()));
                loansListController.delayedPaymentsColumnRI.setText("Delayed payments : " + String.valueOf(selectedLoan.getInRiskCounter()));
                loansListController.totalDelayedColumnRI.setText("Total delayed : " + String.valueOf(selectedLoan.getDebt()));
                if (selectedLoan.getLoanStatus() == DTOLoanStatus.FINISHED) {
                    loansListController.endYazColumnFI.setVisible(true);
                    loansListController.startYazLabelFI.setVisible(true);
                    loansListController.endYazColumnFI.setText("End Yaz : " + String.valueOf(selectedLoan.getEndedYaz()));
                    loansListController.startYazLabelFI.setText("Start Yaz : " + String.valueOf(selectedLoan.getStartedYazInActive()));
                }
            }
        }
        return selectedLoan;
    }

    private void clearComponents(LoansListController loansListController){
        loansListController.activeStatusTableView.getItems().clear();
        loansListController.inRiskStatusTableView.getItems().clear();
        loansListController.loansAccordionInformation.setVisible(false);
        loansListController.endYazColumnFI.setVisible(false);
        loansListController.startYazLabelFI.setVisible(false);
        loansListController.leftPaymentsLabel.setText("");
        loansListController.totalPaymentsLabel.setText("");
        loansListController.lendersTableView.getItems().clear();
        loansListController.activeStatusTableView.getItems().clear();
        loansListController.inRiskStatusTableView.getItems().clear();
        loansListController.delayedPaymentsColumnRI.setText("");
        loansListController.totalDelayedColumnRI.setText("");
        loansListController.endYazColumnFI.setText("");
        loansListController.startYazLabelFI.setText("");
        loansListController.endYazColumnFI.setText("");
        loansListController.startYazLabelFI.setText("");
    }

    private void chargeOrWithdrawAction(int indexOperation) {
        String textOperation;
        if (indexOperation == 1)
            textOperation = "charge";
        else
            textOperation = "withdraw";
        TextInputDialog paymentDialog = new TextInputDialog();
        paymentDialog.setTitle("Run an action.");
        paymentDialog.setContentText("Please enter the amount of money you would like to " + textOperation + ": ");
        paymentDialog.setHeaderText("Current Balance: " + dtoCustomer.getAmount());
        paymentDialog.showAndWait();
        try {
            int amount = Integer.parseInt(paymentDialog.getResult());

            if (paymentDialog.getResult() != null && amount > 0) {
                if (indexOperation == 1) {
                    cashDepositAndWithdrawalServlet(DEPOSIT_PAGE_CUSTOMER, String.valueOf(amount));
                    dtoCustomer.addAmount(amount);
                } else if (indexOperation == 2 && amount <= dtoCustomer.getAmount()) {
                    cashDepositAndWithdrawalServlet(WITHDRAWAL_PAGE_CUSTOMER, String.valueOf(amount));
                    dtoCustomer.addAmount(amount * (-1));
                } else
                    throw new NumberFormatException();
            }
        }catch (NumberFormatException e){
            popupMessage("Error","Please be sure to enter a number");
        }
    }

    private void cashDepositAndWithdrawalServlet(String url, String amount) {
        String finalUrl = HttpUrl
                .parse(url)
                .newBuilder()
                .addQueryParameter("amount", amount)
                .build()
                .toString();

        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        popupMessage("Error", "Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            popupMessage("Error", "Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        String rawBody = null;
                        try {
                            rawBody = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        DTOMovement dtoMovement = GSON_INSTANCE.fromJson(rawBody, DTOMovement.class);
                        customerMovments.getItems().add(dtoMovement);
                    });
                }
            }
        });
    }


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
        if (amountFromUser <= 0 || amountFromUser > dtoCustomer.getAmount())
            throw new NumberFormatException("In 'Amount to investment' - Invalid input!! Please enter a number greater than 0 and less than " + dtoCustomer.getAmount());

        return amountFromUser;
    }


    @FXML
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
            DTOInlay dtoInlay = new DTOInlay(dtoCustomer, investAmount, list.toString().substring(1, list.toString().length() - 1), minimumInterestYaz, minimumTotalYaz, maximumLoansOpenToTheBorrower,maximumLoanOwnershipPercentage);
            inlayServlet(dtoInlay);
        } catch (Exception e) {
            String message = e.getMessage();
            errorTextArea.setVisible(true);
            errorTextArea.setText("ERROR: " + message);
        }
    }

    private void selectPotentialLoans(ArrayList<DTOLoan> loans) {
        if (loans.isEmpty()) {
            popupMessage("Failed!!!", "No loans were found for this inlay.");
        } else {
            mySetVisible(true);
            allInlayLoansBorderPane.setCenter(allInlayListView);
            chosenInlayLoansBorderPane.setCenter(chosenInlayListView);
            showLoanInformationInAdminAndCustomerView(allInlayListView, loans,false);
            popupMessage("Success!!!", "Please click on the loan you want and then click on the 'Add loan' button.");

            chooseLoanButton.setOnAction(e -> {
                chooseLoanButtonSetOnAction();
            });
            unChosenLoanButton.setOnAction(e -> {
                unChosenLoanButtonSetOnAction();
            });
            doneChosenLoanButton.setOnAction(e -> {
                workerScrambleTask = new Task<Boolean>() {
                    @Override
                    protected Boolean call() throws Exception {
                        selectedPotentialLoansServlet( new ArrayList<>(chosenInlayListView.getItems()));
                        sleepForSomeTime();
                        return true;
                    }
                };
                ProgressDialog progressDialog = new ProgressDialog(workerScrambleTask);
                progressDialog.setContentText("Please wait...");
                progressDialog.setTitle("Scramble");
                progressDialog.setHeaderText("Doing the scramble");
                new Thread(workerScrambleTask).start();
                progressDialog.showAndWait();
                clearAllLoansPayListView();
                //addTheLoansThatShouldPayToAllTheLoansPayListView(loansThatShouldPay);
                allInlayListView.getItems().clear();
                mySetVisible(false);
                popupMessage("Success!", "The operation is done.");
            });
        }
    }

    private void clearAllLoansPayListView(){
            loansListController.LoansListView.getItems().clear();
    }


    private void inlayServlet(DTOInlay inlay){//ToDo we are getting the potential loans and than we need to seperait the methode above and continue withe the second method
        String finalUrl = HttpUrl
                .parse(POTENTIAL_LOANS_PAGE_CUSTOMER)
                .newBuilder()
                .build()
                .toString();
        String json = GSON_INSTANCE.toJson(inlay);
        HttpClientUtil.runAsyncJson(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        popupMessage("There is not Customer by this name","Something went wrong: " + e.getMessage())//ToDo
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            popupMessage("Error","Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        String rawBody = null;
                        try {
                            rawBody = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        DTOLoansList dtoLoansList=GSON_INSTANCE.fromJson(rawBody, DTOLoansList.class);
                        selectPotentialLoans(dtoLoansList.getDTOLoans());
                    });
                }
            }
        },json);
    }

    private void selectedPotentialLoansServlet(ArrayList<DTOLoan> loans){//ToDo we are getting the potential loans and than we need to seperait the methode above and continue withe the second method
        String finalUrl = HttpUrl
                .parse(SELECTED_LOANS_LOAN_PAGE_CUSTOMER)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsyncSelectedLoans(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        popupMessage("There is not Customer by this name","Something went wrong: " + e.getMessage())//ToDo
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            popupMessage("Error","Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        String rawBody = null;
                        try {
                            rawBody = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        },loans);
    }

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
        chooseLoanButton.setDisable(false);
        unChosenLoanButton.setVisible(parameter);
        doneChosenLoanButton.setVisible(parameter);
    }


    private void sleepForSomeTime() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ignored) {
        }
    }

    protected void setAbsControllerRef(ABSController absController) {
        this.absControllerRef = absController;
    }

    private void onLoanClickProperty(LoansListController loansListController,CustomerController customerController){
        loansListController.LoansListView.getSelectionModel().selectedItemProperty().addListener(e -> {
            if(!loansListController.LoansListView.getItems().isEmpty()) {
                DTOLoan selectedLoan= loansListControllerHandler(loansListController);
                    if (selectedLoan.getLoanStatus() == DTOLoanStatus.RISK || (!selectedLoan.getIsPaid() && selectedLoan.numberOfYazTillNextPulseWithoutTheIncOfWindowOfPaymentCounterDK(currentYaz.get()) == 0)) {
                        if(dtoCustomer.getRewind()){
                            payButton.setDisable(true);
                            return;
                        }
                        payButton.setDisable(false);
                        payButton.setOnAction(e1 -> {
                            try {
                                if (selectedLoan.getLoanStatus() == DTOLoanStatus.RISK) {
                                    TextInputDialog paymentDialog = new TextInputDialog();
                                    paymentDialog.setTitle("Loan In Risk");
                                    paymentDialog.setContentText("Please enter the amount of money you would like to pay:");
                                    paymentDialog.setHeaderText("Current Balance: " + dtoCustomer.getAmount() + "\nDebt Amount: " + (selectedLoan.getDebt()));
                                    paymentDialog.showAndWait();

                                    if (paymentDialog.getResult() != null && (Integer.parseInt(paymentDialog.getResult()) <= selectedLoan.getDebt())) {
                                        operateThePaymentOfTheLoanDesktopServlet(selectedLoan,Integer.parseInt(paymentDialog.getResult()));
                                       // bank.operateThePaymentOfTheLoanDesktop(selectedLoan, Integer.parseInt(paymentDialog.getResult()));
                                    } else {
                                        throw new Exception("The payment was not preformed!");
                                    }
                                }
                                else if (selectedLoan.numberOfYazTillNextPulseWithoutTheIncOfWindowOfPaymentCounterDK(currentYaz.get()) == 0) {
                                    operateThePaymentOfTheLoanDesktopServlet(selectedLoan,selectedLoan.paymentPerPulse());
                                    //bank.operateThePaymentOfTheLoanDesktop(selectedLoan, selectedLoan.paymentPerPulse());
                                }
                            } catch (Exception ex ) {
                                popupMessage("Error",ex.getMessage());
                            }
                            loansListControllerHandler(loansListController);
                            payButton.setDisable(true);
                            loansListController.loansAccordionInformation.setVisible(false);
                        });
                    }
                    if (selectedLoan.getLoanStatus() == DTOLoanStatus.ACTIVE) {
                       closeLoanButton.setDisable(false);
                        closeLoanButton.setOnAction(e2 -> {
                            operateThePaymentOfTheLoanDesktopServlet(selectedLoan,selectedLoan.getTotalCapitalPayTillEnd());
                            //bank.operateThePaymentOfTheLoanDesktop(selectedLoan, selectedLoan.getTotalCapitalPayTillEnd());
                            loansListControllerHandler(loansListController);
                            closeLoanButton.setDisable(true);
                            loansListController.loansAccordionInformation.setVisible(false);

                        });
                    }
                }
        });
    }

    private void operateThePaymentOfTheLoanDesktopServlet(DTOLoan selectedLoan,int amount){
        String finalUrl = HttpUrl
                .parse(PAYMENT_PAGE_CUSTOMER)
                .newBuilder()
                .build()
                .toString();

        MultipartBody.Builder MpB = new MultipartBody.Builder().setType(MultipartBody.FORM);
        MpB.addFormDataPart("loan",selectedLoan.getId());
        MpB.addFormDataPart("amount", String.valueOf(amount));
        RequestBody body=MpB.build();

        HttpClientUtil.runAsyncPost(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        popupMessage("Error","Something went wrong: " + e.getMessage())//ToDo
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            popupMessage("Error","Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        String rawBody = null;
                        try {
                            rawBody = response.body().string();
                            popupMessage("Success",rawBody);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    });
                }
            }
        },body);
    }
}
