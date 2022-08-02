package servlets.customer.scramble;

import dataObjects.dtoBank.dtoAccount.DTOInlay;
import dataObjects.dtoBank.dtoAccount.DTOLoan;
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

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static utils.ServletUtils.GSON_INSTANCE;


@WebServlet(name = "ScrambleSelectedLoansCustomerServlet",urlPatterns = "/customer/scramble/selectedLoans")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class ScrambleSelectedLoansServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UIInterfaceLogic bankManager = ServletUtils.getBank(getServletContext());
        List<DTOInlay> dtoInlayList=ServletUtils.getInlays(getServletContext());
        String customerFromSession = SessionUtils.getCustomer(request);
        DTOInlay dtoInlay=dtoInlayList.stream().filter(i-> Objects.equals(i.getName(), customerFromSession)).collect(Collectors.toList()).get(0);
        PrintWriter out = response.getWriter();
        response.setContentType("text/plain;charset=UTF-8");
        Collection<Part> Parts = request.getParts();
        List<DTOLoan> listOfDTOLoans=new ArrayList<>();
       for (Part part:Parts){
           String partValue=new BufferedReader(new InputStreamReader(part.getInputStream())).readLine();
           listOfDTOLoans.add(bankManager.getLoanById(partValue));
       }
       synchronized (this) {
           try {
               bankManager.addMovementPerLoanFromInlayDK(dtoInlay, listOfDTOLoans, dtoInlay.getInvestAmount(), dtoInlay.getMaximumLoanOwnershipPercentage());
               bankManager.yazProgressLogicDesktop(null);
               dtoInlayList.remove(dtoInlay);
           } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
               e.printStackTrace();
           }
       }
    }
}
