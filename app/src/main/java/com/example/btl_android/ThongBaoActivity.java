package com.example.btl_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class ThongBaoActivity extends AppCompatActivity {

    LinearLayout navHome, navLichKham, navCaNhan;
    ImageView btnback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thong_bao);

        // ===== FIND VIEW =====
        navHome = findViewById(R.id.nav_home);
        navLichKham = findViewById(R.id.nav_lichkham);
        navCaNhan = findViewById(R.id.nav_canhan);
        btnback = findViewById(R.id.btn_back);
        // ===== TRANG CHỦ =====
        navHome.setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class));
        });

        // ===== THÔNG BÁO =====
        navLichKham.setOnClickListener(v -> {
            startActivity(new Intent(this, LichKhamActivity.class));
        });

        // ===== CÁ NHÂN =====
        navCaNhan.setOnClickListener(v -> {
            startActivity(new Intent(this, CaNhanActivity.class));
        });
        btnback.setOnClickListener( v->{
            startActivity(new Intent(this, HomeActivity.class));
        });
    }
}
