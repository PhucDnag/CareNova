package com.example.btl_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_android.Database.DatabaseHelper;

public  class MainActivity extends AppCompatActivity {

    Button btnProfile, btnHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_draft);

        btnProfile = findViewById(R.id.btnOpenProfile);
        btnHistory = findViewById(R.id.btnOpenHistory);



        btnHistory.setOnClickListener(v -> {


        });
    }
}