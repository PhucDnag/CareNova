package com.example.btl_android.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;

import org.jspecify.annotations.NonNull;

import java.util.List;

import com.example.btl_android.Object.MedicalRecord;
import com.example.btl_android.R;
import com.example.btl_android.RecordDetailActivity;
import com.example.btl_android.XemBenhAnActivity;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

    private List<MedicalRecord> list;
    private Context context;

    public RecordAdapter(List<MedicalRecord> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MedicalRecord record = list.get(position);

        holder.tvDate.setText("Ngày khám: " + record.getVisitDate());
        holder.tvDiagnosis.setText(record.getDiagnosis());

        com.example.btl_android.DoctorRepository doctorRepo = new com.example.btl_android.DoctorRepository(context);
        com.example.btl_android.Object.Doctor doctor = doctorRepo.getById(record.getDoctorId());

        if (doctor != null) {
            holder.tvDoctor.setText("BS: " + doctor.getName());
        } else {
            holder.tvDoctor.setText("BS ID: " + record.getDoctorId());
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, XemBenhAnActivity.class);
            intent.putExtra("record_id", record.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvDiagnosis, tvDoctor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDiagnosis = itemView.findViewById(R.id.tvDiagnosis);
            tvDoctor = itemView.findViewById(R.id.tvDoctor);
        }
    }
}