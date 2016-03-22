package com.example.dopin.notificationtest;

import android.app.NotificationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        NotificationManager notificationManager=(NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
    }
}
