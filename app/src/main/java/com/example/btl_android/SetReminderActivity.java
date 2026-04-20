package com.example.btl_android;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import android.provider.AlarmClock;
public class SetReminderActivity extends AppCompatActivity {

    private TextView txtTime;
    private MaterialButton btnPickTime, btnConfirm;
    private TextInputEditText edtMessage;
    private Switch switchVibrate;
    private CheckBox cbMon, cbTue, cbWed, cbThu, cbFri, cbSat, cbSun;
    private Toolbar toolbar;
    private int selectedHour = -1;
    private int selectedMinute = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        // Find views
        toolbar = findViewById(R.id.toolbar);
        txtTime = findViewById(R.id.txtTime);
        btnPickTime = findViewById(R.id.btnPickTime);
        edtMessage = findViewById(R.id.edtMessage);
        switchVibrate = findViewById(R.id.switchVibrate);
        cbMon = findViewById(R.id.cbMon);
        cbTue = findViewById(R.id.cbTue);
        cbWed = findViewById(R.id.cbWed);
        cbThu = findViewById(R.id.cbThu);
        cbFri = findViewById(R.id.cbFri);
        cbSat = findViewById(R.id.cbSat);
        cbSun = findViewById(R.id.cbSun);
        btnConfirm = findViewById(R.id.btnConfirm);

        // Setup toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Set initial time display
        final Calendar c = Calendar.getInstance();
        selectedHour = c.get(Calendar.HOUR_OF_DAY);
        selectedMinute = c.get(Calendar.MINUTE);
        txtTime.setText(String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute));

        btnPickTime.setOnClickListener(v -> showTimePickerDialog());

        btnConfirm.setOnClickListener(v -> {
            String message = edtMessage.getText().toString();
            if (message.isEmpty()) {
                Toast.makeText(this, getString(R.string.toast_enter_reminder_message), Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedHour == -1) {
                Toast.makeText(this, getString(R.string.toast_pick_time), Toast.LENGTH_SHORT).show();
                return;
            }
            boolean isVibrate = switchVibrate.isChecked();

            // Gọi hàm đặt báo thức vào ứng dụng Đồng hồ hệ thống
            setSystemAlarm(selectedHour, selectedMinute, message, getRepeatDays(), isVibrate);
            setAlarm(selectedHour, selectedMinute, message, isVibrate);
        });
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    selectedHour = hourOfDay;
                    selectedMinute = minute;
                    txtTime.setText(String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute));
                }, selectedHour, selectedMinute, true); // true for 24-hour view
        timePickerDialog.show();
    }

    private void setAlarm(int hour, int minute, String message, boolean isVibrate) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        ArrayList<Integer> repeatDays = getRepeatDays();

        if (repeatDays.isEmpty()) {
            // NẾU KHÔNG CHỌN NGÀY LẶP LẠI -> Đặt báo thức 1 lần
            scheduleSingleAlarm(alarmManager, hour, minute, message, isVibrate);
            Toast.makeText(this, getString(R.string.toast_one_time_reminder_set), Toast.LENGTH_SHORT).show();
        } else {
            // NẾU CÓ CHỌN NGÀY LẶP LẠI -> Lên lịch cho từng ngày được chọn
            for (int dayOfWeek : repeatDays) {
                scheduleWeeklyAlarm(alarmManager, hour, minute, message, dayOfWeek, isVibrate);
            }
            Toast.makeText(this, getString(R.string.toast_weekly_reminder_set), Toast.LENGTH_SHORT).show();
        }
        finish(); // Thoát màn hình sau khi đặt xong
    }

    private void scheduleSingleAlarm(AlarmManager alarmManager, int hour, int minute, String message, boolean isVibrate) {
        Intent intent = new Intent(this, ReminderReceiver.class);
        intent.putExtra("message", message);
        intent.putExtra("vibrate", isVibrate);
        intent.putExtra("isRepeating", false);

        // Dùng requestCode = 0 cho báo thức 1 lần
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // Nếu thời gian đã qua trong ngày, chuyển sang ngày mai
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        if (alarmManager != null) {
            // Kiểm tra nếu thiết bị chạy Android 12 (API 31) trở lên
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    // Đã có quyền, tiến hành đặt báo thức
                    try {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                        Toast.makeText(this, getString(R.string.toast_alarm_permission_error), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Chưa có quyền -> Mở trang cài đặt để người dùng cấp quyền
                    Intent intent2 = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                    startActivity(intent2);
                    Toast.makeText(this, getString(R.string.toast_need_exact_alarm_permission), Toast.LENGTH_LONG).show();
                    return; // Dừng việc đặt báo thức lại cho đến khi có quyền
                }
            } else {
                // Với Android 11 trở xuống, hệ thống mặc định cho phép mà không cần hỏi
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }
    }

    private void scheduleWeeklyAlarm(AlarmManager alarmManager, int hour, int minute, String message, int dayOfWeek, boolean isVibrate) {
        Intent intent = new Intent(this, ReminderReceiver.class);
        intent.putExtra("message", message);
        intent.putExtra("hour", hour);             // Gửi kèm giờ để Receiver biết đường đặt lại
        intent.putExtra("minute", minute);         // Gửi kèm phút
        intent.putExtra("dayOfWeek", dayOfWeek);
        intent.putExtra("vibrate", isVibrate);// Gửi kèm thứ trong tuần
        intent.putExtra("isRepeating", true);      // Đánh dấu đây là báo thức lặp lại

        // QUAN TRỌNG: Dùng chính 'dayOfWeek' làm requestCode để các ngày không đè lên nhau
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, dayOfWeek, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Tính toán số ngày cần cộng thêm để tới đúng 'Thứ' được chọn
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int daysUntilNext = dayOfWeek - currentDayOfWeek;

        // Nếu 'Thứ' đó đã qua trong tuần này, hoặc chính là hôm nay nhưng giờ báo thức đã qua -> cộng thêm 7 ngày sang tuần sau
        if (daysUntilNext < 0 || (daysUntilNext == 0 && calendar.getTimeInMillis() <= System.currentTimeMillis())) {
            daysUntilNext += 7;
        }

        calendar.add(Calendar.DAY_OF_MONTH, daysUntilNext);

        if (alarmManager != null) {
            // Kiểm tra nếu thiết bị chạy Android 12 (API 31) trở lên
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    // Đã có quyền, tiến hành đặt báo thức
                    try {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                        Toast.makeText(this, getString(R.string.toast_alarm_permission_error), Toast.LENGTH_SHORT).show();                    }
                } else {
                    // Chưa có quyền -> Mở trang cài đặt để người dùng cấp quyền
                    Intent intent2 = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                    startActivity(intent2);
                    Toast.makeText(this, getString(R.string.toast_need_exact_alarm_permission), Toast.LENGTH_LONG).show();
                    return; // Dừng việc đặt báo thức lại cho đến khi có quyền
                }
            } else {
                // Với Android 11 trở xuống, hệ thống mặc định cho phép mà không cần hỏi
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }
    }
    private void setSystemAlarm(int hour, int minute, String message, ArrayList<Integer> repeatDays, boolean isVibrate) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        intent.putExtra(AlarmClock.EXTRA_HOUR, hour);
        intent.putExtra(AlarmClock.EXTRA_MINUTES, minute);
        intent.putExtra(AlarmClock.EXTRA_MESSAGE, message);
        intent.putExtra(AlarmClock.EXTRA_VIBRATE, isVibrate);

        // Đặt 'true' để báo thức được thiết lập ngầm, không mở giao diện ứng dụng Đồng hồ lên làm gián đoạn người dùng
        intent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);

        // Nếu người dùng có chọn ngày lặp lại
        if (!repeatDays.isEmpty()) {
            intent.putExtra(AlarmClock.EXTRA_DAYS, repeatDays);
        }

        // Kiểm tra xem thiết bị có ứng dụng Đồng hồ hỗ trợ Intent này không
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
            Toast.makeText(this, getString(R.string.toast_alarm_added_to_clock), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, getString(R.string.toast_no_clock_app), Toast.LENGTH_SHORT).show();
        }
    }
    private ArrayList<Integer> getRepeatDays() {
        ArrayList<Integer> days = new ArrayList<>();
        if (cbSun.isChecked()) days.add(Calendar.SUNDAY);
        if (cbMon.isChecked()) days.add(Calendar.MONDAY);
        if (cbTue.isChecked()) days.add(Calendar.TUESDAY);
        if (cbWed.isChecked()) days.add(Calendar.WEDNESDAY);
        if (cbThu.isChecked()) days.add(Calendar.THURSDAY);
        if (cbFri.isChecked()) days.add(Calendar.FRIDAY);
        if (cbSat.isChecked()) days.add(Calendar.SATURDAY);
        return days;
    }
}
