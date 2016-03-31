package com.example.dopin.androidpractice2;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends Activity implements View.OnClickListener{

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
        setContentView(R.layout.activity_setting);

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

        setSetting();

        MainActivity.mDrawerLayout.closeDrawer(Gravity.LEFT);
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