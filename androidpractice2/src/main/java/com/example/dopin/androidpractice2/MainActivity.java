package com.example.dopin.androidpractice2;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
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



public class MainActivity extends ListActivity implements SwipeRefreshLayout.OnRefreshListener  {

    private TextView titleView;
    private SwipeRefreshLayout mSwipeLayout;
    private List<Item> itemList;
    private  ListView mListView;
    ListView listview;
    Handler handler;
    List<Map<String, Object>> data;
    List<Map<String, String>> httpList;
    ArrayList<String> titleList;
    private GoogleApiClient client;
    private  DrawerLayout mDrawerLayout;
    private int index=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.id_swipe_ly);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(R.color.blue);


        titleList=new ArrayList<String>();
        httpList=new ArrayList<Map<String, String>>();
        setHttpList();

        itemList=new ArrayList<Item>();
        initMenuList();
        MenuAdapter adapter = new MenuAdapter(this,R.layout.menu_item,itemList);
        mListView = (ListView)findViewById(R.id.item_list_view);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View item, int position, long id) {
                item.setSelected(true);
                setPage(position);
                index = position;
                mDrawerLayout.closeDrawer(Gravity.LEFT);

            }
        });
        setPage(index);
    }

    @Override
    public void onRefresh(){
        setPage(index);
    }

    private void initMenuList() {

        Item home=new Item("知乎·日报",R.drawable.zhihu);
        Item discovery=new Item("果壳·科学人",R.drawable.guoke);
        Item follow=new Item("译言·精选",R.drawable.yiyan);
        Item collection=new Item("虎嗅·资讯",R.drawable.huxiu);
        Item table=new Item("十五言·推荐",R.drawable.shiwuyan);
        Item message=new Item("豆瓣阅读·专栏",R.drawable.douban);
        itemList.add(home);
        itemList.add(discovery);
        itemList.add(follow);
        itemList.add(collection);
        itemList.add(table);
        itemList.add(message);
    }
    private void setHttpList(){
        Map<String,String> httpMap1=new HashMap<>();
        httpMap1.put("url","http://daily.zhihu.com/");
        httpMap1.put("strPattern","<a href=\"(.*?)\" class=\"link-button\">.*?<span class=\"title\">(.*?)</span></a>");
        httpMap1.put("UrlHead","http://daily.zhihu.com");

        Map<String,String> httpMap2=new HashMap<>();
        httpMap2.put("url","http://www.guokr.com/scientific/");
        httpMap2.put("strPattern","<a class=\"article-title\" href=\"(.*?)\" target=\"_blank\" data-gaevent=.*?>(.*?)</a>");
        httpMap2.put("UrlHead","");

        Map<String,String> httpMap3=new HashMap<>();
        httpMap3.put("url","http://select.yeeyan.org/");
        httpMap3.put("strPattern","<a target=\"_blank\" href=\"(.*?)\">(.*?)</a>");
        httpMap3.put("UrlHead","");

        Map<String,String> httpMap4=new HashMap<>();
        httpMap4.put("url","http://www.huxiu.com/business");
        httpMap4.put("strPattern","<a href=\"(.*?)\" class=\"transition\" target=\"_blank\">(.*?)</a>");
        httpMap4.put("UrlHead", "http://www.huxiu.com");

        Map<String,String> httpMap5=new HashMap<>();
        httpMap5.put("url","http://www.15yan.com/topic/shi-wu-yan-chuang-kou-wen-zhang-ku/trending/");
        httpMap5.put("strPattern", "<a class=\"post-item-title-edit\" href=\"(.*?)\" title=\"(.*?)\">");
        httpMap5.put("UrlHead","http://www.15yan.com");

        Map<String,String> httpMap6=new HashMap<>();
        httpMap6.put("url","https://read.douban.com/columns/");
        httpMap6.put("strPattern", "<a href=\"(.*?)\" class=\"chapter-title\">(.*?)</a>");
        httpMap6.put("UrlHead","https://read.douban.com");

        httpList.add(httpMap1);
        httpList.add(httpMap2);
        httpList.add(httpMap3);
        httpList.add(httpMap4);
        httpList.add(httpMap5);
        httpList.add(httpMap6);
    }

    public void setPage(int index){
        mSwipeLayout.setRefreshing(true);
        titleView=(TextView)findViewById(R.id.index_title);

        Map<String,String> map;
        switch (index){
            case 0:
                titleView.setText("知乎·日报");
                 map=httpList.get(0);
                createPage(map.get("url"), map.get("strPattern"), map.get("UrlHead"));

                break;
            case 1:
                titleView.setText("果壳·科学人");
                 map=httpList.get(1);
                createPage(map.get("url"), map.get("strPattern"),map.get("UrlHead"));
                break;
            case 2:
                titleView.setText("译言·精选");
                 map=httpList.get(2);
                createPage(map.get("url"), map.get("strPattern"),map.get("UrlHead"));
                break;
            case 3:
                titleView.setText("虎嗅·资讯");
                 map=httpList.get(3);
                createPage(map.get("url"), map.get("strPattern"),map.get("UrlHead"));
                break;
            case 4:
                titleView.setText("十五言·推荐");
                 map=httpList.get(4);
                createPage(map.get("url"), map.get("strPattern"),map.get("UrlHead"));
                break;
            case 5:
                titleView.setText("豆瓣阅读·专栏");
                map=httpList.get(5);
                createPage(map.get("url"), map.get("strPattern"),map.get("UrlHead"));
                break;
            default:
                break;
        }
    }
    private  void createPage(String url,String strPattern,String urlHead){
        handler = getHandler();//处理message
        ThreadStart(url, strPattern, urlHead);//开启线程

    }
    /**
     * 新开辟线程处理联网操作
     */
    private void ThreadStart(final String url,final String strPattern,final String urlHead) {
        new Thread() {
            public void run() {
                Message msg = new Message();
                try {
                    data = getNetDate(url, strPattern, urlHead);
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
    private List<Map<String, Object>> getNetDate(String url,String strPattern,String urlHead) {
        titleList.clear();//title只存储当前列表的数据，启动新的createPage时清空titleList

        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        String response = http_get(url);
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(response);

        while (m.find()) {
            MatchResult mr = m.toMatchResult();
            Map<String, Object> map = new HashMap<String, Object>();

                map.put("url", urlHead+mr.group(1));
                map.put("title", mr.group(2));
                titleList.add((String) map.get("title"));
                titleList.add((String) map.get("url"));
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
        mSwipeLayout.setRefreshing(false);

        listview=(ListView)findViewById(android.R.id.list);
        SimpleAdapter adapter = new SimpleAdapter(this, data,
                R.layout.message_item, new String[]{"title"},
                new int[]{R.id.title});
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View item, int position,
                                    long id) {
                Map<String, Object> map = data.get(position);
                String url = (String)map.get("url");
                Intent intent = new Intent(MainActivity.this,WebViewActivity.class);
                intent.putExtra("url",url);
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
    public void onBackPressed(){
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)){
            mDrawerLayout.closeDrawers();
        }else super.onBackPressed();
    }

    @Override
    public void onStart() {
        super.onStart();
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.dopin.androidpractice2/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.dopin.androidpractice2/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public void find(View view){
        Intent intent=new Intent(MainActivity.this,SearchActivity.class);
        intent.putStringArrayListExtra("titleList", titleList);
        startActivity(intent);
    }
    public void message(View view){
        Toast.makeText(this,"alert",Toast.LENGTH_SHORT).show();
    }
    public void menu(View view){
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }
}
