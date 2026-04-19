package com.example.btl_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class XemNhanhTtinActivity extends AppCompatActivity {

    ImageView btnBack;
    Button btnKhamNgay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xem_nhanh_ttin);

        // ===== FIND VIEW =====
        btnBack = findViewById(R.id.btn_back);
        btnKhamNgay = findViewById(R.id.btn_kham_ngay);

        // ===== BACK =====
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(XemNhanhTtinActivity.this, HomeActivity.class);
            startActivity(intent);
            finish(); // đóng màn hiện tại
        });

        // ===== KHÁM NGAY =====
        btnKhamNgay.setOnClickListener(v -> {
            Toast.makeText(
                    XemNhanhTtinActivity.this,
                    "Đã gửi thông báo tới bệnh nhân!",
                    Toast.LENGTH_SHORT
            ).show();
        });

    }
}
