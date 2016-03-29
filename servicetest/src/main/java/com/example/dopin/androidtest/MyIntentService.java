package com.example.dopin.androidtest;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;


public class MyIntentService extends IntentService {

    public MyIntentService() {
        super("MyIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
       Log.d("tag", "Thread id is " + Thread.currentThread().getId());
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("tag","onDestroy");
    }
}
