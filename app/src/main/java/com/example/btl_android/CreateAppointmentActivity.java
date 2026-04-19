package com.example.btl_android;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_android.Database.DatabaseHelper;
import com.example.btl_android.Object.Appointment;
import com.example.btl_android.Object.Notification;
import com.example.btl_android.Object.Patient1;
import com.google.android.material.appbar.MaterialToolbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateAppointmentActivity extends AppCompatActivity {

    private EditText edtBookerName, edtPhone, edtPatientName, edtDate, edtTime, edtReason;
    private Button btnConfirm;
    private DatabaseHelper db;
    private Patient1 currentPatient1;
    private NotificationHelper notificationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_appointment);

        db = DatabaseHelper.getInstance(this);
        notificationHelper = new NotificationHelper(this);

        edtBookerName = findViewById(R.id.edtBookerName);
        edtPhone = findViewById(R.id.edtPhone);
        edtPatientName = findViewById(R.id.edtPatientName);
        edtDate = findViewById(R.id.edtDate);
        edtTime = findViewById(R.id.edtTime);
        edtReason = findViewById(R.id.edtReason);
        btnConfirm = findViewById(R.id.btnConfirm);
        MaterialToolbar toolbar = findViewById(R.id.toolbar);

        loadUserData();
        toolbar.setNavigationOnClickListener(v -> finish());
        setupDateTimePickers();
        btnConfirm.setOnClickListener(v -> createAppointment());
    }

    private void loadUserData() {
        currentPatient1 = db.getPatientProfile(1); // Assuming patient ID is 1
        if (currentPatient1 != null) {
            edtBookerName.setText(currentPatient1.getFullName());
            edtPhone.setText(currentPatient1.getPhone());
            edtPatientName.setText(currentPatient1.getFullName()); // Set patient name as well
        } else {
            Toast.makeText(this, "Lỗi: Không thể tải thông tin người dùng.", Toast.LENGTH_LONG).show();
        }
    }

    private void setupDateTimePickers() {
        edtDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, day) -> {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, day);
                int dayOfWeek = selectedDate.get(Calendar.DAY_OF_WEEK);

                if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                    Toast.makeText(this, "Vui lòng không chọn thứ 7 hoặc Chủ nhật", Toast.LENGTH_SHORT).show();
                } else {
                    edtDate.setText(String.format(Locale.getDefault(), "%02d/%02d/%d", day, month + 1, year));
                }
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });

        edtTime.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new TimePickerDialog(this, (view, hour, minute) -> {
                if (hour < 8 || hour >= 17) {
                    Toast.makeText(this, "Vui lòng chọn giờ hành chính (8:00 - 17:00)", Toast.LENGTH_SHORT).show();
                } else {
                    edtTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                }
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
        });
    }

    private void createAppointment() {
        String date = edtDate.getText().toString().trim();
        String time = edtTime.getText().toString().trim();
        String reason = edtReason.getText().toString().trim();

        if (TextUtils.isEmpty(date) || TextUtils.isEmpty(time)) {
            Toast.makeText(this, "Vui lòng chọn ngày và giờ khám", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(reason)) {
            Toast.makeText(this, "Vui lòng nhập lý do khám", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentPatient1 == null) {
            Toast.makeText(this, "Lỗi: Không có thông tin người dùng để đặt lịch.", Toast.LENGTH_LONG).show();
            return;
        }

        Appointment newAppointment = new Appointment(0, currentPatient1.getId(), currentPatient1.getFullName(), "BS. Nguyễn Thị A", "Nội tổng hợp", date, time, "Chờ duyệt");
        long appointmentId = db.addAppointment(newAppointment);

        if (appointmentId != -1) {
            String notifTitle = "Đặt lịch thành công";
            String notifMessage = "Lịch khám của bạn với bác sĩ đang chờ được duyệt.";
            String notifTime = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());

            db.addNotification(new Notification(notifTitle, notifMessage, notifTime, R.drawable.ic_notifications));
            notificationHelper.showNotification(notifTitle, notifMessage, false);

            Toast.makeText(this, "Đặt lịch thành công!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(CreateAppointmentActivity.this, AppointmentSuccessActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Đặt lịch thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
