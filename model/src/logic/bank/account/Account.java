package logic.bank.account;

import dataObjects.dtoBank.dtoAccount.DTOAccount;
import dataObjects.dtoBank.dtoAccount.DTOInlay;
import dataObjects.dtoBank.dtoAccount.DTOLoan;
import dataObjects.dtoBank.dtoAccount.DTOMovement;

import java.util.List;


public interface Account extends DTOAccount {

    void setAmount(int amount);

    void setName(String name);

    void setMovements(List<DTOMovement> movements);

    void setInlays(List<DTOInlay> inlays);

    void setLoaner(List<DTOLoan> loaners);

    void setBorrower(List<DTOLoan> borrowers);

    void cashDeposit(int sum);

    void cashWithdrawal(int sum);


}
