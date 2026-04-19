package com.example.btl_android;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_android.Adapter.TreatmentAdapter;
import com.example.btl_android.Object.TreatmentItem;
import com.example.btl_android.TreatMentRepository;

import java.util.List;

public class DsBenhNhanPhacDoActivity extends AppCompatActivity {

    ImageView btnBack;
    ListView lvTreatments;

    TreatMentRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ds_benhnhan_phacdo);

        btnBack = findViewById(R.id.btn_back);
        lvTreatments = findViewById(R.id.lvTreatments);

        repository = new TreatMentRepository(this);

        btnBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {

        List<TreatmentItem> list = repository.getAllTreatments();

        TreatmentAdapter adapter =
                new TreatmentAdapter(this, list);

        lvTreatments.setAdapter(adapter);
    }
}
