package com.ojire.sdk.opg;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebViewClient extends WebViewClient {

    // Fires BEFORE navigation happens (best place to intercept)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        System.out.println("Masuk");
        String urlString = request.getUrl().toString();
        handleUrlChange(urlString);

        System.out.println("handleUrlChange: "+urlString);

        if (urlString.contains("status=succeeded")) {
            //onSuccess(url);
            System.out.println("OK sukses");
            return true; // Equivalent to .cancel
        }

        if (urlString.contains("status=pending")) {
            //onPending(url);
            System.out.println("OK pending");
            return true; // Equivalent to .cancel
        }

        if (urlString.contains("status=failed")) {
            //onFailed(url);
            System.out.println("OK failed");
            return true; // Equivalent to .cancel
        }

        if (urlString.contains("action=close")) {
            //onClose();
            System.out.println("OK close");
            return true; // Equivalent to .cancel
        }
        // return true if you want to block navigation
        return false;
    }

    // Fires when page actually starts loading
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        handleUrlChange(url);
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        super.doUpdateVisitedHistory(view, url, isReload);
        handleUrlChange(url);
    }

    public void handleUrlChange(String url) {
        Log.d("WEBVIEW_URL", "Changed URL: " + url);
        System.out.println("Change URL: "+url);

        // example: custom scheme from JS
//        if (url.startsWith("myapp://")) {
//            Uri uri = Uri.parse(url);
//
//            String action = uri.getHost();
//            String value = uri.getQueryParameter("data");
//
//            Log.d("WEBVIEW_EVENT", action + " -> " + value);
//        }
    }
}
