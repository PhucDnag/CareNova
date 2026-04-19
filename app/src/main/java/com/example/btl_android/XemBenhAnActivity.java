package com.example.btl_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_android.Object.Doctor;
import com.example.btl_android.Object.MedicalRecord;
import com.example.btl_android.Object.Patient;

public class XemBenhAnActivity extends AppCompatActivity {

    ImageView btnBack;
    Button btnChinhSua, btnXoa, btnThem;

    TextView tvPatientName, tvPatientInfo, tvPatientCode,
            tvVisitDate, tvDoctorName,
            tvDiagnosis, tvDescription;

    MedicalRecordRepository recordRepository;
    DoctorRepository doctorRepository;
    PatientRepository patientRepository;

    int recordId;
    MedicalRecord record;

    private static final int REQUEST_EDIT = 200;
    private static final int REQUEST_ADD = 201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xem_benh_an);

        // ===== FIND VIEW =====
        btnBack = findViewById(R.id.btn_back);
        btnChinhSua = findViewById(R.id.btn_chinh_sua);
        btnThem = findViewById(R.id.btn_them_moi);
        btnXoa = findViewById(R.id.btn_xoa);

        tvPatientName = findViewById(R.id.tvPatientName);
        tvPatientInfo = findViewById(R.id.tvPatientInfo);
        tvPatientCode = findViewById(R.id.tvPatientCode);
        tvVisitDate = findViewById(R.id.tvVisitDate);
        tvDoctorName = findViewById(R.id.tvDoctorName);
        tvDiagnosis = findViewById(R.id.tvDiagnosis);
        tvDescription = findViewById(R.id.tvDescription);

        // ===== INIT REPO =====
        recordRepository = new MedicalRecordRepository(this);
        doctorRepository = new DoctorRepository(this);
        patientRepository = new PatientRepository(this);

        // ===== GET record_id =====
        recordId = getIntent().getIntExtra("record_id", -1);

        loadRecord();

        // ===== BACK =====
        btnBack.setOnClickListener(v -> finish());

        // ===== THÊM BỆNH ÁN =====
        btnThem.setOnClickListener(v -> {
            if (record != null) {
                Intent intent = new Intent(this, ThemBenhAnActivity.class);
                intent.putExtra("patient_id", record.getPatientId());
                intent.putExtra("doctor_id", record.getDoctorId());
                startActivityForResult(intent, REQUEST_ADD);
            } else {
                Toast.makeText(this,
                        "Không có dữ liệu bệnh án.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // ===== CHỈNH SỬA =====
        btnChinhSua.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChinhSuaBenhAnActivity.class);
            intent.putExtra("record_id", recordId);
            startActivityForResult(intent, 200);
        });

        // ===== XÓA =====
        btnXoa.setOnClickListener(v -> {

            if (recordId == -1) return;

            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa bệnh án này không?")
                    .setPositiveButton("Xóa", (dialog, which) -> {

                        int result = recordRepository.delete(recordId);

                        if (result > 0) {

                            Toast.makeText(this,
                                    "Đã xóa bệnh án",
                                    Toast.LENGTH_SHORT).show();

                            setResult(RESULT_OK);
                            finish();

                        } else {

                            Toast.makeText(this,
                                    "Xóa thất bại",
                                    Toast.LENGTH_SHORT).show();
                        }

                    })
                    .setNegativeButton("Hủy", null)
                    .setCancelable(true)
                    .show();
        });
    }

    // ===== LOAD DATA =====
    private void loadRecord() {

        if (recordId == -1) return;

        record = recordRepository.getById(recordId);

        if (record == null) return;

        Patient patient = patientRepository.getById(record.getPatientId());

        if (patient != null) {
            tvPatientName.setText("Bệnh nhân: " + patient.getFullName());
            tvPatientInfo.setText(patient.getGender() + " | " + patient.getDob());
            tvPatientCode.setText("Mã BN: " + patient.getId());
        }

        Doctor doctor = doctorRepository.getById(record.getDoctorId());

        if (doctor != null) {
            tvDoctorName.setText("BS: " + doctor.getName());
        }

        tvVisitDate.setText("Ngày khám: " + record.getVisitDate());
        tvDiagnosis.setText(record.getDiagnosis());
        tvDescription.setText(record.getDoctorNotes());
    }

    // ===== REFRESH SAU THÊM / SỬA =====
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == REQUEST_EDIT || requestCode == REQUEST_ADD)
                && resultCode == RESULT_OK) {

            loadRecord();   // load lại dữ liệu
        }
    }
}