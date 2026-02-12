package com.ojire.app.opgdemo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CartAdapter.OnCartChangedListener {
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private List<CartItem> itemList;
    private Button btnCheckoutJava, btnCheckoutKotlin;
    private List<CartItem> cartList;
    private TextView tvTotal;
    private int TOTAL_CHECKOUT;
    private int ENV_TYPE;
    private Spinner spnEnvType;

    private final ActivityResultLauncher<Intent> startForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        String payment_msg = data.getStringExtra("payment_msg");
                        showAlert(payment_msg);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TOTAL_CHECKOUT = 20500;
        ENV_TYPE = 0;

        recyclerView = findViewById(R.id.recyclerView);
        btnCheckoutJava = findViewById(R.id.btnCheckoutJava);
        btnCheckoutKotlin = findViewById(R.id.btnCheckoutKotlin);
        tvTotal = findViewById(R.id.tvTotal);
        spnEnvType = findViewById(R.id.spnEnvType);

        btnCheckoutJava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ENV_TYPE = spnEnvType.getSelectedItemPosition();
                Intent checkoutIntent = new Intent(MainActivity.this, CheckoutActivity.class);
                checkoutIntent.putExtra("ENV_TYPE", ENV_TYPE);
                checkoutIntent.putExtra("TOTAL_CHECKOUT", TOTAL_CHECKOUT);
                startForResult.launch(checkoutIntent);
            }
        });

        btnCheckoutKotlin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ENV_TYPE = spnEnvType.getSelectedItemPosition();
                Intent checkoutIntent = new Intent(MainActivity.this, CheckoutActivity2.class);
                checkoutIntent.putExtra("ENV_TYPE", ENV_TYPE);
                checkoutIntent.putExtra("TOTAL_CHECKOUT", TOTAL_CHECKOUT);
                startForResult.launch(checkoutIntent);
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