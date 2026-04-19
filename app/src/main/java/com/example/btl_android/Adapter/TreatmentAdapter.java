package com.example.btl_android.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import com.example.btl_android.Object.TreatmentItem;
import com.example.btl_android.R;
import com.example.btl_android.XemPhacDoActivity;

public class TreatmentAdapter extends BaseAdapter {

    private Activity activity;
    private List<TreatmentItem> list;

    public TreatmentAdapter(Activity activity, List<TreatmentItem> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getTreatmentId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(activity)
                    .inflate(R.layout.item_treatment, parent, false);
        }

        TextView tvInfo = convertView.findViewById(R.id.tvInfo);
        Button btnView = convertView.findViewById(R.id.btnView);

        TreatmentItem item = list.get(position);

        tvInfo.setText(
                item.getPatientName() + " - " + item.getMedicineName()
        );

        btnView.setOnClickListener(v -> {
            Intent intent = new Intent(activity, XemPhacDoActivity.class);
            intent.putExtra("treatment_id", item.getTreatmentId());
            activity.startActivity(intent);
        });

        return convertView;
    }
}
