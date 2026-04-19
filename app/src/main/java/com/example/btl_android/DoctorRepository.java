package com.example.btl_android;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.btl_android.Database.DatabaseHelper;
import com.example.btl_android.Object.Doctor;

public class DoctorRepository {

    private DatabaseHelper dbHelper;

    public DoctorRepository(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    public Doctor getById(int doctorId) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM doctors WHERE id=?",
                new String[]{String.valueOf(doctorId)}
        );

        Doctor doctor = null;

        if (cursor.moveToFirst()) {
            doctor = new Doctor(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4)
            );
        }

        cursor.close();

        return doctor;
    }
    public Doctor getFirstDoctor() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                "doctors",
                null,
                null,
                null,
                null,
                null,
                "id ASC",
                "1"
        );

        Doctor doctor = null;

        if (cursor != null && cursor.moveToFirst()) {

            doctor = new Doctor(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("specialty")),
                    cursor.getString(cursor.getColumnIndexOrThrow("phone")),
                    cursor.getString(cursor.getColumnIndexOrThrow("education"))
            );
        }

        if (cursor != null) cursor.close();

        return doctor;
    }

}
