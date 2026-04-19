package com.example.btl_android;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.btl_android.Database.DatabaseHelper;
import com.example.btl_android.Object.Notification;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra("message");
        if (message == null || message.isEmpty()) {
            message = "Đã đến giờ lịch khám của bạn!";
        }

        String title = "Nhắc nhở lịch khám";

        // Lấy tùy chọn rung từ Intent
        boolean vibrate = intent.getBooleanExtra("vibrate", false);

        // Hiển thị thông báo trên hệ thống Android
        NotificationHelper notificationHelper = new NotificationHelper(context);
        notificationHelper.createNotificationChannel();
        notificationHelper.showNotification(title, message, vibrate);

        // Lấy thời gian hiện tại để lưu vào CSDL
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());
        String currentTime = sdf.format(new Date());

        // Lưu thông báo vào SQLite để hiển thị trong NotificationFragment
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        // Lưu ý: R.drawable.ic_notification (bạn có thể đổi thành icon phù hợp có sẵn trong res/drawable)
        Notification dbNotification = new Notification(title, message, currentTime, R.drawable.ic_notification);
        dbHelper.addNotification(dbNotification);

        boolean isRepeating = intent.getBooleanExtra("isRepeating", false);
        if (isRepeating) {
            int hour = intent.getIntExtra("hour", 0);
            int minute = intent.getIntExtra("minute", 0);
            int dayOfWeek = intent.getIntExtra("dayOfWeek", 0);

            rescheduleForNextWeek(context, hour, minute, message, dayOfWeek, vibrate);
        }
    }

    private void rescheduleForNextWeek(Context context, int hour, int minute, String message, int dayOfWeek, boolean vibrate) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra("message", message);
        intent.putExtra("hour", hour);
        intent.putExtra("minute", minute);
        intent.putExtra("dayOfWeek", dayOfWeek);
        intent.putExtra("vibrate", vibrate);
        intent.putExtra("isRepeating", true);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, dayOfWeek, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Cộng thêm 7 ngày
        calendar.add(Calendar.DAY_OF_MONTH, 7);

        // Thực sự ra lệnh đặt lại báo thức cho hệ thống
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }
    }
}