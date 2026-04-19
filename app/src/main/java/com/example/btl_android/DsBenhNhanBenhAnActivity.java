package com.example.btl_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import com.example.btl_android.Adapter.MedicalRecordAdapter;
import com.example.btl_android.Database.DatabaseHelper;
import com.example.btl_android.Object.MedicalRecord;
import com.example.btl_android.R;
import com.example.btl_android.DoctorRepository;
import com.example.btl_android.MedicalRecordRepository;

public class DsBenhNhanBenhAnActivity extends AppCompatActivity {

    MedicalRecordRepository medicalRecordRepository;
    DoctorRepository doctorRepository;

    ImageView btnBack;
    ListView lvRecords;


    int doctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ds_benhnhan_benhan);

        // ===== FIND VIEW =====
        btnBack = findViewById(R.id.btn_back);
        lvRecords = findViewById(R.id.lvRecords);


        medicalRecordRepository = new MedicalRecordRepository(this);
        doctorRepository = new DoctorRepository(this);

        doctorId = getIntent().getIntExtra("doctor_id", 1);

        // ===== LOAD DATA =====
        loadMedicalRecords();

        // ===== BACK =====
        btnBack.setOnClickListener(v -> finish());

        int recordId = getIntent().getIntExtra("record_id", -1);

    }

    private void loadMedicalRecords() {

        List<MedicalRecord> list =
                medicalRecordRepository.getByDoctorId(doctorId);

        MedicalRecordAdapter adapter =
                new MedicalRecordAdapter(this, list);

        lvRecords.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMedicalRecords();
    }
}
