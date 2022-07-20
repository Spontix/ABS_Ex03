package servlets.admin;

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

@WebServlet(name = "AdminLoginAdminServlet",urlPatterns = "/admin/login")
public class LoginServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out=response.getWriter();
        String adminFromSession = SessionUtils.getCustomer(request);
        if (adminFromSession == null) {
            String usernameFromParameter = request.getParameter(USERNAME);
            if (usernameFromParameter == null || usernameFromParameter.isEmpty() || (!usernameFromParameter.equals("Admin") && !usernameFromParameter.equals("admin"))) {
                out.println("Incorrect username,you are not the admin/Admin");
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            } else {
                usernameFromParameter = usernameFromParameter.trim();
                synchronized (this) {
                    if (ServletUtils.isAdminLoggedIn(getServletContext())) {
                        String errorMessage = "Admin is already logged-in!";

                        // stands for unauthorized as there is already such user with this name
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        out.println(errorMessage);
                    } else {
                        out.println("Welcome " + "Admin"+" to the bank app!");
                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                }
            }
        } else {
            //user is already logged in
            out.println("The Admin name is already in use!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

}
