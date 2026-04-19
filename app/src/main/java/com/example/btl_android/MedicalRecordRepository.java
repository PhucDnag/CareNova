package com.example.btl_android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.example.btl_android.Database.DatabaseHelper;
import com.example.btl_android.Object.MedicalRecord;

public class MedicalRecordRepository {

    private DatabaseHelper dbHelper;

    public MedicalRecordRepository(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }


    // ================= LẤY THEO ID =================
    public MedicalRecord getById(int id) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM medical_records WHERE id=?",
                new String[]{String.valueOf(id)}
        );

        MedicalRecord record = null;

        if (cursor != null && cursor.moveToFirst()) {

            record = new MedicalRecord(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("patient_id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("doctor_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("visit_date")),
                    cursor.getString(cursor.getColumnIndexOrThrow("diagnosis")),
                    cursor.getString(cursor.getColumnIndexOrThrow("prescription")),
                    cursor.getString(cursor.getColumnIndexOrThrow("doctor_notes"))
            );
        }

        if (cursor != null) cursor.close();

        return record;
    }

    // ================= LẤY THEO DOCTOR =================
    public List<MedicalRecord> getByDoctorId(int doctorId) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<MedicalRecord> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM medical_records WHERE doctor_id=? ORDER BY id DESC",
                new String[]{String.valueOf(doctorId)}
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {

                MedicalRecord record = new MedicalRecord(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("patient_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("doctor_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("visit_date")),
                        cursor.getString(cursor.getColumnIndexOrThrow("diagnosis")),
                        cursor.getString(cursor.getColumnIndexOrThrow("prescription")),
                        cursor.getString(cursor.getColumnIndexOrThrow("doctor_notes"))
                );

                list.add(record);

            } while (cursor.moveToNext());
        }

        if (cursor != null) cursor.close();

        return list;
    }

    public long insert(int patientId,
                       int doctorId,
                       String visitDate,
                       String diagnosis,
                       String notes) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("patient_id", patientId);
        values.put("doctor_id", doctorId);
        values.put("visit_date", visitDate);
        values.put("diagnosis", diagnosis);
        values.put("doctor_notes", notes);

        return db.insert("medical_records", null, values);
    }

    public int update(MedicalRecord record) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("diagnosis", record.getDiagnosis());
        values.put("doctor_notes", record.getDoctorNotes());

        return db.update(
                "medical_records",
                values,
                "id=?",
                new String[]{String.valueOf(record.getId())}
        );
    }

    public int delete(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        return db.delete(
                "medical_records",
                "id = ?",
                new String[]{String.valueOf(id)}
        );
    }

    public MedicalRecord getLastMedicalRecord(int patientId) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                "medical_records",
                null,
                "patient_id=?",
                new String[]{String.valueOf(patientId)},
                null, null,
                "id DESC",
                "1"
        );

        MedicalRecord record = null;

        if (cursor != null && cursor.moveToFirst()) {

            record = new MedicalRecord(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("patient_id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("doctor_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("visit_date")),
                    cursor.getString(cursor.getColumnIndexOrThrow("diagnosis")),
                    cursor.getString(cursor.getColumnIndexOrThrow("prescription")),
                    cursor.getString(cursor.getColumnIndexOrThrow("doctor_notes"))
            );
        }

        if (cursor != null) cursor.close();

        return record;
    }

}
