package com.example.btl_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button patientButton = findViewById(R.id.patient_button);
        Button doctorButton = findViewById(R.id.doctor_button);

        patientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThemeRoleManager.applyRoleTheme(LoginActivity.this, ThemeRoleManager.ROLE_PATIENT);
                Intent intent = new Intent(LoginActivity.this, MainActivity1.class);
                startActivity(intent);
            }
        });

        doctorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThemeRoleManager.applyRoleTheme(LoginActivity.this, ThemeRoleManager.ROLE_DOCTOR);
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}
