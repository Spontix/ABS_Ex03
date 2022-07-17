package absController;

import dataObjects.dtoBank.dtoAccount.DTOLoan;
import dataObjects.dtoBank.dtoAccount.DTOLoanStatus;
import dataObjects.dtoCustomer.DTOCustomer;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.List;


public class HelperFunction {

    protected void showLoanInformationInAdminAndCustomerView(ListView<DTOLoan> listView, List<DTOLoan> loanList, Boolean isCustomerListViewInPayment) {
        listView.getItems().clear();
        if(isCustomerListViewInPayment){
            for (DTOLoan dtoLoan:loanList) {
                if(dtoLoan.getLoanStatus()!= DTOLoanStatus.NEW && dtoLoan.getLoanStatus()!=DTOLoanStatus.FINISHED && dtoLoan.getLoanStatus()!=DTOLoanStatus.PENDING )
                       listView.getItems().add(dtoLoan);
            }
        }
        else {
            listView.getItems().addAll(loanList);
        }
    }

    protected void showCustomerInformationAdminView(ListView<DTOCustomer> listView,  List<DTOCustomer> list){
        listView.getItems().clear();
        listView.getItems().addAll(list);
    }


    protected  <T> T myFXMLLoader(String resource){
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url =  getClass().getResource(resource);
        fxmlLoader.setLocation(url);
        try {
            fxmlLoader.load(url.openStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return fxmlLoader.getController();
    }

    public void popupMessage (String title, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}

