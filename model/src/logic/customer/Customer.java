package logic.customer;

import dataObjects.dtoBank.dtoAccount.DTOInlay;
import dataObjects.dtoBank.dtoAccount.DTOLoan;
import dataObjects.dtoBank.dtoAccount.DTOMovement;
import dataObjects.dtoCustomer.DTOCustomer;
import logic.bank.account.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//Eliran
public class Customer extends DTOCustomer implements Account {

    @Override
    public void setAmount(int startAmount) {
        amount = startAmount;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setMovements(List<DTOMovement> movements) {
        this.movements = movements;
    }

    @Override
    public void setInlays(List<DTOInlay> inlays) {
        this.inlays = inlays;
    }

    @Override
    public void setLoaner(List<DTOLoan> loaner) {
        this.loaner = loaner;
    }

    @Override
    public void setBorrower(List<DTOLoan> borrower) {
        this.borrower = borrower;
    }

    private Customer() {

    }

    @Override
    public void cashDeposit(int sum) {
        amount += sum;
    }

    @Override
    public void cashWithdrawal(int sum) {
        if (amount >= sum) {
            amount -= sum;
        }
    }


}
