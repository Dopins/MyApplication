package com.example.dopin.androidpractice;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dopin on 2016/3/13.
 */
public class Fragment1 extends Fragment{
    private ListView sessionListView;
    private SessionAdapter adapter;
    private List<String> sessionList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_1_view,container,false);
        sessionList=new ArrayList<String>();
        adapter=new SessionAdapter(getActivity(),R.layout.session_item,sessionList);
        initSession();
        sessionListView = (ListView)view.findViewById(R.id.session_list_view);
        sessionListView.setAdapter(adapter);

        sessionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name=sessionList.get(position);
                openSession(name);
            }
        });
        return view;
    }
    private void initSession(){
        sessionList.add("用户1");
        sessionList.add("用户2");
        sessionList.add("用户3");
        sessionList.add("用户4");
        sessionList.add("用户5");
        sessionList.add("用户6");
        sessionList.add("用户7");
        sessionList.add("用户8");
        sessionList.add("用户9");
        sessionList.add("用户10");
    }
    private void openSession(String name){
        Intent intent=new Intent(getActivity(),MessageActivity.class);
        intent.putExtra("name",name);
        startActivity(intent);
    }
}
