package logic.bank.account;

import dataObjects.dtoBank.dtoAccount.DTOAccount;
import dataObjects.dtoBank.dtoAccount.DTOInlay;
import dataObjects.dtoCustomer.DTOCustomer;

public class Inlay extends DTOInlay {

    private Inlay(){
    }

    public void setInvestAmount(int inlayInvestAmount){
        investAmount=inlayInvestAmount;
    }

    public void setMaximumLoansOpenToTheBorrower(int inlayMaximumLoansOpenToTheBorrower){maximumLoansOpenToTheBorrower = inlayMaximumLoansOpenToTheBorrower;}

    public void setCategory(String inlayCategory){
        category = inlayCategory;
    }

    public void setMinInterestYaz(double inlayMinInterestYaz){
        minInterestYaz = inlayMinInterestYaz;
    }

    public void setMinYazTime(int inlayMinYazTime){
        minYazTime = inlayMinYazTime;
    }

    public void setCustomerName(String name){
        this.name=name;
    }

    public void setInlayCustomer(DTOAccount customer){
        name=customer.getName();
    }


}
