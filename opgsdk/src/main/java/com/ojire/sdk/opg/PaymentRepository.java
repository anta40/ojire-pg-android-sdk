package com.ojire.sdk.opg;

import android.content.Context;

import com.ojire.sdk.opg.model.PaymenIntent;
import com.ojire.sdk.opg.model.PaymentIntentResponse;

import kotlin.sequences.ConstrainedOnceSequence;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentRepository {

    private OPGAPIService apiService;
    private Context ctxt;
    public interface PaymentCallback {
        void onSuccess(PaymentIntentResponse user);
        void onError(String errorMessage);
    }

    public PaymentRepository(Context ctxt) {
        this.ctxt = ctxt;
        String BASE_URL = ctxt.getString(R.string.BASE_URL);
        String CLIENT_SECRET = ctxt.getString(R.string.CLIENT_SECRET);
        this.apiService = OPGAPIClient.getAPIServicet(BASE_URL, CLIENT_SECRET);
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
