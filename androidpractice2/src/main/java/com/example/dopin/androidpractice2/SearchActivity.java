package com.example.dopin.androidpractice2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private ArrayList<String> titleList;
    private EditText edit_title;
    private Button btn_find;
    private ListView historyList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search);

        edit_title=(EditText)findViewById(R.id.edit_title);
        btn_find=(Button)findViewById(R.id.btn_find);
        historyList=(ListView)findViewById(R.id.history_list);
        Intent intent=getIntent();
        titleList=intent.getStringArrayListExtra("titleList");
    }
}
