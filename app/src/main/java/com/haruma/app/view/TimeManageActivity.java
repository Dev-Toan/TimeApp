package com.haruma.app.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.haruma.app.R;
import com.haruma.app.adapter.CustomAdapter;
import com.haruma.app.utility.AdapterSessionManager;
import com.haruma.app.utility.DatabaseHelper;
import com.haruma.app.model.ChangeCallback;
import com.haruma.app.model.Timetable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;

public class TimeManageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_manage); // Đảm bảo layout là activity_time_manage

        // Thiết lập Toolbar và tiêu đề
        Toolbar toolbar = findViewById(R.id.toolbar); // Lấy Toolbar từ layout
        setSupportActionBar(toolbar); // Đặt Toolbar làm ActionBar
        setTitle("Quản lý thời gian"); // Thiết lập tiêu đề cho Toolbar

        // Xử lý sự kiện quay lại
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Hiển thị nút quay lại
            getSupportActionBar().setHomeButtonEnabled(true); // Cho phép nhấn nút quay lại
        }

        // Lắng nghe sự kiện nhấn nút quay lại
        toolbar.setNavigationOnClickListener(view -> onBackPressed());


        ListView listView = findViewById(R.id.myListView);
        DatabaseHelper db = new DatabaseHelper(this);
        List<Timetable> myList = db.getAllTimeTable();
        Map<String, ChangeCallback> myCallback = new HashMap<>();

        myCallback.put("onChange", (id) -> {
            Intent intent = new Intent(TimeManageActivity.this, EditActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });

        myCallback.put("onDelete", (id) -> {
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa thời gian biểu hiện tại này không?")
                    .setPositiveButton("Xác nhận", (dialog, which) -> {
                        try {
                            db.deleteTimetable(id);
                            List<Timetable> updatedList = db.getAllTimeTable();
                            CustomAdapter adapter = AdapterSessionManager.getInstance().getCustomAdapter();
                            if (adapter != null) {
                                adapter.setList(updatedList);
                            }
                            Toast.makeText(TimeManageActivity.this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(TimeManageActivity.this, "Lỗi khi xóa: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Hủy", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        });

        myCallback.put("onDetail", (id) -> {
            Intent intent = new Intent(TimeManageActivity.this, DetailActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });

        CustomAdapter adapter = new CustomAdapter(this, R.layout.timetable_list_tile, myList, myCallback);
        listView.setAdapter(adapter);
        AdapterSessionManager.getInstance().setCustomAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);

        // Chuyển hướng màn hình tới trang thêm khi nhấn nút "+"
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(TimeManageActivity.this, AddActivity.class);
            startActivity(intent);
        });




    }
}
