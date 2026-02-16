package com.ojire.app.opgdemo;

import com.ojire.app.opgdemo.OPGAPIService;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OPGAPIClient {
    private static Retrofit retrofit = null;

    public static OPGAPIService getAPIService(String env, String CLIENT_SECRET) {
        if (retrofit == null) {
            String BASE_URL = "";

            if (env.equals("DEV")){
                BASE_URL = "https://api-dev.ojire.com/";
            } else if (env.equals("SANDBOX")){
                BASE_URL = "https://api-sandbox.ojire.com/";
            } else if (env.equals("PROD")){
                BASE_URL = "https://api.ojire.online";
            }

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request newRequest = chain.request().newBuilder()
                                .addHeader("Content-Type", "application/json")
                                .addHeader("X-Secret-Key", CLIENT_SECRET)
                                .build();
                        return chain.proceed(newRequest);
                    }).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(OPGAPIService.class);
    }

}
