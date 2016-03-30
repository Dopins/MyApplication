package com.example.dopin.androidpractice2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WebViewActivity extends Activity {

    private LinearLayout progressLayout;
    private ProgressBar progressBar;
    private TextView progressText;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_web_view);

        init();
        setProgressBar();
    }
    private void setProgressBar(){
        webView.setWebChromeClient(new WebChromeClient()
        {
            public void onProgressChanged(WebView view,int newProgress)
            {
                if(newProgress>50){
                    progressBar.setVisibility(View.GONE);
                    progressText.setVisibility(View.GONE);
                    progressLayout.setVisibility(View.GONE);
                }
            }
        });

    }
    private void init(){
        progressLayout = (LinearLayout) findViewById(R.id.progress_layout);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressText = (TextView) findViewById(R.id.progress_text);

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
    }
}
