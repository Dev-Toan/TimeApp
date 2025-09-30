package com.haruma.app.viewmodel;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;

import androidx.databinding.BaseObservable;

import com.haruma.app.model.Callback;

public class HelpViewModel extends BaseObservable {
    private final Application application;
    private final Callback onCall;

    public HelpViewModel(Application application, Callback onCall) {
        this.application = application;
        this.onCall = onCall;
    }

    public void onCall() {
        onCall.run();
    }

    public void onSMS() {
        Uri smsUri = Uri.parse("smsto:0123456789");
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsUri);
        intent.putExtra("sms_body", "Xin chào, tôi cần hỗ trợ.");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        application.startActivity(intent);
    }

    public void onEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:android8@nhom5.com"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Yêu cầu hỗ trợ");
        intent.putExtra(Intent.EXTRA_TEXT, "Xin chào nhóm phát triển, tôi cần hỗ trợ về...");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        application.startActivity(intent);
    }
}
