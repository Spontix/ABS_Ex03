package servlets.customer;

import dataObjects.dtoBank.dtoAccount.*;
import dataObjects.dtoCustomer.DTOCustomer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import logic.UIInterfaceLogic;
import logic.bank.account.Loan;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static constants.Constants.LOAN;
import static constants.Constants.USERNAME;
import static utils.ServletUtils.GSON_INSTANCE;


@WebServlet(name = "SellLoanCustomerServlet",urlPatterns = "/customer/sell-loan")
public class SellLoanServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String customerFromSession = SessionUtils.getCustomer(request);
        ArrayList<DTOCustomer> customersManager = ServletUtils.getCustomers(getServletContext());
        UIInterfaceLogic bankManager = ServletUtils.getBank(getServletContext());
        DTOLoansForSaleList dtoLoansForSaleList = new DTOLoansForSaleList();
        String loanFromParameter = request.getParameter(LOAN);
        if (loanFromParameter.equals("")) {
            response.setContentType("application/json");
            dtoLoansForSaleList.setDTOLoansForSale(bankManager.getListLoansSales());
            String json = GSON_INSTANCE.toJson(dtoLoansForSaleList);
            //LoansForSalesListView.getItems().addAll(localLoan);
            out.println(json);
            out.flush();
        } else {
            try {
                DTOLoan localLoan = bankManager.getLoanById(loanFromParameter);
                if (localLoan != null) {
                    if (localLoan.getLoanStatus() != DTOLoanStatus.ACTIVE) {
                        response.setStatus(HttpServletResponse.SC_CONFLICT);
                        out.println("Operation failed!\nLoan status is not ACTIVE");
                    }
                    else if ((!bankManager.getListLoansSales().isEmpty()) && (Objects.equals(bankManager.getListLoansSales().stream().filter(l -> Objects.equals(l.getLoanForSale().getId(), localLoan.getId())).collect(Collectors.toList()).get(0).getNameOfBorrower(), customerFromSession))) {
                        // !bank.getLoansSales().isEmpty()&&(!(bank.getLoansSales().stream().filter(l -> Objects.equals(l.getNameOfBorrower(), dtoCustomer.getCustomerName())).collect(Collectors.toList()).stream().filter(la -> Objects.equals(la.getLoanForSale().getId(), localLoan.getId()))).collect(Collectors.toList()).isEmpty()))
                        response.setStatus(HttpServletResponse.SC_CONFLICT);
                        out.println("Operation failed!\nThis loan is already for sale");
                    }
                    else {
                        int amountToBuyTheLoan=0;
                        List<DTOInlay> inlays = (localLoan.getListOfInlays().stream().filter(l -> Objects.equals(l.getName(), customerFromSession)).collect(Collectors.toList()));
                        for (DTOInlay dtoInlay:inlays) {
                            amountToBuyTheLoan+=dtoInlay.getAmountLeftRemainingRepayLoan();
                        }
                        //int amountToBuyTheLoan = (localLoan.getListOfInlays().stream().filter(l -> Objects.equals(l.getName(), customerFromSession)).collect(Collectors.toList()).get(0).getAmountLeftRemainingRepayLoan());
                        bankManager.loanForSaleBuild(customerFromSession, amountToBuyTheLoan, localLoan);///ToDo

                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    out.println("Operation failed!\nYour lender loans table list is empty.");
                }
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
    }
}
