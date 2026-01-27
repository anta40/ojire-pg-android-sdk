package com.ojire.sdk.opg;

public interface OPGListener {
    void onPageStarted(String url);
    void onPageFinished(String url);
    void onError(int errorCode, String description);
}
