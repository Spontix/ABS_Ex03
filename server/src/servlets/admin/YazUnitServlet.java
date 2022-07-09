package servlets.admin;

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

import static utils.ServletUtils.GSON_INSTANCE;


@WebServlet(name = "YazUnitProgressAdminServlet",urlPatterns = "/admin/yazUnitProgress")
public class YazUnitServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        UIInterfaceLogic bankManager = ServletUtils.getBank(getServletContext());

        synchronized (this) {
            YazLogicDesktop.currentYazUnitProperty.set(YazLogicDesktop.currentYazUnitProperty.get() + 1);
            try {
                bankManager.yazProgressLogicDesktop();
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            //TodO for test
            String json = GSON_INSTANCE.toJson(bankManager.getPaymentsPerYaz(YazLogicDesktop.currentYazUnitProperty.getValue()));
            out.println(json);
            out.flush();
        }
    }
}
