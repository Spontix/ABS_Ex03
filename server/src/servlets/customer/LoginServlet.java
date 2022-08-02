package servlets.customer;

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
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import static constants.Constants.USERNAME;

@WebServlet(name = "CustomerLoginCustomerServlet",urlPatterns = "/customer/login")
public class LoginServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out=response.getWriter();
        String customerFromSession = SessionUtils.getCustomer(request);
        ArrayList<DTOCustomer> customersManager = ServletUtils.getCustomers(getServletContext());
        UIInterfaceLogic bankManager = ServletUtils.getBank(getServletContext());

        if (customerFromSession == null) {
            String usernameFromParameter = request.getParameter(USERNAME);
            if (usernameFromParameter == null || usernameFromParameter.isEmpty() ) {
                out.println("Please enter a username...");
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            }
            else if(usernameFromParameter.equals("Admin") || usernameFromParameter.equals("admin")){
                out.println("You are not an admin!");
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            }
            else {
                usernameFromParameter = usernameFromParameter.trim();
                synchronized (this) {
                    String finalUsernameFromParameter = usernameFromParameter;
                    if (customersManager.stream().anyMatch(c -> c.getName().equals(finalUsernameFromParameter))) {
                        String errorMessage = "Username " + usernameFromParameter + " already exists. Please enter a different username.";

                        // stands for unauthorized as there is already such user with this name
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        out.println(errorMessage);
                    } else {
                        DTOCustomer dtoCustomer = DTOCustomer.builderServer(finalUsernameFromParameter);

                        //add the new customer by sending a DTOCustomer to the users list
                        try {
                            bankManager.addCustomer(dtoCustomer);
                        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        //set the username in a session so it will be available on each request
                        //the true parameter means that if a session object does not exists yet
                        //create a new one
                        request.getSession(true).setAttribute(USERNAME, usernameFromParameter);

                        //redirect the request to the chat room - in order to actually change the URL
                        out.println("Welcome " + usernameFromParameter+" to the bank app!");
                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                }
            }
        } else {
            //user is already logged in
            out.println("The user name is already in use!");
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
    }
}


