package com.example.btl_android;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import com.example.btl_android.Adapter.PatientAdapter;
import com.example.btl_android.R;
import com.example.btl_android.Object.Patient;
import com.example.btl_android.PatientRepository;

public class DsBenhNhanTtinActivity extends AppCompatActivity {

    ImageView btnBack;
    ListView lvPatients;

    PatientRepository repository;
    List<Patient> patientList;
    PatientAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ds_benhnhan_ttin);

        btnBack = findViewById(R.id.btn_back);
        lvPatients = findViewById(R.id.lvPatients);

        btnBack.setOnClickListener(v -> finish());

        repository = new PatientRepository(this);
        patientList = repository.getAllPatients();

        adapter = new PatientAdapter(this, patientList);
        lvPatients.setAdapter(adapter);
    }
}
