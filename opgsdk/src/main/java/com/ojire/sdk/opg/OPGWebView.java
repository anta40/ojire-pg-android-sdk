package com.ojire.sdk.opg;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class OPGWebView extends WebView  {

    private String foo;
    private OPGListener listener;
    private String currentUrl;
    private boolean urlIsChanged;
    private Context ctxt;

    public OPGWebView(Context ctxt){
        super(ctxt);
        this.ctxt = ctxt;
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
        }
    }

    public OPGWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

        setWebViewClient(new WebViewClient() {@Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            urlIsChanged = true;
            currentUrl = url;
            if (listener != null) {
                listener.onPending(url);
            }
        }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (listener != null) {
                    listener.onSuccess(url);
                }
            }

            @Override
            public void onReceivedError(
                    WebView view,
                    WebResourceRequest request,
                    WebResourceError error) {

                if (listener != null) {
                    listener.onFailed(view.getUrl());
//                    listener.onError(
//                            error.getErrorCode(),
//                            error.getDescription().toString()
//                    );
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
}
