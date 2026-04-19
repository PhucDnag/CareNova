package com.example.btl_android;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MedicalHistoryActivity extends AppCompatActivity {

    private TabLayout tabLayoutHistory;
    private ViewPager2 viewPagerHistory;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_history1);

        // Ánh xạ View
        tabLayoutHistory = findViewById(R.id.tabLayoutHistory);
        viewPagerHistory = findViewById(R.id.viewPagerHistory);
        ivBack = findViewById(R.id.ivBack);

        // Thiết lập Adapter cho ViewPager2
        HistoryViewPagerAdapter adapter = new HistoryViewPagerAdapter(this);
        viewPagerHistory.setAdapter(adapter);

        // Kết nối TabLayout với ViewPager2
        new TabLayoutMediator(tabLayoutHistory, viewPagerHistory, (tab, position) -> {
            if (position == 0) {
                tab.setText("Lịch sử bệnh");
            } else {
                tab.setText("Lịch sử đặt hẹn");
            }
        }).attach();

        // Xử lý nút quay lại
        ivBack.setOnClickListener(v -> finish());
    }
}
