package servlets.customer;

import dataObjects.dtoBank.dtoAccount.DTOLoan;
import dataObjects.dtoBank.dtoAccount.DTOLoansList;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.UIInterfaceLogic;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import static utils.ServletUtils.GSON_INSTANCE;

@WebServlet(name = "LoansAsBorrowerCustomerServlet",urlPatterns = "/customer/getLoansAsBorrower")

public class LoansAsBorrower extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String customerFromSession = SessionUtils.getCustomer(request);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        UIInterfaceLogic bank = ServletUtils.getBank(getServletContext());
        DTOLoansList dtoLoansList=new DTOLoansList();
        dtoLoansList.setDTOLoans((ArrayList<DTOLoan>) bank.getCustomerByName(customerFromSession).getBorrower());
        String json = GSON_INSTANCE.toJson(dtoLoansList);
        out.println(json);
        out.flush();
    }
}
