package dataObjects.dtoBank.dtoAccount;

import java.util.ArrayList;
import java.util.List;

public class DTOPaymentsPerYaz {
    protected int currentYaz;
    protected List<DTOLoan> dtoLoanList=new ArrayList<>();

    public static DTOPaymentsPerYaz build(DTOPaymentsPerYaz paymentsPerYaz){
        DTOPaymentsPerYaz dtoPaymentsPerYaz=new DTOPaymentsPerYaz();
        dtoPaymentsPerYaz.currentYaz=paymentsPerYaz.currentYaz;
        ArrayList<DTOLoan> dtoLoanArrayList=new ArrayList<>();
        for (DTOLoan dtoLoan:dtoPaymentsPerYaz.dtoLoanList) {
            dtoLoanArrayList.add(DTOLoan.build(dtoLoan));
        }
        dtoPaymentsPerYaz.dtoLoanList=dtoLoanArrayList;
        return dtoPaymentsPerYaz;
    }

    public int getCurrentYaz(){
        return currentYaz;
    }

    public List<DTOLoan> getDTOLoansList(){
        return dtoLoanList;
    }
}
