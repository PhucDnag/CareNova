package com.example.btl_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

public class ServiceDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);

        // Ánh xạ Views
        ImageView ivBack = findViewById(R.id.ivBack);
        TextView tvServiceTitle = findViewById(R.id.tvServiceTitle);
        TextView tvServiceDescription = findViewById(R.id.tvServiceDescription);
        Button btnCreateAppointment = findViewById(R.id.btnCreateAppointment);
        MaterialCardView layoutDoctorInfo = findViewById(R.id.layoutDoctorInfo);

        // Xử lý nút back
        ivBack.setOnClickListener(v -> finish());

        // Nhận dữ liệu từ Intent và hiển thị
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("SERVICE_TITLE");
            String description = intent.getStringExtra("SERVICE_DESCRIPTION");

            tvServiceTitle.setText(title);
            tvServiceDescription.setText(description);
        }

        // Xử lý sự kiện click
        btnCreateAppointment.setOnClickListener(v -> {
            Intent createAppointmentIntent = new Intent(ServiceDetailActivity.this, CreateAppointmentActivity.class);
            startActivity(createAppointmentIntent);
        });

        layoutDoctorInfo.setOnClickListener(v -> {
            Intent doctorDetailIntent = new Intent(ServiceDetailActivity.this, DoctorDetailActivity.class);
            startActivity(doctorDetailIntent);
        });
    }
}
