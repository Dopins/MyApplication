package com.example.dopin.sunflower;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.dopin.androidpractice2.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends Activity implements View.OnClickListener{

    private String feedbackUrl=MainActivity.serverIP+"/SunflowerService/FeedbackServlet";
    String feedback;
    private TimePicker timePicker;
    private LinearLayout btn_dayTime;
    private LinearLayout btn_nightTime;
    private  Switch swi_clean_cache;
    private  Switch swi_clean_cache2;
    private Switch swi_save_flow;
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
        findViewById(R.id.about).setOnClickListener(this);
        findViewById(R.id.feedback).setOnClickListener(this);
        cacheSize=(TextView)findViewById(R.id.cache_size);
        setCacheText();

        swi_clean_cache=(Switch)findViewById(R.id.swi_clean_cache);
        swi_clean_cache2=(Switch)findViewById(R.id.swi_clean_cache2);
        swi_theme=(Switch)findViewById(R.id.swi_theme);
        swi_save_flow=(Switch)findViewById(R.id.swi_save_flow);

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
        dayTimeView.setText(getTimeString(hour, minute));
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
        nightTimeView.setText(getTimeString(hour, minute));
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
        swi_save_flow.setChecked(pref.getBoolean("save_flow", false));

        int dayHour=pref.getInt("day_hour",6);
        int dayMinute=pref.getInt("day_minute",0);
        int nightHour=pref.getInt("night_hour",18);
        int nightMinute=pref.getInt("night_minute",0);
        TextView dayTimeView=(TextView)findViewById(R.id.day_time_view);
        TextView nightTimeView=(TextView)findViewById(R.id.night_time_view);
        dayTimeView.setText(getTimeString(dayHour, dayMinute));
        nightTimeView.setText(getTimeString(nightHour, nightMinute));
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
        if(swi_save_flow.isChecked()) {
            MainActivity.save_flow = true;
            editor.putBoolean("save_flow", true);
        }
        else {
            MainActivity.save_flow = false;
            editor.putBoolean("save_flow", false);
        }
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
            case R.id.about:
                showAbout();
                break;
            case R.id.feedback:
                showFeedback();
                break;
            default:
                break;
        }
    }
    private void showAbout(){
        View aboutView = LayoutInflater.from(this).inflate(R.layout.about, null);
        AlertDialog.Builder customDia = new AlertDialog.Builder(this);
        customDia.setTitle("关于");
        customDia.setView(aboutView);
        AlertDialog feedbackDia=customDia.create();
        feedbackDia.show();
    }
    private void showFeedback(){
        View feedbackView = LayoutInflater.from(this).inflate(R.layout.feedback, null);
        AlertDialog.Builder customDia = new AlertDialog.Builder(this);
        customDia.setTitle("意见反馈");
        customDia.setView(feedbackView);
        final AlertDialog feedbackDia=customDia.create();

        final EditText feedbackText=(EditText)feedbackView.findViewById(R.id.feedback_text);
        Button send=(Button)feedbackView.findViewById(R.id.btn_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedback=feedbackText.getText().toString();
                new Thread(feedbackTask).start();
                feedbackDia.dismiss();
            }
        });
        feedbackDia.show();
    }

    Runnable feedbackTask = new Runnable() {

        @Override
        public void run() {
            NameValuePair pair1 = new BasicNameValuePair("account", LeftMenuFrag.user_account);
            NameValuePair pair2 = new BasicNameValuePair("feedback", feedback);

            List<NameValuePair> pairList = new ArrayList<NameValuePair>();
            pairList.add(pair1);
            pairList.add(pair2);
            try
            {
                HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList, HTTP.UTF_8);//设置编码
                HttpPost httpPost = new HttpPost(feedbackUrl);
                httpPost.setEntity(requestHttpEntity);
                HttpClient httpClient = new DefaultHttpClient();
                httpClient.execute(httpPost);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    };
}