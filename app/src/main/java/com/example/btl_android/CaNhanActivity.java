package com.example.btl_android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.switchmaterial.SwitchMaterial;

import com.example.btl_android.Database.DatabaseHelper;
import com.example.btl_android.Object.Doctor;
import com.example.btl_android.DoctorRepository;

public class CaNhanActivity extends AppCompatActivity {

    LinearLayout navHome, navLichKham, navThongBao;
    LinearLayout layoutLogout;
    com.google.android.material.card.MaterialCardView layoutLanguage;
    SwitchMaterial switchDarkMode;

    ImageView btnBack;

    TextView txtDoctorName, txtSpecialty, txtPhone, tvDoctorLanguage;
    TextView txtEducation1, txtEducation2;

    DoctorRepository doctorRepository;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ca_nhan);

        doctorRepository = new DoctorRepository(this);

        // ===== FIND VIEW =====
        navHome = findViewById(R.id.nav_home);
        navLichKham = findViewById(R.id.nav_lichkham);
        navThongBao = findViewById(R.id.nav_thongbao);

        txtDoctorName = findViewById(R.id.txtDoctorName);
        txtSpecialty = findViewById(R.id.txtSpecialty);
        txtPhone = findViewById(R.id.txtPhone);
        txtEducation1 = findViewById(R.id.txtEducation1);
        txtEducation2 = findViewById(R.id.txtEducation2);
        btnBack = findViewById(R.id.btn_back);
        layoutLogout = findViewById(R.id.layout_logout);
        layoutLanguage = findViewById(R.id.layout_language);
        tvDoctorLanguage = findViewById(R.id.tvDoctorLanguage);
        switchDarkMode = findViewById(R.id.switch_dark_mode);

        btnBack.setOnClickListener(v -> finish());

        // ===== LOAD DATA =====
        loadDoctorInfo();
        setupDarkMode();
        updateLanguageLabel();

        // ===== TRANG CHỦ =====
        navHome.setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class));
        });

        // ===== LỊCH KHÁM =====
        navLichKham.setOnClickListener(v -> {
            startActivity(new Intent(this, LichKhamActivity.class));
        });

        // ===== THÔNG BÁO =====
        navThongBao.setOnClickListener(v -> {
            startActivity(new Intent(this, ThongBaoActivity.class));
        });

        layoutLanguage.setOnClickListener(v -> showLanguagePicker());

        // ===== ĐĂNG XUẤT =====
        layoutLogout.setOnClickListener(v -> {
            Intent intent = new Intent(CaNhanActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Toast.makeText(CaNhanActivity.this, getString(R.string.doctor_logout), Toast.LENGTH_SHORT).show();
        });
    }

    private void setupDarkMode() {
        boolean isDark = ThemeRoleManager.isRoleDarkMode(this, ThemeRoleManager.ROLE_DOCTOR);
        switchDarkMode.setChecked(isDark);

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ThemeRoleManager.setRoleDarkMode(this, ThemeRoleManager.ROLE_DOCTOR, isChecked);
            ThemeRoleManager.applyRoleTheme(this, ThemeRoleManager.ROLE_DOCTOR);
        });
    }

    private void loadDoctorInfo() {

        Doctor doctor = doctorRepository.getFirstDoctor();

        if (doctor != null) {

            txtDoctorName.setText(doctor.getName());
            txtSpecialty.setText(doctor.getSpecialty());
            txtPhone.setText(doctor.getPhone());

            // Nếu muốn tách học vấn thành nhiều dòng
            txtEducation1.setText("- " + doctor.getEducation());
            txtEducation2.setText("");
        }
    }

    private void showLanguagePicker() {
        String[] languageNames = {
                getString(R.string.language_vietnamese),
                getString(R.string.language_english)
        };
        String[] languageCodes = {"vi", "en"};

        String currentLanguage = AppCompatDelegate.getApplicationLocales().toLanguageTags();
        int checkedIndex = currentLanguage.startsWith("en") ? 1 : 0;

        new AlertDialog.Builder(this)
                .setTitle(R.string.choose_language)
                .setSingleChoiceItems(languageNames, checkedIndex, (dialog, which) -> {
                    dialog.dismiss();
                    LanguageManager.setLanguageAndRestart(this, languageCodes[which]);
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void updateLanguageLabel() {
        String currentLanguage = AppCompatDelegate.getApplicationLocales().toLanguageTags();
        if (currentLanguage.startsWith("en")) {
            tvDoctorLanguage.setText(getString(R.string.language_row_title, getString(R.string.language_english)));
        } else {
            tvDoctorLanguage.setText(getString(R.string.language_row_title, getString(R.string.language_vietnamese)));
        }
    }
}
