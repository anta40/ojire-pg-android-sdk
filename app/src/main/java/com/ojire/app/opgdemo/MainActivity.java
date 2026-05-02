package com.ojire.app.opgdemo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.moczul.ok2curl.CurlInterceptor;
import com.moczul.ok2curl.logger.Logger;
import com.ojire.app.opgdemo.model.PaymentIntentParam;
import com.ojire.app.opgdemo.model.PaymentIntentResponse;
import com.ojire.app.opgdemo.model.PaymentMetadata;
import com.ojire.sdk.opg.OPGActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity implements CartAdapter.OnCartChangedListener {
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private List<CartItem> itemList;
    private Button btnCheckout;
    private List<CartItem> cartList;
    private TextView tvTotal;
    private int TOTAL_CHECKOUT;

    private final String X_CLIENT_SECRET = "sk_1769591280469729bd24176959128046989e6f78b694f70b4131";
    private final String X_PUBLIC_KEY = "pk_1769591280469729bd24176959128046990a6531e6a9fdf3cbd6";
    private final String MERCHANT_ID = "949f9617-1333-4626-b29b-a049b45aa568";

    private final ActivityResultLauncher<Intent> startForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        String payment_msg = data.getStringExtra("OPG_EVENT_STATUS");
                        showAlert("ArtoPay SDK Demo", payment_msg);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TOTAL_CHECKOUT = 20500;

        recyclerView = findViewById(R.id.recyclerView);
        btnCheckout = findViewById(R.id.btnCheckout);
        tvTotal = findViewById(R.id.tvTotal);

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaymentIntentParam param = new PaymentIntentParam();
                int randomNum = ThreadLocalRandom.current().nextInt(10000, 100000);
                param.amount = TOTAL_CHECKOUT;
                param.currency = "IDR";
                param.customerId = "customer_"+randomNum;
                param.description = "Test payment "+randomNum;
                param.merchantId = MERCHANT_ID;
                PaymentMetadata metadata = new PaymentMetadata();
                metadata.orderId = "order_"+randomNum;
                param.metadata = metadata;

                String jsonString = new Gson().toJson(param);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(jsonString);

                    OkHttpClient customHttpClient = new OkHttpClient.Builder()
                            .addInterceptor(new CurlInterceptor(new Logger() {
                                @Override
                                public void log(String message) {
                                    Log.v("OK2CURL", message);
                                }
                            }))
                            .build();

                    AndroidNetworking.initialize(getApplicationContext(), customHttpClient);
                    AndroidNetworking.post("https://api-dev.arto-pay.com/v1/payment-intents")
                            .addHeaders("X-Secret-Key", X_CLIENT_SECRET)
                            .addJSONObjectBody(jsonObject)
                            .setTag("test")
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    PaymentIntentResponse piresponse  = new Gson().fromJson(response.toString(),
                                            PaymentIntentResponse.class);
                                    System.out.println("FAN response: "+response.toString());
                                    Intent checkoutIntent = new Intent(MainActivity.this, OPGActivity.class);
                                    checkoutIntent.putExtra("ENV", "DEV");
                                    checkoutIntent.putExtra("PUBLIC_KEY", X_PUBLIC_KEY);
                                    checkoutIntent.putExtra("CLIENT_SECRET",piresponse.clientSecret);
                                    checkoutIntent.putExtra("PAYMENT_ID",piresponse.id);
                                    checkoutIntent.putExtra("CUSTOMER_TOKEN",piresponse.customerToken);
                                    startForResult.launch(checkoutIntent);
                                }
                                @Override
                                public void onError(ANError error) {
                                    System.out.println("FAN error: "+error.getErrorDetail());
                                    showAlert("ArtoPay SDK Demo", error.getErrorDetail());
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        prepareDummyData();

        adapter = new CartAdapter(cartList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    private void prepareDummyData() {
        cartList = new ArrayList<>();
        cartList.add(new CartItem(1, "Buku Tulis", 3500, R.drawable.book));
        cartList.add(new CartItem(2, "Kalkulator", 8000, R.drawable.calculator));
        cartList.add(new CartItem(3, "Sepatu", 5000, R.drawable.sneakers));
        cartList.add(new CartItem(3, "Bolpoin", 4000, R.drawable.pen));
    }

    @Override
    public void onUpdateTotal() {
        int total = 0;
        for (CartItem item : cartList) {
            total += item.getPrice() * item.getQuantity();
        }
        TOTAL_CHECKOUT = total;
        tvTotal.setText("Total: Rp"+total);
    }

    public void showAlert(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
}