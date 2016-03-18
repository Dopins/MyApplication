package com.example.dopin.androidpractice;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by dopin on 2016/3/18.
 */
public class SessionTitle extends LinearLayout {
    public SessionTitle(Context context,AttributeSet attrs){
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.index_title, this);
        Button back=(Button)findViewById(R.id.findButton);
        Button user=(Button)findViewById(R.id.addButton);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        user.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                user();
            }
        });

    }
    public void back(){

    }
    public void user(){
        Toast.makeText(getContext(),"add",Toast.LENGTH_SHORT).show();
    }
}
