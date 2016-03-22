package com.example.dopin.androidpractice2;
import android.app.Activity;
import android.support.v4.widget.DrawerLayout;

import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private ArrayList<String> arrayList;
    //private DrawerLayout mDrawerLayout;
    private ListView mListView;
    private ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_frag);
        mListView = (ListView) findViewById(R.id.drawer_content);
        arrayList = new ArrayList<String>();
        arrayList.add("1");
        arrayList.add("1");
        arrayList.add("1");
        arrayList.add("1");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        mListView.setAdapter(adapter);
    }
}
