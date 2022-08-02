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

    public static Customer build(Customer customer){
        Customer dtoCustomer=new Customer();
        dtoCustomer.name=customer.name;
        dtoCustomer.amount=customer.amount;
        ArrayList<DTOMovement> dtoMovements=new ArrayList<>();
        ArrayList<DTOLoan> Loaner = new ArrayList<>();
        ArrayList<DTOLoan> Borrower = new ArrayList<>();
        ArrayList<DTOInlay> inlays=new ArrayList<>();
        for (DTOLoan loan : customer.borrower) {
            Borrower.add(DTOLoan.build(loan));
        }

        for (DTOInlay inlay : customer.inlays) {
            inlays.add(DTOInlay.build(inlay));
        }

        for (DTOLoan loan : customer.loaner) {
            Loaner.add(DTOLoan.build(loan));
        }

        for (DTOMovement movement : customer.movements) {
            dtoMovements.add(DTOMovement.build(movement));
        }

        dtoCustomer.movements=dtoMovements;
        dtoCustomer.loaner=Loaner;
        dtoCustomer.borrower=Borrower;
        dtoCustomer.inlays=inlays;
        return dtoCustomer;
    }

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
