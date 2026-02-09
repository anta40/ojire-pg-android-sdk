package com.ojire.sdk.opg;

import android.graphics.Bitmap;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class OPGWebClient extends WebViewClient {
    private OPGListener listener;
    private String lastUrl = null;


    public OPGWebClient(OPGListener listener){
        this.listener = listener;
    }

    private void checkUrl(String newUrl) {
        System.out.println("newURL: "+newUrl);
        if (lastUrl == null || !lastUrl.equals(newUrl)) {
            if (listener != null) {
                if (newUrl.contains("status=succeeded")){
                    System.out.println("STATUS: SUKSES");
                    listener.onSuccess(newUrl);
                }
                else if (newUrl.contains("status=pending")){
                    System.out.println("STATUS: PENDING");
                    listener.onPending(newUrl);
                }
                else if (newUrl.contains("status=failed")){
                    System.out.println("STATUS: GAGAL");
                    listener.onFailed(newUrl);
                }
            }
            lastUrl = newUrl;
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        checkUrl(request.getUrl().toString());
        return false;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        System.out.println("onPageStarted URL: "+url);
        checkUrl(url);
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        System.out.println("onPageFinished URL: "+url);
        checkUrl(url);
        super.onPageFinished(view, url);
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        System.out.println("onReceivedError URL: "+view.getUrl());
        checkUrl(view.getUrl());
        super.onReceivedError(view, request, error);
    }
}
