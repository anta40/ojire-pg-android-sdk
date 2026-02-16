package com.ojire.sdk.opg;

public interface InternalOPGListener {
    void onSuccess(String url);
    void onPending(String url);
    void onFailed(String url);
}
