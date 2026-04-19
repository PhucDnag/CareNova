package com.example.btl_android;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_android.R;
import com.example.btl_android.TreatMentRepository;

public class ChinhSuaPhacDoActivity extends AppCompatActivity {

    ImageView btnBack;
    Button btnXacNhanSua;

    EditText edtMedicineName, edtMethod, edtTimes, edtPurpose, edtGuide;

    TreatMentRepository repository;

    int treatmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chinh_sua_phac_do);

        btnBack = findViewById(R.id.btn_back);
        btnXacNhanSua = findViewById(R.id.btn_xac_nhan_sua);

        edtMedicineName = findViewById(R.id.edtMedicineName);
        edtMethod = findViewById(R.id.edtMethod);
        edtTimes = findViewById(R.id.edtTimes);
        edtPurpose = findViewById(R.id.edtPurpose);
        edtGuide = findViewById(R.id.edtGuide);

        repository = new TreatMentRepository(this);

        treatmentId = getIntent().getIntExtra("treatment_id", -1);

        btnBack.setOnClickListener(v -> finish());

        loadData();

        btnXacNhanSua.setOnClickListener(v -> updateTreatment());
    }

    private void loadData() {
        Cursor cursor = repository.getTreatmentById(treatmentId);

        if (cursor != null && cursor.moveToFirst()) {
            edtMedicineName.setText(cursor.getString(4));
            edtMethod.setText(cursor.getString(5));
            edtTimes.setText(String.valueOf(cursor.getInt(6)));
            edtPurpose.setText(cursor.getString(7));
            edtGuide.setText(cursor.getString(8));
        }

        if (cursor != null) cursor.close();
    }

    private void updateTreatment() {

        String medicine = edtMedicineName.getText().toString().trim();
        String method = edtMethod.getText().toString().trim();
        String timesStr = edtTimes.getText().toString().trim();
        String purpose = edtPurpose.getText().toString().trim();
        String guide = edtGuide.getText().toString().trim();

        if (medicine.isEmpty() || method.isEmpty() || timesStr.isEmpty()) {
            Toast.makeText(this,
                    "Vui lòng nhập đầy đủ thông tin",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        int times = Integer.parseInt(timesStr);

        boolean success = repository.updateTreatment(
                treatmentId,
                medicine,
                method,
                times,
                purpose,
                guide
        );

        if (success) {
            Toast.makeText(this,
                    "Chỉnh sửa thành công",
                    Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this,
                    "Chỉnh sửa thất bại",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
