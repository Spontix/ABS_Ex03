package dataObjects.dtoBank;

import dataObjects.dtoBank.dtoAccount.DTOAccount;
import dataObjects.dtoBank.dtoAccount.DTOLoan;
import dataObjects.dtoCustomer.DTOCustomer;

import java.util.ArrayList;
import java.util.List;

public class DTOBank {//

    protected List<DTOCustomer> accounts;
    protected List<DTOLoan> loans;
    protected List<String> categories;



    public DTOBank(){
        /*accounts=new ArrayList<>();
        loans=new ArrayList<>();
        categories=new ArrayList<>();*/
    }

    public List<DTOCustomer> getAccounts(){
        return accounts;
    }

    public List<DTOLoan> getLoans() {
        return loans;
    }

    public List<String> getCategories() {
        return categories;
    }

}
