package com.ojire.sdk.opg;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
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
    //private OPGListener listener;
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


//    @JavascriptInterface
//    public void closeWebView() {
//        ((Activity)ctxt).finish();
//        if (listener != null){
//            listener.onClose();
//            this.state = OPGSTATE.CLOSE;
//        }
//    }

    public OPGWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        this.ctxt = context;

        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);

    }


    public void initPayment(String pubKey, String clientSecret, String customerToken){
//       setWebChromeClient(new WebChromeClient(){
//           @Override
//           public void onCloseWindow(WebView window) {
//               super.onCloseWindow(window);
//           }
//       });
       setWebViewClient(new WebViewClient(){
           @Override
           public void onPageFinished(WebView view, String url) {
               super.onPageFinished(view, url);

               try {
                   JSONObject payload = new JSONObject();
                   payload.put("type", "INIT");
                   payload.put("clientSecret", clientSecret);
                   payload.put("publicKey", pubKey);
                   payload.put("token", customerToken);

                   String jsCode  =
                           "window.postMessage(" +
                                   payload.toString() +
                                   ", '*'); true;";

                   post(new Runnable() {
                       @Override
                       public void run() {
                           evaluateJavascript(jsCode, new ValueCallback<String>() {
                               @Override
                               public void onReceiveValue(String s) {
                                   if (s == null) System.out.println("eval result: null");
                                   else System.out.println("eval result: "+s);
                               }
                           });
                       }
                   });


               } catch (JSONException je){
                   System.out.println(je.getMessage());
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

//    @Override
//    protected void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//        listener.onClose();
//        state = OPGSTATE.CLOSE;
//    }
}
