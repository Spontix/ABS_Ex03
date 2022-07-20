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
import logic.bank.Bank;
import logic.bank.XmlSerialization;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import static constants.Constants.USERNAME;


@WebServlet(name = "LoadFileCustomerServlet",urlPatterns = "/customer/upload-file")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class LoadFileServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Collection<Part> parts = request.getParts();
        String customerFromSession = SessionUtils.getCustomer(request);
        ArrayList<DTOCustomer> customersManager = ServletUtils.getCustomers(getServletContext());

        if (customerFromSession != null) {
            synchronized (this) {
                try {
                    UIInterfaceLogic bankManager = ServletUtils.getBank(getServletContext());
                    for (Part part : parts) {
                        XmlSerialization.addToBank(part.getContentType(), part.getInputStream(), (Bank) bankManager, customerFromSession);
                    }
                } catch (FileNotFoundException fileNotFoundException) {
                    out.println("The system could not find the file, please check the file path again");
                } catch (Exception ex) {
                    if (ex.getMessage().equals(" ")) {
                        out.println("The file is not an xml file");
                    } else {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        out.println(ex.getMessage());
                    }
                }
                out.println("The file was uploaded successfully\n");
            }
        } else {
            //user is not log-in
            out.println("Please be sure to log-in first");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}

