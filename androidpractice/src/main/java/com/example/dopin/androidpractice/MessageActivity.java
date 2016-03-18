package com.example.dopin.androidpractice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends Activity {

    private Button send;
    private EditText inputText;
    private ListView msgListView;
    private MsgAdapter adapter;
    private List<Msg> msgList=new ArrayList<Msg>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_message);
        Intent intent=getIntent();
        String name=intent.getStringExtra("name");
        initMsgs();
        adapter=new MsgAdapter(this,R.layout.msg_item,msgList);
        msgListView=(ListView)findViewById(R.id.msg_list_view);
        msgListView.setAdapter(adapter);
        inputText=(EditText)findViewById(R.id.input_text);
        send=(Button)findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content=inputText.getText().toString();
                if(!content.equals("")){
                    Msg msg=new Msg(content,Msg.TYPE_SEND);
                    msgList.add(msg);
                    adapter.notifyDataSetChanged();
                    msgListView.setSelection(msgList.size());
                    inputText.setText("");
                }
            }
        });
    }
    private void initMsgs(){
        Msg msg=new Msg("Hello!",Msg.TYPE_RECEIVED);
        msgList.add(msg);
    }
}
