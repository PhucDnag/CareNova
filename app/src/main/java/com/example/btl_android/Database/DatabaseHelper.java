package com.example.btl_android.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.btl_android.Object.Appointment;
import com.example.btl_android.Object.MedicalRecord1;
import com.example.btl_android.Object.Notification;
import com.example.btl_android.Object.Patient1;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper instance;

    private static final String DATABASE_NAME = "BenhAnDienTu.db";
    private static final int DATABASE_VERSION = 9;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE doctors (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, specialty TEXT, phone TEXT, education TEXT)");

        db.execSQL("CREATE TABLE patients (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "full_name TEXT, dob TEXT, gender TEXT, phone TEXT, address TEXT, medical_history TEXT, profile_image_path TEXT)");

        db.execSQL("CREATE TABLE appointments (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "patient_id INTEGER, doctor_id INTEGER, " +
                "patient_name TEXT, doctor_name TEXT, specialty TEXT, " +
                "appointment_date TEXT, appointment_time TEXT, status TEXT)");

        db.execSQL("CREATE TABLE medical_records (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "patient_id INTEGER, doctor_id INTEGER, doctor_name TEXT, " +
                "appointment_id INTEGER, visit_date TEXT, diagnosis TEXT, prescription TEXT, doctor_notes TEXT)");

        db.execSQL("CREATE TABLE treatment_plans (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "record_id INTEGER, medicine_name TEXT, method TEXT, " +
                "times_per_day INTEGER, purpose TEXT, guide TEXT)");

        db.execSQL("CREATE TABLE notifications (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, message TEXT, time TEXT, icon_res_id INTEGER)");

        insertSampleDoctor(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 7) {
            db.execSQL("ALTER TABLE patients ADD COLUMN profile_image_path TEXT;");
        } else {
            db.execSQL("DROP TABLE IF EXISTS notifications");
            db.execSQL("DROP TABLE IF EXISTS treatment_plans");
            db.execSQL("DROP TABLE IF EXISTS medical_records");
            db.execSQL("DROP TABLE IF EXISTS appointments");
            db.execSQL("DROP TABLE IF EXISTS patients");
            db.execSQL("DROP TABLE IF EXISTS doctors");
            onCreate(db);
        }
    }

    // ================= PATIENT =================

    public long addPatient(Patient1 patient1) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("full_name", patient1.getFullName());
        v.put("dob", patient1.getDob());
        v.put("gender", patient1.getGender());
        v.put("phone", patient1.getPhone());
        v.put("address", patient1.getAddress());
        v.put("medical_history", patient1.getMedicalHistory());
        v.put("profile_image_path", patient1.getProfileImagePath());
        return db.insert("patients", null, v);
    }

    public Patient1 getPatientProfile(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Patient1 p = null;

        Cursor c = db.query("patients", null, "id=?",
                new String[]{String.valueOf(id)},
                null, null, null);

        if (c != null && c.moveToFirst()) {
            p = new Patient1(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4),
                    c.getString(5),
                    c.getString(6),
                    c.getString(7)
            );
            c.close();
        }
        return p;
    }

    public boolean updatePatientProfile(Patient1 p) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("full_name", p.getFullName());
        v.put("dob", p.getDob());
        v.put("gender", p.getGender());
        v.put("phone", p.getPhone());
        v.put("address", p.getAddress());
        v.put("medical_history", p.getMedicalHistory());
        v.put("profile_image_path", p.getProfileImagePath());
        return db.update("patients", v, "id=?",
                new String[]{String.valueOf(p.getId())}) > 0;
    }

    // ================= APPOINTMENT =================

    public long addAppointment(Appointment a) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("patient_id", a.getPatientId());
        v.put("patient_name", a.getPatientName());
        v.put("doctor_name", a.getDoctorName());
        v.put("specialty", a.getSpecialty());
        v.put("appointment_date", a.getAppointmentDate());
        v.put("appointment_time", a.getAppointmentTime());
        v.put("status", a.getStatus());
        return db.insert("appointments", null, v);
    }

    public List<Appointment> getAllAppointments(int patientId) {
        List<Appointment> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.query("appointments", null,
                "patient_id=?",
                new String[]{String.valueOf(patientId)},
                null, null, "id DESC");

        if (c.moveToFirst()) {
            do {
                list.add(new Appointment(
                        c.getInt(0),
                        c.getInt(1),
                        c.getString(3),
                        c.getString(4),
                        c.getString(5),
                        c.getString(6),
                        c.getString(7),
                        c.getString(8)
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public List<Appointment> getAllAppointmentsForDoctor() {
        List<Appointment> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.query("appointments", null, null, null, null, null, "id DESC");

        if (c.moveToFirst()) {
            do {
                list.add(new Appointment(
                        c.getInt(0),
                        c.getInt(1),
                        c.getString(3),
                        c.getString(4),
                        c.getString(5),
                        c.getString(6),
                        c.getString(7),
                        c.getString(8)
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public boolean confirmAppointment(int id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("status", "Đã xác nhận");
        return db.update("appointments", v, "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean cancelAppointment(int id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("status", "Đã hủy");
        return db.update("appointments", v, "id=?",
                new String[]{String.valueOf(id)}) > 0;
    }

    // ================= MEDICAL RECORD =================

    public long addMedicalRecord(MedicalRecord1 r) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("patient_id", r.getPatientId());
        v.put("doctor_id", r.getDoctorId());
        v.put("appointment_id", r.getAppointmentId());
        v.put("doctor_name", r.getDoctorName());
        v.put("visit_date", r.getVisitDate());
        v.put("diagnosis", r.getDiagnosis());
        v.put("prescription", r.getPrescription());
        v.put("doctor_notes", r.getDoctorNotes());
        return db.insert("medical_records", null, v);
    }

    public List<MedicalRecord1> getMedicalHistory(int patientId) {
        List<MedicalRecord1> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.query("medical_records", null,
                "patient_id=?",
                new String[]{String.valueOf(patientId)},
                null, null, "id DESC");

        if (c.moveToFirst()) {
            do {
                list.add(new MedicalRecord1(
                        c.getInt(0),
                        c.getInt(1),
                        c.getInt(2),
                        c.getInt(4),
                        c.getString(3),
                        c.getString(5),
                        c.getString(6),
                        c.getString(7),
                        c.getString(8)
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public MedicalRecord1 getRecordForAppointment(int appointmentId) {
        SQLiteDatabase db = getReadableDatabase();
        MedicalRecord1 r = null;

        Cursor c = db.query("medical_records", null,
                "appointment_id=?",
                new String[]{String.valueOf(appointmentId)},
                null, null, null);

        if (c != null && c.moveToFirst()) {
            r = new MedicalRecord1(
                    c.getInt(0),
                    c.getInt(1),
                    c.getInt(4),
                    c.getInt(2),
                    c.getString(3),
                    c.getString(5),
                    c.getString(6),
                    c.getString(7),
                    c.getString(8)
            );
            c.close();
        }
        return r;
    }

    // ================= NOTIFICATION =================

    public void addNotification(Notification n) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("title", n.getTitle());
        v.put("message", n.getMessage());
        v.put("time", n.getTime());
        v.put("icon_res_id", n.getIconResId());
        db.insert("notifications", null, v);
    }

    public List<Notification> getAllNotifications() {
        List<Notification> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.query("notifications", null,
                null, null, null, null, "id DESC");

        if (c.moveToFirst()) {
            do {
                list.add(new Notification(
                        c.getString(1),
                        c.getString(2),
                        c.getString(3),
                        c.getInt(4)
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    // ================= SAMPLE DOCTOR =================

    private void insertSampleDoctor(SQLiteDatabase db) {
        if (DatabaseUtils.queryNumEntries(db, "doctors") == 0) {
            ContentValues v = new ContentValues();
            v.put("name", "Nguyễn Thị A");
            v.put("specialty", "Nội tổng hợp");
            v.put("phone", "0987654321");
            v.put("education", "ĐH Y Dược TP.HCM");
            db.insert("doctors", null, v);
        }
    }
    public void createDefaultUserIfEmpty() {
        SQLiteDatabase db = this.getReadableDatabase();

        if (DatabaseUtils.queryNumEntries(db, "patients") == 0) {

            // ===== Tạo bệnh nhân mặc định =====
            Patient1 defaultPatient1 = new Patient1(
                    0,
                    "Nguyễn Bông Min",
                    "21/12/2005",
                    "Nam",
                    "0987654321",
                    "Hà Nội",
                    "Không có tiền sử bệnh lý",
                    null
            );

            long patientId = addPatient(defaultPatient1);

            // ===== Tạo lịch hẹn mẫu =====
            Appointment appointment = new Appointment(
                    0,
                    (int) patientId,
                    "Nguyễn Bông Min",
                    "BS. Nguyễn Thị A",
                    "Nội tổng hợp",
                    "25/12/2024",
                    "09:00",
                    "Chờ duyệt"
            );

            long appointmentId = addAppointment(appointment);

            // ===== Tạo hồ sơ bệnh án mẫu =====
            MedicalRecord1 record = new MedicalRecord1(
                    0,
                    (int) patientId,
                    1,
                    (int) appointmentId,
                    "BS. Nguyễn Thị A",
                    "25/12/2024",
                    "Viêm da cơ địa",
                    "- Thuốc bôi ngoài da Hydrocortisone\n- Uống Loratadin 10mg",
                    "Tái khám sau 7 ngày"
            );

            // Lấy recordId để làm khóa ngoại cho phác đồ điều trị
            long recordId = addMedicalRecord(record);

            // ===== Tạo phác đồ điều trị mẫu (Treatment Plans) =====
            if (recordId != -1) {
                SQLiteDatabase dbWrite = this.getWritableDatabase();

                // Thuốc 1: Hydrocortisone
                ContentValues plan1 = new ContentValues();
                plan1.put("record_id", recordId);
                plan1.put("medicine_name", "Thuốc bôi Hydrocortisone 1%");
                plan1.put("method", "Bôi ngoài da");
                plan1.put("times_per_day", 2);
                plan1.put("purpose", "Giảm viêm, ngứa, sưng đỏ");
                plan1.put("guide", "Bôi một lớp mỏng lên vùng da tổn thương vào sáng và tối.");
                dbWrite.insert("treatment_plans", null, plan1);

                // Thuốc 2: Loratadin 10mg
                ContentValues plan2 = new ContentValues();
                plan2.put("record_id", recordId);
                plan2.put("medicine_name", "Loratadin 10mg");
                plan2.put("method", "Uống");
                plan2.put("times_per_day", 1);
                plan2.put("purpose", "Kháng histamin, giảm dị ứng");
                plan2.put("guide", "Uống 1 viên vào buổi tối sau khi ăn.");
                dbWrite.insert("treatment_plans", null, plan2);
            }

            // ===== Tạo thông báo mẫu =====
            addNotification(new Notification(
                    "Chào mừng bạn",
                    "Chào mừng bạn đến với ứng dụng chăm sóc sức khỏe.",
                    "1 phút trước",
                    0
            ));
        }
    }

}
