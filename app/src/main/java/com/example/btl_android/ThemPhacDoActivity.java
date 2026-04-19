package com.example.btl_android;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_android.R;
import com.example.btl_android.TreatMentRepository;

public class ThemPhacDoActivity extends AppCompatActivity {

    ImageView btnBack;
    Button btnXacNhan;

    EditText edtMedicineName, edtMethod, edtTimes, edtPurpose, edtGuide;

    TreatMentRepository repository;

    int patientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.them_phac_do);

        btnBack = findViewById(R.id.btn_back);
        btnXacNhan = findViewById(R.id.btn_xac_nhan);

        edtMedicineName = findViewById(R.id.edtMedicineName);
        edtMethod = findViewById(R.id.edtMethod);
        edtTimes = findViewById(R.id.edtTimes);
        edtPurpose = findViewById(R.id.edtPurpose);
        edtGuide = findViewById(R.id.edtGuide);

        repository = new TreatMentRepository(this);

        // Nhận patient_id
        patientId = getIntent().getIntExtra("patient_id", -1);

        btnBack.setOnClickListener(v -> finish());

        btnXacNhan.setOnClickListener(v -> saveTreatment());
    }

    private void saveTreatment() {

        String medicine = edtMedicineName.getText().toString().trim();
        String method = edtMethod.getText().toString().trim();
        String timesStr = edtTimes.getText().toString().trim();
        String purpose = edtPurpose.getText().toString().trim();
        String guide = edtGuide.getText().toString().trim();

        if (medicine.isEmpty() || method.isEmpty() || timesStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        int times = Integer.parseInt(timesStr);

        boolean success = repository.insertTreatment(
                patientId,
                medicine,
                method,
                times,
                purpose,
                guide
        );

        if (success) {
            Toast.makeText(this, "Thêm phác đồ thành công", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}
