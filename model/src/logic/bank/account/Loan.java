package logic.bank.account;

//Eliran123


import dataObjects.dtoBank.dtoAccount.*;
import dataObjects.dtoCustomer.DTOCustomer;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.Comparator;

public class Loan extends DTOLoan {


    private Loan() {

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




