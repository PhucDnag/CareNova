package com.example.btl_android.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import com.example.btl_android.DoctorRepository;
import com.example.btl_android.Object.Doctor;
import com.example.btl_android.Object.MedicalRecord;
import com.example.btl_android.R;
import com.example.btl_android.XemBenhAnActivity;

public class MedicalRecordAdapter extends BaseAdapter {

    private Context context;
    private List<MedicalRecord> list;
    private DoctorRepository doctorRepository; // Thêm repository

    public MedicalRecordAdapter(Context context, List<MedicalRecord> list) {
        this.context = context;
        this.list = list;
        this.doctorRepository = new DoctorRepository(context); // Khởi tạo repository
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_medical_record, parent, false);
        }

        TextView tvInfo = convertView.findViewById(R.id.tvInfo);
        Button btnView = convertView.findViewById(R.id.btnView);

        MedicalRecord record = list.get(position);

        // --- BẮT ĐẦU ĐOẠN CODE LẤY TÊN BÁC SĨ ---
        Doctor doctor = doctorRepository.getById(record.getDoctorId());
        String doctorName = "Không xác định (ID: " + record.getDoctorId() + ")";
        if (doctor != null) {
            doctorName = doctor.getName();
        }
        // --- KẾT THÚC ---

        // Cập nhật lại text để hiển thị tên bác sĩ
        tvInfo.setText(
                "Bác sĩ: " + doctorName +
                        "\nNgày khám: " + record.getVisitDate() +
                        "\nChẩn đoán: " + record.getDiagnosis()
        );

        btnView.setOnClickListener(v -> {
            Intent intent = new Intent(context, XemBenhAnActivity.class);
            intent.putExtra("record_id", record.getId());
            context.startActivity(intent);
        });

        return convertView;
    }
}