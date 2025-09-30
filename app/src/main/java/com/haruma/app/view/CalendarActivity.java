package com.haruma.app.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.haruma.app.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private TextView textView2, textView3, textView4, textView5, textView6;
    private Button button2;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private final SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", new Locale("vi")); // Thứ
    private final SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Xem lịch");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        // Ánh xạ view
        calendarView = findViewById(R.id.calendarView);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        button2 = findViewById(R.id.button2);
        textView6 = findViewById(R.id.textView6);

        Calendar today = Calendar.getInstance();
        updateDateInfo(today);

        // Xử lý chọn ngày
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar selectedCal = Calendar.getInstance();
            selectedCal.set(year, month, dayOfMonth);
            updateDateInfo(selectedCal);
        });

        // Khi bấm nút quay về hôm nay
        button2.setOnClickListener(view -> {
            long todayMillis = System.currentTimeMillis();
            calendarView.setDate(todayMillis, true, true);
            Calendar todayCal = Calendar.getInstance();
            updateDateInfo(todayCal);
        });
    }
    private void updateDateInfo(Calendar cal) {
        Date date = cal.getTime();

        // Thứ
        String thu = new SimpleDateFormat("EEEE", new Locale("vi")).format(date);
        textView2.setText(thu);

        // Ngày
        String ngay = new SimpleDateFormat("dd", Locale.getDefault()).format(date);
        textView3.setText(ngay);

        // Tháng và Năm
        String thangNam = new SimpleDateFormat("'Tháng' MM 'Năm' yyyy", Locale.getDefault()).format(date);
        textView4.setText(thangNam);

        // Giờ âm (giờ hoàng đạo)
        String gioAm = getGioAm(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        textView5.setText("Giờ " + gioAm);

        // Chỉ số ngày tốt (ví dụ đơn giản: chỉ số = (ngày % 9) * 10 + 20)
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        int chiSo = (dayOfMonth % 9) * 10 + 20; // Từ 20 đến 100
        if (chiSo > 100) chiSo = 100;
        textView6.setText("Chỉ số ngày tốt: " + chiSo + "%");
    }
    private String getGioAm(int hour) {
        if (hour >= 23 || hour < 1) return "Tý";
        if (hour >= 1 && hour < 3) return "Sửu";
        if (hour >= 3 && hour < 5) return "Dần";
        if (hour >= 5 && hour < 7) return "Mão";
        if (hour >= 7 && hour < 9) return "Thìn";
        if (hour >= 9 && hour < 11) return "Tỵ";
        if (hour >= 11 && hour < 13) return "Ngọ";
        if (hour >= 13 && hour < 15) return "Mùi";
        if (hour >= 15 && hour < 17) return "Thân";
        if (hour >= 17 && hour < 19) return "Dậu";
        if (hour >= 19 && hour < 21) return "Tuất";
        if (hour >= 21 && hour < 23) return "Hợi";
        return "";
    }

}
