package servlets.customer;

import dataObjects.dtoBank.dtoAccount.DTOCustomersList;
import dataObjects.dtoBank.dtoAccount.DTOLoan;
import dataObjects.dtoBank.dtoAccount.DTOLoansList;
import dataObjects.dtoBank.dtoAccount.DTOPaymentsPerYaz;
import dataObjects.dtoCustomer.DTOCustomer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import logic.UIInterfaceLogic;
import logic.YazLogicDesktop;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static utils.ServletUtils.GSON_INSTANCE;


@WebServlet(name = "LoansThatShouldBePaidCustomerServlet",urlPatterns = "/customer/loans-to-pay")
public class PaymentsInfoServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json");
        String customerFromSession = SessionUtils.getCustomer(request);
        PrintWriter out = response.getWriter();
        DTOLoansList dtoLoansList = new DTOLoansList();
        UIInterfaceLogic bankManager=ServletUtils.getBank(getServletContext());
        DTOPaymentsPerYaz dtoPaymentsPerYaz=bankManager.getPaymentsPerYaz(YazLogicDesktop.currentYazUnitProperty.getValue());
        ArrayList<DTOLoan> dtoLoans=new ArrayList<>();
        if(dtoPaymentsPerYaz!=null){
           dtoLoans=dtoPaymentsPerYaz.getDtoLoanListByCustomer(customerFromSession);
        }
        dtoLoansList.setDTOLoans(dtoLoans);
        String json = GSON_INSTANCE.toJson(dtoLoansList);
        out.println(json);
        out.flush();

    }
}
