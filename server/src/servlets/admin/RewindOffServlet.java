package servlets.admin;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.UIInterfaceLogic;
import logic.YazLogicDesktop;
import logic.bank.Bank;
import logic.bank.bankLogicYazHistory.BankLogicBankHistoryList;
import logic.bank.bankLogicYazHistory.BankLogicYazHistory;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;

import static constants.Constants.YAZ;

@WebServlet(name = "RewindOffAdminServlet",urlPatterns = "/admin/rewindOff")
public class RewindOffServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out=response.getWriter();
        UIInterfaceLogic bankManager = ServletUtils.getBank(getServletContext());
        BankLogicBankHistoryList bankLogicBankHistoryList=ServletUtils.getBankLogicYazHistoryList(getServletContext());
        getServletContext().setAttribute("bank",bankLogicBankHistoryList.getLastBankLogicYazHistory());

    }
}

