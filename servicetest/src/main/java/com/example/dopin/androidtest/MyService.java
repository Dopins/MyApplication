package com.example.dopin.androidtest;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by dopin on 2016/3/26.
 */
public class MyService extends Service{
    private DownloadBinder mBinder=new DownloadBinder();
    class DownloadBinder extends Binder {
        public void startDownload(){
            Log.d("tag","startDownload");
        }
        public int getProgress(){
            Log.d("tag","getProgress");
            return 0;
        }
    }
    @Override
    public IBinder onBind(Intent intent){
        return mBinder;
    }
    @Override
    public void onCreate(){
        Log.d("tag","onCreate");
        super.onCreate();
        Notification.Builder builder = new Notification.Builder(this).setTicker("显示于屏幕顶端状态栏的文本")
                .setSmallIcon(R.mipmap.ic_launcher);
        Intent intent=new Intent(this,MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.
                getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notification = builder.setContentIntent(pendingIntent).setContentTitle("title").
                setContentText("text").build();
        notification.defaults=Notification.DEFAULT_ALL;

        startForeground(1, notification);
    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        Log.d("tag","onStartCommand");
        return super.onStartCommand(intent,flags,startId);
    }
    @Override
    public void onDestroy(){
        Log.d("tag","onDestroy");
        super.onDestroy();
    }
}
