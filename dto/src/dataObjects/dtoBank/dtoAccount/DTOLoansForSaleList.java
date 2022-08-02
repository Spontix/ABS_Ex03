package dataObjects.dtoBank.dtoAccount;

import java.util.ArrayList;
import java.util.List;

public class DTOLoansForSaleList {
    ArrayList<DTOLoanForSale> loanForSales=new ArrayList<>();


    public void setDTOLoansForSale(ArrayList<DTOLoanForSale> dtoLoansForSale){
        this.loanForSales=dtoLoansForSale;
    }

    public ArrayList<DTOLoanForSale> getDTOLoansForSale(){
        return loanForSales;
    }

    public DTOLoansForSaleList(){

    }
}
