package com.example.btl_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_android.Database.DatabaseHelper;
import com.example.btl_android.Object.MedicalRecord1;
import com.example.btl_android.Object.Patient1;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RecordDetailActivity1 extends AppCompatActivity {

    private TextView tvPatientName, tvPatientId, tvDiagnosis, tvPrescription, tvDoctorNotes;
    private ImageView ivBack;
    private Button btnSetReminder;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail1);

        db = DatabaseHelper.getInstance(this);

        ivBack = findViewById(R.id.ivBack);
        tvPatientName = findViewById(R.id.tvPatientName);
        tvPatientId = findViewById(R.id.tvPatientId);
        tvDiagnosis = findViewById(R.id.tvDiagnosis);
        tvPrescription = findViewById(R.id.tvPrescription);
        tvDoctorNotes = findViewById(R.id.tvDoctorNotes);
        btnSetReminder = findViewById(R.id.btnSetReminder);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("RECORD_DATA")) {
            MedicalRecord1 record = (MedicalRecord1) intent.getSerializableExtra("RECORD_DATA");
            if (record != null) {
                Patient1 patient1 = db.getPatientProfile(record.getPatientId());
                if (patient1 != null) {
                    int age = calculateAge(patient1.getDob());
                    tvPatientName.setText(patient1.getFullName() + ", " + age + " tuổi");
                    tvPatientId.setText("Mã số: " + String.format("%06d", patient1.getId()));
                }

                tvDiagnosis.setText(record.getDiagnosis());
                tvPrescription.setText(record.getPrescription());
                tvDoctorNotes.setText(record.getDoctorNotes());
            }
        }

        ivBack.setOnClickListener(v -> finish());
        btnSetReminder.setOnClickListener(v -> {
            Intent reminderIntent = new Intent(RecordDetailActivity1.this, SetReminderActivity.class);
            startActivity(reminderIntent);
        });
    }

    private int calculateAge(String dob) {
        if (dob == null || dob.isEmpty()) {
            return 0;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date birthDate = sdf.parse(dob);
            Calendar birthCal = Calendar.getInstance();
            birthCal.setTime(birthDate);
            Calendar todayCal = Calendar.getInstance();
            int age = todayCal.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR);
            if (todayCal.get(Calendar.DAY_OF_YEAR) < birthCal.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }
            return age;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
