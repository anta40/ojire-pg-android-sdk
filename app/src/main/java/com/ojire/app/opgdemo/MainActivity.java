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

        itemList = new ArrayList<>();
        itemList.add(new CartItem("Kaos Sepakbola", "Rp 125.000"));
        itemList.add(new CartItem("Buku Tulis", "Rp 5.000"));
        itemList.add(new CartItem("Sepatu ukuran 45", "Rp 100.000"));
        itemList.add(new CartItem("Jam Tangan", "Rp 830.000"));

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CartAdapter(itemList);
        recyclerView.setAdapter(adapter);

    }

}