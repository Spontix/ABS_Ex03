package servlets.customer;

import dataObjects.dtoBank.dtoAccount.DTOLoan;
import dataObjects.dtoBank.dtoAccount.DTOLoansList;
import dataObjects.dtoBank.dtoAccount.DTOMovement;
import dataObjects.dtoBank.dtoAccount.DTOMovementsList;
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


@WebServlet(name = "Movements",urlPatterns = "/customer/movements")
public class Movements extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String customerFromSession = SessionUtils.getCustomer(request);
        String usernameFromParameter = request.getParameter(USERNAME);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        UIInterfaceLogic bank = ServletUtils.getBank(getServletContext());
        DTOMovementsList dtoMovementsList=new DTOMovementsList();
        dtoMovementsList.setDTOMovements((ArrayList<DTOMovement>) bank.getCustomerByName(usernameFromParameter.equals("")?customerFromSession:usernameFromParameter).getMovements());
        String json = GSON_INSTANCE.toJson(dtoMovementsList);
        out.println(json);
        out.flush();
    }
}
