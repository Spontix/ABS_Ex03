package logic.bank.account;

//Eliran123


import dataObjects.dtoBank.dtoAccount.*;
import dataObjects.dtoCustomer.DTOCustomer;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Loan extends DTOLoan {


    private Loan() {

    }


    public static Loan build(Loan loan) {
        Loan dtoLoan = new Loan();
        dtoLoan.isPaid=loan.isPaid;
        dtoLoan.nextYazToBePaid=loan.nextYazToBePaid;
        dtoLoan.totalInterestPayTillEnd= loan.totalInterestPayTillEnd;
        dtoLoan.totalCapitalPayTillEnd=loan.totalCapitalPayTillEnd;
        dtoLoan.totalCapitalPayTillNow=loan.totalCapitalPayTillNow;
        dtoLoan.totalInterestPayTillNow= loan.totalInterestPayTillNow;;
        dtoLoan.windowOfPaymentCounter=loan.windowOfPaymentCounter;
        dtoLoan.capital = loan.capital;
        dtoLoan.loanStatus = loan.loanStatus;
        dtoLoan.paysEveryYaz = loan.paysEveryYaz;
        dtoLoan.id = loan.id;
        dtoLoan.owner = loan.owner;
        dtoLoan.totalYazTime = loan.totalYazTime;
        dtoLoan.interestPerPayment = loan.interestPerPayment;
        dtoLoan.category = loan.category;
        dtoLoan.startedYazInActive=loan.startedYazInActive;
        dtoLoan.endedYaz=loan.endedYaz;
        dtoLoan.inRiskCounter=loan.inRiskCounter;
        dtoLoan.debt=loan.debt;
        dtoLoan.capitalSumLeftTillActive=loan.capitalSumLeftTillActive;
        List<DTOCustomer> accompaniedList = new ArrayList<>();
        List<DTOInlay> inlaysList = new ArrayList<>();
        List<DTOMovement> movementsList=new ArrayList<>();
        List<Integer> listOfYazPayments=new ArrayList<>();
        List<Integer> listOfInRiskYazPayments=new ArrayList<>();
        List<DTOPaymentsInfo> paymentsInfoList=new ArrayList<>();
        for (DTOPaymentsInfo dtoPaymentsInfo : loan.paymentsInfoList) {
            paymentsInfoList.add(DTOPaymentsInfo.build(dtoPaymentsInfo));
        }
        for (DTOCustomer dtoAccount : loan.listOfAccompanied) {
            accompaniedList.add(DTOCustomer.build(dtoAccount));
        }
        for (DTOInlay dtoInlay : loan.listOfInlays) {
            inlaysList.add(DTOInlay.build(dtoInlay));
        }
        for (DTOMovement dtoMovement : loan.listOfMovements) {
            movementsList.add(DTOMovement.build(dtoMovement));
        }
        listOfYazPayments.addAll(loan.listOfYazPayments);
        listOfInRiskYazPayments.addAll(loan.listOfInRiskYazPayments);
        dtoLoan.listOfInRiskYazPayments=listOfInRiskYazPayments;
        dtoLoan.listOfYazPayments=listOfYazPayments;
        dtoLoan.listOfInlays=inlaysList;
        dtoLoan.listOfAccompanied=accompaniedList;
        dtoLoan.listOfMovements=movementsList;
        dtoLoan.paymentsInfoList=paymentsInfoList;
        dtoLoan.massagesProperty=loan.massagesProperty;
        dtoLoan.pulseCounterThatHappened=loan.pulseCounterThatHappened;


        return dtoLoan;
    }

    public void setTotalYaz(int totalYazTime) {
        this.totalYazTime= totalYazTime;
    }

    private void setPendingDetails() {
        //int sum=listOfAccompanied.stream().mapToInt(a -> a.get)
    }


    public void setStartedYazInActive(int yaz){
        if(startedYazInActive==0)
            startedYazInActive=yaz;
    }

    public void setLoanStatus(DTOLoanStatus loanStatus){
        this.loanStatus=loanStatus;

    }

    public void incrInRiskCounter(){
        this.inRiskCounter++;
    }

    public void decInRiskCounter(){
        this.inRiskCounter--;
    }

    public void incrCapitalSumLeftTillActive(int paymentPerPulse){
        this.capitalSumLeftTillActive+=paymentPerPulse;
    }


    public void setId(String idLoan) {
        this.id=idLoan;
    }

    public void setOwner(String ownerLoan) {
        this.owner=ownerLoan;
    }

    public void setCategory(String categoryLoan) {
        this.category=categoryLoan;
    }

    public void setCapital(int capitalLoan) {
        this.capital=capitalLoan;
    }

    public void setPaysEveryYaz(int paysEveryYazLoan) {
        this.paysEveryYaz=paysEveryYazLoan;
    }

    public void setInterestPerPayment(int interestPerPaymentLoan) {
        this.interestPerPayment=interestPerPaymentLoan;
    }

    public void setListOfAccompanied(ArrayList<DTOCustomer> accompanied) {
        this.listOfAccompanied=accompanied;
    }

    public void setListOfInlays(ArrayList<DTOInlay> inlays) {
        this.listOfInlays=inlays;
    }

    public void setCapitalSumLeftTillActive(int capitalSumLeftTillActive) {
        this.capitalSumLeftTillActive=capitalSumLeftTillActive;
    }

    public void incrPulseCounterThatHappenedByOne(){
        pulseCounterThatHappened++;
    }

    public void setListOfMovements(ArrayList<DTOMovement> movements) {
        this.listOfMovements=movements;
    }

    public void setListOfYazPayment(ArrayList<Integer> listOfYazPayment) {
        this.listOfYazPayments = listOfYazPayment;

    }

    public void setListOfInRiskYazPayments(ArrayList<Integer> listOfInRiskYazPayments) {
        this.listOfInRiskYazPayments=listOfInRiskYazPayments;
    }

    public void setEndedYaz(int endedYaz){
        this.endedYaz=endedYaz;
    }

    public void myAddListenerToStringProperty(ListView<String> listener){
        massagesProperty.addListener(e->listener.getItems().add(massagesProperty.getValue()));
    }

    public void setStringPropertyValue(String value){
        massagesProperty.setValue(value);
    }

    public void addDebt(int sum){
        this.debt+=sum;
    }

    public void decDebt(int sum){
        this.debt-=sum;
    }

    public void setPaymentsInfoList(ArrayList<DTOPaymentsInfo> paymentsInfoList){
        this.paymentsInfoList=paymentsInfoList;
    }


    public void setIsPaid(boolean isPaid) {
        this.isPaid=isPaid;
    }
}




