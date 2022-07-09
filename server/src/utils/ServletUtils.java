package utils;


import com.google.gson.Gson;
import dataObjects.dtoBank.dtoAccount.DTOInlay;
import dataObjects.dtoCustomer.DTOCustomer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import logic.UIInterfaceLogic;
import logic.bank.Bank;

import java.util.ArrayList;

import static constants.Constants.INT_PARAMETER_ERROR;


public class ServletUtils {

	private static final String BANK_ATTRIBUTE_NAME = "bank";
	private static final String CUSTOMERS_ATTRIBUTE_NAME = "customersList";
	private static final String INLAYS_ATTRIBUTE_NAME = "inlaysList";
	public final static Gson GSON_INSTANCE = new Gson();


	/*
	Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
	the actual fetch of them is remained un-synchronized for performance POV
	 */
	private static final Object bankManagerLock = new Object();
	/*private static final Object chatManagerLock = new Object();*/

	public static UIInterfaceLogic getBank(ServletContext servletContext) {
		synchronized (bankManagerLock) {
			if (servletContext.getAttribute(BANK_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(BANK_ATTRIBUTE_NAME, new Bank());
			}
		}
		return (UIInterfaceLogic) servletContext.getAttribute(BANK_ATTRIBUTE_NAME);
	}

	public static ArrayList<DTOCustomer> getCustomers(ServletContext servletContext) {
		return getBank(servletContext).getCustomers();
	}

	public static ArrayList<DTOInlay> getInlays(ServletContext servletContext) {
		synchronized (bankManagerLock) {
			if (servletContext.getAttribute(INLAYS_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(INLAYS_ATTRIBUTE_NAME, new ArrayList<DTOInlay>());
			}
		}
		return (ArrayList<DTOInlay>)servletContext.getAttribute(INLAYS_ATTRIBUTE_NAME);
	}

	public static int getIntParameter(HttpServletRequest request, String name) {
		String value = request.getParameter(name);
		if (value != null) {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException numberFormatException) {

			}
		}
		return INT_PARAMETER_ERROR;
	}
}
