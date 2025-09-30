package com.haruma.app.utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TaskScheduler {

    private static final int NOTIFICATION_MINUTES_BEFORE_START = 1;
    private static final int NOTIFICATION_MINUTES_BEFORE_END = 1;

    public static void scheduleTaskNotifications(Context context, String taskName, String dateStr, String startTimeStr, String endTimeStr) {
        try {
            Calendar startCalendar = convertToCalendar(dateStr, startTimeStr);
            Calendar endCalendar = convertToCalendar(dateStr, endTimeStr);

            // Chỉ lên lịch thông báo nếu thời gian trong tương lai
            if (startCalendar.getTimeInMillis() > System.currentTimeMillis()) {
                scheduleStartNotification(context, taskName, startCalendar.getTimeInMillis(), NOTIFICATION_MINUTES_BEFORE_START);
            }

            if (endCalendar.getTimeInMillis() > System.currentTimeMillis()) {
                scheduleEndNotification(context, taskName, endCalendar.getTimeInMillis(), NOTIFICATION_MINUTES_BEFORE_END);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static Calendar convertToCalendar(String dateStr, String timeStr) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = dateFormat.parse(dateStr);

        String[] timeParts = timeStr.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        return calendar;
    }

    private static void scheduleStartNotification(Context context, String taskName, long taskStartTime, long notifyMinutesBefore) {
        long notificationTime = taskStartTime - (notifyMinutesBefore * 60 * 1000);

        if (notificationTime <= System.currentTimeMillis()) {
            return;
        }

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(AlarmReceiver.EXTRA_TASK_NAME, taskName);
        intent.putExtra(AlarmReceiver.EXTRA_TIME_REMAINING, notifyMinutesBefore);
        intent.putExtra(AlarmReceiver.EXTRA_NOTIFICATION_TYPE, AlarmReceiver.TYPE_STARTING_SOON);

        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                taskName.hashCode(),
                intent,
                flags
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, notificationTime, pendingIntent);
                    } else {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, notificationTime, pendingIntent);
                    }
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTime, pendingIntent);
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, notificationTime, pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, notificationTime, pendingIntent);
            }
        } catch (SecurityException e) {
            // Fallback nếu không có quyền SCHEDULE_EXACT_ALARM
            alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTime, pendingIntent);
        }
    }

    private static void scheduleEndNotification(Context context, String taskName, long taskEndTime, long notifyMinutesBefore) {
        long notificationTime = taskEndTime - (notifyMinutesBefore * 60 * 1000);

        if (notificationTime <= System.currentTimeMillis()) {
            return;
        }

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(AlarmReceiver.EXTRA_TASK_NAME, taskName);
        intent.putExtra(AlarmReceiver.EXTRA_TIME_REMAINING, notifyMinutesBefore);
        intent.putExtra(AlarmReceiver.EXTRA_NOTIFICATION_TYPE, AlarmReceiver.TYPE_ENDING_SOON);

        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                (taskName + "_end").hashCode(),
                intent,
                flags
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, notificationTime, pendingIntent);
                    } else {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, notificationTime, pendingIntent);
                    }
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTime, pendingIntent);
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, notificationTime, pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, notificationTime, pendingIntent);
            }
        } catch (SecurityException e) {
            // Fallback nếu không có quyền SCHEDULE_EXACT_ALARM
            alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTime, pendingIntent);
        }
    }

    public static void cancelTaskNotifications(Context context, String taskName) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Hủy thông báo bắt đầu
        Intent startIntent = new Intent(context, AlarmReceiver.class);
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }

        PendingIntent startPendingIntent = PendingIntent.getBroadcast(
                context,
                taskName.hashCode(),
                startIntent,
                flags
        );
        alarmManager.cancel(startPendingIntent);

        // Hủy thông báo kết thúc
        Intent endIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent endPendingIntent = PendingIntent.getBroadcast(
                context,
                (taskName + "_end").hashCode(),
                endIntent,
                flags
        );
        alarmManager.cancel(endPendingIntent);
    }
}