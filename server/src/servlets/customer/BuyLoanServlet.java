package servlets.customer;

import dataObjects.dtoBank.dtoAccount.*;
import dataObjects.dtoCustomer.DTOCustomer;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.UIInterfaceLogic;
import logic.bank.account.Inlay;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static constants.Constants.LOAN;
import static constants.Constants.USERNAME;
import static utils.ServletUtils.GSON_INSTANCE;


@WebServlet(name = "BuyLoanCustomerServlet",urlPatterns = "/customer/buy-loan")
public class BuyLoanServlet  extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UIInterfaceLogic bankManager = ServletUtils.getBank(getServletContext());
        String customerFromSession = SessionUtils.getCustomer(request);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        BufferedReader rd = request.getReader();
        String line = null;
        StringBuilder rawBody = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            rawBody.append(line);
        }
        synchronized (this) {
            DTOLoanForSale dtoLoanForSale = GSON_INSTANCE.fromJson(rawBody.toString(), DTOLoanForSale.class);
            DTOLoanForSale realLoanForSale=bankManager.getOriginalLoanForSale(dtoLoanForSale);

            try {
                if (realLoanForSale != null) {
                    //DTOLoanSale localLoanSale = bank.getListInformationLoansSales().stream().filter(l -> Objects.equals(l.getLoan(), localLoan.getLoan().getId())).collect(Collectors.toList()).get(0);
                    if (Objects.equals(realLoanForSale.getNameOfBorrower(), customerFromSession)) {
                        response.setStatus(HttpServletResponse.SC_CONFLICT);
                        out.println("Operation failed!\nThe loan already belongs to you, you can not buy your loan.");
                    }
                    else if (Objects.equals(realLoanForSale.getLoanForSale().getOwner(), customerFromSession)) {
                        response.setStatus(HttpServletResponse.SC_CONFLICT);
                        out.println("Operation failed!\nYou cannot buy a lender's share of your loan.");
                    }
                    else if (bankManager.getCustomerByName(customerFromSession).getAmount() <= realLoanForSale.getBalanceOfTheFund()) {
                        response.setStatus(HttpServletResponse.SC_CONFLICT);
                        out.println("Operation failed!\nYou do not have enough money in your account for this purchase.");
                    }
                    else {
                        int sum=0;
                         List<DTOInlay> inlays = bankManager.getLoansList().stream().filter(l -> Objects.equals(l.getOwner(), realLoanForSale.getLoanForSale().getOwner())).collect(Collectors.toList()).get(0).getListOfInlays().stream().filter(i -> Objects.equals(i.getName(), realLoanForSale.getNameOfBorrower())).collect(Collectors.toList());
                        for (DTOInlay dtoInlay:inlays) {
                            sum+=dtoInlay.getAmountLeftRemainingRepayLoan();
                        }
                        // DTOInlay inlay = bankManager.getLoansList().stream().filter(l -> Objects.equals(l.getOwner(), realLoanForSale.getLoanForSale().getOwner())).collect(Collectors.toList()).get(0).getListOfInlays().stream().filter(i -> Objects.equals(i.getName(), realLoanForSale.getNameOfBorrower())).collect(Collectors.toList()).get(0);
                        bankManager.removeLoanFromListOfAccompanied(realLoanForSale.getLoanForSale(), realLoanForSale.getNameOfBorrower());
                        bankManager.removeLoanFromLoansSales(realLoanForSale, realLoanForSale.getLoanForSale());
                        bankManager.addLoanToListOfAccompanied(realLoanForSale.getLoanForSale(), bankManager.getCustomerByName(customerFromSession));
                        bankManager.removeInlayFromListOfInlays(realLoanForSale.getLoanForSale(), realLoanForSale.getNameOfBorrower());
                        //DTOInlay dtoInlay = DTOInlay.build(inlay);
                        DTOInlay dtoInlay = bankManager.inlayBuildForDK(bankManager.getCustomerByName(customerFromSession), sum, inlays.get(0).getCategory(), inlays.get(0).getMinInterestYaz(), inlays.get(0).getMinYazTime(), inlays.get(0).getMaximumLoansOpenToTheBorrower(),sum);
                        bankManager.addInlayToClientByCustomerName(realLoanForSale.getLoanForSale(), dtoInlay);
                        bankManager.removeInlaysFromOldLenderAndAddToNewLender(realLoanForSale.getNameOfBorrower(),customerFromSession,inlays,dtoInlay);

                        DTOMovement dtoMovement = bankManager.movementBuildToCustomer(bankManager.getCustomerByName(customerFromSession), realLoanForSale.getBalanceOfTheFund(), "-", bankManager.getCustomerByName(customerFromSession).getAmount(), bankManager.getCustomerByName(customerFromSession).getAmount() - realLoanForSale.getBalanceOfTheFund());
                        bankManager.cashWithdrawal(bankManager.getRealCustomerByName(customerFromSession), realLoanForSale.getBalanceOfTheFund());
                        DTOMovement dtoMovement1 = bankManager.movementBuildToCustomer(bankManager.getRealCustomerByName(realLoanForSale.getNameOfBorrower()), realLoanForSale.getBalanceOfTheFund(), "+", bankManager.getRealCustomerByName(realLoanForSale.getNameOfBorrower()).getAmount(), bankManager.getRealCustomerByName(realLoanForSale.getNameOfBorrower()).getAmount() + realLoanForSale.getBalanceOfTheFund());
                        bankManager.cashDeposit(bankManager.getRealCustomerByName(realLoanForSale.getNameOfBorrower()), realLoanForSale.getBalanceOfTheFund());
                        out.println("The buy operation was preformed!");
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    out.println("Operation failed!\nYou have not selected the loan you are interested in buying.");
                }
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException ex) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                out.println(ex.toString());
            }
        }
    }
}
