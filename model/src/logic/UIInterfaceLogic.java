package logic;

import dataObjects.dtoBank.dtoAccount.*;
import dataObjects.dtoCustomer.DTOCustomer;
import javafx.scene.control.ListView;
import logic.bank.account.Inlay;
import logic.bank.account.Loan;
import logic.bank.account.Movement;
import logic.bank.bankLogicYazHistory.BankLogicBankHistoryList;
import logic.customer.Customer;

import java.awt.image.AreaAveragingScaleFilter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public interface UIInterfaceLogic {
    ArrayList<DTOLoanForSale> getListLoansSales();

    DTOLoanForSale loanForSaleBuild(String customerName, int amount, DTOLoan loan) throws InvocationTargetException, InstantiationException, IllegalAccessException;



    DTOLoan loanBuilderFileUploadServer(String idLoan, String ownerLoan, String categoryLoan, int capitalLoan, int totalYazTimeLoan, int paysEveryYazLoan, int interestPerPaymentLoan) throws InvocationTargetException, InstantiationException, IllegalAccessException;

    DTOMovement addMovementToClient(int customerIndex, Movement movement);

    DTOPaymentsPerYaz getPaymentsPerYaz(int desirableYaz);

    ArrayList<DTOPaymentsPerYaz> getPaymentsPerYazList();

    DTOMovement addMovementToClient(DTOCustomer customer, Movement movement);

    DTOInlay addInlayToClient(int customerIndex, Inlay inlay);

    int getAmountOfCustomer(int customerIndex);

    DTOCustomer getCustomer(int customerIndex);

    DTOCustomer getCustomerByName(String customerName);

    String getCustomerName(int customerIndex);

    Customer getRealCustomerByName(String customerName);

    int getNumbersOfOpenLoansBorrowerDK(int customerIndex);

    int getTotalCustomersSize();

    void cashDeposit(int customerIndex, int sum);

    void cashDeposit(DTOCustomer customer, int sum);

    void cashWithdrawal(DTOCustomer customer, int sum);

    void cashWithdrawal(int customerIndex, int sum);

    void addCustomer(DTOCustomer customer) throws InvocationTargetException, InstantiationException, IllegalAccessException;

    ArrayList<DTOCustomer> getCustomers();

    List<DTOLoan> getCustomerLoanersList(String customerName);

    List<DTOLoan> getCustomerBorrowersList(String customerName);

    void addLoanToBank(Loan loan);

    ArrayList<String> getCategoriesGroup();

    String getCategory(int categoryIndex);

    ArrayList<DTOLoan> getLoansList();

    ArrayList<DTOLoan> loansSustainInlay(DTOInlay inlay);
    //////////copy of loansSustainInlay with addition//////////
    ArrayList<DTOLoan> loansSustainInlayDK(DTOInlay inlay);

    ArrayList<DTOLoan> loansSustainInlayAndClientChoose(ArrayList<DTOLoan> loansSupportInlay, String[] arrayStringsScanner);

    void yazProgressLogic(boolean progressTheYaz) throws InvocationTargetException, InstantiationException, IllegalAccessException;//ToDo!!

    ArrayList<DTOMovement> addMovementPerLoanFromInlay(DTOInlay inlay, ArrayList<DTOLoan> loansCustomerChosen, int chosenInvestAmount, int customerIndexGiveMoney) throws InvocationTargetException, InstantiationException, IllegalAccessException;

    void checksInvestAmount(DTOCustomer customer,int investAmount);

    DTOLoan loanBuilder(String idLoan, String ownerLoan, String categoryLoan, int capitalLoan, int totalYazTimeLoan, int paysEveryYazLoan, int interestPerPaymentLoan) throws InvocationTargetException, InstantiationException, IllegalAccessException;

    Inlay inlayBuild(DTOCustomer customer, int investAmount, String category, double minInterestYaz, int minYazTime) throws InvocationTargetException, InstantiationException, IllegalAccessException;
    //////////copy of inlayBuild with addition//////////
    Inlay inlayBuildForDK(DTOCustomer customer, int investAmount, String category, double minInterestYaz, int minYazTime, int maximumLoansOpenToTheBorrower,int getRemain) throws InvocationTargetException, InstantiationException, IllegalAccessException;

    DTOMovement movementBuildToCustomer(DTOCustomer customer, int movementSum, String movementOperation, int movementSumBeforeOperation, int movementSumAfterOperation) throws InvocationTargetException, InstantiationException, IllegalAccessException;

    DTOMovement movementBuildToCustomer(DTOCustomer customer, int movementSum, String movementOperation) throws InvocationTargetException, InstantiationException, IllegalAccessException;

    DTOMovement movementBuildToLoan(DTOLoan dtoLoan,int movementSum,String movementOperation,int movementSumBeforeOperation,int movementSumAfterOperation) throws InvocationTargetException, InstantiationException, IllegalAccessException;

    DTOCustomer customerBuild(String name, int amount) throws InvocationTargetException, InstantiationException, IllegalAccessException;

    DTOLoan getLoanById(String id);

    ArrayList<DTOLoan> yazProgressLogicDesktop(BankLogicBankHistoryList bankLogicBankHistoryList) throws InvocationTargetException, InstantiationException, IllegalAccessException;

    void myAddListenerToStringPropertyLoans(ListView<String> listener,DTOLoan dtoLoan);

    ArrayList<DTOMovement> addMovementPerLoanFromInlayDK(DTOInlay inlay, List<DTOLoan> loansCustomerChosen, int chosenInvestAmount, int maximumOwnershipLoanPercentage) throws InvocationTargetException, InstantiationException, IllegalAccessException;

    void operateThePaymentOfTheLoanDesktop(DTOLoan loan,int customerPayment) throws InvocationTargetException, InstantiationException, IllegalAccessException;

    void setRewind(boolean rewind);

    boolean getRewind();

    DTOLoanForSale getOriginalLoanForSale(DTOLoanForSale dtoLoanForSale);

    void removeLoanFromListOfAccompanied(DTOLoan loanForSale, String nameOfBorrower);

    void removeLoanFromLoansSales(DTOLoanForSale realLoanForSale, DTOLoan loanForSale);

    void addLoanToListOfAccompanied(DTOLoan realLoanForSale, DTOCustomer customerByName);

    void removeInlayFromListOfInlays(DTOLoan realLoanForSale, String nameOfBorrower);

    void addInlayToClientByCustomerName(DTOLoan loanForSale, DTOInlay dtoInlay);

    void removeInlaysFromOldLenderAndAddToNewLender(String nameOfBorrower, String customerFromSession,List<DTOInlay> oldInlay,DTOInlay newInlay);
}

