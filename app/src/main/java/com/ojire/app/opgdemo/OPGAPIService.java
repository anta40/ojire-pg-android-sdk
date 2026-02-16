package com.ojire.app.opgdemo;

import com.ojire.app.opgdemo.model.PaymentIntentParam;
import com.ojire.app.opgdemo.model.PaymentIntentResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OPGAPIService {

    @POST("v1/payment-intents")
    Call<PaymentIntentResponse> createPaymentIntent(@Body PaymentIntentParam param);
}
