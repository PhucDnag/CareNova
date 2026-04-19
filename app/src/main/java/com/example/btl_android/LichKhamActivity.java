package com.example.btl_android;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.Adapter.DoctorAppointmentAdapter;
import com.example.btl_android.Database.DatabaseHelper;
import com.example.btl_android.Object.Appointment;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

public class LichKhamActivity extends AppCompatActivity {

    private RecyclerView recyclerViewAppointments;
    private DoctorAppointmentAdapter adapter;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_appointments);

        db = DatabaseHelper.getInstance(this);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        recyclerViewAppointments = findViewById(R.id.recyclerViewAppointments);
        recyclerViewAppointments.setLayoutManager(new LinearLayoutManager(this));

        loadAppointments();
    }

    private void loadAppointments() {
        List<Appointment> appointmentList = db.getAllAppointmentsForDoctor();
        adapter = new DoctorAppointmentAdapter(this, appointmentList);
        recyclerViewAppointments.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload data in case the doctor confirms an appointment and returns
        loadAppointments();
    }
}
