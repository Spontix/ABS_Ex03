package dataObjects.dtoBank.dtoAccount;

import java.util.List;

public interface DTOAccount {

    List<DTOLoan> getLoaner();
    List<DTOLoan> getBorrower();
    List<DTOInlay> getInlays();
    List<DTOMovement> getMovements();
    String getName();
    int getAmount();
    String toString();
    int getAllOpenLoansToBorrower();
}
