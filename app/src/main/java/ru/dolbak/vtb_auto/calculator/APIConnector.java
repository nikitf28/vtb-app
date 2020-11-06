package ru.dolbak.vtb_auto.calculator;

import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class APIConnector {
    private static String APIKey = "";
    private static OkHttpClient client = new OkHttpClient();
    private static Gson gson = new Gson();

    public static void SetAPIKey(String key) {
        APIKey = key;
    }

    public static CalculatorSettings GetSettings() throws Exception {
        Request request = new Request.Builder()
                .url("https://gw.hackathon.vtb.ru/vtb/hackathon/settings?name=Haval&language=ru-RU")
                .get()
                .addHeader("x-ibm-client-id", APIKey)
                .addHeader("accept", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        if(response.code() != 200) {
            throw new Exception("server request failed");
        }
        ResponseBody body = response.body();
        // вроде бы такого быть не должно
        if(body == null) {
            return null;
        }
        return gson.fromJson(body.string(), CalculatorSettings.class);
    }

    public static PaymentGraph GetPaymentsGraph(GraphSettings settings) throws Exception {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, gson.toJson(settings));
        Request request = new Request.Builder()
                .url("https://gw.hackathon.vtb.ru/vtb/hackathon/payments-graph")
                .post(body)
                .addHeader("x-ibm-client-id", APIKey)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200) {
            throw new Exception("server request failed");
        }
        ResponseBody responseBody = response.body();
        // вроде бы такого быть не должно
        if(responseBody == null) {
            return null;
        }
        return gson.fromJson(responseBody.string(), PaymentGraph.class);
    }

    public static CalculatedLoan CalculateLoan(LoanSettings parameters) throws Exception {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, gson.toJson(parameters));
        Request request = new Request.Builder()
                .url("https://gw.hackathon.vtb.ru/vtb/hackathon/calculate")
                .post(body)
                .addHeader("x-ibm-client-id", APIKey)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200) {
            throw new Exception("server request failed");
        }
        ResponseBody responseBody = response.body();
        // вроде бы такого быть не должно
        if(responseBody == null) {
            return null;
        }
        return gson.fromJson(responseBody.string(), CalculatedLoan.class);
    }

    public static FinalizedLoanResponse PostLoan(FinalizedLoan loan) throws Exception {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, gson.toJson(loan));

        Request request = new Request.Builder()
                .url("https://gw.hackathon.vtb.ru/vtb/hackathon/carloan")
                .post(body)
                .addHeader("x-ibm-client-id", APIKey)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200) {
            throw new Exception("server request failed");
        }
        ResponseBody responseBody = response.body();
        // вроде бы такого быть не должно
        if(responseBody == null) {
            return null;
        }
        return gson.fromJson(responseBody.string(), FinalizedLoanResponse.class);
    }
}
