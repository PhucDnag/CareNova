package com.example.btl_android;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.example.btl_android.Database.DatabaseHelper;
import com.example.btl_android.Object.Patient;

public class PatientRepository {

    private DatabaseHelper dbHelper;

    public PatientRepository(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    public Patient getById(int id) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM patients WHERE id=?",
                new String[]{String.valueOf(id)}
        );

        Patient patient = null;

        if (cursor.moveToFirst()) {
            patient = new Patient(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6)
            );
        }

        cursor.close();

        return patient;
    }

    public List<Patient> getAllPatients() {
        List<Patient> list = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM patients", null);

        if (cursor.moveToFirst()) {
            do {
                Patient p = new Patient(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("full_name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("dob")),
                        cursor.getString(cursor.getColumnIndexOrThrow("gender")),
                        cursor.getString(cursor.getColumnIndexOrThrow("phone")),
                        cursor.getString(cursor.getColumnIndexOrThrow("address")),
                        cursor.getString(cursor.getColumnIndexOrThrow("medical_history"))
                );
                list.add(p);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return list;
    }
}
