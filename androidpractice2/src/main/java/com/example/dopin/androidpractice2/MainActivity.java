package com.example.dopin.androidpractice2;
import android.app.Activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;


public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

    }
    public void find(View view){
        Toast.makeText(this, "find", Toast.LENGTH_SHORT).show();
    }
    public void add(View view){
        Toast.makeText(this,"alert",Toast.LENGTH_SHORT).show();
    }
}
