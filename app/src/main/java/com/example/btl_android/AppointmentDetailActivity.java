package com.example.btl_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

// <<< SỬA LỖI: THÊM CÁC IMPORT ĐÚNG >>>
import com.example.btl_android.Database.DatabaseHelper;
import com.example.btl_android.Object.Appointment;
import com.example.btl_android.Object.MedicalRecord1;
import com.example.btl_android.Object.Patient1;
import com.google.android.material.appbar.MaterialToolbar;

public class AppointmentDetailActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private Appointment currentAppointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail);

        db = DatabaseHelper.getInstance(this);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        Button btnViewMedicalRecord = findViewById(R.id.btnViewMedicalRecord);
        Button btnViewTreatmentPlan = findViewById(R.id.btnViewTreatmentPlan);
        TextView tvDoctorName = findViewById(R.id.tvDoctorName);
        TextView tvDoctorSpecialty = findViewById(R.id.tvDoctorSpecialty);
        TextView tvAppointmentDateTime = findViewById(R.id.tvAppointmentDateTime);
        TextView tvPatientName = findViewById(R.id.tvPatientName);
        TextView tvPatientDob = findViewById(R.id.tvPatientDob);
        TextView tvPatientPhone = findViewById(R.id.tvPatientPhone);
        TextView tvReason = findViewById(R.id.tvReason);
        TextView tvStatus = findViewById(R.id.tvStatus);

        toolbar.setNavigationOnClickListener(v -> finish());

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("APPOINTMENT_DATA")) {
            currentAppointment = (Appointment) intent.getSerializableExtra("APPOINTMENT_DATA");
            if (currentAppointment != null) {
                Patient1 patient1 = db.getPatientProfile(currentAppointment.getPatientId());

                tvDoctorName.setText("Bác sĩ: " + currentAppointment.getDoctorName());
                tvDoctorSpecialty.setText("Chuyên khoa: " + currentAppointment.getSpecialty());
                tvAppointmentDateTime.setText("Ngày khám: " + currentAppointment.getAppointmentDate() + " " + currentAppointment.getAppointmentTime());
                tvStatus.setText("Trạng thái: " + currentAppointment.getStatus());
                tvReason.setText("Lý do khám: Da mẩn đỏ"); 
                
                if (patient1 != null) {
                    tvPatientName.setText("Tên người đặt lịch: " + patient1.getFullName());
                    tvPatientDob.setText("Ngày sinh: " + patient1.getDob());
                    tvPatientPhone.setText("Số điện thoại: " + patient1.getPhone());
                }
            }
        }

        btnViewMedicalRecord.setOnClickListener(v -> {
            if (currentAppointment != null) {
                MedicalRecord1 record = db.getRecordForAppointment(currentAppointment.getId());
                if (record != null) {
                    Intent recordIntent = new Intent(AppointmentDetailActivity.this, RecordDetailActivity1.class);
                    recordIntent.putExtra("RECORD_DATA", record);
                    startActivity(recordIntent);
                } else {
                    Toast.makeText(this, "Chưa có bệnh án cho lượt khám này.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnViewTreatmentPlan.setOnClickListener(v -> {
             if (currentAppointment != null) {
                MedicalRecord1 record = db.getRecordForAppointment(currentAppointment.getId());
                if (record != null) {
                    Intent treatmentIntent = new Intent(AppointmentDetailActivity.this, TreatmentPlanActivity.class);
                    treatmentIntent.putExtra("RECORD_DATA", record);
                    startActivity(treatmentIntent);
                } else {
                     Toast.makeText(this, "Chưa có phác đồ điều trị cho lượt khám này.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
