package dataObjects.dtoCustomer;


import dataObjects.dtoBank.dtoAccount.*;

import java.util.ArrayList;
import java.util.List;

public class DTOCustomer {

    protected String name;
    protected int amount;
    protected List<DTOMovement> movements;
    protected List<DTOInlay> inlays;
    protected List<DTOLoan> loaner;
    protected List<DTOLoan> borrower;
    protected boolean rewind;

    public DTOCustomer(){
        inlays = new ArrayList<>();
        movements=new ArrayList<>();
        loaner=new ArrayList<>();
        borrower=new ArrayList<>();
    }

    public static DTOCustomer builderServer(String name) {
        DTOCustomer dtoCustomer=new DTOCustomer();
        dtoCustomer.name=name;
        return dtoCustomer;
    }

    public boolean getRewind(){
        return rewind;
    }

    public int getAmount() {
        return amount;
    }

    public List<DTOMovement> getMovements(){
        return movements;
    }

    public List<DTOLoan> getLoaner() {
        return loaner;
    }

    public List<DTOLoan> getBorrower() {
        return borrower;
    }

    public List<DTOInlay> getInlays(){return inlays;}

    public String getName(){
        return name;
    }

    public int getAllOpenLoansToBorrower(){
        return (int) borrower.stream().filter(l -> l.getLoanStatus() != DTOLoanStatus.FINISHED).count();
    }

    @Override
    public String toString()
    {
       // return "Name : "+name+"\n"+"Current amount : "+amount+"\n"+"-----------Movements-----------"+"\n"+movements.toString();
        return "Name : "+name+"\n"+"Current amount : "+amount;
    }
    
    public static DTOCustomer build(DTOCustomer customer){
        DTOCustomer dtoCustomer=new DTOCustomer();
        dtoCustomer.name=customer.name;
        dtoCustomer.amount=customer.amount;
        ArrayList<DTOMovement> dtoMovements=new ArrayList<>();
        ArrayList<DTOLoan> Loaner = new ArrayList<>();
        ArrayList<DTOLoan> Borrower = new ArrayList<>();
        for (DTOLoan loan : customer.borrower) {
            Borrower.add(DTOLoan.build(loan));
        }

        for (DTOLoan loan : customer.loaner) {
            Loaner.add(DTOLoan.build(loan));
        }

        for (DTOMovement movement : customer.movements) {
            dtoMovements.add(DTOMovement.build(movement));
        }

        dtoCustomer.movements=dtoMovements;
        dtoCustomer.loaner=customer.loaner;
        dtoCustomer.borrower=customer.borrower;
        dtoCustomer.inlays=customer.inlays;
        return dtoCustomer;
    }

    public void addAmount(int amount){
        this.amount+=amount;
    }

    public void setRewind(boolean rewind){
        this.rewind=rewind;
    }
}
