package dataObjects.dtoBank.dtoAccount;

import dataObjects.dtoCustomer.DTOCustomer;

public enum DTOLoanStatus {
    PENDING{
        @Override
        public String operationTwo(DTOLoan dtoLoan) {
            return getLoansListAsString(dtoLoan) + PENDING.operationThree(dtoLoan);
        }

        public String operationThree(DTOLoan dtoLoan){
            int sum =0;
            for (DTOInlay dtoInlay:dtoLoan.listOfInlays) {
                sum += dtoInlay.investAmount;
            }
            return "The missing amount until the loan will become active is - " +  (dtoLoan.capital-sum)+"\n";
        }
    },
    ACTIVE{
        @Override
        public String operationTwo(DTOLoan dtoLoan) {
            return  "------------ Pending Information ------------\n" + getLoansListAsString(dtoLoan) + "------------ Active Information ------------\n" +
                    "The Yaz when the status loan become ACTIVE is: "+dtoLoan.startedYazInActive +
                    "\n"+ ACTIVE.operationThree(dtoLoan) + "\n" +
                    "The amount of the pulse payment is : "+dtoLoan.getCapital() / dtoLoan.pulseNumber() + "\n" +
                    "The interest per pulse is : "+(int)(dtoLoan.getCapital()) / dtoLoan.pulseNumber() * ( (double)(dtoLoan.getInterestPerPayment() / 100.0))+"\n" +
                    "The total pulse withe interest payment : " + dtoLoan.paymentPerPulse() + "\n" +
                    "The payments were preformed in Yaz's : " + dtoLoan.getListOfYazPayments() + "\n" +
                    "The total capital that was payed till now are : " + dtoLoan.getTotalCapitalPayTillNowConsole() + "\n" +
                    "The total capital that left to pay is : " + dtoLoan.getTotalCapitalPayTillEndConsole() + "\n" +
                    "The total interest that was payed till now are : " + dtoLoan.getTotalInterestPayTillNowConsole() + "\n" +
                    "The total interest that left to pay is : " + dtoLoan.getTotalInterestPayTillEndConsole() + "\n"
                    ;
        }

        @Override
        public String operationThree(DTOLoan dtoLoan) {
            return "The next Yaz to be paid is: "+dtoLoan.getNextYazToBePaid()+"\n"+"The expected payment is: "+dtoLoan.paymentPerPulse();
        }
    }
    ,RISK{
        @Override
        public String operationTwo(DTOLoan dtoLoan) {
            return ACTIVE.operationTwo(dtoLoan) + "------------ Risk Information ------------\n" +
                    "The Yazs that did pay by the loaner are : " + dtoLoan.getListOfInRiskYazPayments() + "\n" +
                    "The total payments that have been delayed are : " + dtoLoan.getInRiskCounter() + "\n" +
                    "The total capital that have been delayed is : " + dtoLoan.getInRiskCounter()*dtoLoan.paymentPerPulse();
        }

        @Override
        public  String operationThree(DTOLoan dtoLoan) {
            return "The total payments number that was not paid are: "+dtoLoan.inRiskCounter+"/"+dtoLoan.pulseNumber()+"\n"+"And the dept is:  "+ dtoLoan.totalAmountThatWasNotPayed();
        }
    }
    ,FINISHED{
        @Override
        public String operationTwo(DTOLoan dtoLoan) {
            return getLoansListAsString(dtoLoan) + "------------ Finished Information ------------\n" +
                    FINISHED.operationThree(dtoLoan) + "\n" +
                    "The amount of the pulse payment is : "+dtoLoan.getCapital() / dtoLoan.pulseNumber() + "\n" +
                    "The interest per pulse is : "+(int)(dtoLoan.getCapital()) / dtoLoan.pulseNumber() * ( (double)(dtoLoan.getInterestPerPayment() / 100.0))+"\n" +
                    "The total pulse withe interest payment : " + dtoLoan.paymentPerPulse() + "\n" +
                    "The payments were preformed in Yaz's : " + dtoLoan.getListOfYazPayments() + "\n"
                    ; //+ •	מידע על כל התשלומים ששולמו בפועל (כפי שמוגדר ב active)
        }

        @Override
        public String operationThree(DTOLoan dtoLoan) {
            return "Started YAZ is: "+dtoLoan.startedYazInActive+"\n"+"Ended YAZ is: "+dtoLoan.endedYaz;
        }
    }
    ,NEW {
        @Override
        public String operationTwo(DTOLoan dtoLoan) {
            return "The loan standing on NEW status there for the capital stand still";
        }

        @Override
        public String operationThree(DTOLoan dtoLoan) {

            return "The loan standing on NEW status there for the capital stand still";
        }
    };

    public abstract String operationTwo(DTOLoan dtoLoan);
    public abstract String operationThree(DTOLoan dtoLoan);

    public String getLoansListAsString(DTOLoan dtoLoan){
        StringBuilder stringPrint = new StringBuilder("");
        int index=1;
        if(dtoLoan.listOfAccompanied == null)
            stringPrint.append("The loaners list is currently empty.");
        else {
            stringPrint.append("------------ Borrowers list ------------\n");
            for (DTOCustomer accompanied : dtoLoan.listOfAccompanied) {
                int sum=dtoLoan.getListOfInlays().stream().filter(i->i.getName().equals(accompanied.getName())).mapToInt(i->i.getInvestAmount()).sum();
                stringPrint.append(index).append(". Name: ").append(accompanied.getName()).append("\nThe invest amount: ").append(sum).append("\n");
            }
        }
        return stringPrint.toString();
    }
}
