package com.example.btl_android;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.btl_android.Database.DatabaseHelper;
import com.example.btl_android.Object.Patient1;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Locale;

public class ProfileEditActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private TextInputEditText edtEditName, edtEditPhone, edtEditAddress, edtEditGender, edtEditDob;
    private Button btnSaveChanges, btnChangeImage;
    private ImageView profileImage;
    private DatabaseHelper db;
    private Patient1 currentPatient1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        db = DatabaseHelper.getInstance(this);

        edtEditName = findViewById(R.id.edtEditName);
        edtEditPhone = findViewById(R.id.edtEditPhone);
        edtEditAddress = findViewById(R.id.edtEditAddress);
        edtEditGender = findViewById(R.id.edtEditGender);
        edtEditDob = findViewById(R.id.edtEditDob);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnChangeImage = findViewById(R.id.btn_change_image);
        profileImage = findViewById(R.id.profile_image);

        // Make DoB field non-editable by keyboard but clickable for dialog
        edtEditDob.setFocusable(false);
        edtEditDob.setClickable(true);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("PATIENT_DATA")) {
            currentPatient1 = (Patient1) intent.getSerializableExtra("PATIENT_DATA");
            if (currentPatient1 != null) {
                edtEditName.setText(currentPatient1.getFullName());
                edtEditPhone.setText(currentPatient1.getPhone());
                edtEditAddress.setText(currentPatient1.getAddress());
                edtEditGender.setText(currentPatient1.getGender());
                edtEditDob.setText(currentPatient1.getDob());

                if (currentPatient1.getProfileImagePath() != null && !currentPatient1.getProfileImagePath().isEmpty()) {
                    imageUri = Uri.parse(currentPatient1.getProfileImagePath());
                    Glide.with(this).load(imageUri).into(profileImage);
                }
            }
        }

        btnChangeImage.setOnClickListener(v -> openImageChooser());
        btnSaveChanges.setOnClickListener(v -> saveProfileChanges());
        edtEditDob.setOnClickListener(v -> showDatePickerDialog());
    }

    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = String.format(Locale.getDefault(), "%d/%d/%d", dayOfMonth, monthOfYear + 1, year1);
                    edtEditDob.setText(selectedDate);
                },
                year, month, day);
        datePickerDialog.show();
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
            Glide.with(this).load(imageUri).into(profileImage);
        }
    }

    private void saveProfileChanges() {
        if (currentPatient1 == null) {
            Toast.makeText(this, "Lỗi: Không có thông tin bệnh nhân!", Toast.LENGTH_SHORT).show();
            return;
        }

        currentPatient1.setFullName(edtEditName.getText().toString());
        currentPatient1.setPhone(edtEditPhone.getText().toString());
        currentPatient1.setAddress(edtEditAddress.getText().toString());
        currentPatient1.setGender(edtEditGender.getText().toString());
        currentPatient1.setDob(edtEditDob.getText().toString());

        if (imageUri != null) {
            currentPatient1.setProfileImagePath(imageUri.toString());
        }

        boolean success = db.updatePatientProfile(currentPatient1);

        if (success) {
            Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            Intent resultIntent = new Intent();
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        } else {
            Toast.makeText(this, "Lỗi khi cập nhật thông tin!", Toast.LENGTH_SHORT).show();
        }
    }
}
