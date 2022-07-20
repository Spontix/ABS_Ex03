package logic.bank;

import dataObjects.dtoBank.DTOBank;
import dataObjects.dtoBank.dtoAccount.*;
import dataObjects.dtoCustomer.DTOCustomer;
import javafx.scene.control.ListView;
import logic.UIInterfaceLogic;
import logic.YazLogic;
import logic.YazLogicDesktop;
import logic.bank.account.*;
import logic.customer.Customer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class Bank implements UIInterfaceLogic {

    protected List<Account> accounts;
    protected List<Loan> loans;
    protected List<String> categories;
    protected List<PaymentsPerYaz> paymentsPerYazList=new ArrayList<>();


    private Object createInstance(Class clazz) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor[] conList = clazz.getDeclaredConstructors();
        Object object = null;
        for (Constructor ctor : conList) {
            if (ctor.getParameters().length == 0) {
                ctor.setAccessible(true);
                object = ctor.newInstance();
                break;
            }
        }
        return object;
    }

    @Override
    public DTOCustomer customerBuild(String name, int amount) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Customer customer = (Customer) createInstance(Customer.class);
        customer.setName(name);
        customer.setAmount(amount);
        customer.setMovements(new ArrayList<>());
        customer.setLoaner(new ArrayList<>());
        customer.setBorrower(new ArrayList<>());
        customer.setInlays(new ArrayList<>());
        accounts.add(customer);

        return DTOCustomer.build(customer);
    }

    @Override
    public DTOLoan getLoanById(String id) {
        return DTOLoan.build(loans.stream().filter(l -> l.getId().equals(id)).collect(Collectors.toList()).get(0));

    }

    @Override
    public DTOMovement movementBuildToCustomer(DTOCustomer customer, int movementSum, String movementOperation, int movementSumBeforeOperation, int movementSumAfterOperation) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Movement movement = createMovement(movementSum, movementOperation, movementSumBeforeOperation, movementSumAfterOperation);
        accounts.stream().filter(a -> a.getName().equals(customer.getName())).collect(Collectors.toList()).get(0).getMovements().add(movement);
        return DTOMovement.build(movement);
    }

    @Override
    public DTOMovement movementBuildToCustomer(DTOCustomer customer, int movementSum, String movementOperation) throws InvocationTargetException, InstantiationException, IllegalAccessException{
        Customer localCustomer= this.getRealCustomerByName(customer.getName());
        Movement movement = createMovement(movementSum, movementOperation, localCustomer.getAmount(), Objects.equals(movementOperation, "-") ?localCustomer.getAmount()-movementSum:localCustomer.getAmount()+movementSum);
        accounts.stream().filter(a -> a.getName().equals(customer.getName())).collect(Collectors.toList()).get(0).getMovements().add(movement);
        return DTOMovement.build(movement);
    }

    @Override
    public DTOMovement movementBuildToLoan(DTOLoan dtoLoan, int movementSum, String movementOperation, int movementSumBeforeOperation, int movementSumAfterOperation) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Movement movement = createMovement(movementSum, movementOperation, movementSumBeforeOperation, movementSumAfterOperation);

        loans.stream().filter(l -> l.getId().equals(dtoLoan.getId())).collect(Collectors.toList()).get(0).getListOfMovements().add(movement);


        return DTOMovement.build(movement);
    }

    private Movement createMovement(int movementSum, String movementOperation, int movementSumBeforeOperation, int movementSumAfterOperation) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Movement movement = (Movement) createInstance(Movement.class);
        movement.setSum(movementSum);
        movement.setOperation(movementOperation);
        movement.setToDoYazTime(YazLogic.currentYazUnit);
        if (movementSumAfterOperation < 0) {
            throw new RuntimeException("The operation cant be preformed because there is not enough money in this account!!");
        }
        movement.setSumAfterOperation(movementSumAfterOperation);
        movement.setSumBeforeOperation(movementSumBeforeOperation);

        return movement;
    }

    @Override
    public Inlay inlayBuild(DTOCustomer customer, int investAmount, String category, double minInterestYaz, int minYazTime) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Inlay inlay = (Inlay) createInstance(Inlay.class);
        checksInvestAmount(customer, investAmount);
        inlay.setInvestAmount(investAmount);
        inlay.setCategory(category);
        inlay.setMinInterestYaz(minInterestYaz);
        inlay.setMinYazTime(minYazTime);
        inlay.setInlayCustomer(accounts.stream().filter(a -> a.getName().equals(customer.getName())).collect(Collectors.toList()).get(0));
        inlay.setCustomerName(inlay.getName());

        return inlay;
    }

    @Override
    public //////////copy of inlayBuild. I had a new member maximumLoansOpenToTheBorrower to DTOInlay and creat Get and Set methods//////////
    Inlay inlayBuildForDK(DTOCustomer customer, int investAmount, String category, double minInterestYaz, int minYazTime, int maximumLoansOpenToTheBorrower) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Inlay inlay = (Inlay) createInstance(Inlay.class);
        checksInvestAmount(customer, investAmount);
        inlay.setInvestAmount(investAmount);
        inlay.setCategory(category);
        inlay.setMinInterestYaz(minInterestYaz);
        inlay.setMaximumLoansOpenToTheBorrower(maximumLoansOpenToTheBorrower);
        inlay.setMinYazTime(minYazTime);
        inlay.setCustomerName(inlay.getName());
        return inlay;
    }

    @Override
    public void checksInvestAmount(DTOCustomer customer, int investAmount) {
        if (investAmount > customer.getAmount() || investAmount < 0) {
            throw new RuntimeException("The investment amount is above the amount balance or a negative one. please try again!");
        }
    }

    @Override
    public DTOLoan loanBuilder(String idLoan, String ownerLoan, String categoryLoan, int capitalLoan, int totalYazTimeLoan, int paysEveryYazLoan, int interestPerPaymentLoan) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Loan loan = (Loan) createInstance(Loan.class);
        loan.setId(idLoan);
        loan.setOwner(ownerLoan);
        loan.setCategory(categoryLoan);
        loan.setCapital(capitalLoan);
        loan.setCapitalSumLeftTillActive(0);
        loan.setTotalYaz(totalYazTimeLoan);
        loan.setPaysEveryYaz(paysEveryYazLoan);
        loan.setInterestPerPayment(interestPerPaymentLoan);
        loan.setListOfYazPayment(new ArrayList<>());
        loan.setListOfInRiskYazPayments(new ArrayList<>());
        loan.setListOfAccompanied(new ArrayList<>());
        loan.setListOfInlays(new ArrayList<>());
        loan.setListOfMovements(new ArrayList<>());
        loan.setPaymentsInfoList(new ArrayList<>());
        loan.addTotalInterestPayTillNow(0);
        loan.addTotalCapitalPayTillNow(0);
        loans.add(loan);


        return DTOLoan.build(loan);
    }

    @Override
    public DTOLoan loanBuilderFileUploadServer(String idLoan, String ownerLoan, String categoryLoan, int capitalLoan, int totalYazTimeLoan, int paysEveryYazLoan, int interestPerPaymentLoan) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Loan loan = (Loan) createInstance(Loan.class);
        loan.setId(idLoan);
        loan.setOwner(ownerLoan);
        loan.setCategory(categoryLoan);
        loan.setCapital(capitalLoan);
        loan.setCapitalSumLeftTillActive(0);
        loan.setTotalYaz(totalYazTimeLoan);
        loan.setPaysEveryYaz(paysEveryYazLoan);
        loan.setInterestPerPayment(interestPerPaymentLoan);
        loan.setListOfYazPayment(new ArrayList<>());
        loan.setListOfInRiskYazPayments(new ArrayList<>());
        loan.setListOfAccompanied(new ArrayList<>());
        loan.setListOfInlays(new ArrayList<>());
        loan.setListOfMovements(new ArrayList<>());
        loan.setPaymentsInfoList(new ArrayList<>());
        loan.addTotalInterestPayTillNow(0);
        loan.addTotalCapitalPayTillNow(0);
        loans.add(loan);
        this.getRealCustomerByName(ownerLoan).getLoaner().add(loan);


        return DTOLoan.build(loan);
    }



    public Bank() {
        accounts=new ArrayList<>();
        loans=new ArrayList<>();
        categories=new ArrayList<>();
    }

    @Override
    public DTOInlay addInlayToClient(int customerIndex, Inlay inlay) {
        accounts.get(customerIndex).getInlays().add(inlay);
        return DTOInlay.build(inlay);
    }

    @Override
    public DTOPaymentsPerYaz getPaymentsPerYaz(int desirableYaz){
        DTOPaymentsPerYaz dtoPaymentsPerYaz=new DTOPaymentsPerYaz();
        for (DTOLoan dtoLoan : this.getLoansList()) {
            if (dtoLoan.getLoanStatus() != DTOLoanStatus.NEW && dtoLoan.getLoanStatus() != DTOLoanStatus.FINISHED && dtoLoan.getLoanStatus() != DTOLoanStatus.PENDING)
                dtoPaymentsPerYaz.getDTOLoansList().add(dtoLoan);
        }
        /*for (PaymentsPerYaz originalPaymentsPerYaz : this.paymentsPerYazList) {
            if(originalPaymentsPerYaz.getCurrentYaz()==desirableYaz)
                dtoPaymentsPerYaz=DTOPaymentsPerYaz.build(originalPaymentsPerYaz);
        }*/
        return dtoPaymentsPerYaz;
    }

    @Override
    public ArrayList<DTOPaymentsPerYaz> getPaymentsPerYazList(){
        ArrayList<DTOPaymentsPerYaz> dtoPaymentsPerYazList=new ArrayList<>();
        for (PaymentsPerYaz originalPaymentsPerYaz : this.paymentsPerYazList) {
            dtoPaymentsPerYazList.add(DTOPaymentsPerYaz.build(originalPaymentsPerYaz));
        }
        return dtoPaymentsPerYazList;
    }

    @Override
    public DTOMovement addMovementToClient(DTOCustomer customer, Movement movement) {
        accounts.stream().filter(a -> Objects.equals(a.getName(), customer.getName())).collect(Collectors.toList()).get(0).getMovements().add(Movement.build(movement));
        return (DTOMovement) movement;
    }

    @Override
    public DTOMovement addMovementToClient(int customerIndex, Movement movement) {
        accounts.get(customerIndex).getMovements().add(Movement.build(movement));
        return (DTOMovement) movement;
    }

    @Override
    public int getAmountOfCustomer(int customerIndex) {
        return this.getCustomer(customerIndex).getAmount();
    }

    /////////////////this one is new and its for the inlay////////////////
    @Override
    public int getNumbersOfOpenLoansBorrowerDK(int customerIndex) {
        int counterLoansOpenToTheBorrower = 0;
        for (DTOLoan loan : this.getLoansList()) {
            if (loan.getOwner().equals(this.getCustomerName(customerIndex)) && loan.getLoanStatus() != DTOLoanStatus.FINISHED)
                counterLoansOpenToTheBorrower++;
        }
        return counterLoansOpenToTheBorrower;
    }

    @Override
    public DTOCustomer getCustomer(int customerIndex) {
        return DTOCustomer.build((Customer) accounts.get(customerIndex));
    }

    @Override
    public String getCustomerName(int customerIndex) {
        return getCustomer(customerIndex).getName();
    }

    @Override
    public DTOCustomer getCustomerByName(String customerName) {
        for (DTOAccount account :accounts) {
            if (Objects.equals(account.getName(), customerName))
                return DTOCustomer.build((Customer) account);
        }
        return null;
    }

    @Override
    public int getTotalCustomersSize() {
        return accounts.size();
    }

    @Override
    public void cashDeposit(int customerIndex, int sum) {
        accounts.get(customerIndex).cashDeposit(sum);
    }

    @Override
    public void cashWithdrawal(int customerIndex, int sum) {
        accounts.get(customerIndex).cashWithdrawal(sum);
    }

    @Override
    public void cashDeposit(DTOCustomer customer, int sum) {
        accounts.stream().filter(a -> Objects.equals(a.getName(), customer.getName())).collect(Collectors.toList()).get(0).cashDeposit(sum);
    }

    @Override
    public void cashWithdrawal(DTOCustomer customer, int sum) {
        accounts.stream().filter(a -> Objects.equals(a.getName(), customer.getName())).collect(Collectors.toList()).get(0).cashWithdrawal(sum);

    }

    @Override
    public void addCustomer(DTOCustomer customer) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        this.customerBuild(customer.getName(),0);
    }

    @Override
    public ArrayList<DTOCustomer> getCustomers() {
        ArrayList<DTOCustomer> dtoCustomers = new ArrayList<>();
        for (DTOAccount account : accounts) {
            dtoCustomers.add(DTOCustomer.build((Customer) account));
        }

        return dtoCustomers;
    }

    @Override
    public ArrayList<DTOLoan> getLoansList() {
        ArrayList<DTOLoan> localLoans = new ArrayList<>();
        for (DTOLoan loan : loans) {
            localLoans.add(DTOLoan.build(loan));
        }
        return localLoans;
    }

    @Override
    public List<DTOLoan> getCustomerLoanersList(String customerName) {
        return loans.stream().filter(l -> l.getOwner().equals(customerName)).collect(Collectors.toList());
    }

    @Override
    public List<DTOLoan> getCustomerBorrowersList(String customerName) {
        return loans.stream()
                .filter(l -> l.getListOfAccompanied().stream().anyMatch(a -> a.getName().equals(customerName)))
                .collect(Collectors.toList());
    }

    @Override
    public void addLoanToBank(Loan loan) {///////////////
        loans.add(loan);
    }


    @Override
    public ArrayList<String> getCategoriesGroup() {
        return new ArrayList<>(categories);
    }

    @Override
    public String getCategory(int categoryIndex) {
        return categories.get(categoryIndex - 1);
    }

    @Override
    public ArrayList<DTOLoan> loansSustainInlay(DTOInlay inlay) {
        ArrayList<DTOLoan> loansSustainInlay = new ArrayList<>();
        for (Loan loan : loans) {
            if (loan.getLoanStatus() == DTOLoanStatus.PENDING || loan.getLoanStatus() == DTOLoanStatus.NEW) {
                if (!Objects.equals(inlay.getName(), loan.getOwner())) {//if i am not the one who asking for the money
                    if ((inlay.getMinInterestYaz() <= loan.getInterestPerPayment() || inlay.getMinInterestYaz() == 0) &&//i dont understand why this is not should be equal
                            (inlay.getMinYazTime() <= loan.getTotalYazTime() || inlay.getMinYazTime() == 0) &&
                            (inlay.getCategory().length() == 0 || inlay.getCategory().contains(loan.getCategory())))

                        loansSustainInlay.add(DTOLoan.build(loan));
                }
            }
        }
        return loansSustainInlay;
    }

    ////////////copy of loansSustainInlay with addition///////////
    @Override
    public ArrayList<DTOLoan> loansSustainInlayDK(DTOInlay inlay) {
        ArrayList<DTOLoan> loansSustainInlayDK = new ArrayList<>();
        for (Loan loan : loans) {
            if (loan.getLoanStatus() == DTOLoanStatus.PENDING || loan.getLoanStatus() == DTOLoanStatus.NEW) {
                if (!Objects.equals(inlay.getName(), loan.getOwner())) {//if i am not the one who asking for the money
                    if ((inlay.getMinInterestYaz() <= loan.getInterestPerPayment() || inlay.getMinInterestYaz() == 0) &&
                            (inlay.getMinYazTime() <= loan.getTotalYazTime() || inlay.getMinYazTime() == 0) &&
                            (inlay.getCategory().length() == 0 || inlay.getCategory().contains(loan.getCategory())) &&
                            ///////////////////// check if it is ok to use getRealCustomerByName() method//////////////////////
                            (getRealCustomerByName(loan.getOwner()).getAllOpenLoansToBorrower() <= inlay.getMaximumLoansOpenToTheBorrower() || inlay.getMaximumLoansOpenToTheBorrower() == 0))

                        loansSustainInlayDK.add(DTOLoan.build(loan));
                }
            }
        }
        return loansSustainInlayDK;
    }

    @Override
    public ArrayList<DTOLoan> loansSustainInlayAndClientChoose(ArrayList<DTOLoan> loansSupportInlay, String[] arrayStringsScanner) {
        String errorMassage = "Please select the number loan that you are interested in participating in. You can select more than one by separating the numbers by space or Enter for cancel. The format is: 2 3 4 ...(without duplication!)";
        ArrayList<DTOLoan> loansChosenCustomer = new ArrayList<>();
        int index = 0;
        while (index < arrayStringsScanner.length) {
            try {
                String number = arrayStringsScanner[index];
                int indexForLoan = (Integer.parseInt(arrayStringsScanner[index]) - 1);
                if (indexForLoan >= 0 && indexForLoan <= arrayStringsScanner.length && !(Arrays.stream(arrayStringsScanner).filter(s -> s.equals(number)).count() > 1) && loansSupportInlay.size() >= arrayStringsScanner.length) {
                    loansChosenCustomer.add(loansSupportInlay.get(indexForLoan));
                } else
                    throw new NumberFormatException(errorMassage);
            } catch (NumberFormatException exception) {
                throw new NumberFormatException(errorMassage);

            }
            index++;
        }

        return loansChosenCustomer;
    }

    @Override/////This methode should return list of movements and print them all
    public ArrayList<DTOMovement> addMovementPerLoanFromInlay(DTOInlay dtoInlay, ArrayList<DTOLoan> loansCustomerChosen, int chosenInvestAmount, int customerIndexGiveMoney) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        ///5000
        int sumPerLoan = 0;//מחזיק חלוקה שרירותית
        int sumPerLoanToBeAdd = 0;//מחזיק חלוקה שרירותית פלוס 1 אם המודולו אינו אפס
        int remainderByModulo = 0;//מחזיק את השארית
        DTOInlay dtoInlayToBeAddToTheLoan;
        int investAmountCounterThatGoingToBeInsertToTheCustomer = 0;
        Customer customer =  this.getRealCustomerByName(dtoInlay.getName());//הלקוח שנותן כסף(logic)
        Loan loan;//משתנה הלוואה
        ArrayList<DTOMovement> listOfDTOMovements = new ArrayList<>();
        DTOMovement dtoMovement;//the movement that i will add to the list ot the DTOmovements that i will return to the console that the console will show the list
        int numberOfLoans = loansCustomerChosen.size();//כמות ההלוואות שהמשתמש בחר
        if (loansCustomerChosen != null) {
            for (DTOLoan dtoLoan : loansCustomerChosen) {//רץ לי על ההלוואות שהלקוח בחר
                remainderByModulo = chosenInvestAmount % numberOfLoans;//מחשב את השארית ===2
                sumPerLoanToBeAdd = sumPerLoan = (chosenInvestAmount / numberOfLoans);//מחשב באופן שרירותי את הסכום פר הלוואה שהלקוח יכול לתת====5000/3=1666
                if (remainderByModulo > 0) {//מודולו=2
                    sumPerLoanToBeAdd = sumPerLoan + 1;////1666+1
                    remainderByModulo--;//מודולו=1
                }
////////////////////////seperate method
                loan = loans.stream().filter(l -> l.getId().equals(dtoLoan.getId())).collect(Collectors.toList()).get(0);//מציאת ההלוואה המקורית מהלוגיקה
                if (sumPerLoanToBeAdd >= (loan.getCapital() - loan.getCapitalSumLeftTillActive())) {//אם הסכום השרירותי גדול מהסכום שנשאר לסגור את ההחוואה אזי --1666>1000
                    dtoInlayToBeAddToTheLoan = inlayBuild(customer, (loan.getCapital() - loan.getCapitalSumLeftTillActive()), dtoInlay.getCategory(), dtoInlay.getMinInterestYaz(), dtoInlay.getMinYazTime());//בונה שיבוץ חדש שהסגכום שלו הוא מה שנשאר כדי לסגור את ההלוואה---1000
                    chosenInvestAmount = chosenInvestAmount - (loan.getCapital() - loan.getCapitalSumLeftTillActive());//הסכום הכולל של הלקוח פחות הסכום שנתן עכשיו להללואה---5000-1000
                    movementBuildToCustomer((DTOCustomer) customer, (loan.getCapital() - loan.getCapitalSumLeftTillActive()), "-", customer.getAmount(), customer.getAmount() - (loan.getCapital() - loan.getCapitalSumLeftTillActive()));
                    cashWithdrawal((Customer) customer, (loan.getCapital() - loan.getCapitalSumLeftTillActive()));
                    dtoMovement = movementBuildToLoan(loan, (loan.getCapital() - loan.getCapitalSumLeftTillActive()), "+", loan.getCapitalSumLeftTillActive(), loan.getCapital());//ההלוואה קיבלה כסף ולכן הפלוס אף אנחנו מנפים את זה מהעתק של הסכום כדי לדעת מתי הגענו ל0
                    listOfDTOMovements.add(dtoMovement);
                    investAmountCounterThatGoingToBeInsertToTheCustomer += (loan.getCapital() - loan.getCapitalSumLeftTillActive());
                    loan.incrCapitalSumLeftTillActive((loan.getCapital() - loan.getCapitalSumLeftTillActive()));//מוריד העתק של הסכום אשר אומר מתי הלוואה היא פעילה
                    numberOfLoans = numberOfLoans - 1;//מחסיר את כמות ההלוואות אשר סגרתי הלוואה 1
                } else {
                    dtoInlayToBeAddToTheLoan = inlayBuild(customer, sumPerLoanToBeAdd, dtoInlay.getCategory(), dtoInlay.getMinInterestYaz(), dtoInlay.getMinYazTime());
                    chosenInvestAmount = chosenInvestAmount - sumPerLoanToBeAdd;//5000-1667
                    movementBuildToCustomer((DTOCustomer) customer, sumPerLoanToBeAdd, "-", customer.getAmount(), customer.getAmount() - sumPerLoanToBeAdd);
                    cashWithdrawal((Customer) customer, sumPerLoanToBeAdd);
                    dtoMovement = movementBuildToLoan(loan, sumPerLoanToBeAdd, "+", loan.getCapitalSumLeftTillActive(), loan.getCapitalSumLeftTillActive() + sumPerLoanToBeAdd);//ההלוואה קיבלה כסף ולכן הפלוס אף אנחנו מנפים את זה מהעתק של הסכום כדי לדעת מתי הגענו ל0
                    listOfDTOMovements.add(dtoMovement);
                    investAmountCounterThatGoingToBeInsertToTheCustomer += sumPerLoanToBeAdd;
                    loan.incrCapitalSumLeftTillActive(sumPerLoanToBeAdd);
                    numberOfLoans = numberOfLoans - 1;
                }
                //////////////////Documentation: Check if the client is not already on the list of loaners..................\\\\\\\\\\\\\\\\
                boolean customerAlreadyExistInListOfAccompanied = false;
                for (DTOCustomer account : loan.getListOfAccompanied()) {
                    if (account == customer) {
                        customerAlreadyExistInListOfAccompanied = true;
                        break;
                    }
                }
                if (!customerAlreadyExistInListOfAccompanied)
                    loan.getListOfAccompanied().add(customer);//מוסיף אותו כמלווה להלוואה
                loan.getListOfInlays().add(dtoInlayToBeAddToTheLoan);//מוסיף את השיבוץ החדש להלוואה עם הסכום שהוא הלווה
                if (loan.getStatusOperation().equals(DTOLoanStatus.NEW)) {
                    loan.setLoanStatus(DTOLoanStatus.PENDING);
                }
                if (loan.getCapitalSumLeftTillActive() == loan.getCapital()) {
                    loan.setLoanStatus(DTOLoanStatus.ACTIVE);
                    movementBuildToCustomer(getRealCustomerByName(loan.getOwner()), loan.getCapital(), "+", getRealCustomerByName(loan.getOwner()).getAmount(), getRealCustomerByName(loan.getOwner()).getAmount() + loan.getCapital());
                    (getRealCustomerByName(loan.getOwner())).cashDeposit(loan.getCapital());
                    loan.setStartedYazInActive(YazLogic.currentYazUnit);
                    if (loan.getPaysEveryYaz() == 1) {
                        yazProgressLogic(false);
                    }
                }

            }
            ((Inlay) dtoInlay).setInvestAmount(investAmountCounterThatGoingToBeInsertToTheCustomer);
            customer.getInlays().add((Inlay) dtoInlay);
        } else
            throw new RuntimeException("You didnt choose any loans.");

        return listOfDTOMovements;
    }

    @Override
    public ArrayList<DTOMovement> addMovementPerLoanFromInlayDK(DTOInlay dtoInlay, List<DTOLoan> loansCustomerChosen, int chosenInvestAmount, int maximumOwnershipLoanPercentage) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        ///5000
        //if(maximumOwnershipLoanPercentage!=0)
        int sumPerLoanWithChosenPercentage = (chosenInvestAmount * maximumOwnershipLoanPercentage / 100);//1500
        int sumPerLoan = 0;//מחזיק חלוקה שרירותית
        int sumPerLoanToBeAdd = 0;//מחזיק חלוקה שרירותית פלוס 1 אם המודולו אינו אפס
        int remainderByModulo = 0;//מחזיק את השארית
        DTOInlay dtoInlayToBeAddToTheLoan;
        int investAmountCounterThatGoingToBeInsertToTheCustomer = 0;
        DTOCustomer customer =  this.getRealCustomerByName(dtoInlay.getName());//הלקוח שנותן כסף(logic)
        Loan loan;//משתנה הלוואה
        ArrayList<DTOMovement> listOfDTOMovements = new ArrayList<>();
        DTOMovement dtoMovement;//the movement that i will add to the list ot the DTOmovements that i will return to the console that the console will show the list
        int numberOfLoans = loansCustomerChosen.size();//כמות ההלוואות שהמשתמש בחר
        if (loansCustomerChosen != null) {
            for (DTOLoan dtoLoan : loansCustomerChosen) {//רץ לי על ההלוואות שהלקוח בחר
                remainderByModulo = chosenInvestAmount % numberOfLoans;//מחשב את השארית ===2
                sumPerLoanToBeAdd = sumPerLoan = (chosenInvestAmount / numberOfLoans);//מחשב באופן שרירותי את הסכום פר הלוואה שהלקוח יכול לתת====5000/3=1666
                if (remainderByModulo > 0) {//מודולו=2
                    sumPerLoanToBeAdd = sumPerLoan + 1;////1666+1
                    remainderByModulo--;//מודולו=1
                }

                loan = loans.stream().filter(l -> l.getId().equals(dtoLoan.getId())).collect(Collectors.toList()).get(0);//מציאת ההלוואה המקורית מהלוגיקה
                if (maximumOwnershipLoanPercentage != 0)
                    sumPerLoanToBeAdd = sumPerLoanWithChosenPercentage;//ToDo : Check if its right
                if (sumPerLoanToBeAdd >= (loan.getCapital() - loan.getCapitalSumLeftTillActive())) {//אם הסכום השרירותי גדול מהסכום שנשאר לסגור את ההחוואה אזי --1666>1000
                    dtoInlayToBeAddToTheLoan = inlayBuild(customer, (loan.getCapital() - loan.getCapitalSumLeftTillActive()), dtoInlay.getCategory(), dtoInlay.getMinInterestYaz(), dtoInlay.getMinYazTime());//בונה שיבוץ חדש שהסגכום שלו הוא מה שנשאר כדי לסגור את ההלוואה---1000
                    chosenInvestAmount = chosenInvestAmount - (loan.getCapital() - loan.getCapitalSumLeftTillActive());//הסכום הכולל של הלקוח פחות הסכום שנתן עכשיו להללואה---5000-1000
                    movementBuildToCustomer(customer, (loan.getCapital() - loan.getCapitalSumLeftTillActive()), "-", customer.getAmount(), customer.getAmount() - (loan.getCapital() - loan.getCapitalSumLeftTillActive()));
                    cashWithdrawal(customer, (loan.getCapital() - loan.getCapitalSumLeftTillActive()));
                    dtoMovement = movementBuildToLoan(loan, (loan.getCapital() - loan.getCapitalSumLeftTillActive()), "+", loan.getCapitalSumLeftTillActive(), loan.getCapital());//ההלוואה קיבלה כסף ולכן הפלוס אף אנחנו מנפים את זה מהעתק של הסכום כדי לדעת מתי הגענו ל0
                    listOfDTOMovements.add(dtoMovement);
                    investAmountCounterThatGoingToBeInsertToTheCustomer += (loan.getCapital() - loan.getCapitalSumLeftTillActive());
                    loan.incrCapitalSumLeftTillActive((loan.getCapital() - loan.getCapitalSumLeftTillActive()));//מוריד העתק של הסכום אשר אומר מתי הלוואה היא פעילה
                    numberOfLoans = numberOfLoans - 1;//מחסיר את כמות ההלוואות אשר סגרתי הלוואה 1
                } else {
                    dtoInlayToBeAddToTheLoan = inlayBuild(customer, sumPerLoanToBeAdd, dtoInlay.getCategory(), dtoInlay.getMinInterestYaz(), dtoInlay.getMinYazTime());
                    chosenInvestAmount = chosenInvestAmount - sumPerLoanToBeAdd;//5000-1667
                    movementBuildToCustomer( customer, sumPerLoanToBeAdd, "-", customer.getAmount(), customer.getAmount() - sumPerLoanToBeAdd);
                    cashWithdrawal(customer, sumPerLoanToBeAdd);
                    dtoMovement = movementBuildToLoan(loan, sumPerLoanToBeAdd, "+", loan.getCapitalSumLeftTillActive(), loan.getCapitalSumLeftTillActive() + sumPerLoanToBeAdd);//ההלוואה קיבלה כסף ולכן הפלוס אף אנחנו מנפים את זה מהעתק של הסכום כדי לדעת מתי הגענו ל0
                    listOfDTOMovements.add(dtoMovement);
                    investAmountCounterThatGoingToBeInsertToTheCustomer += sumPerLoanToBeAdd;
                    loan.incrCapitalSumLeftTillActive(sumPerLoanToBeAdd);
                    numberOfLoans = numberOfLoans - 1;
                }
                //////////////////Documentation: Check if the client is not already on the list of loaners..................\\\\\\\\\\\\\\\\
                boolean customerAlreadyExistInListOfAccompanied = false;
                for (DTOCustomer account : loan.getListOfAccompanied()) {
                    if (account == (DTOAccount)customer) {
                        customerAlreadyExistInListOfAccompanied = true;
                        break;
                    }
                }
                if (!customerAlreadyExistInListOfAccompanied)
                    loan.getListOfAccompanied().add(customer);//מוסיף אותו כמלווה להלוואה

                loan.getListOfInlays().add(dtoInlayToBeAddToTheLoan);//מוסיף את השיבוץ החדש להלוואה עם הסכום שהוא הלווה
                if (loan.getStatusOperation().equals(DTOLoanStatus.NEW)) {
                    loan.setLoanStatus(DTOLoanStatus.PENDING);
                }
                if (loan.getCapitalSumLeftTillActive() == loan.getCapital()) {
                    loan.setLoanStatus(DTOLoanStatus.ACTIVE);
                    movementBuildToCustomer(getRealCustomerByName(loan.getOwner()), loan.getCapital(), "+", getRealCustomerByName(loan.getOwner()).getAmount(), getRealCustomerByName(loan.getOwner()).getAmount() + loan.getCapital());
                    (getRealCustomerByName(loan.getOwner())).cashDeposit(loan.getCapital());
                    loan.setStartedYazInActive(YazLogicDesktop.currentYazUnitProperty.getValue());
                    /*if(loan.getPaysEveryYaz()==1){
                        yazProgressLogic(false);
                    }*/
                }

            }
            ((Inlay)dtoInlay).setInvestAmount(investAmountCounterThatGoingToBeInsertToTheCustomer);
            customer.getInlays().add(dtoInlay);
        } else
            throw new RuntimeException("You didnt choose any loans.");

        return listOfDTOMovements;
    }

    @Override
    public Customer getRealCustomerByName(String customerName) {
        return (Customer) (accounts.stream().filter(c -> Objects.equals(c.getName(), customerName)).collect(Collectors.toList())).get(0);
    }


    @Override
    public void yazProgressLogic(boolean progressTheYaz) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        /*if(progressTheYaz)
           YazLogic.currentYazUnit++;*/
        boolean inRiskToActive;
        /////////////2.Payment should be paid by the loaner+sorted the list because of the logic of the app
        List<Loan> loansThatShouldPay = loans.stream().filter(l -> (l.numberOfYazTillNextPulse(YazLogic.currentYazUnit) == 0 && l.getLoanStatus() == DTOLoanStatus.ACTIVE) || l.getLoanStatus() == DTOLoanStatus.RISK).sorted(new Comparator<Loan>() {
            @Override
            public int compare(Loan l1, Loan l2) {
                if (l1.getStartedYazInActive() - l2.getStartedYazInActive() == 0 && l1.paymentPerPulse() - l2.paymentPerPulse() == 0)//if the yaz equals and the payment per pulse is equal so sort by the number of pulse
                    return l1.pulseNumber() - l2.pulseNumber();
                else if (l1.getStartedYazInActive() - l2.getStartedYazInActive() == 0) {//if the yaz equals so sort by the payment per pulse
                    return l1.paymentPerPulse() - l2.paymentPerPulse();
                }
                return l1.getStartedYazInActive() - l2.getStartedYazInActive();//else sort by the activation time of the loan
            }
        }).collect(Collectors.toList());
        for (Loan loan : loansThatShouldPay) {
            ///2.1 the loaner can't afford to pay the amount in the right Yaz and that's why the inRisk counter is elevating
            if (loan.paymentPerPulse() > getRealCustomerByName(loan.getOwner()).getAmount()) {
                loan.setLoanStatus(DTOLoanStatus.RISK);
                if (loan.numberOfYazTillNextPulse(YazLogic.currentYazUnit) == 0) {
                    loan.incrInRiskCounter();
                    loan.getListOfInRiskYazPayments().add(YazLogic.currentYazUnit);
                }
            }
            ///2.2 the loaner can afford to pay the amount in the right Yaz and calculate all the amount that should be spread
            else {
                inRiskToActive = operateThePaymentOfTheLoan(loan, false);
                if (inRiskToActive && loan.numberOfYazTillNextPulse(YazLogic.currentYazUnit) == 0) { //אם ההלוואה הפכה להיות פעילה אחרי שהיא הייתה בסכנה ובנוסף אם זה הזמן שלה לשלם אז אני אבצע תשלום נוסף
                    operateThePaymentOfTheLoan(loan, true);
                    if (loan.getInRiskCounter() == 1) {////ToDo
                        loansThatShouldPay.remove(loansThatShouldPay.stream().filter(l -> l.getId().equals(loan.getId())).collect(Collectors.toList()).get(0));//הוא ניסה לשלם עכשיו שהוא פעיל וזה הזמן תשלום שלו אבל הוא לא הצליח ולכן אין לי מה לרוץ עליו בפעם השניה
                    }
                }
            }
        }
        for (Loan loan : loansThatShouldPay) {/////run one more time on the loans if there is more money to the customer to close the risk loans that he have or just decrece the depth pulses
            if (loan.getLoanStatus().equals(DTOLoanStatus.RISK)) {
                inRiskToActive = operateThePaymentOfTheLoan(loan, false);
                if (inRiskToActive && loan.numberOfYazTillNextPulse(YazLogic.currentYazUnit) == 0)
                    operateThePaymentOfTheLoan(loan, true);
            }
        }
    }

    private boolean operateThePaymentOfTheLoan(Loan loan, boolean needToIncreaseTheInRiskCounter) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        boolean inRiskToActive = false;
        if (loan.paymentPerPulse() <= getRealCustomerByName(loan.getOwner()).getAmount()) {//בודק אם יש לי מספיק כסף בחשבון
            if (loan.getStatusOperation().equals(DTOLoanStatus.RISK)) {
                loan.decInRiskCounter();
                loan.getListOfInRiskYazPayments().remove(loan.getInRiskCounter());
                if (loan.getInRiskCounter() == 0) {
                    loan.setLoanStatus(DTOLoanStatus.ACTIVE);
                    inRiskToActive = true;
                }
            }
            movementBuildToCustomer(getRealCustomerByName(loan.getOwner()), loan.paymentPerPulse(), "-", getRealCustomerByName(loan.getOwner()).getAmount(), getRealCustomerByName(loan.getOwner()).getAmount() - loan.paymentPerPulse());
            cashWithdrawal(getRealCustomerByName(loan.getOwner()), loan.paymentPerPulse());
            loan.incrPulseCounterThatHappenedByOne();
            loan.getListOfYazPayments().add(YazLogic.currentYazUnit);
            for (DTOInlay dtoInlay : loan.getListOfInlays()) {
                movementBuildToCustomer((Customer)  this.getRealCustomerByName(dtoInlay.getName()), loan.calculatePaymentToLoanerConsole((Customer)  this.getRealCustomerByName(dtoInlay.getName())), "+",  this.getRealCustomerByName(dtoInlay.getName()).getAmount(),  this.getRealCustomerByName(dtoInlay.getName()).getAmount() + loan.calculatePaymentToLoanerConsole((Customer)  this.getRealCustomerByName(dtoInlay.getName())));
                cashDeposit((Customer)  this.getRealCustomerByName(dtoInlay.getName()), loan.calculatePaymentToLoanerConsole((Customer)  this.getRealCustomerByName(dtoInlay.getName())));
            }

            if (loan.getPulseCounterThatHappened() - loan.getInRiskCounter() == loan.pulseNumber()) {
                loan.setLoanStatus(DTOLoanStatus.FINISHED);
                loan.setEndedYaz(YazLogic.currentYazUnit);
            }
        } else if (needToIncreaseTheInRiskCounter) {
            loan.setLoanStatus(DTOLoanStatus.RISK);
            loan.incrInRiskCounter();
            loan.getListOfInRiskYazPayments().add(YazLogic.currentYazUnit);
        }

        return inRiskToActive;

    }//

    @Override
    public void operateThePaymentOfTheLoanDesktop(DTOLoan loan, int customerPayment) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Loan logicLoan = loans.stream().filter(l -> l.getId().equals(loan.getId())).collect(Collectors.toList()).get(0);
        if (customerPayment <= getRealCustomerByName(logicLoan.getOwner()).getAmount()) {//בודק אם יש לי מספיק כסף בחשבון
            movementBuildToCustomer(getRealCustomerByName(logicLoan.getOwner()), customerPayment, "-", getRealCustomerByName(logicLoan.getOwner()).getAmount(), getRealCustomerByName(logicLoan.getOwner()).getAmount() - customerPayment);
            cashWithdrawal(getRealCustomerByName(logicLoan.getOwner()), customerPayment);
            if (logicLoan.getStatusOperation() == DTOLoanStatus.RISK) {
                findThePaymentInfoByYazAndRemove(logicLoan.getPaymentsInfoList(), logicLoan.getListOfInRiskYazPayments(), customerPayment, logicLoan);
                if (logicLoan.getDebt() == 0|| (logicLoan.getPaysEveryYaz()==1 && logicLoan.getDebt()-logicLoan.paymentPerPulse()==0)) {
                    logicLoan.setLoanStatus(DTOLoanStatus.ACTIVE);
                    if(logicLoan.numberOfYazTillNextPulseWithoutTheIncOfWindowOfPaymentCounterDK(YazLogicDesktop.currentYazUnitProperty.intValue())==0)
                        logicLoan.setStringPropertyValue("Name : " + loan.getId() + "\n" + "Payment Yaz: " + YazLogicDesktop.currentYazUnitProperty.getValue() + "\n" + "Amount of required payment : " + loan.paymentPerPulse() +
                                "\n" + "Status : " + loan.getLoanStatus());
                }

            } else {
                logicLoan.setIsPaid(true);
                logicLoan.incrPulseCounterThatHappenedByOne();
            }
            buildPaymentInfoList(logicLoan, customerPayment, "Yes", YazLogicDesktop.currentYazUnitProperty.getValue());
            logicLoan.addTotalCapitalPayTillNow(customerPayment-customerPayment * logicLoan.getInterestPerPayment() / (100 + logicLoan.getInterestPerPayment()));
            logicLoan.addTotalInterestPayTillNow(customerPayment * logicLoan.getInterestPerPayment() / (100 + logicLoan.getInterestPerPayment()));
            for (DTOInlay dtoInlay : logicLoan.getListOfInlays()) {
                movementBuildToCustomer((Customer)  this.getRealCustomerByName(dtoInlay.getName()), logicLoan.calculatePaymentToLoaner((Customer)  this.getRealCustomerByName(dtoInlay.getName()), customerPayment), "+",  this.getRealCustomerByName(dtoInlay.getName()).getAmount(),  this.getRealCustomerByName(dtoInlay.getName()).getAmount() + logicLoan.calculatePaymentToLoaner((Customer)  this.getRealCustomerByName(dtoInlay.getName()), customerPayment));
                cashDeposit((Customer)  this.getRealCustomerByName(dtoInlay.getName()), logicLoan.calculatePaymentToLoaner((Customer)  this.getRealCustomerByName(dtoInlay.getName()), customerPayment));
            }

            if (logicLoan.getTotalCapitalPayTillNow() - logicLoan.getTotalCapitalPayTillEnd() == logicLoan.getTotalCapitalPayTillNow()) {
                logicLoan.setLoanStatus(DTOLoanStatus.FINISHED);
                logicLoan.setEndedYaz(YazLogicDesktop.currentYazUnitProperty.getValue());
            }
        }
    }

    private synchronized void findThePaymentInfoByYazAndRemove(List<DTOPaymentsInfo> paymentsInfoList, List<Integer> yaz, int customerPayment, Loan logicLoan) {
        int indexOfPaymentInfoList = 0;
        Integer indexOfYazList = 0;
        ArrayList<DTOPaymentsInfo> indexOfPaymentInfoListToRemove = new ArrayList<>();
        ArrayList<Integer> indexOfYazListToRemove = new ArrayList<>();
        for (DTOPaymentsInfo dtoPayments : paymentsInfoList) {
            if (Objects.equals(dtoPayments.getIsPaid(), "No")) {
                if (customerPayment - dtoPayments.getCapitalAndInterest() >= 0) {
                    customerPayment -= dtoPayments.getCapitalAndInterest();
                    indexOfPaymentInfoListToRemove.add(dtoPayments);
                    logicLoan.decInRiskCounter();
                    logicLoan.incrPulseCounterThatHappenedByOne();
                    logicLoan.decDebt(dtoPayments.getCapitalAndInterest());
                    indexOfYazListToRemove.add(indexOfYazList);
                } else {
                    int newInterest = paymentsInfoList.get(indexOfPaymentInfoList).getInterestAmount() - (customerPayment * logicLoan.getInterestPerPayment() / (100 + logicLoan.getInterestPerPayment()));
                    int newCapital = paymentsInfoList.get(indexOfPaymentInfoList).getCapitalAmount() - (customerPayment - customerPayment * logicLoan.getInterestPerPayment() / (100 + logicLoan.getInterestPerPayment()));
                    ((PaymentsInfo) paymentsInfoList.get(indexOfPaymentInfoList)).setInterestAmount(newInterest);
                    ((PaymentsInfo) paymentsInfoList.get(indexOfPaymentInfoList)).setCapitalAmount(newCapital);
                    ((PaymentsInfo) paymentsInfoList.get(indexOfPaymentInfoList)).setCapitalAndInterest(newCapital + newInterest);
                    logicLoan.decDebt(customerPayment);
                    break;
                }
                if (indexOfYazList >= yaz.size()) {
                    removeAllIndexesFromPaymentsInfoList(indexOfPaymentInfoListToRemove, (ArrayList<DTOPaymentsInfo>) paymentsInfoList);
                    removeAllIndexesFromListOfInRiskYazPayments(indexOfYazListToRemove, (ArrayList<Integer>) logicLoan.getListOfInRiskYazPayments());
                    break;
                }
                indexOfYazList++;

            }
            indexOfPaymentInfoList++;
        }
        removeAllIndexesFromPaymentsInfoList(indexOfPaymentInfoListToRemove, (ArrayList<DTOPaymentsInfo>) paymentsInfoList);
        removeAllIndexesFromListOfInRiskYazPayments(indexOfYazListToRemove, (ArrayList<Integer>) logicLoan.getListOfInRiskYazPayments());
    }

    private void removeAllIndexesFromPaymentsInfoList(ArrayList<DTOPaymentsInfo> indexesListToRemove, ArrayList<DTOPaymentsInfo> paymentsInfoList) {
        paymentsInfoList.removeAll(indexesListToRemove);
    }

    private void removeAllIndexesFromListOfInRiskYazPayments(ArrayList<Integer> indexesListToRemove, ArrayList<Integer> listOfInRiskYazPayments) {
        listOfInRiskYazPayments.removeAll(indexesListToRemove);
    }

    private void buildPaymentInfoList(Loan logicLoan, int customerPayment, String isPaid, int Yaz) {
        logicLoan.getPaymentsInfoList().add(PaymentsInfo.build()
                .setYazPayment(Yaz).
                setCapitalAmount(customerPayment - customerPayment * logicLoan.getInterestPerPayment() / (100 + logicLoan.getInterestPerPayment())).
                setInterestAmount(customerPayment * logicLoan.getInterestPerPayment() / (100 + logicLoan.getInterestPerPayment())).
                setIsPaid(isPaid).
                setCapitalAndInterest(customerPayment - customerPayment * logicLoan.getInterestPerPayment() / (100 + logicLoan.getInterestPerPayment()) + customerPayment * logicLoan.getInterestPerPayment() / (100 + logicLoan.getInterestPerPayment())));
    }


     @Override//ToDo : need to check...Does it works?
    public ArrayList<DTOLoan> yazProgressLogicDesktop() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        /////////////2.Payment should be paid by the loaner+sorted the list because of the logic of the app
        List<Loan> loansThatShouldPay = loans.stream().filter(l -> ( l.numberOfYazTillNextPulseDK(YazLogicDesktop.currentYazUnitProperty.intValue()) == 0 && l.getLoanStatus() == DTOLoanStatus.ACTIVE ) || l.getLoanStatus() == DTOLoanStatus.ACTIVE || l.getLoanStatus() == DTOLoanStatus.RISK).sorted(new Comparator<Loan>() {
            @Override
            public int compare(Loan l1, Loan l2) {
                if (l1.getStartedYazInActive() - l2.getStartedYazInActive() == 0 && l1.paymentPerPulse() - l2.paymentPerPulse() == 0)//if the yaz equals and the payment per pulse is equal so sort by the number of pulse
                    return l1.pulseNumber() - l2.pulseNumber();
                else if (l1.getStartedYazInActive() - l2.getStartedYazInActive() == 0) {//if the yaz equals so sort by the payment per pulse
                    return l1.paymentPerPulse() - l2.paymentPerPulse();
                }
                return l1.getStartedYazInActive() - l2.getStartedYazInActive();//else sort by the activation time of the loan
            }
        }).collect(Collectors.toList());
        for (Loan loan : loansThatShouldPay) {
            if (loan.getPulseCounterThatHappened() < loan.getWindowOfPaymentCounter() - 1) {//ToDO!!!!!Check with Eden
                if (loan.numberOfYazTillNextPulseWithoutTheIncOfWindowOfPaymentCounterDK(YazLogicDesktop.currentYazUnitProperty.intValue()) != 0 || (loan.getPaysEveryYaz() == 1)) {
                    loan.setLoanStatus(DTOLoanStatus.RISK);
                    if ((loan.getPaysEveryYaz() == 1)) {
                        if (loan.getPulseCounterThatHappened() < loan.getWindowOfPaymentCounter() - 2)
                            loan.setLoanStatus(DTOLoanStatus.RISK);
                        else
                            loan.setLoanStatus(DTOLoanStatus.ACTIVE);
                    }
                    if (loan.numberOfYazTillNextPulseWithoutTheIncOfWindowOfPaymentCounterDK(YazLogicDesktop.currentYazUnitProperty.intValue()) - 1 <= 0) {
                        loan.getListOfInRiskYazPayments().add(YazLogicDesktop.currentYazUnitProperty.getValue() - 1);
                        if(loan.getPaysEveryYaz()==1 && loan.getLoanStatus()==DTOLoanStatus.ACTIVE)
                            loan.setStringPropertyValue("Name : " + loan.getId() + "\n" + "Payment Yaz: " + YazLogicDesktop.currentYazUnitProperty.getValue() + "\n" + "Amount of required payment : " + loan.paymentPerPulse() +
                                    "\n" + "Status : " + loan.getLoanStatus());
                        else
                            loan.setStringPropertyValue("Name : " + loan.getId() + "\n" + "Payment Yaz that didnt happened : " + (YazLogicDesktop.currentYazUnitProperty.getValue() - 1) + "\n" + "Amount of required payment : " + loan.paymentPerPulse() +
                                    "\n" + "Status : " + loan.getLoanStatus());
                        if (loan.getTotalNonePaidPayments() < loan.getTotalCapitalPayTillEnd() && loan.getLoanStatus() == DTOLoanStatus.RISK) {
                            buildPaymentInfoList(loan, loan.paymentPerPulse(), "No", YazLogicDesktop.currentYazUnitProperty.getValue() - 1);
                            loan.addDebt(loan.paymentPerPulse());
                            loan.incrInRiskCounter();
                        }
                    }
                } else if (loan.numberOfYazTillNextPulseWithoutTheIncOfWindowOfPaymentCounterDK(YazLogicDesktop.currentYazUnitProperty.intValue()) == 0) {
                    loan.setStringPropertyValue("Name : " + loan.getId() + "\n" + "Payment Yaz: " + YazLogicDesktop.currentYazUnitProperty.getValue() + "\n" + "Amount of required payment : " + loan.paymentPerPulse() +
                            "\n" + "Status : " + loan.getLoanStatus());
                }
            } else if (loan.numberOfYazTillNextPulseWithoutTheIncOfWindowOfPaymentCounterDK(YazLogicDesktop.currentYazUnitProperty.intValue()) == 0) {
                loan.setLoanStatus(DTOLoanStatus.ACTIVE);
                loan.setStringPropertyValue("Name : " + loan.getId() + "\n" + "Payment Yaz: " + YazLogicDesktop.currentYazUnitProperty.getValue() + "\n" + "Amount of required payment : " + loan.paymentPerPulse() +
                        "\n" + "Status : " + loan.getLoanStatus());
            }
        }
        ArrayList<DTOLoan> DTOLoansThatShouldPay = new ArrayList<>();
        loansThatShouldPay.forEach(l -> DTOLoansThatShouldPay.add(DTOLoan.build(l)));

        return DTOLoansThatShouldPay;
    }

        @Override
        public void myAddListenerToStringPropertyLoans (ListView < String > listener, DTOLoan dtoLoan){
            this.loans.stream().filter(l -> Objects.equals(l.getId(), dtoLoan.getId())).forEach(l -> l.myAddListenerToStringProperty(listener));
        }
}



