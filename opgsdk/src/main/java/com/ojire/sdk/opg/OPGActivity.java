package com.ojire.sdk.opg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

public class OPGActivity extends AppCompatActivity {

    WebView opgWebView;
    private String PUBLIC_KEY;
    private String CLIENT_SECRET;
    private String CUSTOMER_TOKEN;
    private String PAYMENT_ID;
    private String ENV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opg);
        opgWebView = findViewById(R.id.main_web_view);
        String paymentUrl = "";

        WebSettings webSettings = opgWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);

        PUBLIC_KEY = getIntent().getStringExtra("PUBLIC_KEY");
        CLIENT_SECRET = getIntent().getStringExtra("CLIENT_SECRET");
        CUSTOMER_TOKEN = getIntent().getStringExtra("CUSTOMER_TOKEN");
        PAYMENT_ID = getIntent().getStringExtra("PAYMENT_ID");
        ENV = getIntent().getStringExtra("ENV");

        if (ENV.equals("DEV")){
            paymentUrl = "https://pay-dev.ojire.com/pay/" + PAYMENT_ID;
        }
        else if (ENV.equals("SANDBOX")){
            paymentUrl = "https://pay-sandbox.ojire.com/pay/" + PAYMENT_ID;
        }
        else if (ENV.equals("PROD")){
            paymentUrl = "https://pay.ojire.online/pay/" + PAYMENT_ID;
        }

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("OPG_RESULT", "CLOSE");
                setResult(Activity.RESULT_OK, returnIntent);
                finish();

                // If you want default behavior after:
                setEnabled(false);
                onBackPressed();
            }
        });

        opgWebView.loadUrl(paymentUrl);
        handlePaymentIntent(PUBLIC_KEY, CLIENT_SECRET, CUSTOMER_TOKEN);
    }



    private void handlePaymentIntent(String pubKey, String clientSecret, String customerToken){

        opgWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (url.contains("status=succeeded")){
                    new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("OPG_RESULT", "SUCCESS");
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        }
                    }, 5000);
                }
                else if (url.contains("status=failed")){
                    new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("OPG_RESULT", "FAILED");
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        }
                    }, 5000);
                }
                else if (url.contains("status=pending")){
                    new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("OPG_RESULT", "PENDING");
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        }
                    }, 5000);
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