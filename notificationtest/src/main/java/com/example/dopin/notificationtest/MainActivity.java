package com.example.dopin.notificationtest;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener{

    private Button sendNotice;
    private  Notification notification;;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendNotice=(Button)findViewById(R.id.send_notice);
        sendNotice.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.send_notice:
                NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                Notification.Builder builder = new Notification.Builder(this).setTicker("显示于屏幕顶端状态栏的文本")
                        .setSmallIcon(R.mipmap.ic_launcher);
                Intent intent=new Intent(this,NotificationActivity.class);

                PendingIntent pendingIntent=PendingIntent.
                        getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                notification = builder.setContentIntent(pendingIntent).setContentTitle("title").
                        setContentText("text").build();
                manager.notify(1, notification);
                break;
            default:
                break;
        }
    }
}
