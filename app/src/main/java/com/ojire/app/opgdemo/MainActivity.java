package com.ojire.app.opgdemo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ojire.app.opgdemo.model.PaymentIntentParam;
import com.ojire.app.opgdemo.model.PaymentIntentResponse;
import com.ojire.app.opgdemo.OPGConfig;
import com.ojire.app.opgdemo.model.PaymentMetadata;
import com.ojire.sdk.opg.OPGActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity implements CartAdapter.OnCartChangedListener {
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private List<CartItem> itemList;
    private Button btnCheckout;
    private List<CartItem> cartList;
    private TextView tvTotal;
    private int TOTAL_CHECKOUT;

    private final String CLIENT_SECRET = "sk_1769591280469729bd24176959128046989e6f78b694f70b4131";
    private final String PUBLIC_KEY = "pk_1769591280469729bd24176959128046990a6531e6a9fdf3cbd6";
    private final String MERCHANT_ID = "949f9617-1333-4626-b29b-a049b45aa568";

    private final ActivityResultLauncher<Intent> startForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        String payment_msg = data.getStringExtra("OPG_RESULT");
                        showAlert(payment_msg);
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

                OPGProcessor repo = new OPGProcessor(MainActivity.this, "DEV", CLIENT_SECRET);
                repo.doGetToken(param, new OPGProcessor.PaymentCallback() {
                    @Override
                    public void onSuccess(PaymentIntentResponse response) {
                        System.out.println("--- CREATE PAYMENT INTENT RESPONSE ---");
                        System.out.println("Payment ID: "+response.id);
                        System.out.println("Token: "+response.customerToken);
                        System.out.println("Client secret: "+response.clientSecret);

                        Intent checkoutIntent = new Intent(MainActivity.this, OPGActivity.class);
                        checkoutIntent.putExtra("ENV", "DEV");
                        checkoutIntent.putExtra("PUBLIC_KEY", PUBLIC_KEY);
                        checkoutIntent.putExtra("CLIENT_SECRET",response.clientSecret);
                        checkoutIntent.putExtra("PAYMENT_ID",response.id);
                        checkoutIntent.putExtra("CUSTOMER_TOKEN",response.customerToken);
                        startForResult.launch(checkoutIntent);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        System.out.println("Create payment intent error: "+errorMessage);
                        Toast.makeText(MainActivity.this, "Create payment intent error: "+errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
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

    public void showAlert(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Android OPG Demo")
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