package dataObjects.dtoBank.dtoAccount;

import java.util.ArrayList;

public class DTOLoansList {
    ArrayList<DTOLoan> dtoLoans=new ArrayList<>();

    public void setDTOLoans(ArrayList<DTOLoan> dtoLoans){
        this.dtoLoans=dtoLoans;
    }

    public ArrayList<DTOLoan> getDTOLoans(){
        return dtoLoans;
    }

    public DTOLoansList(){

    }
}
