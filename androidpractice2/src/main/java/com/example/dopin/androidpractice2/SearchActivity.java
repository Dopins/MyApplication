package com.example.dopin.androidpractice2;

import android.app.Activity;
import android.content.Intent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import java.util.regex.Pattern;

import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchActivity extends Activity {

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

        edit_title=(EditText)findViewById(R.id.edit_title);
        btn_find=(Button)findViewById(R.id.btn_find);
        Intent intent=getIntent();
        titleList=intent.getStringArrayListExtra("titleList");

        data = new ArrayList<Map<String, Object>>();

        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title=edit_title.getText().toString();
                if(title.equals("")) return;
                boolean match=false;
                Pattern pattern;
                Matcher matcher;
                for(int i = 0; i<titleList.size(); i+=2){
                    pattern = Pattern.compile(title);
                    matcher = pattern.matcher(titleList.get(i));
                    if(matcher.find())
                    {
                        match=true;
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("title", titleList.get(i));
                        map.put("url", titleList.get(i+1));
                        data.add(map);
                    }
                }
                if(match)
                {
                    res_list=(ListView)findViewById(android.R.id.list);
                    SimpleAdapter adapter = new SimpleAdapter(SearchActivity.this,data,
                            R.layout.message_item, new String[]{"title"},
                            new int[]{R.id.title});
                    res_list.setAdapter(adapter);
                    res_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                                long arg3) {
                            Map<String, Object> map = data.get(arg2);
                            String url = "https://www.zhihu.com"+(map.get("url"));
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            startActivity(intent);
                        }
                    });
                }
                else{
                    Toast.makeText(SearchActivity.this, "没有找到此标题", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
