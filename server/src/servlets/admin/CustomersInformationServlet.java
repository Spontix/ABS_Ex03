package servlets.admin;

import com.google.gson.Gson;
import dataObjects.dtoBank.dtoAccount.DTOCustomersList;
import dataObjects.dtoBank.dtoAccount.DTOInlay;
import dataObjects.dtoCustomer.DTOCustomer;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.UIInterfaceLogic;
import logic.bank.Bank;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static utils.ServletUtils.GSON_INSTANCE;

@WebServlet(name = "CustomersInformationAdminServlet",urlPatterns = "/admin/customersInformation")
public class CustomersInformationServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        ArrayList<DTOCustomer> dtoCustomers=ServletUtils.getCustomers(getServletContext());
        DTOCustomersList dtoCustomersList=new DTOCustomersList();
        dtoCustomersList.setDTOCustomers(dtoCustomers);
        String json = GSON_INSTANCE.toJson(dtoCustomersList);
        out.println(json);
        out.flush();
    }
}

