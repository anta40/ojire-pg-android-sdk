package com.ojire.sdk.opg.model;

import com.google.gson.annotations.SerializedName;

public class PaymenIntent {
    @SerializedName("amount")
    public int amount;
    @SerializedName("currency")
    public String currency;
    @SerializedName("merchant_id")
    public String merchantId;
    @SerializedName("customer_id")
    public String customerId;

    @SerializedName("description")
    public String description;

    @SerializedName("metadata")
    public PaymentMetadata metadata;
}
