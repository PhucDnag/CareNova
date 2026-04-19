package com.example.btl_android.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import com.example.btl_android.Object.Patient;
import com.example.btl_android.R;
import com.example.btl_android.XemTtinBenhNhanActivity;

public class PatientAdapter extends ArrayAdapter<Patient> {

    private Context context;
    private List<Patient> list;

    public PatientAdapter(Context context, List<Patient> list) {
        super(context, 0, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_patient, parent, false);
        }

        Patient patient = list.get(position);

        TextView tvInfo = convertView.findViewById(R.id.tvInfo);
        Button btnView = convertView.findViewById(R.id.btnView);

        String info = "Họ tên: " + patient.getFullName() +
                "\nNgày sinh: " + patient.getDob() +
                "\nGiới tính: " + patient.getGender() +
                "\nSĐT: " + patient.getPhone();

        tvInfo.setText(info);

        btnView.setOnClickListener(v -> {
            Intent intent = new Intent(context, XemTtinBenhNhanActivity.class);
            intent.putExtra("patient_id", patient.getId());
            context.startActivity(intent);
        });

        return convertView;
    }
}
