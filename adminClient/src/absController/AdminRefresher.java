package absController;

import dataObjects.dtoBank.DTOBank;
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

import static util.Constants.GSON_INSTANCE;


public class AdminRefresher extends TimerTask {
    private final Consumer<DTOBank> bankConsumer;

    public AdminRefresher(Consumer<DTOBank> bankConsumer) {
        this.bankConsumer = bankConsumer;
    }

    @Override
    public void run() {
        String finalUrl = HttpUrl
                .parse(Constants.BANK_PAGE_ADMIN)
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
                String jsonArrayOfCustomersNames = response.body().string();
                    DTOBank dtoBank  = GSON_INSTANCE.fromJson(jsonArrayOfCustomersNames, DTOBank.class);
                    bankConsumer.accept(dtoBank);
            }
        });
    }
}
