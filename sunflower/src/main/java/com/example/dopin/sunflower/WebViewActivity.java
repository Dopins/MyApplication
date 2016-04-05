package com.example.dopin.sunflower;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.dopin.androidpractice2.R;


public class WebViewActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {

    private WebView webView;
    private SwipeRefreshLayout mSwipeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
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

        Intent intent=getIntent();
        String url=intent.getStringExtra("url");

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
        mSwipeLayout.setColorSchemeResources(R.color.zhihu, R.color.guoke,R.color.yiyan,
                R.color.huxiu,R.color.shiwuyan,R.color.douban );

        mSwipeLayout.setProgressViewOffset(false, 0, 20);
        mSwipeLayout.setRefreshing(true);
    }
}