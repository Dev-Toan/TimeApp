package com.haruma.app.viewmodel;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModel;

import com.haruma.app.view.TimerActivity;

public class TimerViewModel extends ViewModel {

    // UI Components
    private EditText etContent;
    private NumberPicker hourPicker;
    private NumberPicker minutePicker;
    private NumberPicker secondPicker;
    private TextView tvTimer;
    private Button btnStart;
    private Button btnPause;

    // Context
    private Context context;
    private AppCompatActivity activity;

    // Timer state
    private CountDownTimer timer;
    private long timeLeftInMillis = 0;
    private long originalTimeInMillis = 0;
    private boolean isTimerRunning = false;
    private boolean isPaused = false;

    // Constants
    private static final String CHANNEL_ID = "timer_notification_channel";
    private static final int NOTIFICATION_ID = 1;
    private static final String TAG = "TimerViewModel";

    // Initialize with context
    public void initialize(AppCompatActivity activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        createNotificationChannel();
    }

    // Connect views
    public void setupViews(EditText etContent, NumberPicker hourPicker, NumberPicker minutePicker,
                           NumberPicker secondPicker, TextView tvTimer, Button btnStart, Button btnPause) {
        this.etContent = etContent;
        this.hourPicker = hourPicker;
        this.minutePicker = minutePicker;
        this.secondPicker = secondPicker;
        this.tvTimer = tvTimer;
        this.btnStart = btnStart;
        this.btnPause = btnPause;

        // Cấu hình NumberPicker
        setupNumberPickers();

        // Thiết lập button listeners
        setupButtonListeners();

        // Ban đầu nút tạm dừng sẽ bị vô hiệu hóa
        btnPause.setEnabled(false);
    }

    private void setupNumberPickers() {
        // Cấu hình NumberPicker cho giờ
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(23);

        // Cấu hình NumberPicker cho phút
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);

        // Cấu hình NumberPicker cho giây
        secondPicker.setMinValue(0);
        secondPicker.setMaxValue(59);
    }

    private void setupButtonListeners() {
        // Xử lý sự kiện nút Bắt đầu/Dừng lại
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTimerRunning) {
                    if (timeLeftInMillis == 0L) {
                        // Lấy thời gian từ NumberPicker
                        int hours = hourPicker.getValue();
                        int minutes = minutePicker.getValue();
                        int seconds = secondPicker.getValue();

                        // Kiểm tra nếu thời gian là 0
                        if (hours == 0 && minutes == 0 && seconds == 0) {
                            Toast.makeText(context, "Vui lòng chọn thời gian hẹn giờ", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Kiểm tra nếu nội dung trống
                        if (etContent.getText().toString().trim().isEmpty()) {
                            Toast.makeText(context, "Vui lòng nhập nội dung hẹn giờ", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Chuyển đổi thời gian thành milliseconds
                        timeLeftInMillis = (((hours * 60 * 60) + (minutes * 60) + seconds) * 1000L);
                        originalTimeInMillis = timeLeftInMillis;
                    }
                    startTimer();
                    btnStart.setText("Dừng lại");
                    btnPause.setEnabled(true);
                } else {
                    stopTimer();
                    resetTimer();
                    btnStart.setText("Bắt đầu");
                    btnPause.setEnabled(false);
                }
            }
        });

        // Xử lý sự kiện nút Tạm dừng
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTimerRunning) {
                    pauseTimer();
                    btnPause.setText("Tiếp tục");
                } else {
                    startTimer();
                    btnPause.setText("Tạm dừng");
                }
            }
        });
    }

    private void startTimer() {
        // Khóa trường nhập nội dung khi đồng hồ đếm ngược bắt đầu
        etContent.setEnabled(false);
        hourPicker.setEnabled(false);
        minutePicker.setEnabled(false);
        secondPicker.setEnabled(false);

        timer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                isPaused = false;
                btnStart.setText("Bắt đầu");
                btnPause.setEnabled(false);
                btnPause.setText("Tạm dừng");

                // Mở khóa trường nhập khi đồng hồ kết thúc
                etContent.setEnabled(true);
                hourPicker.setEnabled(true);
                minutePicker.setEnabled(true);
                secondPicker.setEnabled(true);

                // Hiển thị thông báo nâng cao khi hẹn giờ kết thúc
                showEnhancedNotification();
                Log.d(TAG, "Timer finished, notification should be displayed");

                // Reset timer
                resetTimer();
            }
        }.start();

        isTimerRunning = true;
        isPaused = false;
    }

    private void pauseTimer() {
        if (timer != null) {
            timer.cancel();
        }
        isTimerRunning = false;
        isPaused = true;

        // Giữ nguyên trạng thái khóa các trường nhập khi tạm dừng
        // Không mở khóa etContent và các NumberPicker vì đồng hồ chỉ tạm dừng, không dừng hẳn
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
        isTimerRunning = false;
        isPaused = false;

        // Mở khóa trường nhập khi đồng hồ bị dừng lại
        etContent.setEnabled(true);
        hourPicker.setEnabled(true);
        minutePicker.setEnabled(true);
        secondPicker.setEnabled(true);
    }

    private void resetTimer() {
        timeLeftInMillis = 0;
        updateTimerText();
    }

    private void updateTimerText() {
        long totalSeconds = timeLeftInMillis / 1000;
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        String timeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        tvTimer.setText(timeFormatted);
    }

    private void createNotificationChannel() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String name = "Timer Notification";
                String descriptionText = "Channel for Timer App notifications";
                int importance = NotificationManager.IMPORTANCE_HIGH;

                // Tạo kênh với cấu hình cao nhất có thể
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                channel.setDescription(descriptionText);
                channel.enableVibration(true);
                channel.enableLights(true);
                channel.setLightColor(android.graphics.Color.RED);
                channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                channel.setBypassDnd(true); // Cho phép thông báo vượt qua chế độ Không làm phiền
                channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
                channel.setShowBadge(true);

                // Log để debug
                Log.d(TAG, "Đang tạo kênh thông báo: " + CHANNEL_ID);

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(channel);
                    Log.d(TAG, "Đã tạo kênh thông báo thành công");
                } else {
                    Log.e(TAG, "NotificationManager là null");
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Lỗi khi tạo kênh thông báo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showEnhancedNotification() {
        String content = etContent.getText().toString().trim();

        try {
            // Log để debug
            Log.d(TAG, "Đang hiển thị thông báo với nội dung: " + content);

            // Intent để mở app khi nhấn vào notification
            Intent intent = new Intent(context, TimerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            // Tạo PendingIntent
            PendingIntent pendingIntent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
            } else {
                pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            }

            // Tạo notification builder với nhiều tính năng hơn
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle("Hẹn giờ kết thúc")
                    .setContentText(content)
                    .setPriority(NotificationCompat.PRIORITY_MAX) // Tăng độ ưu tiên lên mức cao nhất
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setVibrate(new long[]{0, 500, 250, 500})
                    .setDefaults(NotificationCompat.DEFAULT_ALL); // Sử dụng cả âm thanh, rung và đèn LED

            // Thêm style cho thông báo nếu nội dung dài
            if (content.length() > 40) {
                NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
                bigTextStyle.bigText(content);
                builder.setStyle(bigTextStyle);
            }

            // Đảm bảo hiển thị thông báo ngay lập tức
            builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                // Hủy thông báo cũ nếu có
                notificationManager.cancel(NOTIFICATION_ID);
                // Hiển thị thông báo mới
                notificationManager.notify(NOTIFICATION_ID, builder.build());

                // Log để xác nhận thông báo đã được gửi
                Log.d(TAG, "Thông báo đã được gửi với ID: " + NOTIFICATION_ID);

                // Hiển thị toast để debug
                Toast.makeText(context, "Hẹn giờ kết thúc!", Toast.LENGTH_LONG).show();
            } else {
                Log.e(TAG, "NotificationManager là null");
            }
        } catch (Exception e) {
            Log.e(TAG, "Lỗi khi hiển thị thông báo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Cleanup resources
    public void cleanup() {
        if (timer != null) {
            timer.cancel();
            Log.d(TAG, "cleanup: Timer đã bị hủy");
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        cleanup();
    }
}
