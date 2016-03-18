package com.example.dopin.androidpractice;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;


public class MainActivity extends Activity implements View.OnClickListener{

    private Fragment1 weChat;
    private Fragment2 discovery;
    private Fragment3 address_book;
    private Fragment4 me;
    private ImageButton weChatButton;
    private ImageButton addressBookButton;
    private ImageButton discoveryButton;
    private ImageButton meButton;
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
    }
    private void turnAddress_book(){
        weChatButton.setBackgroundResource(R.drawable.message);
        addressBookButton.setBackgroundResource(R.drawable.address_list_blue);
        discoveryButton.setBackgroundResource(R.drawable.compass);
        meButton.setBackgroundResource(R.drawable.user);
    }
    private void turnDiscovery(){
        weChatButton.setBackgroundResource(R.drawable.message);
        addressBookButton.setBackgroundResource(R.drawable.address_list);
        discoveryButton.setBackgroundResource(R.drawable.compass_blue);
        meButton.setBackgroundResource(R.drawable.user);
    }
    private void turnMe(){
        weChatButton.setBackgroundResource(R.drawable.message);
        addressBookButton.setBackgroundResource(R.drawable.address_list);
        discoveryButton.setBackgroundResource(R.drawable.compass);
        meButton.setBackgroundResource(R.drawable.user_blue);
    }

}
