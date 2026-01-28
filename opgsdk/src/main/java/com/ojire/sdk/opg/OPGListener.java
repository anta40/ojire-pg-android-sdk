package com.ojire.sdk.opg;

public interface OPGListener {
    void onSuccess(String url);
    void onPending(String url);
    void onFailed(String url);
    void onClose();
}
