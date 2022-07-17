package servlets.customer;

import dataObjects.dtoBank.dtoAccount.DTOMovement;
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

import static constants.Constants.AMOUNT;
import static utils.ServletUtils.GSON_INSTANCE;

@WebServlet(name = "WithdrawCustomerServlet",urlPatterns = "/customer/withdraw")
public class WithdrawServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String customerFromSession = SessionUtils.getCustomer(request);
        ArrayList<DTOCustomer> customersManager = ServletUtils.getCustomers(getServletContext());
        UIInterfaceLogic bankManager = ServletUtils.getBank(getServletContext());

        if (customerFromSession != null) {
            try {
                String sumToDrawFromParameter = request.getParameter(AMOUNT);
                DTOCustomer dtoCustomer = DTOCustomer.builderServer(customerFromSession);
                int sumToDraw=getAmountFromUser(sumToDrawFromParameter);
                DTOMovement dtoMovement=bankManager.movementBuildToCustomer(dtoCustomer,sumToDraw, "-");
                bankManager.cashWithdrawal(dtoCustomer,sumToDraw);
                response.setContentType("application/json");
                String json = GSON_INSTANCE.toJson(dtoMovement);
                out.println(json);
                out.flush();
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (NumberFormatException exception) {
                out.println("Incorrect input,please note that you entered an integer number!!\n");
            } catch (RuntimeException exception) {
                out.println(exception.getMessage());
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        else {
            out.println("There is not a username with this name in the system!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    public int getAmountFromUser(String amount) {
        int amountFromUser = Integer.parseInt(amount);
        if (amountFromUser < 0)
            throw new NumberFormatException();
        return amountFromUser;
    }
}
