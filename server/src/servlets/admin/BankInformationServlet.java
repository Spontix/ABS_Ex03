package servlets.admin;

import dataObjects.dtoBank.DTOBank;
import dataObjects.dtoBank.dtoAccount.DTOCustomersList;
import dataObjects.dtoCustomer.DTOCustomer;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import static utils.ServletUtils.GSON_INSTANCE;


@WebServlet(name = "BankInformationAdminServlet",urlPatterns = "/admin/bankInformation")
public class BankInformationServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        synchronized (this) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            String json = GSON_INSTANCE.toJson(ServletUtils.getBank(getServletContext()));
            out.println(json);
            out.flush();
        }
    }
}
