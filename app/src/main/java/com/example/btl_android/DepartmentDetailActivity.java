package com.example.btl_android;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_detail);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Quay lại màn hình trước đó
            }
        });

        // Thiết lập RecyclerView cho danh sách bác sĩ
        RecyclerView recyclerDoctors = findViewById(R.id.recyclerDoctors);
        recyclerDoctors.setLayoutManager(new LinearLayoutManager(this));

        // Tạo dữ liệu mẫu cho danh sách bác sĩ
        List<Doctor> doctorList = new ArrayList<>();
        doctorList.add(new Doctor("BS. Nguyễn Văn A", "Chuyên khoa Nội tổng quát", R.drawable.sample_doctor));
        doctorList.add(new Doctor("BS. Trần Thị B", "Chuyên khoa Nội tổng quát", R.drawable.sample_doctor));
        doctorList.add(new Doctor("BS. Lê Văn C", "Chuyên khoa Nội tổng quát", R.drawable.sample_doctor));

        DoctorAdapter adapter = new DoctorAdapter(doctorList);
        recyclerDoctors.setAdapter(adapter);
    }
}