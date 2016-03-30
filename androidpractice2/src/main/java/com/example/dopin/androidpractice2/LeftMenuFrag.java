package com.example.dopin.androidpractice2;

import android.app.Fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;


/**
 * Created by dopin on 2016/3/22.
 */

public class LeftMenuFrag extends Fragment implements View.OnClickListener {


    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null != view) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (null != parent) {
                parent.removeView(view);
            }
        } else {
            view = inflater.inflate(R.layout.left_mune_frag, container, false);
            initView(view);// 控件初始化
        }
        return view;
    }

    private void  initView(View view){
        view.findViewById(R.id.btn_setting).setOnClickListener(this);
        view.findViewById(R.id.btn_theme).setOnClickListener(this);
        view.findViewById(R.id.btn_collection).setOnClickListener(this);
        view.findViewById(R.id.btn_about).setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id) {
            case R.id.btn_setting:
                Toast.makeText(getActivity(), "setting", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_theme:
                Toast.makeText(getActivity(), "theme", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_collection:
                Toast.makeText(getActivity(), "collection", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_about:
                Toast.makeText(getActivity(), "about", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

}

