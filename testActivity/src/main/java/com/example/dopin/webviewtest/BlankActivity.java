package com.example.dopin.webviewtest;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;


public class BlankActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<String> mDatas;
    private HomeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initData();

        init();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentFullscreenActivity();
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                        StaggeredGridLayoutManager.VERTICAL));
                StaggeredHomeAdapter adapter=new StaggeredHomeAdapter(BlankActivity.this,mDatas);
                mRecyclerView.setAdapter(adapter);
            }
        });
    }
    private void init(){
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        if(getScreenState()){//横屏
            mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
            mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        }else{//竖屏
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                    DividerItemDecoration.VERTICAL_LIST));
        }
        mRecyclerView.setAdapter(mAdapter = new HomeAdapter(BlankActivity.this,mDatas));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }
    public boolean getScreenState(){
        Configuration mConfiguration = BlankActivity.this.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation ;          //获取屏幕方向
        if(ori == Configuration.ORIENTATION_LANDSCAPE){             //横屏
            return true;
        }else if(ori == Configuration.ORIENTATION_PORTRAIT){        //竖屏
            return false;
        }
        return false;
    }
    protected void initData()
    {
        mDatas = new ArrayList<String>();
        for (int i = 'A'; i < 'z'; i++)
        {
            mDatas.add("" + (char) i);
        }
    }

    private void intentFullscreenActivity(){
        Intent intent=new Intent(BlankActivity.this,FullscreenActivity.class);
        startActivity(intent);
    }

}