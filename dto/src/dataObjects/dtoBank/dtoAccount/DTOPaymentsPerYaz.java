package dataObjects.dtoBank.dtoAccount;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DTOPaymentsPerYaz {
    protected int currentYaz;
    protected ArrayList<DTOLoan> dtoLoanList=new ArrayList<>();

    public static DTOPaymentsPerYaz build(DTOPaymentsPerYaz paymentsPerYaz){
        DTOPaymentsPerYaz dtoPaymentsPerYaz=new DTOPaymentsPerYaz();
        dtoPaymentsPerYaz.currentYaz=paymentsPerYaz.currentYaz;
        ArrayList<DTOLoan> dtoLoanArrayList=new ArrayList<>();
        for (DTOLoan dtoLoan:paymentsPerYaz.dtoLoanList) {
            dtoLoanArrayList.add(DTOLoan.build(dtoLoan));
        }
        dtoPaymentsPerYaz.dtoLoanList=dtoLoanArrayList;
        return dtoPaymentsPerYaz;
    }

    public int getCurrentYaz(){
        return currentYaz;
    }

    public ArrayList<DTOLoan> getDTOLoansList(){
        return dtoLoanList;
    }

    public ArrayList<DTOLoan> getDtoLoanListByCustomer(String customerName){
        return (ArrayList<DTOLoan>) dtoLoanList.stream().filter(l-> Objects.equals(l.getOwner(), customerName)).collect(Collectors.toList());
    }
}
