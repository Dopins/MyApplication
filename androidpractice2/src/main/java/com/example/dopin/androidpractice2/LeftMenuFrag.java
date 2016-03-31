package com.example.dopin.androidpractice2;

import android.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;


/**
 * Created by dopin on 2016/3/22.
 */

public class LeftMenuFrag extends Fragment {


    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.left_mune_frag, container, false);
        return view;
    }

}

