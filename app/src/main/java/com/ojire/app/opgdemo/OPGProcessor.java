package com.ojire.app.opgdemo;

import android.content.Context;

import com.ojire.app.opgdemo.model.PaymentIntentParam;
import com.ojire.app.opgdemo.model.PaymentIntentResponse;
import com.ojire.app.opgdemo.OPGAPIClient;
import com.ojire.app.opgdemo.OPGAPIService;
import com.ojire.app.opgdemo.OPGConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OPGProcessor {

    private OPGAPIService apiService;
    private Context ctxt;
    private OPGConfig config;
    public interface PaymentCallback {
        void onSuccess(PaymentIntentResponse user);
        void onError(String errorMessage);
    }

    public OPGProcessor(Context ctxt, String env, String clientSecret) {
        this.ctxt = ctxt;
        this.config = config;
        this.apiService = OPGAPIClient.getAPIService(env, clientSecret);
    }

    public void doGetToken(PaymentIntentParam param, PaymentCallback callback){

        apiService.createPaymentIntent(param).enqueue(new Callback<PaymentIntentResponse>() {
            @Override
            public void onResponse(Call<PaymentIntentResponse> call, Response<PaymentIntentResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Server error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<PaymentIntentResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
}
