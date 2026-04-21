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

import static com.example.btl_android.LanguageManager.isEnglish;

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

        holder.tvDoctorName.setText(getLocalizedLabel(R.string.doctor_label, R.string.doctor_label_en) + appointment.getDoctorName());
        holder.tvSpecialty.setText(getLocalizedLabel(R.string.specialty_label, R.string.specialty_label_en) + appointment.getSpecialty());
        holder.tvAppointmentDateTime.setText(getLocalizedLabel(R.string.time_label, R.string.time_label_en) + appointment.getAppointmentTime() + " - " + appointment.getAppointmentDate());
        holder.tvAppointmentStatus.setText(getLocalizedStatus(validatedStatus));

        holder.btnSetReminder.setVisibility(View.VISIBLE);
        holder.btnSetReminder.setOnClickListener(v -> {
            Intent intent = new Intent(context, SetReminderActivity.class);
            context.startActivity(intent);
        });

        switch (validatedStatus) {
            case "pending":
                holder.tvAppointmentStatus.setBackgroundResource(R.drawable.status_pending_background);
                holder.btnCancelAppointment.setVisibility(View.VISIBLE);
                holder.btnCancelAppointment.setOnClickListener(v -> showCancelConfirmationDialog(appointment, position));
                break;
            case "cancelled":
                holder.tvAppointmentStatus.setBackgroundResource(R.drawable.status_cancelled_background);
                holder.btnCancelAppointment.setVisibility(View.GONE);
                break;
            case "completed":
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
        if ("Đã hủy".equals(appointment.getStatus()) || "cancelled".equalsIgnoreCase(appointment.getStatus())) {
            return "cancelled";
        }
        if ("Chờ duyệt".equals(appointment.getStatus()) || "pending".equalsIgnoreCase(appointment.getStatus())) {
            return "pending";
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            Date appointmentDateTime = sdf.parse(appointment.getAppointmentDate() + " " + appointment.getAppointmentTime());
            Date now = new Date();

            if (appointmentDateTime != null && now.after(appointmentDateTime)) {
                return "completed";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "upcoming";
    }

    private void showCancelConfirmationDialog(Appointment appointment, int position) {
        new AlertDialog.Builder(context)
            .setTitle(isEnglish(context) ? "Cancel appointment" : "Xác nhận hủy lịch")
            .setMessage(isEnglish(context) ? "Are you sure you want to cancel this appointment?" : "Bạn có chắc chắn muốn hủy lịch khám này không?")
            .setPositiveButton(isEnglish(context) ? "Yes" : "Có", (dialog, which) -> {
                boolean success = db.cancelAppointment(appointment.getId());
                if (success) {
                    appointment.setStatus("cancelled");
                    notifyItemChanged(position);
                }
            })
            .setNegativeButton(isEnglish(context) ? "No" : "Không", null)
            .show();
    }

    private String getLocalizedStatus(String status) {
        switch (status) {
            case "pending":
                return isEnglish(context) ? "Pending approval" : "Chờ duyệt";
            case "cancelled":
                return isEnglish(context) ? "Cancelled" : "Đã hủy";
            case "completed":
                return isEnglish(context) ? "Completed" : "Đã qua";
            default:
                return isEnglish(context) ? "Upcoming" : "Sắp tới";
        }
    }

    private String getLocalizedLabel(int viResId, int enResId) {
        return isEnglish(context) ? context.getString(enResId) : context.getString(viResId);
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
