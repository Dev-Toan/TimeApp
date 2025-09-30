package com.haruma.app.utility;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class NotificationHelper {
    private static final String CHANNEL_ID = "task_notifications";
    private static final String CHANNEL_NAME = "Thông báo công việc";
    private static final String CHANNEL_DESC = "Thông báo cho lịch trình công việc";

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription(CHANNEL_DESC);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void showStartingSoonNotification(Context context, String taskName, long timeRemainingMinutes) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info) // Thay thế bằng icon của bạn nếu có
                .setContentTitle("Sắp đến giờ làm việc")
                .setContentText("Công việc " + taskName + " sẽ bắt đầu trong " + timeRemainingMinutes + " phút")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(taskName.hashCode(), builder.build());
    }

    public static void showEndingSoonNotification(Context context, String taskName, long timeRemainingMinutes) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_alert) // Thay thế bằng icon của bạn nếu có
                .setContentTitle("Sắp hết giờ làm việc")
                .setContentText("Công việc " + taskName + " sẽ kết thúc trong " + timeRemainingMinutes + " phút")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((taskName + "_end").hashCode(), builder.build());
    }
}