package com.example.dopin.baidumaptest;

import android.app.Activity;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;

public class MainActivity extends Activity {

    private MapView mapview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        mapview=(MapView)findViewById(R.id.map_view);
    }
    @Override
    protected  void onDestroy(){
        super.onDestroy();
        mapview.onDestroy();
    }
    @Override
    protected void onPause(){
        super.onPause();
        mapview.onPause();
    }
    @Override
    protected void onResume(){
        super.onResume();
        mapview.onResume();
    }
}
