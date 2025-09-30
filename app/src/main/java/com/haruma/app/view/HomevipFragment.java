package com.haruma.app.view;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.haruma.app.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class HomevipFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_homevip, container, false);

        // Tìm LinearLayout có id là btn1
        LinearLayout btn1 = rootView.findViewById(R.id.btn1);
        LinearLayout btn2 = rootView.findViewById(R.id.btn2);
        LinearLayout btn3 = rootView.findViewById(R.id.btn3);
        LinearLayout btn4 = rootView.findViewById(R.id.btn4);


        // Đặt sự kiện click cho LinearLayout
        btn1.setOnClickListener(v -> {
            // Chuyển đến TimeManageActivity khi nhấn vào LinearLayout
            Intent intent = new Intent(getActivity(), TimeManageActivity.class);
            startActivity(intent);
        });
        btn2.setOnClickListener(v -> {
            // Chuyển đến TimeManageActivity khi nhấn vào LinearLayout
            Intent intent = new Intent(getActivity(), CalendarActivity.class);
            startActivity(intent);
        });
        // Đặt sự kiện click cho LinearLayout
        btn3.setOnClickListener(v -> {
            // Chuyển đến TimeManageActivity khi nhấn vào LinearLayout
            Intent intent = new Intent(getActivity(), TimerActivity.class);
            startActivity(intent);
        });

        btn4.setOnClickListener(v -> {
            // Chuyển đến TimeManageActivity khi nhấn vào LinearLayout
            Intent intent = new Intent(getActivity(), StopWatchActivity.class);
            startActivity(intent);
        });

        return rootView;
    }
}
