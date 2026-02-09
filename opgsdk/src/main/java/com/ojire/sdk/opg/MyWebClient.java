package com.ojire.sdk.opg;

import android.graphics.Bitmap;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebClient extends WebViewClient {
    private String lastUrl = null;
    private UrlChangeListener listener;

    public MyWebClient(UrlChangeListener listener){
        this.listener = listener;
    }

    private void checkUrl(String newUrl) {
        if (newUrl == null) return;

        // Ensure we don't trigger the listener twice for the same URL
        if (lastUrl == null || !lastUrl.equals(newUrl)) {
            System.out.println("URL DETECTED: " + newUrl);
            if (listener != null) {
                listener.onUrlChanged(lastUrl, newUrl);
            }
            lastUrl = newUrl;
        }
    }

    // 1. Catches "Silent" URL changes (Single Page Apps / AJAX navigation)
    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        System.out.println("[URL] doUpdateVisitedHistory: "+url);
        super.doUpdateVisitedHistory(view, url, isReload);
        checkUrl(url);
    }

    // 2. Catches standard page reloads and redirects
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        System.out.println("[URL] onPageStarted: "+url);
        super.onPageStarted(view, url, favicon);
        checkUrl(url);
    }

    // 3. Catches user clicks on links
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        checkUrl(request.getUrl().toString());
        return false;
    }
}