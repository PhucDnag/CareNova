package com.example.btl_android;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.btl_android.Database.DatabaseHelper;
import com.example.btl_android.Object.Patient1;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class PersonalInfoActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private TextInputEditText edtName, edtPhone, edtEmail, edtDob, edtAddress;
    private RadioGroup radioGender;
    private RadioButton radioMale, radioFemale;
    private MaterialButton btnEdit, btnSave, btnChangeImage;
    private ImageButton btnBack;
    private ImageView imgAvatar;

    private boolean isEditMode = false;
    private DatabaseHelper db;
    private Patient1 currentPatient;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        db = DatabaseHelper.getInstance(this);

        // Find views
        btnBack = findViewById(R.id.btnBack);
        btnEdit = findViewById(R.id.btnEdit);
        btnSave = findViewById(R.id.btnSave);
        btnChangeImage = findViewById(R.id.btnChangeImage);
        imgAvatar = findViewById(R.id.imgAvatar);
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.edtEmail);
        radioGender = findViewById(R.id.radioGender);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);
        edtDob = findViewById(R.id.edtDob);
        edtAddress = findViewById(R.id.edtAddress);

        // Load patient data
        loadPatientData();

        // Set initial state (view mode)
        toggleEditMode(false);

        // Set listeners
        btnBack.setOnClickListener(v -> finish());
        btnEdit.setOnClickListener(v -> toggleEditMode(true));
        btnSave.setOnClickListener(v -> saveChanges());
        btnChangeImage.setOnClickListener(v -> openImageChooser());
        edtDob.setOnClickListener(v -> {
            if (isEditMode) {
                showDatePickerDialog();
            }
        });
    }

    private void loadPatientData() {
        currentPatient = db.getPatientProfile(1); // Assuming patient ID is 1
        if (currentPatient != null) {
            edtName.setText(currentPatient.getFullName());
            edtPhone.setText(currentPatient.getPhone());
            edtAddress.setText(currentPatient.getAddress());
            edtDob.setText(currentPatient.getDob());
            edtEmail.setText("mol@gmail.com"); // Placeholder

            if ("Nam".equalsIgnoreCase(currentPatient.getGender())) {
                radioMale.setChecked(true);
            } else {
                radioFemale.setChecked(true);
            }

            if (currentPatient.getProfileImagePath() != null && !currentPatient.getProfileImagePath().isEmpty()) {
                imageUri = Uri.parse(currentPatient.getProfileImagePath());
                Glide.with(this).load(imageUri).into(imgAvatar);
            }
        }
    }

    private void toggleEditMode(boolean enable) {
        isEditMode = enable;
        edtName.setEnabled(enable);
        edtPhone.setEnabled(enable);
        edtEmail.setEnabled(enable);
        edtAddress.setEnabled(enable);
        radioMale.setEnabled(enable);
        radioFemale.setEnabled(enable);
        edtDob.setFocusable(enable);
        edtDob.setFocusableInTouchMode(enable);

        if (enable) {
            btnEdit.setVisibility(View.GONE);
            btnSave.setVisibility(View.VISIBLE);
            btnChangeImage.setVisibility(View.VISIBLE);
        } else {
            btnEdit.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.GONE);
            btnChangeImage.setVisibility(View.GONE);
        }
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Glide.with(this).load(imageUri).into(imgAvatar);
        }
    }

    private void saveChanges() {
        if (currentPatient == null) return;

        currentPatient.setFullName(edtName.getText().toString());
        currentPatient.setPhone(edtPhone.getText().toString());
        currentPatient.setAddress(edtAddress.getText().toString());
        currentPatient.setDob(edtDob.getText().toString());
        currentPatient.setGender(radioMale.isChecked() ? "Nam" : "Nữ");

        if (imageUri != null) {
            currentPatient.setProfileImagePath(imageUri.toString());
        }

        boolean success = db.updatePatientProfile(currentPatient);
        if (success) {
            Toast.makeText(this, "Lưu thông tin thành công!", Toast.LENGTH_SHORT).show();
            toggleEditMode(false);
            setResult(Activity.RESULT_OK); // Set result for ProfileFragment to reload
        } else {
            Toast.makeText(this, "Lỗi khi lưu thông tin!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> edtDob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1),
                year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void finish() {
        super.finish();
        // Reload data in ProfileFragment when returning
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
    }
}
