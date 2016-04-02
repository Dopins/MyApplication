package com.example.dopin.androidpractice2;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class SettingActivity extends Activity implements View.OnClickListener{

    private TimePicker timePicker;
    private LinearLayout btn_dayTime;
    private LinearLayout btn_nightTime;
    private  Switch swi_clean_cache;
    private  Switch swi_clean_cache2;
    private Switch swi_theme;
    private TextView cacheSize;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(MainActivity.night){
            setContentView(R.layout.activity_setting_night);
        }else{
            setContentView(R.layout.activity_setting);
        }
        init();
    }
    private void setCacheText(){
        try {
            String size = DataCleanManager.getCacheSize(this.getCacheDir());
            cacheSize.setText(size);
        }catch (Exception e){

        }
    }
    private void init(){
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        editor=pref.edit();

        findViewById(R.id.clean_cache).setOnClickListener(this);
        cacheSize=(TextView)findViewById(R.id.cache_size);
        setCacheText();

        swi_clean_cache=(Switch)findViewById(R.id.swi_clean_cache);
        swi_clean_cache2=(Switch)findViewById(R.id.swi_clean_cache2);
        swi_theme=(Switch)findViewById(R.id.swi_theme);
        btn_dayTime=(LinearLayout)findViewById(R.id.day_time);
        btn_nightTime=(LinearLayout)findViewById(R.id.night_time);
        btn_dayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker("day");
            }
        });
        btn_nightTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker("night");
            }
        });

        setSetting();

        MainActivity.mDrawerLayout.closeDrawer(Gravity.LEFT);
    }
    private void showTimePicker(final String time) {
        final View viewDia = LayoutInflater.from(SettingActivity.this).inflate(R.layout.time_picker, null);
        AlertDialog.Builder customDia = new AlertDialog.Builder(SettingActivity.this);

        timePicker=(TimePicker)viewDia.findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);
        customDia.setTitle("选择时间");
        customDia.setView(viewDia);
        customDia.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();
                if (time.equals("night")) {
                    saveNightTime(hour, minute);
                } else {
                    saveDayTime(hour, minute);
                }
            }
        });
        customDia.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        customDia.create().show();
    }
    private void saveDayTime(int hour,int minute){
        TextView dayTimeView=(TextView)findViewById(R.id.day_time_view);
        editor.putInt("day_hour", hour);
        editor.putInt("day_minute", minute);
        editor.commit();
        dayTimeView.setText(getTimeString(hour,minute));
    }
    private String getTimeString(int hour,int minute){
        if(minute<10){
            return hour+":0"+minute;
        }else{
            return hour+":"+minute;
        }
    }
    private void saveNightTime(int hour,int minute){
        TextView nightTimeView=(TextView)findViewById(R.id.night_time_view);
        editor.putInt("night_hour", hour);
        editor.putInt("night_minute", minute);
        editor.commit();
        nightTimeView.setText(getTimeString(hour,minute));
    }

    @Override
    public void onDestroy(){
        putSetting();
        super.onDestroy();
    }

    private void setSetting(){
        swi_clean_cache.setChecked(pref.getBoolean("clean_when_close", false));
        swi_clean_cache2.setChecked(pref.getBoolean("clean_when_size", false));
        swi_theme.setChecked(pref.getBoolean("auto_theme", false));
        int dayHour=pref.getInt("day_hour",6);
        int dayMinute=pref.getInt("day_minute",0);
        int nightHour=pref.getInt("night_hour",18);
        int nightMinute=pref.getInt("night_minute",0);
        TextView dayTimeView=(TextView)findViewById(R.id.day_time_view);
        TextView nightTimeView=(TextView)findViewById(R.id.night_time_view);
        dayTimeView.setText(getTimeString(dayHour,dayMinute));
        nightTimeView.setText(getTimeString(nightHour,nightMinute));
    }
    private void putSetting(){
        if(swi_clean_cache.isChecked())
            editor.putBoolean("clean_when_close",true);
        else
            editor.putBoolean("clean_when_close",false);
        if(swi_clean_cache2.isChecked())
            editor.putBoolean("clean_when_size",true);
        else
            editor.putBoolean("clean_when_size",false);
        if(swi_theme.isChecked())
            editor.putBoolean("auto_theme",true);
        else
            editor.putBoolean("auto_theme", false);

        editor.commit();
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.clean_cache:
                DataCleanManager.cleanInternalCache(this);
                setCacheText();
                Toast.makeText(this, "清理成功", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}