package com.ojire.sdk.opg;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class OPGWebClient extends WebViewClient {
    private OPGListener listener;
    private String lastUrl = null;
    private boolean isCallbackHandled = false; // Prevents multiple triggers

    public OPGWebClient(OPGListener listener){
        this.listener = listener;
    }

    @JavascriptInterface
    public void onJsMessage(String message) {
        // JS interface runs on a background thread. Move to Main Thread.
        new Handler(Looper.getMainLooper()).post(() -> {
            System.out.println("JS Message Received: " + message);
            checkUrl(message);
        });
    }

    private void checkUrl(String newUrl) {
        if (newUrl == null || isCallbackHandled) return;

        // Log for debugging - check your Logcat!
        System.out.println("Checking URL: " + newUrl);

        if (listener != null) {
            if (newUrl.contains("status=succeeded")) {
                isCallbackHandled = true; // Mark as handled
                System.out.println("STATUS: SUKSES");
                listener.onSuccess(newUrl);
            }
            else if (newUrl.contains("status=pending")) {
                isCallbackHandled = true;
                System.out.println("STATUS: PENDING");
                listener.onPending(newUrl);
            }
            else if (newUrl.contains("status=failed")) {
                isCallbackHandled = true;
                System.out.println("STATUS: GAGAL");
                listener.onFailed(newUrl);
            }
        }
        lastUrl = newUrl;
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        super.doUpdateVisitedHistory(view, url, isReload);
        // Best for Single Page Apps (React/Vue)
        checkUrl(url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        // Best for standard server-side redirects
        checkUrl(url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();
        checkUrl(url);
        // Return false to allow the WebView to actually load the page
        return false;
    }

    // Optional: Only use onPageFinished if the status appears late in the load
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        view.evaluateJavascript(
                "window.addEventListener('message', function(event) {" +
                        "   var data = typeof event.data === 'object' ? JSON.stringify(event.data) : event.data;" +
                        "   if(window.Android) window.Android.onJsMessage(data);" +
                        "});", null);
        checkUrl(url);
    }

}