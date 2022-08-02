package logic.bank.bankLogicYazHistory;

import logic.UIInterfaceLogic;
import logic.bank.Bank;

import java.util.ArrayList;

public class BankLogicYazHistory {
    protected UIInterfaceLogic bank;
    protected int yazUnit;

    static public BankLogicYazHistory build(Bank bankLogicHistory,int yazUnit){
        BankLogicYazHistory bankLogicYazHistory=new BankLogicYazHistory();
        bankLogicYazHistory.yazUnit=yazUnit;
        bankLogicYazHistory.bank=Bank.buildBank(bankLogicHistory);

        return bankLogicYazHistory;
    }

    public UIInterfaceLogic getBank(){
        return bank;
    }
}
