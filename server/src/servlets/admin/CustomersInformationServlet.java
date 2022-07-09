package servlets.admin;

import com.google.gson.Gson;
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
        List<DTOCustomer> dtoCustomersList=ServletUtils.getCustomers(getServletContext());
        UIInterfaceLogic bank = ServletUtils.getBank(getServletContext());
        //ArrayList<DTOCustomer> customersList = bank.getCustomers();
        String json = GSON_INSTANCE.toJson(bank);
        //String json = GSON_INSTANCE.toJson(new DTOInlay());
        out.println(json);
        out.flush();
    }
}

