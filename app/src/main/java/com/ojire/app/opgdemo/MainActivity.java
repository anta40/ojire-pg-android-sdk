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

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private List<CartItem> itemList;
    private Button btnCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCheckout = findViewById(R.id.btnCheckout);
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent checkoutIntent = new Intent(MainActivity.this, CheckoutActivity.class);
                startActivity(checkoutIntent);
            }
        });

        // 1. Prepare Dummy Data
        itemList = new ArrayList<>();
        itemList.add(new CartItem("Sony Alpha a7 IV", "$2,499"));
        itemList.add(new CartItem("35mm f/1.4 Lens", "$1,299"));
        itemList.add(new CartItem("Zine Paper Glossy", "$15"));
        itemList.add(new CartItem("Camera Strap", "$45"));

        // 2. Setup RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 3. Set Adapter
        adapter = new CartAdapter(itemList);
        recyclerView.setAdapter(adapter);

    }

}