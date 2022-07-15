package absController;

import com.google.gson.Gson;
import absController.login.LoginController;
import dataObjects.dtoBank.dtoAccount.DTOCustomersList;
import dataObjects.dtoBank.dtoAccount.DTOLoan;
import dataObjects.dtoBank.dtoAccount.DTOLoanStatus;
import dataObjects.dtoBank.dtoAccount.DTOLoansList;
import dataObjects.dtoCustomer.DTOCustomer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static util.Constants.GSON_INSTANCE;

public class ABSController extends HelperFunction implements Initializable {

    private AdminController adminController;
    private LoansListController loansListController;
    private LoginController loginController;
    private CustomersListController customersListController;


    @FXML
    private BorderPane loginBorderPane;

    @FXML
    private MenuButton skinsMenuButton;

    @FXML
    private MenuItem defaultSkinMenuButton;

    @FXML
    protected MenuItem skinMenuButton;

    @FXML
    public MenuButton viewBy;

    @FXML
    protected MenuItem Admin;

    @FXML
    protected Label filePath;

    @FXML
    protected Label currentYaz;

    @FXML
    protected BorderPane myBorderPane;

    @FXML
    protected Button loadFileButton;


    @FXML
    void contextMenuRequested(ContextMenuEvent event) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginController =myFXMLLoader("login/login.fxml");
        loginBorderPane.setCenter(loginController.loginGridPane);
        loansListController=myFXMLLoader("LoansListViewer.fxml");
        adminController = myFXMLLoader("MyAdminView.fxml");
        customersListController=myFXMLLoader("CustomerListViewer.fxml");

        adminController.increaseYazButton.setOnAction(e-> {
            YazLogicDesktopIncreaseServlet();
        });

        loginController.setAbsController(this);
        onLoadFileClick();
        onLoanClickProperty(loansListController,null);
        onCustomerClickProperty();
    }

    private void YazLogicDesktopIncreaseServlet(){
        String finalUrl = HttpUrl
                .parse(Constants.YAZ_INCREASE_PAGE_ADMIN)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        myAlert("Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.code() != 200) {
                    Platform.runLater(() ->
                            myAlert(responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        loansListControllerHandler(loansListController);
                        currentYaz.setText("Current Yaz : "+ responseBody);
                    });
                }
            }
        });
    }

    private void onLoadFileClick(){
        loadFileButton.setOnAction(e -> {
            String file = fileChooserImplementation(e);
            String finalUrl = HttpUrl
                    .parse(Constants.UPLOAD_FILE_PAGE)
                    .newBuilder()
                    .build()
                    .toString();
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("file1",file,
                            RequestBody.create(MediaType.parse("application/octet-stream"),
                                    new File(file)))
                    .build();
            HttpClientUtil.runAsyncPost(finalUrl, new Callback() {

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Platform.runLater(() ->
                            filePath.setText("Something went wrong: " + e.getMessage())
                    );
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.code() != 200) {
                        String responseBody = response.body().string();
                        Platform.runLater(() ->
                            myAlert(responseBody)
                        );
                    } else {
                        Platform.runLater(() -> {
                            filePath.setText("File Path : " + file);
                        });
                    }
                    response.close();
                }
            },body);
        });
    }

    private void myAlert(String body){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Load File Error");
        alert.setHeaderText("Could not open your file");
        alert.setContentText(body);

        alert.showAndWait();
    }

    public void afterLoginClickAdmin(String name) {
        myBorderPane.setCenter(adminController.adminGridPane);
        adminController.LoansBoardPane.setCenter(loansListController.LoansMainGridPane);
        adminController.CustomerBoardPane.setCenter(customersListController.CustomersMainGridPane);
        viewBy.setText(name);
        loginBorderPane.setVisible(false);
        viewBy.setVisible(true);
        currentYaz.setVisible(true);
        showLoanInformationInAdminAndCustomerViewServlet(Constants.LOANS_PAGE_ADMIN,loansListController.LoansListView,null);
        showCustomerInformationAdminViewServlet();

    }

    private String fileChooserImplementation(javafx.event.ActionEvent e){
        Node node = (Node) e.getSource();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML","*.xml"));
        fileChooser.setTitle("Load File");
        return fileChooser.showOpenDialog(node.getScene().getWindow()).getPath();
    }

    private void onLoanClickProperty(LoansListController loansListController,CustomerController customerController) {
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
            if (selectedLoan.getLoanStatus() !=DTOLoanStatus.NEW) {
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

    private void showLoanInformationInAdminAndCustomerViewServlet(String url,ListView<DTOLoan> loanListView,String customerName){
        String finalUrl = HttpUrl
                .parse(url)
                .newBuilder()
                .addQueryParameter("username", customerName)
                .build()
                .toString();

        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        filePath.setText("Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            filePath.setText("Something went wrong: " + responseBody)
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
                        showLoanInformationInAdminAndCustomerView(loanListView,dtoLoansList.getDTOLoans(),false);
                    });
                }
            }
        });
    }

    private void showCustomerInformationAdminViewServlet(){
        String finalUrl = HttpUrl
                .parse(Constants.CUSTOMERS_PAGE_ADMIN)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        filePath.setText("Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            filePath.setText("Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        String rawBody = null;
                        try {
                            rawBody = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        DTOCustomersList dtoLoansList=GSON_INSTANCE.fromJson(rawBody, DTOCustomersList.class);
                        showCustomerInformationAdminView(customersListController.customersListView,dtoLoansList.getDTOCustomers());
                    });
                }
            }
        });
    }

    private void onCustomerClickProperty(){
        customersListController.customersListView.getSelectionModel().selectedItemProperty().addListener(e -> {
            if(! customersListController.customersListView.getItems().isEmpty()) {
                customersListController.customersAccordionInformation.setVisible(true);
                DTOCustomer localCustomer = customersListController.customersListView.getSelectionModel().getSelectedItem();
                customersListController.movementsTableView.setItems(FXCollections.observableArrayList(localCustomer.getMovements()));
                customersListController.loansListLoanerView.getItems().clear();
                showLoanInformationInAdminAndCustomerViewServlet(Constants.AS_LOANER_PAGE_CUSTOMER,customersListController.loansListLoanerView,localCustomer.getName());
                customersListController.loansListLenderView.getItems().clear();
                showLoanInformationInAdminAndCustomerViewServlet(Constants.AS_BORROWER_PAGE_CUSTOMER,customersListController.loansListLenderView,localCustomer.getName());
            }
        });

    }

        /*showLoanInformationInAdminAndCustomerView(loansListController.LoansListView, bank.getLoansList(),false);
        showLoanInformationInAdminAndCustomerView(customerController.loanerLoansListView, bank.getCustomerLoanersList(dtoCustomer.getCustomerName()),false);
        showLoanInformationInAdminAndCustomerView(customerController.LenderLoansTableListView, bank.getCustomerBorrowersList(dtoCustomer.getCustomerName()),false);
        showLoanInformationInAdminAndCustomerView(customerController.loansListController.LoansListView, bank.getCustomerLoanersList(dtoCustomer.getCustomerName()), true);
        customerController.customerMovments.setItems(FXCollections.observableArrayList(dtoCustomer.getMovements()));
        showCustomerInformationAdminView(customersListController.customersListView, bank.getCustomers());*/

        /*customerController.errorTextArea.setVisible(false);
        customerController.allInlayListView.getItems().clear();
        customerController.chosenInlayListView.getItems().clear();
        customerController.allInlayListView.setVisible(false);
        customerController.chosenInlayListView.setVisible(false);
        customerController.chooseLoanButton.setVisible(false);
        customerController.unChosenLoanButton.setVisible(false);
        customerController.doneChosenLoanButton.setVisible(false);*/
        //loansListControllerHandler(customerController.loansListController);
            /*if(bank!=null) {
                customersListController.customersAccordionInformation.setVisible(false);
                showLoanInformationInAdminAndCustomerView(loansListController.LoansListView,bank.getLoansList(),false);
                showCustomerInformationAdminView( customersListController.customersListView,bank.getCustomers());
                loansListControllerHandler(loansListController);
            }
        });*/



   /* @Override
    public void initialize(URL location, ResourceBundle resources) {
        adminController =myFXMLLoader("MyAdminView.fxml");
        loansListController=myFXMLLoader("LoansListViewer.fxml");
        customersListController=myFXMLLoader("CustomerListViewer.fxml");
        customersController=new ArrayList<>();
        adminController.loadFileButton.setOnAction(e-> {
            try {
                String file=fileChooserImplementation(e);
                bank=XmlSerialization.buildBank(file.trim());
                filePath.setText("File Path : "+file);
                YazLogicDesktop.currentYazUnitProperty.setValue(1);
                showLoanInformationInAdminAndCustomerView(loansListController.LoansListView,bank.getLoansList(),false);
                showCustomerInformationAdminView( customersListController.customersListView,bank.getCustomers());
                adminController.LoansBoardPane.setCenter(loansListController.LoansMainGridPane);
                adminController.CustomerBoardPane.setCenter(customersListController.CustomersMainGridPane);

                setTheAdminAndCustomersAsMenuItems();
            }
            catch (NullPointerException ex){
                popupMessage("Error","Please be sure to choose a file...");
            }
            catch (Exception ex) {
                popupMessage("Error",ex.getMessage());
            }
        });
        onAdminMenuItemClick();
        //C:\Users\Eliran\IdeaProjects\ABS\model\src\resources
        onLoanClickProperty(loansListController,null);
        onCustomerClickProperty();
        adminController.increaseYazButton.setOnAction(e-> {
            YazLogicDesktop.currentYazUnitProperty.setValue(YazLogicDesktop.currentYazUnitProperty.getValue() + 1);
            loansListControllerHandler(loansListController);
        });
        YazLogicDesktop.currentYazUnitProperty.addListener(((observable, oldValue, newValue) -> {
            if(bank!=null)
               currentYaz.setText("Current Yaz : "+newValue);
        }));

        YazLogicDesktop.currentYazUnitProperty.addListener(((observable, oldValue, newValue) -> {
            try {
                List<DTOLoan> loansThatShouldPay=bank.yazProgressLogicDesktop();
                clearAllLoansPayListView();
                addTheLoansThatShouldPayToAllTheLoansPayListView(loansThatShouldPay);
                showLoanInformationInAdminAndCustomerView(loansListController.LoansListView,bank.getLoansList(),false);
                showCustomerInformationAdminView( customersListController.customersListView,bank.getCustomers());
            } catch (Exception e) {
                popupMessage("Error","Please be sure to choose a file...");
            }
        }));




        //ToDo: Function


    }

   public MenuItem getSkinMenuButton(){
        return skinMenuButton;
   }

   public MenuItem getDefaultSkinMenuButton(){
        return defaultSkinMenuButton;
   }

   public MenuButton getSkinsMenuButton(){
        return skinsMenuButton;
   }

    private void setTheAdminAndCustomersAsMenuItems() {

        if( viewBy.getItems().size()>1)
             viewBy.getItems().remove(1,viewBy.getItems().size());
        for (DTOCustomer dtoCustomer : bank.getCustomers()) {
            MenuItem customerAsMenuItem = new MenuItem(dtoCustomer.getCustomerName());
            viewBy.getItems().add(customerAsMenuItem);
            CustomerController customerController = myFXMLLoader("MyCustomerView.fxml");
            customersController.add(customerController);
            onLoanClickProperty(customerController.loansListController,customerController);
            customerController.setCurrentCustomer(dtoCustomer);
            customerController.setBankInCustomerController(bank);
            customerController.setAbsControllerRef(this);
            List<DTOLoan> dtoLoan = bank.getLoansList().stream().filter(l -> Objects.equals(l.getOwner(), dtoCustomer.getCustomerName())).collect(Collectors.toList());
            if (!dtoLoan.isEmpty())
                bank.myAddListenerToStringPropertyLoans(customerController.notificationAreaListView, dtoLoan.get(0));
            customerAsMenuItem.setOnAction(e -> {
                    //viewBy.setText(dtoCustomer.getCustomerName());
                    myBorderPane.setCenter(customerController.customerTablePane);
                    final ObservableList<String> categories = FXCollections.observableArrayList();
                    categories.addAll(bank.getCategoriesGroup());
                    customerController.categoriesList.getItems().clear();
                    customerController.categoriesList.getItems().addAll(categories);
                  
                    showLoanInformationInAdminAndCustomerView(loansListController.LoansListView, bank.getLoansList(),false);
                    showLoanInformationInAdminAndCustomerView(customerController.loanerLoansListView, bank.getCustomerLoanersList(dtoCustomer.getCustomerName()),false);
                    showLoanInformationInAdminAndCustomerView(customerController.LenderLoansTableListView, bank.getCustomerBorrowersList(dtoCustomer.getCustomerName()),false);
                    showLoanInformationInAdminAndCustomerView(customerController.loansListController.LoansListView, bank.getCustomerLoanersList(dtoCustomer.getCustomerName()), true);
                    customerController.customerMovments.setItems(FXCollections.observableArrayList(dtoCustomer.getMovements()));
                    showCustomerInformationAdminView(customersListController.customersListView, bank.getCustomers());
                  
                    customerController.errorTextArea.setVisible(false);
                    customerController.allInlayListView.getItems().clear();
                    customerController.chosenInlayListView.getItems().clear();
                    customerController.allInlayListView.setVisible(false);
                    customerController.chosenInlayListView.setVisible(false);
                    customerController.chooseLoanButton.setVisible(false);
                    customerController.unChosenLoanButton.setVisible(false);
                    customerController.doneChosenLoanButton.setVisible(false);
                    loansListControllerHandler(customerController.loansListController);
            });
        }

    }

    private String fileChooserImplementation(javafx.event.ActionEvent e){
        Node node = (Node) e.getSource();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML","*.xml"));
        fileChooser.setTitle("Load File");
        return fileChooser.showOpenDialog(node.getScene().getWindow()).getPath();
    }

    protected void  addTheLoansThatShouldPayToAllTheLoansPayListView(List<DTOLoan> loansThatShouldPay){
        for (CustomerController customerController: customersController) {
            for (DTOLoan dtoLoan: loansThatShouldPay) {
                if(Objects.equals(customerController.dtoCustomer.getCustomerName(), dtoLoan.getOwner())) {
                    customerController.loansListController.LoansListView.getItems().add(dtoLoan);
                }
            }
        }
    }

    protected void clearAllLoansPayListView(){
        for (CustomerController customerController: customersController) {
            customerController.loansListController.LoansListView.getItems().clear();
        }
    }

    private void onLoanClickProperty(LoansListController loansListController,CustomerController customerController){
        loansListController.LoansListView.getSelectionModel().selectedItemProperty().addListener(e -> {
            if(!loansListController.LoansListView.getItems().isEmpty()) {
               DTOLoan selectedLoan= loansListControllerHandler(loansListController);
                if(customerController!=null) {
                    if (selectedLoan.getLoanStatus() == DTOLoanStatus.RISK || (!selectedLoan.getIsPaid() && selectedLoan.numberOfYazTillNextPulseWithoutTheIncOfWindowOfPaymentCounterDK() == 0)) {
                        customerController.payButton.setDisable(false);
                        customerController.payButton.setOnAction(e1 -> {
                         try {
                            if (selectedLoan.getLoanStatus() == DTOLoanStatus.RISK) {
                                TextInputDialog paymentDialog = new TextInputDialog();
                                paymentDialog.setTitle("Loan In Risk");
                                paymentDialog.setContentText("Please enter the amount of money you would like to pay:");
                                paymentDialog.setHeaderText("Current Balance: " + bank.getCustomerByName(selectedLoan.getOwner()).getAmount() + "\nDebt Amount: " + (selectedLoan.getDebt()));
                                paymentDialog.showAndWait();

                                if (paymentDialog.getResult() != null && (Integer.parseInt(paymentDialog.getResult()) <= selectedLoan.getDebt())) {
                                        bank.operateThePaymentOfTheLoanDesktop(selectedLoan, Integer.parseInt(paymentDialog.getResult()));
                                } else {
                                    throw new Exception("The payment was not preformed!");
                                }
                            } else if (selectedLoan.numberOfYazTillNextPulseWithoutTheIncOfWindowOfPaymentCounterDK() == 0) {
                                    bank.operateThePaymentOfTheLoanDesktop(selectedLoan, selectedLoan.paymentPerPulse());
                            }
                         } catch (Exception ex ) {
                             popupMessage("Error",ex.getMessage());
                         }
                            showLoanInformationInAdminAndCustomerView(loansListController.LoansListView, bank.getCustomerLoanersList(selectedLoan.getOwner()), true);
                            loansListControllerHandler(loansListController);
                            customerController.payButton.setDisable(true);
                            loansListController.loansAccordionInformation.setVisible(false);
                        });
                    }
                    if (selectedLoan.getLoanStatus() == DTOLoanStatus.ACTIVE) {
                        customerController.closeLoanButton.setDisable(false);
                        customerController.closeLoanButton.setOnAction(e2 -> {
                            try {
                                bank.operateThePaymentOfTheLoanDesktop(selectedLoan, selectedLoan.getTotalCapitalPayTillEnd());
                            } catch (InvocationTargetException | InstantiationException | IllegalAccessException ex) {
                                popupMessage("System Error!",ex.getMessage());
                            }
                            showLoanInformationInAdminAndCustomerView(loansListController.LoansListView, bank.getCustomerLoanersList(selectedLoan.getOwner()), true);
                            loansListControllerHandler(loansListController);
                            customerController.closeLoanButton.setDisable(true);
                            loansListController.loansAccordionInformation.setVisible(false);

                        });
                    }
                }
            }
        });
    }

    private void onCustomerClickProperty(){
        customersListController.customersListView.getSelectionModel().selectedItemProperty().addListener(e -> {
            if(! customersListController.customersListView.getItems().isEmpty()) {
                customersListController.customersAccordionInformation.setVisible(true);
                DTOCustomer localCustomer = customersListController.customersListView.getSelectionModel().getSelectedItem();
                customersListController.movementsTableView.setItems(FXCollections.observableArrayList(localCustomer.getMovements()));
                customersListController.loansListLoanerView.getItems().clear();
                for (DTOLoan dtoLoan:bank.getCustomerLoanersList(localCustomer.getCustomerName()))
                    customersListController.loansListLoanerView.getItems().add(dtoLoan);
                customersListController.loansListLenderView.getItems().clear();
                for (DTOLoan dtoLoan:bank.getCustomerBorrowersList(localCustomer.getCustomerName()))
                    customersListController.loansListLenderView.getItems().add(dtoLoan);
            }
        });

    }

    private void onAdminMenuItemClick(){
        Admin.setOnAction(ie->{
            myBorderPane.setCenter(adminController.adminGridPane);
            viewBy.setText("Admin");
            if(bank!=null) {
                customersListController.customersAccordionInformation.setVisible(false);
                showLoanInformationInAdminAndCustomerView(loansListController.LoansListView,bank.getLoansList(),false);
                showCustomerInformationAdminView( customersListController.customersListView,bank.getCustomers());
                loansListControllerHandler(loansListController);
            }
        });
    }

    private DTOLoan loansListControllerHandler(LoansListController loansListController){
        clearComponents(loansListController);
        DTOLoan selectedLoan = loansListController.LoansListView.getSelectionModel().getSelectedItem();
        if(selectedLoan!=null) {
            loansListController.loansAccordionInformation.setVisible(true);
            if (selectedLoan.getLoanStatus() !=DTOLoanStatus.NEW) {
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
*/

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

   public MenuItem getSkinMenuButton(){
       return skinMenuButton;
   }

    public MenuItem getDefaultSkinMenuButton(){
        return defaultSkinMenuButton;
    }

    public MenuButton getSkinsMenuButton(){
        return skinsMenuButton;
    }
}

