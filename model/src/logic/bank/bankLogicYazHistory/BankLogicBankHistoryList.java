package logic.bank.bankLogicYazHistory;

import logic.UIInterfaceLogic;
import logic.YazLogicDesktop;

import java.util.ArrayList;

public class BankLogicBankHistoryList {

    ArrayList<BankLogicYazHistory> bankLogicYazHistoriesList=new ArrayList<>();

    public BankLogicYazHistory getBankLogicYazHistoryByYaz(int yazUnit){
        BankLogicYazHistory bankLogicYazHistory=null;
        for (BankLogicYazHistory bankLogicHistory:bankLogicYazHistoriesList) {
            if(bankLogicHistory.yazUnit==yazUnit){
                bankLogicYazHistory=bankLogicHistory;
                bankLogicYazHistory.getBank().setRewind(true);
            }
        }

        return bankLogicYazHistory;
    }

    public void addBankLogicYazHistory(BankLogicYazHistory bankLogicYazHistory){
        this.bankLogicYazHistoriesList.add(bankLogicYazHistory);
    }

    public UIInterfaceLogic getLastBankLogicYazHistory(){
        UIInterfaceLogic bank=this.bankLogicYazHistoriesList.get(this.bankLogicYazHistoriesList.size()-1).bank;
        YazLogicDesktop.currentYazUnitProperty.set(this.bankLogicYazHistoriesList.get(this.bankLogicYazHistoriesList.size()-1).yazUnit);
        this.bankLogicYazHistoriesList.remove(this.bankLogicYazHistoriesList.size()-1);
        return bank;
    }
}
