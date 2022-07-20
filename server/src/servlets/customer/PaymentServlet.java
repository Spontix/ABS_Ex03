package servlets.customer;

import dataObjects.dtoCustomer.DTOCustomer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import logic.UIInterfaceLogic;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Objects;

import static constants.Constants.LOAN;


@WebServlet(name = "LoanPaymentCustomerServlet",urlPatterns = "/customer/loanPayment")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class PaymentServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {//loans reciver
        UIInterfaceLogic bankManager = ServletUtils.getBank(getServletContext());
        Collection<Part> Parts = request.getParts();
        PrintWriter out = response.getWriter();
        String partValue = null;
        String customerAmount = "";
        for (Part part : Parts) {
            if (Objects.equals(part.getName(), LOAN))
                partValue = new BufferedReader(new InputStreamReader(part.getInputStream())).readLine();
            else
                customerAmount = new BufferedReader(new InputStreamReader(part.getInputStream())).readLine();
        }
        synchronized (this) {
            try {
                bankManager.operateThePaymentOfTheLoanDesktop(bankManager.getLoanById(partValue), Integer.parseInt(customerAmount));
                out.println("The payment paid!");
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            } catch (NumberFormatException exception) {
                out.println("Incorrect input,please note that you entered an integer number!!\n");
            }
        }
    }
}
