package com.example.dopin.androidpractice2;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by dopin on 2016/3/22.
 */

public class LeftMenuFrag extends Fragment {
    private ArrayList<String> arrayList;

    private ListView mListView;
    private ArrayAdapter<String> adapter;
    private ImageView item_image;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.left_mune_frag,container,false);

        mListView = (ListView)view.findViewById(R.id.drawer_content);
        item_image=(ImageView)view.findViewById(R.id.item_image);
        initMenuList();
        return view;
    }
    private void initMenuList() {

        arrayList = new ArrayList<String>();
        arrayList.add("首页");
        arrayList.add("发现");
        arrayList.add("关注");
        arrayList.add("收藏");
        arrayList.add("圆桌");
        arrayList.add("私信");
        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.menu_item, arrayList);
        mListView.setAdapter(adapter);
    }
}

