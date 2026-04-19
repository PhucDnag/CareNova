package com.example.btl_android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// <<< SỬA LỖI: THÊM CÁC IMPORT ĐÚNG >>>
import com.example.btl_android.Adapter.AppointmentAdapter;
import com.example.btl_android.Database.DatabaseHelper;
import com.example.btl_android.Object.Appointment;

import java.util.ArrayList;
import java.util.List;

public class AppointmentListFragment extends Fragment {

    private RecyclerView recyclerViewAppointments;
    private AppointmentAdapter adapter;
    private DatabaseHelper db;
    private List<Appointment> appointmentList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_appointment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = DatabaseHelper.getInstance(getContext());

        recyclerViewAppointments = view.findViewById(R.id.recyclerViewAppointments);
        recyclerViewAppointments.setLayoutManager(new LinearLayoutManager(getContext()));

        loadAppointments();

        adapter = new AppointmentAdapter(appointmentList, getContext());
        recyclerViewAppointments.setAdapter(adapter);
    }

    private void loadAppointments() {
        List<Appointment> dataFromDb = db.getAllAppointments(1);
        appointmentList.clear();
        appointmentList.addAll(dataFromDb);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAppointments();
    }
}
