package absController;

import absController.login.LoginController;
import dataObjects.dtoBank.DTOBank;
import dataObjects.dtoBank.dtoAccount.DTOLoan;
import dataObjects.dtoBank.dtoAccount.DTOLoanStatus;
import dataObjects.dtoBank.dtoAccount.DTOLoansList;
import dataObjects.dtoCustomer.DTOCustomer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
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
import java.net.URL;
import java.util.*;

import static util.Constants.*;

public class ABSController extends HelperFunction implements Initializable {

    private Timer timer;
    private TimerTask listRefresher;

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
    protected ToggleButton rewindToggleButton;

    @FXML
    protected TextArea rewindText;

    int currentYazAsNumber;


    @FXML
    void contextMenuRequested(ContextMenuEvent event) {
    }


    private void updateCustomersList(DTOBank dtoBank){
        Platform.runLater(()->{
            try{
                showCustomerInformationAdminView(customersListController.customersListView,dtoBank.getAccounts());
                showLoanInformationInAdminAndCustomerView(loansListController.LoansListView,dtoBank.getLoans());
                getYazUnitServlet();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        });
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
                        currentYaz.setText("Current Yaz : "+ rawBody);
                        currentYazAsNumber=Integer.parseInt(rawBody);
                    });
                }
            }
        });
    }

    public void setListRefresher(){
        listRefresher=new AdminRefresher(this::updateCustomersList);
        timer= new Timer();
        timer.schedule(listRefresher,REFRESH_RATE,REFRESH_RATE);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginController =myFXMLLoader("login/login.fxml");
        loginBorderPane.setCenter(loginController.loginGridPane);
        loansListController=myFXMLLoader("LoansListViewer.fxml");
        adminController = myFXMLLoader("MyAdminView.fxml");
        customersListController=myFXMLLoader("CustomerListViewer.fxml");

        onRewindToggleButtonClick();
        adminController.increaseYazButton.setOnAction(e-> YazLogicDesktopIncreaseServlet());
        loginController.setAbsController(this);
        onLoadFileClick();
        onLoanClickProperty(loansListController,null);
        onCustomerClickProperty();
    }

    private void onRewindToggleButtonClick(){
        /*rewindToggleButton.setOnKeyPressed(e->onRewindToggleButtonClickServlet(REWIND_ON_PAGE_ADMIN));
        rewindToggleButton.setOnKeyReleased(e->onRewindToggleButtonClickServlet(REWIND_OFF_PAGE_ADMIN));*/
        rewindToggleButton.setOnAction(e->onRewindToggleButtonClickServlet());
    }

    private void onRewindToggleButtonClickServlet() {
        //noinspection ConstantConditions
        String url=null;
        int yazChosen=Integer.parseInt(rewindText.getText());
        if(yazChosen>=currentYazAsNumber){
            popupMessage("Be aware!","You can insert only yaz below the current yaz od the app");
        }

        if(rewindToggleButton.isSelected()){
            url=REWIND_ON_PAGE_ADMIN;
        }
        else
            url=REWIND_OFF_PAGE_ADMIN;

        String finalUrl = HttpUrl
                .parse(url)
                .newBuilder()
                .addQueryParameter("yaz", rewindText.getText())
                .build()
                .toString();

        String finalUrl1 = url;
        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        popupMessage("Error","Something went wrong: " + e.getMessage())
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
                        loansListController.loansAccordionInformation.setVisible(true);
                        customersListController.customersAccordionInformation.setVisible(true);
                        loansListController.LoansListView.getItems().clear();
                        customersListController.customersListView.getItems().clear();
                        adminController.increaseYazButton.setDisable(finalUrl1.equals(REWIND_ON_PAGE_ADMIN));
                    });
                }
            }
        });

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
                        popupMessage("Response failed" ,"Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.code() != 200) {
                    Platform.runLater(() ->
                            popupMessage("Increase yaz Error",responseBody)
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
                            popupMessage("Load File Error",responseBody)
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


    public void afterLoginClickAdmin(String name) {
        myBorderPane.setCenter(adminController.adminGridPane);
        adminController.LoansBoardPane.setCenter(loansListController.LoansMainGridPane);
        adminController.CustomerBoardPane.setCenter(customersListController.CustomersMainGridPane);
        viewBy.setText(name);
        loginBorderPane.setVisible(false);
        viewBy.setVisible(true);
        currentYaz.setVisible(true);
        rewindToggleButton.setVisible(true);
        rewindText.setVisible(true);
        setListRefresher();
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
                        loanListView.getItems().clear();
                        loanListView.getItems().addAll(dtoLoansList.getDTOLoans());
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
                showLoanInformationInAdminAndCustomerViewServlet(Constants.AS_LOANER_PAGE_CUSTOMER,customersListController.loansListLoanerView,localCustomer.getName());
                showLoanInformationInAdminAndCustomerViewServlet(Constants.AS_BORROWER_PAGE_CUSTOMER,customersListController.loansListLenderView,localCustomer.getName());
            }
        });

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

