package com.example.dopin.sunflower;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.dopin.androidpractice2.R;

/**
 * Created by dopin on 2016/3/18.
 */
public class IndexTitle extends LinearLayout {
    public IndexTitle(Context context, AttributeSet attrs){
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.index_title, this);
    }
}
