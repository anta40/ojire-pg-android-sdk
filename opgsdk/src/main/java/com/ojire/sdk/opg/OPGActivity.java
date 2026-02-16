package com.ojire.sdk.opg;

import android.os.Bundle;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

public class OPGActivity extends AppCompatActivity {

    WebView opgWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opg);
        opgWebView = findViewById(R.id.main_web_view);

        WebSettings webSettings = opgWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
    }

    private void handlePaymentIntent(String pubKey, String clientSecret, String customerToken){

        opgWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                System.out.println("OPGWebView::onPageFinished URL: "+url);
                if (url.contains("status=succeeded")){
                    //listener.onSuccess(url);
                }
                else if (url.contains("status=failed")){
                   // listener.onFailed(url);
                }
                else if (url.contains("status=pending")){
                   // listener.onPending(url);
                }
                else {
                    try {
                        JSONObject payload = new JSONObject();
                        payload.put("type", "INIT");
                        payload.put("clientSecret", clientSecret);
                        payload.put("publicKey", pubKey);
                        payload.put("token", customerToken);

                        String jsCode =
                                "window.postMessage(" +
                                        payload.toString() +
                                        ", '*'); true;";

                        opgWebView.post(new Runnable() {
                            @Override
                            public void run() {
                                opgWebView.evaluateJavascript(jsCode, new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String s) {
                                        if (s == null) System.out.println("eval result: null");
                                        else System.out.println("eval result: " + s);
                                    }
                                });
                            }
                        });


                    } catch (JSONException je) {
                        System.out.println(je.getMessage());
                    }
                }
            }

        });

    }
}