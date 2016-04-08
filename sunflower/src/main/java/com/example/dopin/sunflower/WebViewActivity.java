package com.example.dopin.sunflower;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.dopin.androidpractice2.R;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.net.URL;
import java.util.Map;


public class WebViewActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {

    private CollDatabaseHelper dbHelper;
    private WebView webView;
    private SwipeRefreshLayout mSwipeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        init();
        setProgressBar();
    }
    private void setProgressBar(){
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress > 60) {
                    mSwipeLayout.setRefreshing(false);
                }
            }
        });
    }
    @Override
    public void onRefresh(){
        mSwipeLayout.setRefreshing(false);
    }
    private void initFloatingButton(final String title,final String url){
        final com.getbase.floatingactionbutton.FloatingActionButton actionA = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.action_a);
        actionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share(title, url);
            }
        });

        final com.getbase.floatingactionbutton.FloatingActionButton actionB = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.action_b);
        actionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collect(title,url);
            }
        });

        final com.getbase.floatingactionbutton.FloatingActionButton actionC = new com.getbase.floatingactionbutton.FloatingActionButton(getBaseContext());
        actionC.setTitle("笔记");
        actionC.setIcon(android.R.drawable.ic_menu_edit);
        actionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNote(title);
            }
        });

        final FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        menuMultipleActions.addButton(actionC);

    }
    private void share(String title,String url){

        String content=title+"\n"+url+"\n"+"分享自『向日葵』";
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent,"分享到"));
    }

    private void setNote(String title){
        if(hasCollected(title)){
            intentNoteActivity(title);
        }else{
            Toast.makeText(WebViewActivity.this, "请先收藏本篇", Toast.LENGTH_SHORT).show();
        }
    }
    private void intentNoteActivity(String title){
        Intent intent=new Intent(WebViewActivity.this,NoteActivity.class);
        intent.putExtra("title",title);
        startActivity(intent);
    }
    private void collect(String title,String url){
        if(hasCollected(title)){
            Toast.makeText(WebViewActivity.this, "本篇已收藏", Toast.LENGTH_SHORT).show();
        }else{
            putCollection(title, url);
            Toast.makeText(WebViewActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean hasCollected(String title){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.query("Collection",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                String titleGet=cursor.getString(cursor.getColumnIndex("title"));
                if(title.equals(titleGet)) return true;

            }while(cursor.moveToNext());
        }
        return false;
    }
    private void putCollection(String title,String url){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("title",title);
        values.put("url", url);
        db.insert("Collection", null, values);
        values.clear();
    }

    private void init(){
        dbHelper=new CollDatabaseHelper(this, "Collection.db",null,1);

        Intent intent=getIntent();
        String url=intent.getStringExtra("url");
        String title=intent.getStringExtra("title");


        webView=(WebView)findViewById(R.id.web_view);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        settings.setDisplayZoomControls(false);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.id_swipe_ly);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(R.color.zhihu, R.color.guoke, R.color.yiyan,
                R.color.huxiu, R.color.shiwuyan, R.color.douban);

        mSwipeLayout.setProgressViewOffset(false, 0, 20);
        mSwipeLayout.setRefreshing(true);

        initFloatingButton(title,url);
    }
}