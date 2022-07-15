package servlets.admin;

import dataObjects.dtoBank.dtoAccount.DTOLoansList;
import dataObjects.dtoCustomer.DTOCustomer;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.UIInterfaceLogic;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static utils.ServletUtils.GSON_INSTANCE;


@WebServlet(name = "LoansInformationAdminServlet",urlPatterns = "/admin/loansInformation")
public class LoansInformationServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        UIInterfaceLogic bank = ServletUtils.getBank(getServletContext());
        DTOLoansList dtoLoansList=new DTOLoansList();
        dtoLoansList.setDTOLoans(bank.getLoansList());
        String json = GSON_INSTANCE.toJson(dtoLoansList);
        out.println(json);
        out.flush();
    }
}
