package com.example.dopin.androidpractice2;

import android.app.Activity;
import android.content.Intent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import java.util.regex.Pattern;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.Toast;
import java.util.ArrayList;

public class SearchActivity extends Activity {

    private LinearLayout searchBack;
    private  MessageAdapter adapter;
    private ArrayList<String> titleList;
    private List<Map<String, Object>> data;
    private EditText edit_title;
    private Button btn_find;
    private ListView res_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search);

        init();
    }

    private void init(){
        edit_title=(EditText)findViewById(R.id.edit_title);
        btn_find=(Button)findViewById(R.id.btn_find);
        searchBack=(LinearLayout)findViewById(R.id.search_background);
        res_list=(ListView)findViewById(android.R.id.list);

        Intent intent=getIntent();
        titleList=intent.getStringArrayListExtra("titleList");
        int index=MainActivity.index;
        setBackgroundColor(index);

        data = new ArrayList<Map<String, Object>>();

        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                found();
            }
        });
    }
    private void setBackgroundColor(int index){
        if(MainActivity.night){
            searchBack.setBackgroundColor(getResources().getColor(R.color.night_title));
            res_list.setBackgroundColor(getResources().getColor(R.color.night_item_back));
            return;
        }
        switch (index){
            case 0:
                searchBack.setBackgroundColor(getResources().getColor(R.color.zhihu));
                break;
            case 1:
                searchBack.setBackgroundColor(getResources().getColor(R.color.guoke));
                break;
            case 2:
                searchBack.setBackgroundColor(getResources().getColor(R.color.yiyan));
                break;
            case 3:
                searchBack.setBackgroundColor(getResources().getColor(R.color.huxiu));
                break;
            case 4:
                searchBack.setBackgroundColor(getResources().getColor(R.color.shiwuyan));
                break;
            case 5:
                searchBack.setBackgroundColor(getResources().getColor(R.color.douban));
                break;
            case 6:
                searchBack.setBackgroundColor(getResources().getColor(R.color.collection));
            default:
                break;
        }
    }

    private void found(){
        String title=edit_title.getText().toString();
        if(title.equals("")) {
            Toast.makeText(SearchActivity.this, "请输入关键词", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean match=false;
        Pattern pattern;
        Matcher matcher;
        try {
            for(int i = 0; i<titleList.size(); i+=2) {
                pattern = Pattern.compile(title);
                matcher = pattern.matcher(titleList.get(i));
                if (matcher.find()) {
                    match = true;
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("title", titleList.get(i));
                    map.put("url", titleList.get(i + 1));
                    data.add(map);
                }
            }
            if (match) {
                isFound();
            } else {
                isNoFound();
            }
        } catch (Exception e) {
                isNoFound();
        }

    }

    private void isFound(){
        adapter = new MessageAdapter(SearchActivity.this,data,
                R.layout.message_item, new String[]{"title"},
                new int[]{R.id.title});
        res_list.setAdapter(adapter);
        res_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Map<String, Object> map = data.get(arg2);
                String url = (String)map.get("url");
                Intent intent = new Intent(SearchActivity.this,WebViewActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });
    }

    private void isNoFound(){
        data.clear();
        if(adapter!=null){
        adapter.notifyDataSetChanged();
        }
        Toast.makeText(SearchActivity.this, "没有找到此标题", Toast.LENGTH_SHORT).show();
    }
}
