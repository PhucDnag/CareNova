package com.example.btl_android;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_android.Object.MedicalRecord;
import com.example.btl_android.Object.Patient;


public class ChinhSuaBenhAnActivity extends AppCompatActivity {

    ImageView btnBack;
    Button btnXacNhanSua;

    TextView tvPatientName, tvPatientInfo, tvPatientCode, tvVisitDate;

    EditText edtReason, edtBefore, edtAfter,
            edtDiagnosis, edtDescription;

    MedicalRecordRepository recordRepository;
    PatientRepository patientRepository;

    int recordId;
    MedicalRecord record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chinh_sua_benh_an);

        // ===== FIND VIEW =====
        btnBack = findViewById(R.id.btn_back);
        btnXacNhanSua = findViewById(R.id.btn_xac_nhan_sua);

        tvPatientName = findViewById(R.id.tvPatientName);
        tvPatientInfo = findViewById(R.id.tvPatientInfo);
        tvPatientCode = findViewById(R.id.tvPatientCode);
        tvVisitDate = findViewById(R.id.tvVisitDate);

        edtReason = findViewById(R.id.edtReason);
        edtBefore = findViewById(R.id.edtBefore);
        edtAfter = findViewById(R.id.edtAfter);
        edtDiagnosis = findViewById(R.id.edtDiagnosis);
        edtDescription = findViewById(R.id.edtDescription);

        // ===== INIT REPO =====
        recordRepository = new MedicalRecordRepository(this);
        patientRepository = new PatientRepository(this);

        // ===== LẤY record_id =====
        recordId = getIntent().getIntExtra("record_id", -1);

        if (recordId != -1) {

            record = recordRepository.getById(recordId);

            if (record != null) {

                Patient patient = patientRepository.getById(record.getPatientId());

                if (patient != null) {
                    tvPatientName.setText("Bệnh nhân: " + patient.getFullName());
                    tvPatientInfo.setText(patient.getGender() + " | " + patient.getDob());
                    tvPatientCode.setText("Mã BN: " + patient.getId());
                }

                tvVisitDate.setText("Ngày khám: " + record.getVisitDate());

                // Load dữ liệu cũ vào EditText
                edtDiagnosis.setText(record.getDiagnosis());
                edtDescription.setText(record.getDoctorNotes());

                // Nếu bạn lưu reason/before/after riêng thì set ở đây
                edtReason.setText(record.getDiagnosis());
                edtBefore.setText("");
                edtAfter.setText("");
            }
        }

        // ===== BACK =====
        btnBack.setOnClickListener(v -> finish());

        // ===== XÁC NHẬN SỬA =====
        btnXacNhanSua.setOnClickListener(v -> {

            if (record == null) return;

            record.setDiagnosis(edtDiagnosis.getText().toString());
            record.setDoctorNotes(edtDescription.getText().toString());

            recordRepository.update(record);

            Toast.makeText(this,
                    "Chỉnh sửa bệnh án thành công",
                    Toast.LENGTH_SHORT).show();

            setResult(RESULT_OK);
            finish();
        });
    }
}
