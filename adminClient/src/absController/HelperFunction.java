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

    static protected void showLoanInformationInAdminAndCustomerView(ListView<DTOLoan> listView, List<DTOLoan> loanList) {
        int index;
        int customersListViewSize=listView.getItems().size();
        //if(loanList.size()!=0) {
            for (index = 0; index < customersListViewSize; index++) {
                DTOLoan dtoLoanToDelete = listView.getItems().get(index);
                listView.getItems().add(index, loanList.get(index));
                listView.getItems().remove(dtoLoanToDelete);
            }
            while (index < loanList.size()) {
                listView.getItems().add(loanList.get(index));
                index++;
            }
       // }
    }

   static protected void showCustomerInformationAdminView(ListView<DTOCustomer> listView,  List<DTOCustomer> list){
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

    static public void popupMessage (String title, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}

