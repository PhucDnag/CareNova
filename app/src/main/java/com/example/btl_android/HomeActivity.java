package com.example.btl_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_android.Database.DatabaseHelper;
import com.example.btl_android.DsBenhNhanBenhAnActivity;
import com.example.btl_android.DsBenhNhanTtinActivity;
import com.example.btl_android.DsBenhNhanPhacDoActivity;
import com.example.btl_android.XemNhanhTtinActivity;
import com.example.btl_android.LichKhamActivity;
import com.example.btl_android.ThongBaoActivity;
import com.example.btl_android.CaNhanActivity;

public class HomeActivity extends AppCompatActivity {

    Button btnXem1, btnKhamNgay1;
    LinearLayout btnHoSo, btnPhacDo, btnBenhNhan;
    LinearLayout navLichKham, navThongBao, navCaNhan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ===== FIND VIEW =====
        btnXem1 = findViewById(R.id.btn_xem_1);
        btnKhamNgay1 = findViewById(R.id.btn_kham_ngay_1);

        btnHoSo = findViewById(R.id.layout_hoso);
        btnPhacDo = findViewById(R.id.layout_phacdo);
        btnBenhNhan = findViewById(R.id.layout_benhnhan);

        navLichKham = findViewById(R.id.nav_lichkham);
        navThongBao = findViewById(R.id.nav_thongbao);
        navCaNhan = findViewById(R.id.nav_canhan);

        // ===== XEM =====
        btnXem1.setOnClickListener(v -> {
            startActivity(new Intent(this, XemNhanhTtinActivity.class));
        });

        // ===== KHÁM NGAY =====
        btnKhamNgay1.setOnClickListener(v -> {
            Toast.makeText(this,
                    "Đã gửi thông báo tới bệnh nhân!",
                    Toast.LENGTH_SHORT).show();
        });

        // ===== HỒ SƠ =====
        btnHoSo.setOnClickListener(v -> {
            startActivity(new Intent(this, DsBenhNhanBenhAnActivity.class));
        });

        // ===== PHÁC ĐỒ =====
        btnPhacDo.setOnClickListener(v -> {
            startActivity(new Intent(this, DsBenhNhanPhacDoActivity.class));
        });

        // ===== BỆNH NHÂN =====
        btnBenhNhan.setOnClickListener(v -> {
            startActivity(new Intent(this, DsBenhNhanTtinActivity.class));
        });

        // ===== MENU DƯỚI =====
        navLichKham.setOnClickListener(v -> {
            startActivity(new Intent(this, LichKhamActivity.class));
        });

        navThongBao.setOnClickListener(v -> {
            startActivity(new Intent(this, ThongBaoActivity.class));
        });

        navCaNhan.setOnClickListener(v -> {
            startActivity(new Intent(this, CaNhanActivity.class));
        });

        DatabaseHelper db = DatabaseHelper.getInstance(this);
        db.createDefaultUserIfEmpty();
    }
}
