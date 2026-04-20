package com.example.btl_android;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_android.Object.Patient;
import com.example.btl_android.ai.GroqAIService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ThemBenhAnActivity extends AppCompatActivity {

    private EditText edtVisitDate, edtReason, edtBeforeTreatment,
            edtAfterTreatment, edtDiagnosis, edtDescription;
    private Button btnConfirm;
    private TextView tvPatientName, tvPatientInfo, tvPatientCode;
    private ImageView btnBack;

    private MedicalRecordRepository recordRepository;
    private PatientRepository patientRepository;

    private int patientId;
    private int doctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.them_benh_an);

        // ===== INIT REPOSITORY =====
        recordRepository = new MedicalRecordRepository(this);
        patientRepository = new PatientRepository(this);

        // ===== GET DATA FROM INTENT =====
        patientId = getIntent().getIntExtra("patient_id", -1);
        doctorId = getIntent().getIntExtra("doctor_id", 1);

        if (patientId == -1) {
            Toast.makeText(this,
                    getString(R.string.toast_error_missing_patient_info),
                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // ===== FIND VIEW =====
        btnBack = findViewById(R.id.btn_back);
        tvPatientName = findViewById(R.id.tvPatientName);
        tvPatientInfo = findViewById(R.id.tvPatientInfo);
        tvPatientCode = findViewById(R.id.tvPatientCode);
        edtVisitDate = findViewById(R.id.edtVisitDate);
        edtReason = findViewById(R.id.edtReason);
        edtBeforeTreatment = findViewById(R.id.edtBeforeTreatment);
        edtAfterTreatment = findViewById(R.id.edtAfterTreatment);
        edtDiagnosis = findViewById(R.id.edtDiagnosis);
        edtDescription = findViewById(R.id.edtDescription);
        btnConfirm = findViewById(R.id.btn_xac_nhan);

        loadPatientInfo();

        // ===== SET CURRENT DATE =====
        String currentDate = new SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.getDefault()
        ).format(new Date());

        edtVisitDate.setText(currentDate);

        // ===== EVENTS =====
        btnBack.setOnClickListener(v -> finish());
        btnConfirm.setOnClickListener(v -> saveMedicalRecord());
    }

    private void loadPatientInfo() {
        Patient patient = patientRepository.getById(patientId);

        if (patient != null) {
            tvPatientName.setText(patient.getFullName());
            tvPatientInfo.setText(patient.getGender() + ", " + patient.getDob());
            tvPatientCode.setText(getString(R.string.patient_code_format, patient.getId()));
        }
    }

    private void saveMedicalRecord() {

        String visitDate = edtVisitDate.getText().toString().trim();
        String reason = edtReason.getText().toString().trim();
        String before = edtBeforeTreatment.getText().toString().trim();
        String after = edtAfterTreatment.getText().toString().trim();
        String diagnosis = edtDiagnosis.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();

        if (visitDate.isEmpty() || diagnosis.isEmpty()) {
            Toast.makeText(this,
                    getString(R.string.toast_fill_required_info),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // ===== INSERT DATABASE =====
        recordRepository.insert(
                patientId,
                doctorId,
                visitDate,
                diagnosis,
                description
        );

        // ===== CALL AI =====
        new Thread(() -> {
            try {

                String aiResult = GroqAIService.analyzeMedicalRecord(
                        reason,
                        before,
                        after,
                        diagnosis
                );

                runOnUiThread(() -> {

                    new AlertDialog.Builder(this)
                            .setTitle(R.string.ai_analysis_title)
                            .setMessage(aiResult)
                            .setCancelable(false)
                            .setPositiveButton("OK", (dialog, which) -> {
                                setResult(RESULT_OK);
                                finish();
                            })
                            .show();
                });

            } catch (Exception e) {

                runOnUiThread(() -> {
                    Toast.makeText(this,
                            getString(R.string.toast_ai_error, e.getMessage()),
                            Toast.LENGTH_LONG).show();

                    // vẫn quay lại màn trước nếu AI lỗi
                    setResult(RESULT_OK);
                    finish();
                });
            }
        }).start();
    }
}