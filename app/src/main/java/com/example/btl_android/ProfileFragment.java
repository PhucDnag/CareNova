package com.example.btl_android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.btl_android.Database.DatabaseHelper;
import com.example.btl_android.Object.Patient1;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private TextView tvProfileName, tvProfileId, tvInfor, tvDisplay;
    private TextView tvMenuHistory, tvMenuReminder, tvMenuFeedback, tvMenuLogout;
    private SwitchMaterial switchDarkModePatient;
    private ImageView ivProfileImage;
    private DatabaseHelper db;
    private Patient1 currentPatient1;
    private static final int PERSONAL_INFO_REQUEST = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_profile1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = DatabaseHelper.getInstance(getContext());

        tvProfileName = view.findViewById(R.id.tvProfileName);
        tvProfileId = view.findViewById(R.id.tvProfileId);
        ivProfileImage = view.findViewById(R.id.ivProfileImage);
        tvMenuHistory = view.findViewById(R.id.tvMenuHistory);
        tvMenuReminder = view.findViewById(R.id.tvMenuReminder);
        tvMenuFeedback = view.findViewById(R.id.tvMenuFeedback);
        tvMenuLogout = view.findViewById(R.id.tvMenuLogout);
        tvInfor = view.findViewById(R.id.tvinfor);
        tvDisplay = view.findViewById(R.id.tvdisplay);
        switchDarkModePatient = view.findViewById(R.id.switch_dark_mode_patient);

        loadPatientData();
        setupDarkModeToggle();
        setupClickListeners();
    }

    private void loadPatientData() {
        currentPatient1 = db.getPatientProfile(1);

        if (currentPatient1 != null) {
            tvProfileName.setText(currentPatient1.getFullName());
            tvProfileId.setText("Mã số bệnh nhân: " + String.format("%06d", currentPatient1.getId()));

            if (currentPatient1.getProfileImagePath() != null && !currentPatient1.getProfileImagePath().isEmpty()) {
                Uri imageUri = Uri.parse(currentPatient1.getProfileImagePath());
                Glide.with(getContext()).load(imageUri).placeholder(R.drawable.ic_profile).into(ivProfileImage);
            } else {
                ivProfileImage.setImageResource(R.drawable.ic_profile);
            }
        } else {
            Toast.makeText(getContext(), "Lỗi: Không tải được thông tin cá nhân.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupDarkModeToggle() {
        boolean isDark = ThemeRoleManager.isRoleDarkMode(requireContext(), ThemeRoleManager.ROLE_PATIENT);
        switchDarkModePatient.setChecked(isDark);

        switchDarkModePatient.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ThemeRoleManager.setRoleDarkMode(requireContext(), ThemeRoleManager.ROLE_PATIENT, isChecked);
            ThemeRoleManager.applyRoleTheme(requireContext(), ThemeRoleManager.ROLE_PATIENT);
        });
    }

    private void setupClickListeners() {
        tvInfor.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PersonalInfoActivity.class);
            startActivityForResult(intent, PERSONAL_INFO_REQUEST);
        });

        tvMenuHistory.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MedicalHistoryActivity.class);
            startActivity(intent);
        });

        tvMenuReminder.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SetReminderActivity.class);
            startActivity(intent);
        });

        tvDisplay.setOnClickListener(v -> switchDarkModePatient.toggle());
        tvMenuFeedback.setOnClickListener(v -> Toast.makeText(getContext(), "Chức năng Phản hồi sẽ được phát triển sau!", Toast.LENGTH_SHORT).show());

        tvMenuLogout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Toast.makeText(getContext(), "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERSONAL_INFO_REQUEST && resultCode == RESULT_OK) {
            // Reload data from the database to reflect changes
            loadPatientData();
        }
    }
}
