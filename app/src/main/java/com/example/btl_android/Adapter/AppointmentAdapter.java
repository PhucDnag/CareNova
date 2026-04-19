package com.example.btl_android.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.AppointmentDetailActivity;
import com.example.btl_android.Database.DatabaseHelper;
import com.example.btl_android.Object.Appointment;
import com.example.btl_android.R;
import com.example.btl_android.SetReminderActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {

    private List<Appointment> appointmentList;
    private Context context;
    private DatabaseHelper db;

    public AppointmentAdapter(List<Appointment> appointmentList, Context context) {
        this.appointmentList = appointmentList;
        this.context = context;
        this.db = DatabaseHelper.getInstance(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_appointment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment appointment = appointmentList.get(position);

        String validatedStatus = getValidatedStatus(appointment);

        holder.tvDoctorName.setText("Bác sĩ: " + appointment.getDoctorName());
        holder.tvSpecialty.setText("Chuyên khoa: " + appointment.getSpecialty());
        holder.tvAppointmentDateTime.setText("Thời gian: " + appointment.getAppointmentTime() + " - " + appointment.getAppointmentDate());
        holder.tvAppointmentStatus.setText(validatedStatus);

        holder.btnSetReminder.setVisibility(View.VISIBLE);
        holder.btnSetReminder.setOnClickListener(v -> {
            Intent intent = new Intent(context, SetReminderActivity.class);
            context.startActivity(intent);
        });

        switch (validatedStatus) {
            case "Sắp tới":
            case "Chờ duyệt":
                holder.tvAppointmentStatus.setBackgroundResource("Chờ duyệt".equals(validatedStatus) ? R.drawable.status_pending_background : R.drawable.status_background);
                holder.btnCancelAppointment.setVisibility(View.VISIBLE);
                holder.btnCancelAppointment.setOnClickListener(v -> showCancelConfirmationDialog(appointment, position));
                break;
            case "Đã hủy":
                holder.tvAppointmentStatus.setBackgroundResource(R.drawable.status_cancelled_background);
                holder.btnCancelAppointment.setVisibility(View.GONE);
                break;
            case "Đã qua":
                holder.tvAppointmentStatus.setBackgroundResource(R.drawable.status_completed_background);
                holder.btnCancelAppointment.setVisibility(View.GONE);
                break;
            default:
                holder.tvAppointmentStatus.setBackgroundResource(R.drawable.status_other_background);
                holder.btnCancelAppointment.setVisibility(View.GONE);
                break;
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AppointmentDetailActivity.class);
            intent.putExtra("APPOINTMENT_DATA", appointment);
            context.startActivity(intent);
        });
    }

    private String getValidatedStatus(Appointment appointment) {
        if ("Đã hủy".equals(appointment.getStatus()) || "Chờ duyệt".equals(appointment.getStatus())) {
            return appointment.getStatus();
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            Date appointmentDateTime = sdf.parse(appointment.getAppointmentDate() + " " + appointment.getAppointmentTime());
            Date now = new Date();

            if (now.after(appointmentDateTime)) {
                return "Đã qua";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        return "Sắp tới";
    }

    private void showCancelConfirmationDialog(Appointment appointment, int position) {
        new AlertDialog.Builder(context)
            .setTitle("Xác nhận hủy lịch")
            .setMessage("Bạn có chắc chắn muốn hủy lịch khám này không?")
            .setPositiveButton("Có", (dialog, which) -> {
                boolean success = db.cancelAppointment(appointment.getId());
                if (success) {
                    appointment.setStatus("Đã hủy");
                    notifyItemChanged(position);
                }
            })
            .setNegativeButton("Không", null)
            .show();
    }

    @Override
    public int getItemCount() {
        return appointmentList != null ? appointmentList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDoctorName, tvSpecialty, tvAppointmentDateTime, tvAppointmentStatus;
        Button btnCancelAppointment, btnSetReminder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDoctorName = itemView.findViewById(R.id.tvDoctorName);
            tvSpecialty = itemView.findViewById(R.id.tvSpecialty);
            tvAppointmentDateTime = itemView.findViewById(R.id.tvAppointmentDateTime);
            tvAppointmentStatus = itemView.findViewById(R.id.tvAppointmentStatus);
            btnCancelAppointment = itemView.findViewById(R.id.btnCancelAppointment);
            btnSetReminder = itemView.findViewById(R.id.btnSetReminder);
        }
    }
}
