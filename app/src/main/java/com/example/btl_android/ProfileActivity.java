package com.example.btl_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.switchmaterial.SwitchMaterial;

import com.example.btl_android.Database.DatabaseHelper;
import com.example.btl_android.Object.Patient1;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvProfileName, tvProfileId;
    private TextView tvMenuHistory, tvMenuReminder, tvMenuFeedback, tvMenuLogout, tvDisplay;
    private SwitchMaterial switchDarkModePatient;
    private DatabaseHelper db;
    private Patient1 currentPatient1;

    private static final int EDIT_PROFILE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile1);

        db = DatabaseHelper.getInstance(this);

        tvProfileName = findViewById(R.id.tvProfileName);
        tvProfileId = findViewById(R.id.tvProfileId);
        tvMenuHistory = findViewById(R.id.tvMenuHistory);
        tvMenuReminder = findViewById(R.id.tvMenuReminder);
        tvMenuFeedback = findViewById(R.id.tvMenuFeedback);
        tvMenuLogout = findViewById(R.id.tvMenuLogout);
        tvDisplay = findViewById(R.id.tvdisplay);
        switchDarkModePatient = findViewById(R.id.switch_dark_mode_patient);

        loadPatientData();
        setupDarkModeToggle();
        setupClickListeners();
    }

    private void loadPatientData() {
        currentPatient1 = db.getPatientProfile(1);

        if (currentPatient1 != null) {
            tvProfileName.setText(currentPatient1.getFullName());
            tvProfileId.setText("Mã số bệnh nhân: " + String.format("%06d", currentPatient1.getId()));
        }
    }

    private void setupDarkModeToggle() {
        boolean isDark = ThemeRoleManager.isRoleDarkMode(this, ThemeRoleManager.ROLE_PATIENT);
        switchDarkModePatient.setChecked(isDark);

        switchDarkModePatient.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ThemeRoleManager.setRoleDarkMode(this, ThemeRoleManager.ROLE_PATIENT, isChecked);
            ThemeRoleManager.applyRoleTheme(this, ThemeRoleManager.ROLE_PATIENT);
        });
    }

    private void setupClickListeners() {
        tvMenuHistory.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, MedicalHistoryActivity.class);
            startActivity(intent);
        });

        tvMenuReminder.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, SetReminderActivity.class);
            startActivity(intent);
        });

        tvDisplay.setOnClickListener(v -> switchDarkModePatient.toggle());
        tvMenuFeedback.setOnClickListener(v -> Toast.makeText(this, "Chức năng Phản hồi sẽ được phát triển sau!", Toast.LENGTH_SHORT).show());
        tvMenuLogout.setOnClickListener(v -> Toast.makeText(this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_PROFILE_REQUEST && resultCode == RESULT_OK) {
            loadPatientData();
        }
    }
}
