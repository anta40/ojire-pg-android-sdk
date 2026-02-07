package com.ojire.app.opgdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CartAdapter.OnCartChangedListener {
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private List<CartItem> itemList;
    private Button btnCheckout;
    private List<CartItem> cartList;
    private TextView tvTotal;
    private int TOTAL_CHECKOUT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TOTAL_CHECKOUT = 0;

        recyclerView = findViewById(R.id.recyclerView);
        btnCheckout = findViewById(R.id.btnCheckout);
        tvTotal = findViewById(R.id.tvTotal);
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent checkoutIntent = new Intent(MainActivity.this, CheckoutActivity.class);
                checkoutIntent.putExtra("TOTAL_CHECKOUT", TOTAL_CHECKOUT);
                startActivity(checkoutIntent);
            }
        });

        prepareDummyData();

        adapter = new CartAdapter(cartList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //updateTotal();

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
}