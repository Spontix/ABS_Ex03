package servlets.customer;

import dataObjects.dtoBank.dtoAccount.DTOMovement;
import dataObjects.dtoBank.dtoAccount.DTOMovementsList;
import dataObjects.dtoCustomer.DTOCustomer;
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

import static constants.Constants.USERNAME;
import static utils.ServletUtils.GSON_INSTANCE;


@WebServlet(name = "getCustomerServlet",urlPatterns = "/customer/get-customer")
public class CustomerServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String customerFromSession = SessionUtils.getCustomer(request);
        String usernameFromParameter = request.getParameter(USERNAME);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        UIInterfaceLogic bank = ServletUtils.getBank(getServletContext());
        DTOCustomer dtoCustomer=bank.getCustomerByName(customerFromSession);
        dtoCustomer.setRewind(bank.getRewind());
        String json = GSON_INSTANCE.toJson(dtoCustomer);
        out.println(json);
        out.flush();
    }
}
