package com.ojire.sdk.opg;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class MyJSInterface {
    private MyWebViewClient client;
    private WebView webView;

    public MyJSInterface(MyWebViewClient client, WebView webView) {
        this.client = client;
        this.webView = webView;
    }

    @JavascriptInterface
    public void postMessageDetected(String data) {
        // Since JS interfaces run on a background thread,
        // we use post() to ensure we stay thread-safe
        System.out.println("postMessage: "+data);
        webView.post(() -> {
            // If the message contains a URL or status, process it
            client.handleUrlChange(data);
        });
    }
}