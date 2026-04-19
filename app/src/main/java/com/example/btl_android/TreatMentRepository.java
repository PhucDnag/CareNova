package com.example.btl_android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.example.btl_android.Database.DatabaseHelper;
import com.example.btl_android.Object.Patient;
import com.example.btl_android.Object.TreatmentItem;

public class TreatMentRepository {
    private DatabaseHelper dbHelper;

    public TreatMentRepository(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    // Lấy danh sách bệnh nhân có phác đồ
    public List<TreatmentItem> getAllTreatments() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<TreatmentItem> list = new ArrayList<>();

        String query =
                "SELECT t.id, p.id, p.full_name, t.medicine_name " +
                        "FROM treatment_plans t " +
                        "INNER JOIN medical_records r ON t.record_id = r.id " +
                        "INNER JOIN patients p ON r.patient_id = p.id";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {

                int treatmentId = cursor.getInt(0);
                int patientId = cursor.getInt(1);
                String patientName = cursor.getString(2);
                String medicine = cursor.getString(3);

                list.add(new TreatmentItem(
                        treatmentId,
                        patientId,
                        patientName,
                        medicine
                ));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
    }


    public Cursor getTreatmentByPatientId(int patientId) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query =
                "SELECT p.full_name, p.gender, p.dob, " +
                        "t.medicine_name, t.method, t.times_per_day, " +
                        "t.purpose, t.guide " +
                        "FROM patients p " +
                        "INNER JOIN medical_records r ON p.id = r.patient_id " +
                        "INNER JOIN treatment_plans t ON r.id = t.record_id " +
                        "WHERE p.id = ?";

        return db.rawQuery(query, new String[]{String.valueOf(patientId)});
    }

    public boolean insertTreatment(int patientId,
                                   String medicine,
                                   String method,
                                   int times,
                                   String purpose,
                                   String guide) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Lấy recordId theo patientId
        Cursor cursor = db.rawQuery(
                "SELECT id FROM medical_records WHERE patient_id = ? ORDER BY id DESC LIMIT 1",
                new String[]{String.valueOf(patientId)}
        );

        int recordId = -1;

        if (cursor.moveToFirst()) {
            recordId = cursor.getInt(0);
        }

        cursor.close();

        if (recordId == -1) {
            db.close();
            return false;
        }

        ContentValues values = new ContentValues();
        values.put("record_id", recordId);
        values.put("medicine_name", medicine);
        values.put("method", method);
        values.put("times_per_day", times);
        values.put("purpose", purpose);
        values.put("guide", guide);

        long result = db.insert("treatment_plans", null, values);

        db.close();

        return result != -1;
    }

    public Cursor getTreatmentById(int treatmentId) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query =
                "SELECT p.id, p.full_name, p.gender, p.dob, " +
                        "t.medicine_name, t.method, t.times_per_day, " +
                        "t.purpose, t.guide " +
                        "FROM treatment_plans t " +
                        "INNER JOIN medical_records r ON t.record_id = r.id " +
                        "INNER JOIN patients p ON r.patient_id = p.id " +
                        "WHERE t.id = ?";

        return db.rawQuery(query,
                new String[]{String.valueOf(treatmentId)});
    }

    public boolean updateTreatment(int treatmentId,
                                   String medicine,
                                   String method,
                                   int times,
                                   String purpose,
                                   String guide) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("medicine_name", medicine);
        values.put("method", method);
        values.put("times_per_day", times);
        values.put("purpose", purpose);
        values.put("guide", guide);

        int rows = db.update(
                "treatment_plans",
                values,
                "id = ?",
                new String[]{String.valueOf(treatmentId)}
        );

        db.close();

        return rows > 0;
    }

    public boolean deleteTreatment(int treatmentId) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int rows = db.delete(
                "treatment_plans",
                "id = ?",
                new String[]{String.valueOf(treatmentId)}
        );

        db.close();

        return rows > 0;
    }


}
