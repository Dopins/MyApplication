package com.example.dopin.androidpractice;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;


public class MainActivity extends Activity implements View.OnClickListener{

    private Fragment1 weChat;
    private Fragment2 discovery;
    private Fragment3 address_book;
    private Fragment4 me;

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

        findViewById(R.id.weChat).setOnClickListener(this);
        findViewById(R.id.address_book).setOnClickListener(this);
        findViewById(R.id.discovery).setOnClickListener(this);
        findViewById(R.id.me).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = getFragmentManager();
        switch (v.getId()){
            case R.id.weChat:
                fm.beginTransaction().replace(R.id.frag_content,weChat).commit();
                break;
            case R.id.address_book:

                fm.beginTransaction().replace(R.id.frag_content,address_book).commit();
                break;
            case R.id.discovery:

                fm.beginTransaction().replace(R.id.frag_content,discovery).commit();
                break;
            case R.id.me:

                fm.beginTransaction().replace(R.id.frag_content,me).commit();
                break;
        }
    }
}
