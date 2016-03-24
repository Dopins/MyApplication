package com.example.dopin.androidpractice2;
import android.app.Activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private  DrawerLayout mDrawerLayout;
    private List<Message> msgList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        msgList=new ArrayList<Message>();
        initMsgList();
        MessageAdapter adapter = new MessageAdapter(this,R.layout.message_item,msgList);
        ListView mListView = (ListView)findViewById(R.id.msg_list_view);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
    private void initMsgList(){

    }
    public void find(View view){
        Toast.makeText(this, "find", Toast.LENGTH_SHORT).show();
    }
    public void add(View view){
        Toast.makeText(this,"alert",Toast.LENGTH_SHORT).show();
    }
    public void menu(View view){
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }
}
