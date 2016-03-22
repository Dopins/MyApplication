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
        Message msg1=new Message("如何看待雷军和董明珠要同时进入电饭煲领域？",
                "“站在风口上，猪都能飞”——可是当风口站满了猪，还飞的起来吗？2015年，在华为的冲击下，" +
                        "小米显得有点狼狈。曾经，小米的品牌附着力核心源于“性价比”，但越来越多的国内品牌在这条路上杀得兴起，" +
                        "小米发现，对自己的定位确实很尴尬，他们正在失去对粉丝的吸引力."
                );
        Message msg2=new Message("如何看待雷军和董明珠要同时进入电饭煲领域？","");
        Message msg3=new Message("如何看待雷军和董明珠要同时进入电饭煲领域？","");
        Message msg4=new Message("如何看待雷军和董明珠要同时进入电饭煲领域？","");
        Message msg5=new Message("如何看待雷军和董明珠要同时进入电饭煲领域？","");
        Message msg6=new Message("如何看待雷军和董明珠要同时进入电饭煲领域？","");
        Message msg7=new Message("如何看待雷军和董明珠要同时进入电饭煲领域？","");
        msgList.add(msg1);
        msgList.add(msg2);
        msgList.add(msg3);
        msgList.add(msg4);
        msgList.add(msg5);
        msgList.add(msg6);
        msgList.add(msg7);
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
