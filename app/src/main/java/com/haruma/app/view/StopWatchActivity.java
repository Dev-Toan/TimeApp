package com.haruma.app.view;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.haruma.app.R;

import java.util.ArrayList;

public class StopWatchActivity extends AppCompatActivity {

    private TextView timerTextView;
    private Button btVong, btBatdau;
    private ListView lvVong;

    private Handler handler = new Handler();
    private long startTime = 0L;
    private long timeInMillis = 0L;
    private boolean running = false;
    private int lapCount = 0; // Thêm biến đếm số vòng

    private ArrayList<String> lapList = new ArrayList<>();
    private ArrayAdapter<String> lapAdapter;

    private Runnable updateTimerRunnable = new Runnable() {
        @Override
        public void run() {
            timeInMillis = System.currentTimeMillis() - startTime;
            int minutes = (int) (timeInMillis / 1000) / 60;
            int seconds = (int) (timeInMillis / 1000) % 60;
            int milliseconds = (int) (timeInMillis % 1000) / 10;

            String time = String.format("%02d:%02d,%02d", minutes, seconds, milliseconds);
            timerTextView.setText(time);
            handler.postDelayed(this, 10);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);


        // Thiết lập Toolbar và tiêu đề
        Toolbar toolbar = findViewById(R.id.toolbar); // Lấy Toolbar từ layout
        setSupportActionBar(toolbar); // Đặt Toolbar làm ActionBar
        setTitle("Bấm giờ"); // Thiết lập tiêu đề cho Toolbar

        // Xử lý sự kiện quay lại
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Hiển thị nút quay lại
            getSupportActionBar().setHomeButtonEnabled(true); // Cho phép nhấn nút quay lại
        }

        // Lắng nghe sự kiện nhấn nút quay lại
        toolbar.setNavigationOnClickListener(view -> onBackPressed());


        timerTextView = findViewById(R.id.timerTextView);
        btVong = findViewById(R.id.btVong);
        btBatdau = findViewById(R.id.btBatdau);
        lvVong = findViewById(R.id.lvVong);

        lapAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lapList);
        lvVong.setAdapter(lapAdapter);

        btBatdau.setOnClickListener(view -> {
            if (!running) {
                startTime = System.currentTimeMillis() - timeInMillis;
                handler.post(updateTimerRunnable);
                running = true;
                btBatdau.setText("Dừng");
                btBatdau.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_red_dark));
                btVong.setText("Vòng");
            } else {
                handler.removeCallbacks(updateTimerRunnable);
                running = false;
                btBatdau.setText("Bắt đầu");
                btBatdau.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_blue_dark));
                btVong.setText("Đặt lại");
            }
        });

        // Sử dụng ArrayAdapter để hiển thị dữ liệu vào ListView


        btVong.setOnClickListener(view -> {
            if (running) {
                lapCount++; // Tăng số vòng lên mỗi lần bấm
                String lapTime = "Vòng " + lapCount + ": " + timerTextView.getText().toString();
                lapList.add(0, lapTime); // Thêm vòng mới vào đầu danh sách
                lapAdapter.notifyDataSetChanged();
            } else {
                timeInMillis = 0L;
                timerTextView.setText("00:00,00");
                lapList.clear();
                lapCount=0;
                lapAdapter.notifyDataSetChanged();
            }
        });
    }
}
