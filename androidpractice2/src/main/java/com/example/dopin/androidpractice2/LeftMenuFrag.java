package com.example.dopin.androidpractice2;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dopin on 2016/3/22.
 */

public class LeftMenuFrag extends Fragment implements View.OnClickListener {

    private List<Item> itemList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.left_mune_frag, container, false);
        itemList=new ArrayList<Item>();
        initMenuList();
        MenuAdapter adapter = new MenuAdapter(getActivity(),R.layout.menu_item,itemList);
        ListView mListView = (ListView)view.findViewById(R.id.item_list_view);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = itemList.get(position);
                Toast.makeText(getActivity(), item.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        view.findViewById(R.id.btn_setting).setOnClickListener(this);
        view.findViewById(R.id.btn_theme).setOnClickListener(this);
        view.findViewById(R.id.user).setOnClickListener(this);
        view.findViewById(R.id.sign_out).setOnClickListener(this);

        return view;
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
            case R.id.user:
                Toast.makeText(getActivity(), "user", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sign_out:
                Toast.makeText(getActivity(), "logout", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
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

