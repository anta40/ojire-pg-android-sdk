package com.ojire.app.opgdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        btnCheckout = findViewById(R.id.btnCheckout);
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent checkoutIntent = new Intent(MainActivity.this, CheckoutActivity.class);
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
        cartList.add(new CartItem(1, "Smartphone", 699.00, R.drawable.computer));
        cartList.add(new CartItem(2, "Laptop", 1200.00, R.drawable.computer));
        cartList.add(new CartItem(3, "Bluetooth Speaker", 45.99, R.drawable.computer));
    }

    @Override
    public void onUpdateTotal() {
        double total = 0;
        for (CartItem item : cartList) {
            total += item.getPrice() * item.getQuantity();
        }
    }
}