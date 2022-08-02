package logic.bank.account;

import dataObjects.dtoBank.dtoAccount.DTOLoan;
import dataObjects.dtoBank.dtoAccount.DTOLoanForSale;

import java.util.ArrayList;

public class LoanForSale extends DTOLoanForSale {


    public LoanForSale(){

    }

    public void setLoanForSale(DTOLoan dtoLoan){
        this.loanForSale=dtoLoan;
    }

    public void setNameOfBorrower(String borrowerName) {
        nameOfBorrower=borrowerName;
    }

    public void setBalanceOfTheFund(int amount) {
        balanceOfTheFund=amount;
    }

    public static LoanForSale build(LoanForSale loan) {
        LoanForSale dtoLoanSale = new LoanForSale();
        dtoLoanSale.nameOfBorrower = loan.nameOfBorrower;
        dtoLoanSale.balanceOfTheFund = loan.balanceOfTheFund;
        dtoLoanSale.loanForSale=DTOLoan.build(loan.loanForSale);
////
        return dtoLoanSale;
    }

}
