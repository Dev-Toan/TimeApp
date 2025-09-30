package com.haruma.app.utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String EXTRA_TASK_NAME = "task_name";
    public static final String EXTRA_TIME_REMAINING = "time_remaining";
    public static final String EXTRA_NOTIFICATION_TYPE = "notification_type";
    public static final String TYPE_STARTING_SOON = "starting_soon";
    public static final String TYPE_ENDING_SOON = "ending_soon";

    @Override
    public void onReceive(Context context, Intent intent) {
        String taskName = intent.getStringExtra(EXTRA_TASK_NAME);
        long timeRemaining = intent.getLongExtra(EXTRA_TIME_REMAINING, 2); // Mặc định 15 phút
        String notificationType = intent.getStringExtra(EXTRA_NOTIFICATION_TYPE);

        if (TYPE_STARTING_SOON.equals(notificationType)) {
            NotificationHelper.showStartingSoonNotification(context, taskName, timeRemaining);
        } else if (TYPE_ENDING_SOON.equals(notificationType)) {
            NotificationHelper.showEndingSoonNotification(context, taskName, timeRemaining);
        }
    }
}