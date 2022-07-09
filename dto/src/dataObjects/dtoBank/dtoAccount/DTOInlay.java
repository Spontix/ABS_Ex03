package dataObjects.dtoBank.dtoAccount;

import dataObjects.dtoCustomer.DTOCustomer;

import java.lang.reflect.InvocationTargetException;

public class DTOInlay {
    protected String name;
    protected int investAmount;
    protected String category;
    protected double minInterestYaz;
    protected int minYazTime;
    protected int maximumLoansOpenToTheBorrower;

    public DTOInlay(){

    }

     //////////copy of inlayBuild. I had a new member maximumLoansOpenToTheBorrower to DTOInlay and creat Get and Set methods//////////
     public DTOInlay(DTOAccount customer, int investAmount, String category, double minInterestYaz, int minYazTime, int maximumLoansOpenToTheBorrower) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        checksInvestAmount(customer, investAmount);
        this.investAmount=investAmount;
        this.category=category;
        this.minInterestYaz=minInterestYaz;
        this.maximumLoansOpenToTheBorrower=maximumLoansOpenToTheBorrower;
        this.minYazTime=minYazTime;
        this.name=name;
    }

    public void checksInvestAmount(DTOAccount customer, int investAmount) {
        if (investAmount > customer.getAmount() || investAmount < 0) {
            throw new RuntimeException("The investment amount is above the amount balance or a negative one. please try again!");
        }
    }

    public int getMaximumLoansOpenToTheBorrower(){return maximumLoansOpenToTheBorrower;}

    public String getCategory() {
        return category;
    }

    public int getInvestAmount() {
        return investAmount;
    }

    public double getMinInterestYaz() {
        return minInterestYaz;
    }

    public int getMinYazTime() {
        return minYazTime;
    }

    public String getName() {
        return name;
    }

    public static DTOInlay build(DTOInlay inlay){
        DTOInlay dtoInlay=new DTOInlay();
        dtoInlay.investAmount=inlay.investAmount;
        dtoInlay.maximumLoansOpenToTheBorrower = inlay.maximumLoansOpenToTheBorrower;
        dtoInlay.category=inlay.category;
        dtoInlay.minInterestYaz=inlay.minInterestYaz;
        dtoInlay.minYazTime=inlay.minYazTime;
        dtoInlay.name=inlay.getName();
        return dtoInlay;
    }

    public void setCustomer(DTOCustomer dtoCustomer){
        name=dtoCustomer.getName();
    }


}
