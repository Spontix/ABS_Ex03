package logic.bank.account;

import dataObjects.dtoBank.dtoAccount.DTOLoan;
import dataObjects.dtoBank.dtoAccount.DTOPaymentsPerYaz;

import java.util.List;

public class PaymentsPerYaz extends DTOPaymentsPerYaz {

    private PaymentsPerYaz(){};

    public static PaymentsPerYaz build(){
        return new PaymentsPerYaz();
    }

    public PaymentsPerYaz setCurrentYaz(int currentYaz){
        this.currentYaz=currentYaz;
        return this;
    }

    public PaymentsPerYaz setLoanList(List<Loan> loans){
        this.dtoLoanList.addAll(loans);
        return this;
    }
}
