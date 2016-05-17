package com.example.dopin.sunflower;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.dopin.androidpractice2.R;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class WebViewActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {

    private String collectUrl =MainActivity.serverIP+ "/SunflowerService/CollectServlet";
    private WebView webView;
    private SwipeRefreshLayout mSwipeLayout;
    String title;
    String url;
    FloatingActionsMenu menuMultipleActions;
    boolean showResult;
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
    private void init(){
        showResult=true;
        Intent intent=getIntent();
        url=intent.getStringExtra("url");
        title=intent.getStringExtra("title");

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

        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent me) {
                if (menuMultipleActions.isExpanded()) {
                    menuMultipleActions.collapse();
                }
                return false;
            }
        });

        initFloatingButton();
    }
    @Override
    public void onBackPressed(){
        if (menuMultipleActions.isExpanded()) {
            menuMultipleActions.collapse();
        } else {
            finish();
        }
    }

    private void initFloatingButton(){
        final com.getbase.floatingactionbutton.FloatingActionButton btn_share =
                (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.action_a);
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share(title, url);
            }
        });

        final com.getbase.floatingactionbutton.FloatingActionButton btn_collect =
                (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.action_b);
        btn_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collect();
            }
        });

        final com.getbase.floatingactionbutton.FloatingActionButton btn_setNote =
                new com.getbase.floatingactionbutton.FloatingActionButton(getBaseContext());
        btn_setNote.setTitle("笔记");
        btn_setNote.setIcon(android.R.drawable.ic_menu_edit);
        btn_setNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNote(title);
            }
        });

        menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        menuMultipleActions.addButton(btn_setNote);
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
        showResult=false;
        collect();
        intentNoteActivity(title);
    }
    private void intentNoteActivity(String title){
        Intent intent=new Intent(WebViewActivity.this,NoteActivity.class);
        intent.putExtra("title",title);
        startActivity(intent);
    }

    private void collect(){
        if(LeftMenuFrag.user_account.equals("")){
            Toast.makeText(this,"请先登录账号",Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(collectTask).start();
    }
    Handler handlerCollect = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            JSONObject jo=(JSONObject)msg.obj;
            try{
                if(jo.getBoolean("result")){
                    Toast.makeText(WebViewActivity.this,"收藏成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(WebViewActivity.this,"本篇已收藏",Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){

            }
        }
    };
    Runnable collectTask = new Runnable() {

        @Override
        public void run() {
            NameValuePair pair1 = new BasicNameValuePair("account", LeftMenuFrag.user_account);
            NameValuePair pair2 = new BasicNameValuePair("title", title);
            NameValuePair pair3 = new BasicNameValuePair("url", url);
            NameValuePair pair4 = new BasicNameValuePair("from", MainActivity.getFrom());

            List<NameValuePair> pairList = new ArrayList<NameValuePair>();
            pairList.add(pair1);
            pairList.add(pair2);
            pairList.add(pair3);
            pairList.add(pair4);
            try
            {
                HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList,HTTP.UTF_8);//设置编码
                HttpPost httpPost = new HttpPost(collectUrl);
                httpPost.setEntity(requestHttpEntity);
                HttpClient httpClient = new DefaultHttpClient();
                HttpResponse httpResponse = httpClient.execute(httpPost);
                if(!showResult) {
                    showResult=true;
                    return;
                }
                if (httpResponse.getStatusLine().getStatusCode()==200)
                {
                    HttpEntity httpEntity = httpResponse.getEntity();
                    String response= EntityUtils.toString(httpEntity, "utf-8");

                    JSONObject jsonObject=parseJSON(response);
                    Message msg = new Message();
                    msg.obj=jsonObject;
                    handlerCollect.sendMessage(msg);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    };
    private JSONObject parseJSON(String jsonData){
        JSONObject jsonObject;
        try{
            jsonObject=new JSONObject(jsonData);
            return jsonObject;
        }catch (Exception e){

        }
        return null;
    }
}