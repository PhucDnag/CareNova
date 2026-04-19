package com.example.btl_android;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class HistoryViewPagerAdapter extends FragmentStateAdapter {

    public HistoryViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // position 0: Lịch sử bệnh
        // position 1: Lịch sử đặt hẹn
        if (position == 1) {
            return new AppointmentHistoryFragment();
        }
        return new MedicalHistoryFragment();
    }

    @Override
    public int getItemCount() {
        return 2; // Có 2 tab
    }
}
