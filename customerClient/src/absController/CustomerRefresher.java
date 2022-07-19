package absController;

import dataObjects.dtoBank.DTOBank;
import dataObjects.dtoCustomer.DTOCustomer;
import javafx.application.Platform;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static util.Constants.GET_CUSTOMER_PAGE_CUSTOMER;
import static util.Constants.GSON_INSTANCE;


public class CustomerRefresher extends TimerTask {
    private final Consumer<DTOCustomer> customerConsumer;

    public CustomerRefresher(Consumer<DTOCustomer> customerConsumer) {
        this.customerConsumer = customerConsumer;
    }

    @Override
    public void run() {
        String finalUrl = HttpUrl
                .parse(Constants.GET_CUSTOMER_PAGE_CUSTOMER)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                HelperFunction.popupMessage("Refresh Error", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfCustomer = response.body().string();
                DTOCustomer dtoCustomer = GSON_INSTANCE.fromJson(jsonArrayOfCustomer, DTOCustomer.class);
                customerConsumer.accept(dtoCustomer);
            }
        });
    }
}