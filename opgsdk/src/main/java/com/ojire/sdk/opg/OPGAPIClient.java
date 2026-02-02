package com.ojire.sdk.opg;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OPGAPIClient {
    private static final String BASE_URL = "https://api.example.com/";
    private static Retrofit retrofit = null;
    private static String CLIENT_SECRET = "sk_1769591280469729bd24176959128046989e6f78b694f70b4131";

    public static OPGAPIService getAPIServicet() {
        if (retrofit == null) {
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
