package com.ojire.app.opgdemo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ojire.sdk.opg.OPGConfig;
import com.ojire.sdk.opg.OPGEnvType;
import com.ojire.sdk.opg.OPGListener;
import com.ojire.sdk.opg.OPGWebView;
import com.ojire.sdk.opg.OPGProcessor;
import com.ojire.sdk.opg.model.PaymenIntent;
import com.ojire.sdk.opg.model.PaymentIntentResponse;
import com.ojire.sdk.opg.model.PaymentMetadata;

import java.util.concurrent.ThreadLocalRandom;

public class CheckoutActivity extends AppCompatActivity implements OPGListener {

    OPGWebView webView;
//    WebView webView;
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
        TOTAL_CHECKOUT = getIntent().getIntExtra("TOTAL_CHECKOUT", 0);
        ENV_TYPE = getIntent().getIntExtra("ENV_TYPE", 0);
        webView = findViewById(R.id.main_web_view);
        webView.setListener(this);
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

                webView.loadUrl(paymentUrl);
                webView.initPayment(PUBLIC_KEY, response.clientSecret, response.customerToken);
            }

            @Override
            public void onError(String errorMessage) {
                System.out.println("Create payment intent error: "+errorMessage);
                Toast.makeText(getApplicationContext(), "Create payment intent error: "+errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onSuccess(String url) {
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                    Toast.makeText(getApplicationContext(), "Pembayaran berhasil.", Toast.LENGTH_LONG).show();
                                }
                            }, 5000);
    }

    @Override
    public void onPending(String url) {
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
               Toast.makeText(getApplicationContext(), "Pembayaran pending.", Toast.LENGTH_LONG).show();
            }
        }, 5000);
    }

    @Override
    public void onFailed(String url) {
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                Toast.makeText(getApplicationContext(), "Pembayaran gagal", Toast.LENGTH_LONG).show();
            }
        }, 5000);
    }

    @Override
    public void onClose() {

    }

    public void showAlert(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Android OPG Demo")
                .setMessage(message)
                .setCancelable(false) // Prevents closing if user clicks outside the dialog
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
}