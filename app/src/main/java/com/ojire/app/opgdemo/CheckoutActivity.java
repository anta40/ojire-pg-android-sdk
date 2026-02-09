package com.ojire.app.opgdemo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.webkit.internal.ApiFeature;

import com.ojire.sdk.opg.MyJSInterface;
import com.ojire.sdk.opg.MyWebClient;
import com.ojire.sdk.opg.MyWebViewClient;
import com.ojire.sdk.opg.OPGConfig;
import com.ojire.sdk.opg.OPGEnvType;
import com.ojire.sdk.opg.OPGListener;
import com.ojire.sdk.opg.OPGWebView;
import com.ojire.sdk.opg.OPGProcessor;
import com.ojire.sdk.opg.OPGWebClient;
import com.ojire.sdk.opg.UrlChangeListener;
import com.ojire.sdk.opg.model.PaymenIntent;
import com.ojire.sdk.opg.model.PaymentIntentResponse;
import com.ojire.sdk.opg.model.PaymentMetadata;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ThreadLocalRandom;

public class CheckoutActivity extends AppCompatActivity {

    OPGWebView webView;
//    WebView webView;
    //String PAYMENT_ID = "";
    private TextView tvPaymentId;

    private final String CLIENT_SECRET = "sk_1769591280469729bd24176959128046989e6f78b694f70b4131";
    private final String PUBLIC_KEY = "pk_1769591280469729bd24176959128046990a6531e6a9fdf3cbd6";
    private int TOTAL_CHECKOUT;
    private int ENV_TYPE;
    private OPGConfig config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        //PAYMENT_ID = getIntent().getStringExtra("PAYMENT_ID");
        TOTAL_CHECKOUT = getIntent().getIntExtra("TOTAL_CHECKOUT", 0);
        ENV_TYPE = getIntent().getIntExtra("ENV_TYPE", 0);
        webView = findViewById(R.id.main_web_view);
        tvPaymentId = findViewById(R.id.tvPaymentId);

        tvPaymentId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textToCopy = tvPaymentId.getText().toString().replace("Payment ID: ","");
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("PaymentId", textToCopy);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(CheckoutActivity.this,
                        "Payment ID is copied to clipboard!", Toast.LENGTH_SHORT).show();
            }
        });

//        webView.setWebChromeClient(new WebChromeClient(){
//            @Override
//            public void onCloseWindow(WebView window) {
//                super.onCloseWindow(window);
//            }
//        });
//        webView.setWebViewClient(new OPGWebClient(new OPGListener() {
//            @Override
//            public void onSuccess(String url) {
//                System.out.println("DBG: "+url);
//                Toast.makeText(getApplicationContext(), "Pembayaran berhasil.", Toast.LENGTH_LONG).show();
//                finish();
//            }
//
//            @Override
//            public void onPending(String url) {
//                System.out.println("DBG: "+url);
//                Toast.makeText(getApplicationContext(), "Pembayaran pending.", Toast.LENGTH_LONG).show();
//                finish();
//            }
//
//            @Override
//            public void onFailed(String url) {
//                System.out.println("DBG: "+url);
//                Toast.makeText(getApplicationContext(), "Pembayaran gagal.", Toast.LENGTH_LONG).show();
//                finish();
//            }
//
//            @Override
//            public void onClose() {
//
//            }
//        }));

        initiatePayment();
    }

    void initiatePayment(){
        PaymenIntent param = new PaymenIntent();
        int randomNum = ThreadLocalRandom.current().nextInt(10000, 100000);
        param.amount = TOTAL_CHECKOUT;
        param.currency = "IDR";
        param.customerId = "customer_"+randomNum;
        param.description = "Test payment "+randomNum;
        param.merchantId = getString(R.string.MERCHANT_ID);
        PaymentMetadata metadata = new PaymentMetadata();
        metadata.orderId = "order_"+randomNum;
        param.metadata = metadata;

        if (ENV_TYPE == 0){
            config = new OPGConfig.ConfigBuilder().setClientSecret(CLIENT_SECRET)
                    .setPublicKey(PUBLIC_KEY)
                    .setEnv(OPGEnvType.Env.DEV)
                    .build();
        }
        else if (ENV_TYPE == 1){
            config = new OPGConfig.ConfigBuilder().setClientSecret(CLIENT_SECRET)
                    .setPublicKey(PUBLIC_KEY)
                    .setEnv(OPGEnvType.Env.SANDBOX)
                    .build();
        }
        else if (ENV_TYPE == 2){
            config = new OPGConfig.ConfigBuilder().setClientSecret(CLIENT_SECRET)
                    .setPublicKey(PUBLIC_KEY)
                    .setEnv(OPGEnvType.Env.PROD)
                    .build();
        }

        OPGProcessor repo = new OPGProcessor(CheckoutActivity.this, config);
        repo.doGetToken(param, new OPGProcessor.PaymentCallback() {
            @Override
            public void onSuccess(PaymentIntentResponse response) {
                System.out.println("--- CREATE PAYMENT INTENT RESPONSE ---");
                System.out.println("Payment ID: "+response.id);
                System.out.println("Token: "+response.customerToken);
                System.out.println("Client secret: "+response.clientSecret);

                tvPaymentId.setText("Payment ID: "+response.id);
                String paymentUrl = config.getBasePaymentURL() + response.id;
                System.out.println("PaymentUrl: "+paymentUrl);

//                webView.setWebChromeClient(new WebChromeClient(){
//                    @Override
//                    public void onCloseWindow(WebView window) {
//                        super.onCloseWindow(window);
//                    }
//                });
//
//                webView.setWebViewClient(new MyWebClient(new UrlChangeListener() {
//                    @Override
//                    public void onUrlChanged(String oldUrl, String newUrl) {
//                        System.out.println("URL changed from "+oldUrl+" to "+newUrl);
//                    }
//                }));

                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setDomStorageEnabled(true);
                webView.setWebViewClient(new MyWebViewClient());
                webView.setWebViewClient(new WebViewClient(){
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        System.out.println("-> onPageFinished: "+url);

                        if (url.contains("status=succeeded")){

                            new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                    Toast.makeText(getApplicationContext(), "Pembayaran berhasil!", Toast.LENGTH_LONG).show();
                                }
                            }, 5000); // Delay in milliseconds
                        }
                        else if (url.contains("status=failed")){
                            new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                    Toast.makeText(getApplicationContext(), "Pembayaran gagal :(", Toast.LENGTH_LONG).show();
                                }
                            }, 5000); // Delay in milliseconds
                        }
                        else if (url.contains("status=pending")){
                            new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                    Toast.makeText(getApplicationContext(), "Pembayaran pending...", Toast.LENGTH_LONG).show();
                                }
                            }, 5000); // Delay in milliseconds
                        }
                        else {
                            try {
                                JSONObject payload = new JSONObject();
                                payload.put("type", "INIT");
                                payload.put("clientSecret", response.clientSecret);
                                payload.put("publicKey", PUBLIC_KEY);
                                payload.put("token", response.customerToken);

                                String jsCode =
                                        "window.postMessage(" +
                                                payload.toString() +
                                                ", '*'); true;";

                                webView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        webView.evaluateJavascript(jsCode, new ValueCallback<String>() {
                                            @Override
                                            public void onReceiveValue(String s) {
                                                if (s == null)
                                                    System.out.println("eval result: null");
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
//                webView.setWebViewClient(new OPGWebClient(new OPGListener() {
//                    @Override
//                    public void onSuccess(String url) {
//                        System.out.println("[DBG]: "+url);
//                        Toast.makeText(getApplicationContext(), "Pembayaran berhasil", Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onPending(String url) {
//                        System.out.println("[DBG]: "+url);
//                        Toast.makeText(getApplicationContext(), "Pembayaran pending", Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onFailed(String url) {
//                        System.out.println("[DBG]: "+url);
//                        Toast.makeText(getApplicationContext(), "Pembayaran gagal", Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onClose() {
//
//                    }
//                }));
//                webView.setWebViewClient(new WebViewClient() {
//                    @Override
//                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                        String url = request.getUrl().toString();
//
//                        System.out.println("shouldOverride: "+url);
//
//                        // Equivalent to "allow" in iOS
//                        return false;
//                    }
//
//                    @Override
//                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                        super.onPageStarted(view, url, favicon);
//                        System.out.println("onPageStarted: "+url);
//                    }
//
//                    @Override
//                    public void onPageFinished(WebView view, String url) {
//                        super.onPageFinished(view, url);
//                        System.out.println("onPageFinished: "+url);
//
//                        try {
//                            JSONObject payload = new JSONObject();
//                            payload.put("type", "INIT");
//                            payload.put("clientSecret", response.clientSecret);
//                            payload.put("publicKey", PUBLIC_KEY);
//                            payload.put("token", response.customerToken);
//
//                            String jsCode  =
//                                    "window.postMessage(" +
//                                            payload.toString() +
//                                            ", '*'); true;";
//
//                            webView.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    webView.evaluateJavascript(jsCode, new ValueCallback<String>() {
//                                        @Override
//                                        public void onReceiveValue(String s) {
//                                            if (s == null) System.out.println("eval result: null");
//                                            else System.out.println("eval result: "+s);
//                                        }
//                                    });
//                                }
//                            });
//
//
//                        } catch (JSONException je){
//                            System.out.println(je.getMessage());
//                        }
//                    }
//                });
               // webView.addJavascriptInterface(new MyJSInterface(new MyWebViewClient(), webView), "AndroidBridge");
                //webView.setWebViewClient(new MyWebViewClient());

//                webView.addJavascriptInterface(new MyJSInterface(newUrl -> {
//                    runOnUiThread(() -> {
//                        System.out.println("URL detected via postMessage: " + newUrl);
//                        // Handle your logic here
//                    });
//                }), "Android");
//                webView.setWebViewClient(new OPGWebClient(new OPGListener() {
//                    @Override
//                    public void onSuccess(String url) {
//                        System.out.println("BLAH: "+url);
//                        Toast.makeText(getApplicationContext(), "Pembayaran berhasil.", Toast.LENGTH_LONG).show();
//                        finish();
//                    }
//
//                    @Override
//                    public void onPending(String url) {
//                        System.out.println("BLAH: "+url);
//                        Toast.makeText(getApplicationContext(), "Pembayaran pending.", Toast.LENGTH_LONG).show();
//                        finish();
//                    }
//
//                    @Override
//                    public void onFailed(String url) {
//                        System.out.println("BLAH: "+url);
//                        Toast.makeText(getApplicationContext(), "Pembayaran gagal.", Toast.LENGTH_LONG).show();
//                        finish();
//                    }
//
//                    @Override
//                    public void onClose() {
//
//                    }
//                }));
                webView.loadUrl(paymentUrl);
                //webView.initPayment(PUBLIC_KEY, response.clientSecret, response.customerToken);
            }

            @Override
            public void onError(String errorMessage) {
                System.out.println("Create payment intent error: "+errorMessage);
                Toast.makeText(getApplicationContext(), "Create payment intent error: "+errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
}