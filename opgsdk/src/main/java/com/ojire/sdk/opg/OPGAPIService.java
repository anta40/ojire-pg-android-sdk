package com.ojire.sdk.opg;

import com.ojire.sdk.opg.model.PaymenIntent;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OPGAPIService {

    @POST("v1/payment-intents")
    Call<User> createPaymentIntent(@Body PaymenIntent param);
}
