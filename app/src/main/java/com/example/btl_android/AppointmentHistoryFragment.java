package com.example.btl_android;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

// <<< SỬA LỖI: THÊM CÁC IMPORT ĐÚNG >>>
import com.example.btl_android.Adapter.AppointmentAdapter;
import com.example.btl_android.Database.DatabaseHelper;
import com.example.btl_android.Object.Appointment;

import java.util.ArrayList;
import java.util.List;

public class AppointmentHistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private AppointmentAdapter adapter;
    private DatabaseHelper db;
    private List<Appointment> appointmentList = new ArrayList<>();

    public AppointmentHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_history, container, false);

        db = DatabaseHelper.getInstance(getContext());
        recyclerView = view.findViewById(R.id.recyclerViewAppointmentHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadAppointmentHistory();
        // Sử dụng AppointmentAdapter từ package Adapter
        adapter = new AppointmentAdapter(appointmentList, getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void loadAppointmentHistory() {
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
        loadAppointmentHistory();
    }
}
