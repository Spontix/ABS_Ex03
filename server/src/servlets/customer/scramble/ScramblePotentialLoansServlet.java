package servlets.customer.scramble;

import dataObjects.dtoBank.dtoAccount.DTOInlay;
import dataObjects.dtoBank.dtoAccount.DTOLoan;
import dataObjects.dtoBank.dtoAccount.DTOLoansList;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.UIInterfaceLogic;
import logic.bank.account.Inlay;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static utils.ServletUtils.GSON_INSTANCE;


@WebServlet(name = " ScramblePotentialLoansCustomerServlet",urlPatterns = "/customer/scramble/potentialLoans")
public class ScramblePotentialLoansServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UIInterfaceLogic bankManager = ServletUtils.getBank(getServletContext());
        List<DTOInlay> dtoInlayList=ServletUtils.getInlays(getServletContext());
        String customerFromSession = SessionUtils.getCustomer(request);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        BufferedReader rd = request.getReader();
        String line = null;
        StringBuilder rawBody = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            rawBody.append(line);
        }
        synchronized (this) {
            DTOInlay dtoInlay = GSON_INSTANCE.fromJson(rawBody.toString(), DTOInlay.class);
            Inlay inlay = null;
            try {
                inlay = bankManager.inlayBuild(bankManager.getCustomerByName(customerFromSession), dtoInlay.getInvestAmount(), dtoInlay.getCategory(), dtoInlay.getMinInterestYaz(), dtoInlay.getMinYazTime());
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            dtoInlayList.add(inlay);
            DTOLoansList potentialLoans = new DTOLoansList();
            potentialLoans.setDTOLoans(bankManager.loansSustainInlayDK(dtoInlay));
            String json = GSON_INSTANCE.toJson(potentialLoans);
            out.println(json);
            out.flush();
        }
    }
}

