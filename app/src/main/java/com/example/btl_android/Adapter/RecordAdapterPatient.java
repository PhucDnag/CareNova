package com.example.btl_android.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.Object.MedicalRecord1;
import com.example.btl_android.R;
import com.example.btl_android.RecordDetailActivity1;

import java.util.List;

public class RecordAdapterPatient extends RecyclerView.Adapter<RecordAdapterPatient.ViewHolder> {

    private List<MedicalRecord1> list;
    private Context context;

    public RecordAdapterPatient(List<MedicalRecord1> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_record1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MedicalRecord1 record = list.get(position);

        holder.tvDoctorName.setText("Bác sĩ " + record.getDoctorName());
        holder.tvDiagnosis.setText("Chẩn đoán: " + record.getDiagnosis());
        holder.tvVisitDate.setText("Ngày khám: " + record.getVisitDate());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RecordDetailActivity1.class);
            intent.putExtra("RECORD_DATA", record);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDoctorName, tvDiagnosis, tvVisitDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDoctorName = itemView.findViewById(R.id.tvDoctorName);
            tvDiagnosis = itemView.findViewById(R.id.tvDiagnosis);
            tvVisitDate = itemView.findViewById(R.id.tvVisitDate);
        }
    }
}
