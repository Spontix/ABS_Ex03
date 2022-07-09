package logic.bank.account;

import dataObjects.dtoBank.dtoAccount.DTOPaymentsInfo;

public class PaymentsInfo extends DTOPaymentsInfo {

    private PaymentsInfo(){

    }

    public static PaymentsInfo build(){
        return new PaymentsInfo();
    }

    public PaymentsInfo setYazPayment(int yazPayment){
        this.yazPayment=yazPayment;
        return this;
    }

    public PaymentsInfo setCapitalAmount(int capitalAmount){
        this.capitalAmount=capitalAmount;
        return this;
    }

    public PaymentsInfo setCapitalAndInterest(int capitalAndInterest){
        this.capitalAndInterest=capitalAndInterest;
        return this;
    }

    public PaymentsInfo setInterestAmount(int interestAmount){
        this.interestAmount=interestAmount;
        return this;
    }

    public PaymentsInfo setIsPaid(String isPaid){
        this.isPaid=isPaid;
        return this;
    }

}
