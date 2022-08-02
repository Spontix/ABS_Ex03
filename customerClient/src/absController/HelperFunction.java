package absController;

import dataObjects.dtoBank.dtoAccount.DTOLoan;
import dataObjects.dtoBank.dtoAccount.DTOLoanForSale;
import dataObjects.dtoBank.dtoAccount.DTOLoanStatus;
import dataObjects.dtoCustomer.DTOCustomer;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.List;


public class HelperFunction {

    static protected void showLoanInformationInAdminAndCustomerView(ListView<DTOLoan> listView, List<DTOLoan> loanList,boolean rewind) {
        if(!rewind) {
            if(loanList.size()!=0) {
                int index;
                int customersListViewSize = listView.getItems().size();
                //if(loanList.size()!=0) {
                for (index = 0; index < customersListViewSize && loanList.size() >= customersListViewSize; index++) {
                    DTOLoan dtoLoanToDelete = listView.getItems().get(index);
                    listView.getItems().add(index, loanList.get(index));
                    listView.getItems().remove(dtoLoanToDelete);
                }
                while (index < loanList.size()) {
                    listView.getItems().add(loanList.get(index));
                    index++;
                }
                while (index > loanList.size()) {
                    listView.getItems().remove(index);
                    index++;
                }
            }
            else
                listView.getItems().clear();
        }
        else{
            listView.getItems().clear();
            listView.getItems().addAll(loanList);
        }


    }

    static protected void showLoansForSaleInformationInCustomerView(ListView<DTOLoanForSale> listView, List<DTOLoanForSale> loanList, boolean rewind) {
        if(!rewind) {
            int index;
            int customersListViewSize = listView.getItems().size();
            //if(loanList.size()!=0) {
            if(loanList.size()!=0) {
                for (index = 0; index < customersListViewSize && loanList.size()>=customersListViewSize; index++) {
                    DTOLoanForSale dtoLoanForSaleToDelete = listView.getItems().get(index);
                    listView.getItems().add(index, loanList.get(index));
                    listView.getItems().remove(dtoLoanForSaleToDelete);
                }
                while (index < loanList.size()) {
                    listView.getItems().add(loanList.get(index));
                    index++;
                }
                while (index > loanList.size()) {
                    listView.getItems().remove(index);
                    index++;
                }
            }
            else
                listView.getItems().clear();
        }
        else{
            listView.getItems().clear();
            listView.getItems().addAll(loanList);
        }


    }
        // }



   /* static protected void showCustomerInformationAdminView(ListView<DTOCustomer> listView,  List<DTOCustomer> list){
        int index;
        int customersListViewSize=listView.getItems().size();
        //if(list.size()!=0) {
        for (index = 0; index < customersListViewSize; index++) {
            DTOCustomer dtoCustomerToDelete = listView.getItems().get(index);
            listView.getItems().add(index, list.get(index));
            listView.getItems().remove(dtoCustomerToDelete);
        }
        while (index < list.size()) {
            listView.getItems().add(list.get(index));
            index++;
        }
        //}
    }*/


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

    static public void popupMessage (String title, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}



/*package absController;

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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}*/

