package com.example.btl_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_android.Object.MedicalRecord;

public class RecordDetailActivity extends AppCompatActivity {

    TextView tvDoctor, tvDate, tvDiagnosis, tvPrescription, tvNotes;
    Button btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);

        // 1. Ánh xạ View
        tvDoctor = findViewById(R.id.tvDetailDoctor);
        tvDate = findViewById(R.id.tvDetailDate);
        tvDiagnosis = findViewById(R.id.tvDetailDiagnosis);
        tvPrescription = findViewById(R.id.tvDetailPrescription);
        tvNotes = findViewById(R.id.tvDetailNotes);
        btnClose = findViewById(R.id.btnClose);

        // 2. Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        if (intent.hasExtra("RECORD_DATA")) {
            // Lấy object ra (Cần ép kiểu về MedicalRecord)
            MedicalRecord record = (MedicalRecord) intent.getSerializableExtra("RECORD_DATA");

            if (record != null) {
                tvDoctor.setText("BS. " + record.getDoctorId());
                tvDate.setText("Ngày khám: " + record.getVisitDate());
                tvDiagnosis.setText(record.getDiagnosis());

                // Xử lý xuống dòng cho đẹp nếu đơn thuốc dài
                tvPrescription.setText(record.getPrescription());

                tvNotes.setText(record.getDoctorNotes());
            }
        }

        // 3. Nút đóng
        btnClose.setOnClickListener(v -> finish());
    }
}