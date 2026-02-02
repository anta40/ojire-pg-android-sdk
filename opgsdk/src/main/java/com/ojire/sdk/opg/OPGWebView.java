package com.ojire.sdk.opg;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.webkit.JavaScriptReplyProxy;
import androidx.webkit.WebMessageCompat;
import androidx.webkit.WebViewCompat;
import androidx.webkit.WebViewFeature;

import com.ojire.sdk.opg.model.PaymentIntentResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class OPGWebView extends WebView  {


    enum OPGSTATE  {
            PENDING,
            SUCCESS,
            CLOSE,
        FAILED
    }

    private String foo;
    private OPGListener listener;
    private String currentUrl;
    private boolean urlIsChanged;
    private Context ctxt;
    private OPGSTATE state;
    private PaymentIntentResponse intentResponse;

    public OPGSTATE getState(){
        return state;
    }

    public void setPaymentIntentResponse(PaymentIntentResponse resp){
        this.intentResponse = resp;
    }

    public OPGWebView(Context ctxt){
        super(ctxt);
        this.ctxt = ctxt;

        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);

    }

    public void setFoo(String newFoo){
        this.foo = newFoo;
    }

    public String getFoo(){
        return this.foo;
    }

    public void setListener(OPGListener listener){
        this.listener = listener;
    }

    @JavascriptInterface
    public void closeWebView() {
        ((Activity)ctxt).finish();
        if (listener != null){
            listener.onClose();
            this.state = OPGSTATE.CLOSE;
        }
    }

    public OPGWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

        addJavascriptInterface(new WebAppInterface(context), "AndroidInterface");

        setWebChromeClient(new WebChromeClient() {
            @Override
            public void onCloseWindow(WebView window) {
                super.onCloseWindow(window);
                if (listener != null){
                    if (window.getUrl().contains("action=close")) {
                        listener.onClose();
                        state = OPGSTATE.CLOSE;
                    }
                }

            }
        });

        setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                urlIsChanged = true;
                currentUrl = url;
                if (listener != null) {
                    if (url.contains("status=pending")) {
                        listener.onPending(url);
                        state = OPGSTATE.PENDING;
                    }
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (listener != null) {
                    if (url.contains("status=success")) {
                        listener.onPending(url);
                        state = OPGSTATE.SUCCESS;
                    }
                }
            }

            @Override
            public void onReceivedError(
                    WebView view,
                    WebResourceRequest request,
                    WebResourceError error) {

                if (listener != null) {
                    if (view.getUrl().contains("status=failed")) {
                        listener.onFailed(view.getUrl());
                        state = OPGSTATE.FAILED;
//                    listener.onError(
//                            error.getErrorCode(),
//                            error.getDescription().toString()
//                    );
                    }
                }
            }

        });
    }

    private void init(Context context, AttributeSet attrs) {
        // Retrieve the attributes defined in attrs.xml
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.OPGWebView,
                0, 0);

        try {
            foo = a.getString(R.styleable.OPGWebView_foo);
        } finally {
            // Always recycle the TypedArray to avoid memory leaks
            a.recycle();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        listener.onClose();
        state = OPGSTATE.CLOSE;
    }
}
