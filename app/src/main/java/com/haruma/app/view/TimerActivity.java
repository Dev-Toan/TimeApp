package com.haruma.app.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.haruma.app.R;
import com.haruma.app.viewmodel.TimerViewModel;
import androidx.constraintlayout.widget.ConstraintLayout;

public class TimerActivity extends AppCompatActivity {

    private EditText etContent;
    private NumberPicker hourPicker;
    private NumberPicker minutePicker;
    private NumberPicker secondPicker;
    private TextView tvTimer;
    private Button btnStart;
    private Button btnPause;
    private ConstraintLayout mainContainer;

    private TimerViewModel viewModel;

    private static final String TAG = "TimerApp";
    private static final int REQUEST_NOTIFICATION_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);



        // Thiết lập Toolbar và tiêu đề
        Toolbar toolbar = findViewById(R.id.toolbar); // Lấy Toolbar từ layout
        setSupportActionBar(toolbar); // Đặt Toolbar làm ActionBar
        setTitle("Hẹn giờ"); // Thiết lập tiêu đề cho Toolbar

        // Xử lý sự kiện quay lại
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Hiển thị nút quay lại
            getSupportActionBar().setHomeButtonEnabled(true); // Cho phép nhấn nút quay lại
        }

        // Lắng nghe sự kiện nhấn nút quay lại
        toolbar.setNavigationOnClickListener(view -> onBackPressed());


        // Khởi tạo ViewModel
        viewModel = new ViewModelProvider(this).get(TimerViewModel.class);
        viewModel.initialize(this);

        // Khởi tạo các view
        etContent = findViewById(R.id.etContent);
        hourPicker = findViewById(R.id.hourPicker);
        minutePicker = findViewById(R.id.minutePicker);
        secondPicker = findViewById(R.id.secondPicker);
        tvTimer = findViewById(R.id.tvTimer);
        btnStart = findViewById(R.id.btnStart);
        btnPause = findViewById(R.id.btnPause);// màu xanh lá cây
        mainContainer = findViewById(R.id.mainContainer);

        // Kết nối ViewModel với View
        viewModel.setupViews(etContent, hourPicker, minutePicker, secondPicker, tvTimer, btnStart, btnPause);

        // Kiểm tra và yêu cầu quyền thông báo (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission();
        }
    }

    // Yêu cầu quyền thông báo cho Android 13+
    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Yêu cầu quyền thông báo
                Log.d(TAG, "Yêu cầu quyền thông báo");
                requestPermissions(
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        REQUEST_NOTIFICATION_PERMISSION);
            } else {
                Log.d(TAG, "Đã có quyền thông báo");
            }
        }
    }

    // Xử lý kết quả của yêu cầu quyền
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Đã cấp quyền thông báo", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Đã được cấp quyền thông báo");
            } else {
                Toast.makeText(this, "Quyền thông báo bị từ chối. Một số tính năng có thể không hoạt động.", Toast.LENGTH_LONG).show();
                Log.d(TAG, "Quyền thông báo bị từ chối");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.cleanup();
    }
}