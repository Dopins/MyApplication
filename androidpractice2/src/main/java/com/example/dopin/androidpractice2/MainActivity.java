package com.example.dopin.androidpractice2;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import android.os.Message;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class MainActivity extends ListActivity {

    ListView listview;
    Handler handler;
    List<Map<String, Object>> data;
    ArrayList<String> titleList;
    private ProgressBar progressBar;
    final String ZHIHUURL = "https://www.zhihu.com/explore/recommendations";
    final String strPattern="<h2><a class=\"question_link\" href=\"(.*?)\">(.*?)</a></h2>";
    private GoogleApiClient client;

    private  DrawerLayout mDrawerLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);

        handler = getHandler();
        ThreadStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    /**
     * 新开辟线程处理联网操作
     */
    private void ThreadStart() {
        new Thread() {
            public void run() {
                Message msg = new Message();
                try {
                    data = getCsdnNetDate();
                    msg.what = data.size();
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = -1;
                }
                handler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 联网获得数据
     */
    private List<Map<String, Object>> getCsdnNetDate() {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        String ZHIHUString = http_get(ZHIHUURL);
        //Pattern p = Pattern.compile("title=\"(.*?)\" href=\"(.*?)\".*?364");

        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(ZHIHUString);
        while (m.find()) {
            MatchResult mr = m.toMatchResult();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("url", mr.group(1));
            map.put("title", mr.group(2));
            titleList.add(mr.group(2).toString());
            result.add(map);
        }
        return result;
    }

    /**
     * 处理联网结果，显示在listview
     */
    private Handler getHandler() {
        return new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what < 0) {
                    Toast.makeText(MainActivity.this, "数据获取失败", Toast.LENGTH_SHORT).show();
                } else {
                    initListview();
                }
            }
        };
    }


    /**
     * 在listview里显示数据
     */
    private void initListview() {
        progressBar=(ProgressBar)findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        listview=(ListView)findViewById(android.R.id.list);
        SimpleAdapter adapter = new SimpleAdapter(this, data,
                R.layout.message_item, new String[]{"title"},
                new int[]{R.id.title});
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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



    /**
     * get请求URL，失败时尝试三次
     */
    private String http_get(String url) {
        final int RETRY_TIME = 3;
        HttpClient httpClient = null;
        HttpGet httpGet = null;

        String responseBody = "";
        int time = 0;
        do {
            try {
                httpClient = getHttpClient();
                httpGet = new HttpGet(url);
                HttpResponse response = httpClient.execute(httpGet);
                if (response.getStatusLine().getStatusCode() == 200) {
                    //用utf-8编码转化为字符串
                    byte[] bResult = EntityUtils.toByteArray(response.getEntity());
                    if (bResult != null) {
                        responseBody = new String(bResult, "utf-8");
                    }
                }
                break;
            } catch (IOException e) {
                time++;
                if (time < RETRY_TIME) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                    }
                    continue;
                }
                e.printStackTrace();
            } finally {
                httpClient = null;
            }
        } while (time < RETRY_TIME);

        return responseBody;
    }

    private HttpClient getHttpClient() {
        HttpParams httpParams = new BasicHttpParams();
        //设定连接超时和读取超时时间
        HttpConnectionParams.setConnectionTimeout(httpParams, 6000);
        HttpConnectionParams.setSoTimeout(httpParams, 30000);
        return new DefaultHttpClient(httpParams);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.dopin.androidpractice2/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.dopin.androidpractice2/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public void find(View view){
        Intent intent=new Intent(MainActivity.this,SearchActivity.class);
        intent.putStringArrayListExtra("titleList",titleList);
        startActivity(intent);
    }
    public void add(View view){
        Toast.makeText(this,"alert",Toast.LENGTH_SHORT).show();
    }
    public void menu(View view){
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }
}
