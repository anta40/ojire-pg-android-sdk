package com.ojire.sdk.opg;

import com.ojire.sdk.opg.model.PaymenIntent;
import com.ojire.sdk.opg.model.PaymentIntentResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentRepository {

    private OPGAPIService apiService;
    public interface PaymentCallback {
        void onSuccess(PaymentIntentResponse user);
        void onError(String errorMessage);
    }

    public PaymentRepository() {
        this.apiService = OPGAPIClient.getAPIServicet();
    }

    public void doGetToken(PaymenIntent param,  PaymentCallback callback){

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
