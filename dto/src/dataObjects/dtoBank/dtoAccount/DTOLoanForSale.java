package dataObjects.dtoBank.dtoAccount;

public class DTOLoanForSale {
    protected String nameOfBorrower;
    protected int balanceOfTheFund;

    protected DTOLoan loanForSale;


    public static DTOLoanForSale build(DTOLoanForSale loan) {
        DTOLoanForSale dtoLoanSale = new DTOLoanForSale();
        dtoLoanSale.nameOfBorrower = loan.nameOfBorrower;
        dtoLoanSale.balanceOfTheFund = loan.balanceOfTheFund;
        dtoLoanSale.loanForSale=DTOLoan.build(loan.loanForSale);
////
        return dtoLoanSale;
    }

    @Override
    public String toString() {

        return (
                "Lender name: " + nameOfBorrower + "\n" +
                        "Interest that left of the lender: "+ balanceOfTheFund + "\n" +
                        "----------------------------------------\n"+ loanForSale.toString());
    }

    public String getNameOfBorrower() {
        return nameOfBorrower;
    }

    public int getBalanceOfTheFund() {
        return balanceOfTheFund;
    }

    public DTOLoan getLoanForSale() {return loanForSale;}

    public void setBalanceOfTheFund(int sum){
        balanceOfTheFund=sum;
    }
}
