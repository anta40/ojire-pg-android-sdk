package com.ojire.app.opgdemo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ojire.sdk.opg.OPGListener;
import com.ojire.sdk.opg.OPGWebView;
import com.ojire.sdk.opg.PaymentRepository;
import com.ojire.sdk.opg.OPGWebClient;
import com.ojire.sdk.opg.model.PaymenIntent;
import com.ojire.sdk.opg.model.PaymentIntentResponse;
import com.ojire.sdk.opg.model.PaymentMetadata;

public class CheckoutActivity extends AppCompatActivity {

    OPGWebView webView;
    String PAYMENT_ID = "";
    private TextView tvPaymentId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        PAYMENT_ID = getIntent().getStringExtra("PAYMENT_ID");
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

        initiatePayment();
    }

    void initiatePayment(){
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

                tvPaymentId.setText("Payment ID: "+response.id);
                String paymentUrl = "https://pay-dev.ojire.com/pay/" + response.id;
                webView.loadUrl(paymentUrl);
                webView.initPayment(response.clientSecret, response.customerToken);
            }

            @Override
            public void onError(String errorMessage) {
                System.out.println("Create payment intent error: "+errorMessage);
                Toast.makeText(getApplicationContext(), "Create payment intent error: "+errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
}