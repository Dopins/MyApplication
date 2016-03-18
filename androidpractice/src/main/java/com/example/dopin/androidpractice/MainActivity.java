package com.example.dopin.androidpractice;

import android.app.Activity;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements View.OnClickListener{

    private Fragment1 weChat;
    private Fragment2 discovery;
    private Fragment3 address_book;
    private Fragment4 me;
    private ImageButton weChatButton;
    private ImageButton addressBookButton;
    private ImageButton discoveryButton;
    private ImageButton meButton;
    private TextView weChatText;
    private TextView address_listText;
    private TextView discoveryText;
    private TextView meText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        weChat= new Fragment1();
        discovery= new Fragment2();
        address_book= new Fragment3();
        me= new Fragment4();

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.frag_content,weChat).commit();

        weChatText=(TextView)findViewById(R.id.weChatText);
        address_listText=(TextView)findViewById(R.id.address_bookText);
        discoveryText=(TextView)findViewById(R.id.discoveryText);
        meText=(TextView)findViewById(R.id.meText);

        weChatButton=(ImageButton)findViewById(R.id.weChatButton);
        addressBookButton=(ImageButton)findViewById(R.id.address_bookButton);
        discoveryButton=(ImageButton)findViewById(R.id.discoveryButton);
        meButton=(ImageButton)findViewById(R.id.meButton);

        weChatButton.setOnClickListener(this);
        addressBookButton.setOnClickListener(this);
        discoveryButton.setOnClickListener(this);
        meButton.setOnClickListener(this);

        findViewById(R.id.weChat).setOnClickListener(this);
        findViewById(R.id.address_book).setOnClickListener(this);
        findViewById(R.id.discovery).setOnClickListener(this);
        findViewById(R.id.me).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = getFragmentManager();
        int id=v.getId();
            if(id==R.id.weChat||id==R.id.weChatButton) {
                fm.beginTransaction().replace(R.id.frag_content, weChat).commit();
                turnWeChat();
            }
            else if(id==R.id.address_book||id==R.id.address_bookButton) {
                fm.beginTransaction().replace(R.id.frag_content, address_book).commit();
                turnAddress_book();
            }
            else if(id==R.id.discovery||id==R.id.discoveryButton) {
                fm.beginTransaction().replace(R.id.frag_content, discovery).commit();
                turnDiscovery();
            }
            else if(id==R.id.me||id==R.id.meButton){
                fm.beginTransaction().replace(R.id.frag_content, me).commit();
                turnMe();
            }
    }
    private void turnWeChat(){
        weChatButton.setBackgroundResource(R.drawable.message_blue);
        addressBookButton.setBackgroundResource(R.drawable.address_list);
        discoveryButton.setBackgroundResource(R.drawable.compass);
        meButton.setBackgroundResource(R.drawable.user);

        weChatText.setTextColor(getResources().getColor(R.color.blue));
        address_listText.setTextColor(getResources().getColor(R.color.darkGray));
        discoveryText.setTextColor(getResources().getColor(R.color.darkGray));
        meText.setTextColor(getResources().getColor(R.color.darkGray));
    }
    private void turnAddress_book(){
        weChatButton.setBackgroundResource(R.drawable.message);
        addressBookButton.setBackgroundResource(R.drawable.address_list_blue);
        discoveryButton.setBackgroundResource(R.drawable.compass);
        meButton.setBackgroundResource(R.drawable.user);

        address_listText.setTextColor(getResources().getColor(R.color.blue));
        weChatText.setTextColor(getResources().getColor(R.color.darkGray));
        discoveryText.setTextColor(getResources().getColor(R.color.darkGray));
        meText.setTextColor(getResources().getColor(R.color.darkGray));
    }
    private void turnDiscovery(){
        weChatButton.setBackgroundResource(R.drawable.message);
        addressBookButton.setBackgroundResource(R.drawable.address_list);
        discoveryButton.setBackgroundResource(R.drawable.compass_blue);
        meButton.setBackgroundResource(R.drawable.user);

        discoveryText.setTextColor(getResources().getColor(R.color.blue));
        address_listText.setTextColor(getResources().getColor(R.color.darkGray));
        weChatText.setTextColor(getResources().getColor(R.color.darkGray));
        meText.setTextColor(getResources().getColor(R.color.darkGray));
    }
    private void turnMe(){
        weChatButton.setBackgroundResource(R.drawable.message);
        addressBookButton.setBackgroundResource(R.drawable.address_list);
        discoveryButton.setBackgroundResource(R.drawable.compass);
        meButton.setBackgroundResource(R.drawable.user_blue);

        meText.setTextColor(getResources().getColor(R.color.blue));
        address_listText.setTextColor(getResources().getColor(R.color.darkGray));
        discoveryText.setTextColor(getResources().getColor(R.color.darkGray));
        weChatText.setTextColor(getResources().getColor(R.color.darkGray));
    }
    public void find(View view){
        Toast.makeText(this, "find", Toast.LENGTH_SHORT).show();
    }
    public void add(View view){
        Toast.makeText(this,"add",Toast.LENGTH_SHORT).show();
    }

}
