package com.ojire.sdk.opg.model;

import com.google.gson.annotations.SerializedName;

public class PaymentIntentParam {
    @SerializedName("amount")
    public int amount;
    @SerializedName("currency")
    public String currency;
    @SerializedName("merchantId")
    public String merchantId;
    @SerializedName("customerId")
    public String customerId;

    @SerializedName("description")
    public String description;

    @SerializedName("metadata")
    public PaymentMetadata metadata;
}
