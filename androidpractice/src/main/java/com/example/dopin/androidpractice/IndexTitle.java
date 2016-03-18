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
public class IndexTitle extends LinearLayout {
    public IndexTitle(Context context,AttributeSet attrs){
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.index_title, this);
        Button find=(Button)findViewById(R.id.findButton);
        Button add=(Button)findViewById(R.id.addButton);
        find.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                find();
            }
        });
        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });

    }
    public void find(){
        Toast.makeText(getContext(),"find",Toast.LENGTH_SHORT).show();
    }
    public void add(){
        Toast.makeText(getContext(),"add",Toast.LENGTH_SHORT).show();
    }
}
