package servlets.customer;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.UIInterfaceLogic;
import logic.YazLogicDesktop;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;


@WebServlet(name = "YazUnitProgressCustomerServlet",urlPatterns = "/customer/yazUnitProgress")
public class YazUnitServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        UIInterfaceLogic bankManager = ServletUtils.getBank(getServletContext());
        //TodO for test
        out.println(YazLogicDesktop.currentYazUnitProperty.getValue().intValue());
        out.flush();
    }
}
