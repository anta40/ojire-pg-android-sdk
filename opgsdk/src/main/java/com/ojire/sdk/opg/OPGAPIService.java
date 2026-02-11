package com.ojire.sdk.opg;

import com.ojire.sdk.opg.model.PaymentIntentParam;
import com.ojire.sdk.opg.model.PaymentIntentResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OPGAPIService {

    @POST("v1/payment-intents")
    Call<PaymentIntentResponse> createPaymentIntent(@Body PaymentIntentParam param);
}
