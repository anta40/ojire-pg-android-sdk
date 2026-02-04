package com.ojire.app.opgdemo;

import android.os.Bundle;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ojire.sdk.opg.OPGAPIClient;
import com.ojire.sdk.opg.OPGAPIService;
import com.ojire.sdk.opg.OPGListener;
import com.ojire.sdk.opg.OPGWebView;
import com.ojire.sdk.opg.PaymentRepository;
import com.ojire.sdk.opg.model.OPGWebClient;
import com.ojire.sdk.opg.model.PaymenIntent;
import com.ojire.sdk.opg.model.PaymentIntentResponse;
import com.ojire.sdk.opg.model.PaymentMetadata;

import org.json.JSONException;
import org.json.JSONObject;

public class CheckoutActivity extends AppCompatActivity {

    OPGWebView webView;
    private final String PUBKEY = "pk_177000551040616e1a131770005510406184b2479ad7758400e1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        webView = findViewById(R.id.main_web_view);
        webView.setWebViewClient(new OPGWebClient(new OPGListener() {
            @Override
            public void onSuccess(String url) {
                Toast.makeText(getApplicationContext(), "Pembayaran berhasil.", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onPending(String url) {
                Toast.makeText(getApplicationContext(), "Pembayaran pending.", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onFailed(String url) {
                Toast.makeText(getApplicationContext(), "Pembayaran gagal.", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onClose() {

            }
        }));

        performGetToken();
    }

    void performGetToken(){
        PaymenIntent param = new PaymenIntent();
        param.amount = 25000;
        param.currency = "IDR";
        param.customerId = "customer_test_234";
        param.description = "Test payment 234";
        param.merchantId = "949f9617-1333-4626-b29b-a049b45aa568";
        PaymentMetadata metadata = new PaymentMetadata();
        metadata.orderId = "order_234";
        param.metadata = metadata;

        PaymentRepository repo = new PaymentRepository(CheckoutActivity.this);
        repo.doGetToken(param, new PaymentRepository.PaymentCallback() {
            @Override
            public void onSuccess(PaymentIntentResponse response) {
                System.out.println("--- CREATE PAYMENT INTENT RESPONSE ---");
                System.out.println("Payment ID: "+response.id);
                System.out.println("Token: "+response.customerToken);
                System.out.println("Client secret: "+response.clientSecret);

                String paymentUrl = "https://pay-dev.ojire.com/pay/" + response.id;
                webView.loadUrl(paymentUrl);
                webView.initPayment(PUBKEY, response.clientSecret, response.customerToken);
//                webView.setWebViewClient(new WebViewClient(){
//                    @Override
//                    public void onPageFinished(WebView view, String url) {
//                        super.onPageFinished(view, url);
//
//                        try {
//                            JSONObject payload = new JSONObject();
//                            payload.put("type", "INIT");
//                            payload.put("clientSecret", response.clientSecret);
//                            payload.put("publicKey", PUBKEY);
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
            }

            @Override
            public void onError(String errorMessage) {
                System.out.println("Create payment intent error: "+errorMessage);
                Toast.makeText(getApplicationContext(), "Create payment intent error: "+errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
}