package com.example.btl_android;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_android.Database.DatabaseHelper;
import com.example.btl_android.Object.Doctor;
import com.example.btl_android.Object.MedicalRecord;
import com.example.btl_android.DoctorRepository;
import com.example.btl_android.MedicalRecordRepository;

public class MedicalRecordDetailActivity extends AppCompatActivity {

    private TextView tvDoctor, tvDate, tvDiagnosis, tvPrescription, tvNotes;
    private Button btnClose;

    private DoctorRepository doctorRepository;

    private MedicalRecordRepository medicalRecordRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);

        // 1️⃣ Ánh xạ View
        tvDoctor = findViewById(R.id.tvDetailDoctor);
        tvDate = findViewById(R.id.tvDetailDate);
        tvDiagnosis = findViewById(R.id.tvDetailDiagnosis);
        tvPrescription = findViewById(R.id.tvDetailPrescription);
        tvNotes = findViewById(R.id.tvDetailNotes);
        btnClose = findViewById(R.id.btnClose);

        doctorRepository = new DoctorRepository(this);
        medicalRecordRepository = new MedicalRecordRepository(this);



        // 2️⃣ Lấy record_id từ Intent
        int recordId = getIntent().getIntExtra("record_id", -1);

        if (recordId != -1) {

            // 3️⃣ Lấy MedicalRecord
            MedicalRecord record = medicalRecordRepository.getById(recordId);

            if (record != null) {

                // 4️⃣ Lấy Doctor theo doctorId
                Doctor doctor = doctorRepository.getById(record.getDoctorId());

                if (doctor != null) {
                    tvDoctor.setText("BS. " + doctor.getName());
                } else {
                    tvDoctor.setText("Không rõ bác sĩ");
                }

                tvDate.setText("Ngày khám: " + record.getVisitDate());
                tvDiagnosis.setText(record.getDiagnosis());
                tvPrescription.setText(record.getPrescription());
                tvNotes.setText(record.getDoctorNotes());
            }
        }

        // 5️⃣ Nút đóng
        btnClose.setOnClickListener(v -> finish());
    }
}
