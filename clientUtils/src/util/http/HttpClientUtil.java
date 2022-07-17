package util.http;

import dataObjects.dtoBank.dtoAccount.DTOLoan;
import okhttp3.*;

import java.util.ArrayList;
import java.util.function.Consumer;

public class HttpClientUtil {

    private final static SimpleCookieManager simpleCookieManager = new SimpleCookieManager();
    private final static OkHttpClient HTTP_CLIENT =
            new OkHttpClient.Builder()
                    .cookieJar(simpleCookieManager)
                    .followRedirects(false)
                    .build();

    public static void setCookieManagerLoggingFacility(Consumer<String> logConsumer) {
        simpleCookieManager.setLogData(logConsumer);
    }

    public static void removeCookiesOf(String domain) {
        simpleCookieManager.removeCookiesOf(domain);
    }

    public static void runAsyncGet(String finalUrl, Callback callback) {
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);

        call.enqueue(callback);
    }

    public static void runAsyncJson(String finalUrl, Callback callback,String json){
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, json);
        Request request = new Request.Builder()
                .url(finalUrl)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);

        call.enqueue(callback);
    }

    public static void runAsyncPost(String finalUrl, Callback callback, RequestBody body) {
        Request request = new Request.Builder()
                .url(finalUrl)
                .method("POST", body)
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);

        call.enqueue(callback);
    }

    public static void shutdown() {
        System.out.println("Shutting down HTTP CLIENT");
        HTTP_CLIENT.dispatcher().executorService().shutdown();
        HTTP_CLIENT.connectionPool().evictAll();
    }

    public static void runAsyncSelectedLoans(String finalUrl, Callback callback, ArrayList<DTOLoan> loans) {
        int index=0;
        MediaType mediaType = MediaType.parse("text/plain");
        MultipartBody.Builder MpB = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (DTOLoan dtoLoan:loans) {
            MpB.addFormDataPart("loan"+ index,dtoLoan.getId());
            index++;
        }
        RequestBody body=MpB.build();
        Request request = new Request.Builder()
                .url(finalUrl)
                .method("POST", body)
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);

        call.enqueue(callback);
    }
}
