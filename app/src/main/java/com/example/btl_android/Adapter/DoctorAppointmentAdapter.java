package com.example.btl_android.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.Database.DatabaseHelper;
import com.example.btl_android.NotificationHelper;
import com.example.btl_android.Object.Appointment;
import com.example.btl_android.Object.Notification;
import com.example.btl_android.R;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DoctorAppointmentAdapter extends RecyclerView.Adapter<DoctorAppointmentAdapter.ViewHolder> {

    private List<Appointment> appointmentList;
    private Context context;
    private DatabaseHelper db;
    private NotificationHelper notificationHelper;

    public DoctorAppointmentAdapter(Context context, List<Appointment> appointmentList) {
        this.context = context;
        this.appointmentList = appointmentList;
        this.db = DatabaseHelper.getInstance(context);
        this.notificationHelper = new NotificationHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor_appointment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment appointment = appointmentList.get(position);

        holder.tvPatientName.setText("Tên bệnh nhân: " + appointment.getPatientName());
        holder.tvAppointmentDateTime.setText("Thời gian: " + appointment.getAppointmentTime() + " - " + appointment.getAppointmentDate());
        holder.tvAppointmentStatus.setText("Trạng thái: " + appointment.getStatus());

        if ("Chờ duyệt".equals(appointment.getStatus())) {
            holder.btnConfirmAppointment.setVisibility(View.VISIBLE);
            holder.btnConfirmAppointment.setOnClickListener(v -> {
                boolean success = db.confirmAppointment(appointment.getId());
                if (success) {
                    // Update UI
                    appointment.setStatus("Đã xác nhận");
                    notifyItemChanged(position);

                    // Create notification for patient
                    String notifTitle = "Lịch hẹn đã được xác nhận";
                    String notifMessage = "Lịch khám của bạn với " + appointment.getDoctorName() + " vào lúc " + appointment.getAppointmentTime() + " ngày " + appointment.getAppointmentDate() + " đã được xác nhận.";
                    String notifTime = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
                    db.addNotification(new Notification(notifTitle, notifMessage, notifTime, R.drawable.ic_notifications));
                    notificationHelper.showNotification(notifTitle, notifMessage, false);

                    Toast.makeText(context, "Đã xác nhận lịch hẹn!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Lỗi khi xác nhận!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            holder.btnConfirmAppointment.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPatientName, tvAppointmentDateTime, tvAppointmentStatus;
        MaterialButton btnConfirmAppointment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPatientName = itemView.findViewById(R.id.tvPatientName);
            tvAppointmentDateTime = itemView.findViewById(R.id.tvAppointmentDateTime);
            tvAppointmentStatus = itemView.findViewById(R.id.tvAppointmentStatus);
            btnConfirmAppointment = itemView.findViewById(R.id.btnConfirmAppointment);
        }
    }
}
