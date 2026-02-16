package com.ojire.app.opgdemo.model;

import com.google.gson.annotations.SerializedName;

public class PaymentIntentResponse {
    @SerializedName("amount")
    public int amount;

    @SerializedName("clientSecret")
    public String clientSecret;

    @SerializedName("createdAt")
    public String createdAt;

    @SerializedName("currency")
    public String currency;

    @SerializedName("customerToken")
    public String customerToken;

    @SerializedName("id")
    public String id;

    @SerializedName("merchantId")
    public String merchantId;

    @SerializedName("metadata")
    public PaymentMetadata metadata;

    @SerializedName("status")
    public String status;
}
