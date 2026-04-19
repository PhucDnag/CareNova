package com.example.btl_android;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.btl_android.Adapter.RecordAdapterPatient;
import com.example.btl_android.Database.DatabaseHelper;
import com.example.btl_android.Object.MedicalRecord1;

import java.util.ArrayList;
import java.util.List;

public class MedicalHistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecordAdapterPatient adapter;
    private DatabaseHelper db;
    private List<MedicalRecord1> recordList = new ArrayList<>();

    public MedicalHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medical_history, container, false);

        db = DatabaseHelper.getInstance(getContext());
        recyclerView = view.findViewById(R.id.recyclerViewMedicalHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadMedicalHistory();
        adapter = new RecordAdapterPatient(recordList, getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void loadMedicalHistory() {
        List<MedicalRecord1> dataFromDb = db.getMedicalHistory(1);
        recordList.clear();
        recordList.addAll(dataFromDb);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
