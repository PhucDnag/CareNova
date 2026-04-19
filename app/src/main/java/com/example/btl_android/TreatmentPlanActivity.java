package com.example.btl_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.example.btl_android.Object.MedicalRecord1;


public class TreatmentPlanActivity extends AppCompatActivity {

    private TextView tvPrescriptionDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment_plan);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        Button btnSetReminder = findViewById(R.id.btnSetReminder);
        tvPrescriptionDetails = findViewById(R.id.tvPrescriptionDetails);

        toolbar.setNavigationOnClickListener(v -> finish());

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("RECORD_DATA")) {
            MedicalRecord1 record = (MedicalRecord1) intent.getSerializableExtra("RECORD_DATA");
            if (record != null) {
                tvPrescriptionDetails.setText(record.getPrescription());
            }
        }

        btnSetReminder.setOnClickListener(v -> {
            Intent reminderIntent = new Intent(TreatmentPlanActivity.this, SetReminderActivity.class);
            startActivity(reminderIntent);
        });
    }
}
