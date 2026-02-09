package com.ojire.sdk.opg;

import android.util.Log;

import androidx.annotation.NonNull;

import com.moczul.ok2curl.CurlInterceptor;
import com.moczul.ok2curl.logger.Logger;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OPGAPIClient {
    private static Retrofit retrofit = null;

    public static OPGAPIService getAPIService(String BASE_URL, String CLIENT_SECRET) {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request newRequest = chain.request().newBuilder()
                                .addHeader("Content-Type", "application/json")
                                .addHeader("X-Secret-Key", CLIENT_SECRET)
                                .build();
                        return chain.proceed(newRequest);
                    }).addInterceptor(new CurlInterceptor(new Logger() {
                        @Override
                        public void log(@NonNull String msg) {
                            Log.v("OkHttp2Curl", msg);
                        }
                    })).build();

//            OkHttpClient client = new OkHttpClient.Builder()
//                    .addInterceptor(chain -> {
//                        Request newRequest = chain.request().newBuilder()
//                                .addHeader("Content-Type", "application/json")
//                                .addHeader("X-Secret-Key", CLIENT_SECRET)
//                                .build();
//                        return chain.proceed(newRequest);
//                    }).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(OPGAPIService.class);
    }

}
