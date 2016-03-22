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
import java.util.List;

/**
 * Created by dopin on 2016/3/22.
 */

public class LeftMenuFrag extends Fragment {

    private List<Item> itemList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.left_mune_frag,container,false);
        itemList=new ArrayList<Item>();
        initMenuList();
        MenuAdapter adapter = new MenuAdapter(getActivity(),R.layout.menu_item,itemList);
        ListView mListView = (ListView)view.findViewById(R.id.item_list_view);
        mListView.setAdapter(adapter);
        return view;
    }
    private void initMenuList() {

        Item home=new Item("首页",R.drawable.setting);
        Item discovery=new Item("发现",R.drawable.discovery);
        Item follow=new Item("关注",R.drawable.eye);
        Item collection=new Item("收藏",R.drawable.label);
        Item table=new Item("圆桌",R.drawable.table);
        Item message=new Item("私信",R.drawable.message);
        itemList.add(home);
        itemList.add(discovery);
        itemList.add(follow);
        itemList.add(collection);
        itemList.add(table);
        itemList.add(message);


    }
}

