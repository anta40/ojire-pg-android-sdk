package com.ojire.app.opgdemo;

import android.net.Uri;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebMessage;
import android.webkit.WebMessagePort;
import android.webkit.WebView;

public class WebBridge {
    private WebMessagePort nativePort;
    private final WebView webView;

    public WebBridge(WebView webView) {
        this.webView = webView;
    }

    public void init() {
        // Create the channel (Requires API 23+)
        WebMessagePort[] channel = webView.createWebMessageChannel();
        nativePort = channel[0];

        // Set up the listener for messages coming FROM the web
        nativePort.setWebMessageCallback(new WebMessagePort.WebMessageCallback() {
            @Override
            public void onMessage(WebMessagePort port, WebMessage message) {
                handleWebMessage(message.getData());
            }
        });

        // Transfer Port 1 to the WebView
        // Replace "*" with your actual domain for better security!
        webView.postWebMessage(new WebMessage("", new WebMessagePort[]{channel[1]}), Uri.parse("*"));
    }

    @JavascriptInterface
    public void postMessage(String data) {
        System.out.println("Raw data on WebBridge postMessage: "+data);
        if (nativePort != null) {
            nativePort.postMessage(new WebMessage(data));
        }
    }

    private void handleWebMessage(String data) {
        Log.i("WebBridge", "Received from Web: " + data);
        // Add your logic to update Android UI or handle events here
    }
}