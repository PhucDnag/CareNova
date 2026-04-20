package com.example.btl_android;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_android.R;
import com.example.btl_android.TreatMentRepository;

public class XemPhacDoActivity extends AppCompatActivity {

    ImageView btnBack;
    TextView tvPatientName, tvGenderAge, tvPatientCode;
    TextView tvMedicineName, tvMethod, tvTimes, tvPurpose, tvGuide;

    Button them, sua, xoa;

    TreatMentRepository repository;

    int treatmentId;
    int patientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xem_phac_do);

        btnBack = findViewById(R.id.btn_back);

        tvPatientName = findViewById(R.id.tvPatientName);
        tvGenderAge = findViewById(R.id.tvGenderAge);
        tvPatientCode = findViewById(R.id.tvPatientCode);

        tvMedicineName = findViewById(R.id.tvMedicineName);
        tvMethod = findViewById(R.id.tvMethod);
        tvTimes = findViewById(R.id.tvTimes);
        tvPurpose = findViewById(R.id.tvPurpose);
        tvGuide = findViewById(R.id.tvGuide);

        them = findViewById(R.id.btn_them_moi);
        sua = findViewById(R.id.btn_chinh_sua);
        xoa = findViewById(R.id.btn_xoa);

        repository = new TreatMentRepository(this);

        treatmentId = getIntent().getIntExtra("treatment_id", -1);

        btnBack.setOnClickListener(v -> finish());

        sua.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChinhSuaPhacDoActivity.class);
            intent.putExtra("treatment_id", treatmentId);
            startActivity(intent);
        });

        them.setOnClickListener(v -> {
            Intent intent = new Intent(this, ThemPhacDoActivity.class);
            intent.putExtra("patient_id", patientId);
            startActivity(intent);
        });

        xoa.setOnClickListener(v -> showDeleteDialog());

        loadData();
    }

    private void loadData() {

        Cursor cursor = repository.getTreatmentById(treatmentId);

        if (cursor != null && cursor.moveToFirst()) {


            patientId = cursor.getInt(0);

            tvPatientName.setText(getString(R.string.patient_label) + " " + cursor.getString(1));
            tvGenderAge.setText(cursor.getString(2) + " - " + cursor.getString(3));
            tvPatientCode.setText(getString(R.string.patient_id_format, String.valueOf(patientId)));

            tvMedicineName.setText(cursor.getString(4));
            tvMethod.setText(cursor.getString(5));
            tvTimes.setText(String.valueOf(cursor.getInt(6)));
            tvPurpose.setText(cursor.getString(7));
            tvGuide.setText(cursor.getString(8));
        }

        if (cursor != null) cursor.close();
    }

    private void showDeleteDialog() {

        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_confirm_title)
                .setMessage(R.string.delete_treatment_confirm_message)
                .setPositiveButton(R.string.delete, (dialog, which) -> {

                    boolean success = repository.deleteTreatment(treatmentId);

                    if (success) {
                        Toast.makeText(
                                this,
                                getString(R.string.toast_delete_success),
                                Toast.LENGTH_SHORT
                        ).show();

                        finish();
                    } else {
                        Toast.makeText(
                                this,
                                getString(R.string.toast_delete_failed),
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}
