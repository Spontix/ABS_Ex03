package util;

import com.google.gson.Gson;

public class Constants {

    // global constants
    public final static String LINE_SEPARATOR = System.getProperty("line.separator");
    public final static String JHON_DOE = "<Anonymous>";
    public final static int REFRESH_RATE = 2000;
    public final static String CHAT_LINE_FORMATTING = "%tH:%tM:%tS | %.10s: %s%n";
/*/customer/login*/
    // fxml locations
    public final static String MAIN_PAGE_FXML_RESOURCE_LOCATION = "/chat/client/component/main/chat-app-main.fxml";
    public final static String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/chat/client/component/login/login.fxml";
    public final static String CHAT_ROOM_FXML_RESOURCE_LOCATION = "/chat/client/component/chatroom/chat-room-main.fxml";

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080"+"/ABSEx03";
    private final static String CONTEXT_PATH_CUSTOMER = "/customer";
    private final static String FULL_SERVER_PATH_CUSTOMER = BASE_URL + CONTEXT_PATH_CUSTOMER;
    private final static String CONTEXT_PATH_ADMIN = "/admin";
    private final static String FULL_SERVER_PATH_ADMIN = BASE_URL + CONTEXT_PATH_ADMIN;

    public final static String LOGIN_PAGE_CUSTOMER = FULL_SERVER_PATH_CUSTOMER + "/login";
    public final static String AS_LOANER_PAGE_CUSTOMER = FULL_SERVER_PATH_CUSTOMER + "/getLoansAsLoaner";
    public final static String AS_BORROWER_PAGE_CUSTOMER = FULL_SERVER_PATH_CUSTOMER + "/getLoansAsBorrower";
    public final static String GET_CUSTOMER_PAGE_CUSTOMER = FULL_SERVER_PATH_CUSTOMER + "/get-customer";
    public final static String DEPOSIT_PAGE_CUSTOMER = FULL_SERVER_PATH_CUSTOMER + "/deposit";
    public final static String WITHDRAWAL_PAGE_CUSTOMER = FULL_SERVER_PATH_CUSTOMER + "/withdraw";
    public final static String POTENTIAL_LOANS_PAGE_CUSTOMER = FULL_SERVER_PATH_CUSTOMER + "/scramble/potentialLoans";
    public final static String SELECTED_LOANS_LOAN_PAGE_CUSTOMER = FULL_SERVER_PATH_CUSTOMER + "/scramble/selectedLoans";
    public final static String YAZ_UNIT_PAGE_CUSTOMER = FULL_SERVER_PATH_CUSTOMER + "/yazUnitProgress";
    public final static String LOANS_TO_PAY_PAGE_CUSTOMER = FULL_SERVER_PATH_CUSTOMER + "/loans-to-pay";
    public final static String PAYMENT_PAGE_CUSTOMER = FULL_SERVER_PATH_CUSTOMER + "/loanPayment";
    public final static String MOVEMENTS_PAGE_CUSTOMER = FULL_SERVER_PATH_CUSTOMER + "/movements";
    public final static String SELL_LOAN_PAGE_CUSTOMER = FULL_SERVER_PATH_CUSTOMER + "/sell-loan";
    public final static String BUY_LOAN_PAGE_CUSTOMER = FULL_SERVER_PATH_CUSTOMER + "/buy-loan";


    public final static String LOGIN_PAGE_ADMIN = FULL_SERVER_PATH_ADMIN + "/login";
    public final static String LOANS_PAGE_ADMIN = FULL_SERVER_PATH_ADMIN + "/loansInformation";
    public final static String CUSTOMERS_PAGE_ADMIN = FULL_SERVER_PATH_ADMIN + "/customersInformation";
    public final static String YAZ_INCREASE_PAGE_ADMIN = FULL_SERVER_PATH_ADMIN + "/yazUnitProgress";
    public final static String BANK_PAGE_ADMIN = FULL_SERVER_PATH_ADMIN + "/bankInformation";
    public final static String REWIND_OFF_PAGE_ADMIN = FULL_SERVER_PATH_ADMIN + "/rewindOff";
    public final static String REWIND_ON_PAGE_ADMIN = FULL_SERVER_PATH_ADMIN + "/rewindOn";





    public final static String UPLOAD_FILE_PAGE = FULL_SERVER_PATH_CUSTOMER + "/upload-file";


    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();
}
