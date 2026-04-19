package com.example.btl_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_android.Database.DatabaseHelper;
import com.example.btl_android.Object.Doctor;
import com.example.btl_android.Object.MedicalRecord;
import com.example.btl_android.Object.Patient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class XemTtinBenhNhanActivity extends AppCompatActivity {

    private ImageView btnBack;
    private LinearLayout layoutBenhAn, layoutPhacDo;
    private Button btnThemBenhAn, btnThemPhacDo;
    private TextView txtName, txtMaBn, txtHoTen, txtGioiTinh, txtTuoi;
    private TextView txtTrangThai, txtLastVisit, txtBacSi;

    private DatabaseHelper db;
    private int patientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xem_ttin_benhnhan); 

        db = DatabaseHelper.getInstance(this);

        // Find Views
        btnBack = findViewById(R.id.btn_back);
        layoutBenhAn = findViewById(R.id.layout_benhan);
        layoutPhacDo = findViewById(R.id.layout_phacdo);
        btnThemBenhAn = findViewById(R.id.btnThemBenhAn); // Assuming this ID exists in your layout
        btnThemPhacDo = findViewById(R.id.btnThemPhacDo); // Assuming this ID exists in your layout
        txtName = findViewById(R.id.txt_name);
        txtMaBn = findViewById(R.id.txt_ma_bn);
        txtHoTen = findViewById(R.id.txt_ho_ten);
        txtGioiTinh = findViewById(R.id.txt_gioi_tinh);
        txtTuoi = findViewById(R.id.txt_tuoi);
        txtTrangThai = findViewById(R.id.txt_trang_thai);
        txtLastVisit = findViewById(R.id.txt_last_visit);
        txtBacSi = findViewById(R.id.txt_bac_si);

        btnBack.setOnClickListener(v -> finish());

        patientId = getIntent().getIntExtra("patient_id", -1);

        if (patientId != -1) {
            loadPatientDetails();
        } else {
            Toast.makeText(this, "Lỗi: Không có thông tin bệnh nhân.", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Listeners for doctor's actions
        layoutBenhAn.setOnClickListener(v -> {
            Intent intent = new Intent(this, XemBenhAnActivity.class);
            intent.putExtra("patient_id", patientId);
            startActivity(intent);
        });

        layoutPhacDo.setOnClickListener(v -> {
            Intent intent = new Intent(this, XemPhacDoActivity.class);
            intent.putExtra("patient_id", patientId);
            startActivity(intent);
        });

        btnThemBenhAn.setOnClickListener(v -> {
            Intent intent = new Intent(this, ThemBenhAnActivity.class);
            intent.putExtra("patient_id", patientId);
            startActivity(intent);
        });

        btnThemPhacDo.setOnClickListener(v -> {
            Intent intent = new Intent(this, ThemPhacDoActivity.class);
            intent.putExtra("patient_id", patientId);
            startActivity(intent);
        });
    }

    private void loadPatientDetails() {
        PatientRepository patientRepository = new PatientRepository(this);
        Patient patient = patientRepository.getById(patientId);

        if (patient != null) {
            txtName.setText("Bệnh nhân " + patient.getFullName());
            txtMaBn.setText("Mã BN: BN00" + patient.getId());
            txtHoTen.setText("Họ và tên: " + patient.getFullName());
            txtGioiTinh.setText("Giới tính: " + patient.getGender());
            txtTuoi.setText("Tuổi: " + calculateAge(patient.getDob()));

            txtTrangThai.setText("Trạng thái: Đang điều trị"); // This can be enhanced later

            MedicalRecordRepository medicalRecordRepository = new MedicalRecordRepository(this);
            MedicalRecord lastRecord = medicalRecordRepository.getLastMedicalRecord(patientId);

            if (lastRecord != null) {
                txtLastVisit.setText("Lần khám gần nhất: " + lastRecord.getVisitDate());
                DoctorRepository doctorRepository = new DoctorRepository(this);
                Doctor doctor = doctorRepository.getById(lastRecord.getDoctorId());
                if (doctor != null) {
                    txtBacSi.setText("Bác sĩ phụ trách: BS. " + doctor.getName());
                }
            } else {
                txtLastVisit.setText("Lần khám gần nhất: Chưa có");
                txtBacSi.setText("Bác sĩ phụ trách: ---");
            }
        }
    }

    private int calculateAge(String dob) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date birthDate = sdf.parse(dob);
            Calendar birth = Calendar.getInstance();
            birth.setTime(birthDate);
            Calendar today = Calendar.getInstance();
            int age = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
            if (today.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }
            return age;
        } catch (Exception e) {
            return 0;
        }
    }
}
