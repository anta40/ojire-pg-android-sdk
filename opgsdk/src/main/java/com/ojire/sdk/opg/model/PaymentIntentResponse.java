package com.ojire.sdk.opg.model;

import com.google.gson.annotations.SerializedName;

public class PaymentIntentResponse {
    @SerializedName("amount")
    public int amount;

    @SerializedName("client_secret")
    public String clientSecret;

    @SerializedName("created_at")
    public String createdAt;

    @SerializedName("currency")
    public String currency;

    @SerializedName("customer_token")
    public String customerToken;

    @SerializedName("id")
    public String id;

    @SerializedName("merchant_id")
    public String merchantId;

    @SerializedName("metadata")
    public PaymentMetadata metadata;

    @SerializedName("status")
    public String status;
}
