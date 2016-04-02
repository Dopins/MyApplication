package com.example.dopin.androidpractice2;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.format.Time;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Message;
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

public class MainActivity extends ListActivity implements SwipeRefreshLayout.OnRefreshListener,View.OnClickListener  {

    public static boolean night;
    private CollDatabaseHelper dbHelper;
    private LinearLayout titleLayout;
    private TextView titleView;
    private SwipeRefreshLayout mSwipeLayout;
    private List<Item> itemList;
    private ListView mListView;
    private ListView listview;
    private Handler handler;
    private List<Map<String, Object>> data;
    private List<Map<String, String>> httpList;
    private ArrayList<String> titleList;
    private GoogleApiClient client;
    public static  DrawerLayout mDrawerLayout;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    public static int index=-1;
    private long exitTime = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        init();
        setHttpList();
        setPage(0);
    }

    private void init(){
        initMenuList();
        listview=(ListView)findViewById(android.R.id.list);
        this.registerForContextMenu(listview);

        dbHelper=new CollDatabaseHelper(this,"Collection.db",null,1);

        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        editor=pref.edit();

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.id_swipe_ly);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(R.color.blue);
        mSwipeLayout.setProgressViewOffset(false, 0, 40);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        titleList=new ArrayList<String>();
        titleView=(TextView)findViewById(R.id.index_title);
        titleLayout=(LinearLayout)findViewById(R.id.title_layout);

        findViewById(R.id.btn_setting).setOnClickListener(this);
        findViewById(R.id.btn_theme).setOnClickListener(this);
        findViewById(R.id.btn_collection).setOnClickListener(this);
        findViewById(R.id.btn_about).setOnClickListener(this);

        setTheme();
    }

    private void setTheme(){
        boolean auto_theme=pref.getBoolean("auto_theme", false);
        if(auto_theme){
            int dayHour=pref.getInt("day_hour",6);
            int dayMinute=pref.getInt("day_minute",0);
            int nightHour=pref.getInt("night_hour",18);
            int nightMinute=pref.getInt("night_minute",0);
            Time time=new Time();
            time.setToNow();
            int hour=time.hour;
            int minute=time.minute;
            if((dayHour < hour && hour < nightHour)||(hour == dayHour && minute > dayMinute)||(hour == nightHour && minute < nightMinute)) {
                setDayTheme();
            }else{
                setNightTheme();
            }
        }else{
            night=false;
        }
    }
    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id) {
            case R.id.btn_setting:
                intentSetting();
                break;
            case R.id.btn_theme:
                changeTheme();
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.btn_collection:
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                setCollection();
                index=6;
                break;
            case R.id.btn_about:
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                intentAbout();
                break;
            default:
                break;
        }
    }
    private void changeTheme(){
        if(night){
            setDayTheme();
        }else{
            setNightTheme();
        }
    }
    private void setNightTheme(){
        night=true;

        Button btn_collection = (Button)findViewById(R.id.btn_collection);
        Button btn_theme=(Button)findViewById(R.id.btn_theme);
        Button btn_setting = (Button) findViewById(R.id.btn_setting);
        Button btn_about=(Button)findViewById(R.id.btn_about);

        mSwipeLayout.setColorSchemeResources(R.color.night_title);
        titleLayout.setBackgroundColor(getResources().getColor(R.color.night_title));
        mListView.setBackgroundColor(getResources().getColor(R.color.night_item_back));
        listview.setBackgroundColor(getResources().getColor(R.color.night_item_back));
        listview.setDivider(new ColorDrawable(getResources().getColor(R.color.night_title)));
        listview.setDividerHeight(15);

        btn_collection.setTextColor(getResources().getColor(R.color.night_item_font));
        btn_theme.setTextColor(getResources().getColor(R.color.night_item_font));
        btn_setting.setTextColor(getResources().getColor(R.color.night_item_font));
        btn_about.setTextColor(getResources().getColor(R.color.night_item_font));

        btn_collection.setBackgroundColor(getResources().getColor(R.color.night_item_back));
        btn_theme.setBackgroundColor(getResources().getColor(R.color.night_item_back));
        btn_setting.setBackgroundColor(getResources().getColor(R.color.night_item_back));
        btn_about.setBackgroundColor(getResources().getColor(R.color.night_item_back));

        MenuAdapter madapter = new MenuAdapter(this,R.layout.menu_item,itemList);
        mListView.setAdapter(madapter);

        if(data!=null) {
            MessageAdapter adapter = new MessageAdapter(this, data,
                    R.layout.message_item, new String[]{"title"},
                    new int[]{R.id.title});
            listview.setAdapter(adapter);
        }
    }
    private void setDayTheme(){
        night=false;

        Button btn_collection=(Button)findViewById(R.id.btn_collection);
        Button btn_theme=(Button)findViewById(R.id.btn_theme);
        Button btn_setting=(Button)findViewById(R.id.btn_setting);
        Button btn_about = (Button)findViewById(R.id.btn_about);

        setTitleBackground();

        mListView.setBackgroundColor(getResources().getColor(R.color.white));
        listview.setBackgroundColor(getResources().getColor(R.color.white));
        listview.setDivider(new ColorDrawable(getResources().getColor(R.color.light_gray)));
        listview.setDividerHeight(15);

        btn_collection.setTextColor(getResources().getColor(R.color.menu_item));
        btn_theme.setTextColor(getResources().getColor(R.color.menu_item));
        btn_setting.setTextColor(getResources().getColor(R.color.menu_item));
        btn_about.setTextColor(getResources().getColor(R.color.menu_item));

        btn_collection.setBackgroundColor(getResources().getColor(R.color.white));
        btn_theme.setBackgroundColor(getResources().getColor(R.color.white));
        btn_setting.setBackgroundColor(getResources().getColor(R.color.white));
        btn_about.setBackgroundColor(getResources().getColor(R.color.white));

        MenuAdapter madapter = new MenuAdapter(this,R.layout.menu_item,itemList);
        mListView.setAdapter(madapter);

        if(data!=null) {
            MessageAdapter adapter = new MessageAdapter(this, data,
                    R.layout.message_item, new String[]{"title"},
                    new int[]{R.id.title});
            listview.setAdapter(adapter);
        }
    }
    private void setTitleBackground(){
        switch (index) {
            case 0:
                setBackgroundColor(R.color.zhihu);
                break;
            case 1:
                setBackgroundColor(R.color.guoke);
                break;
            case 2:
                setBackgroundColor(R.color.yiyan);
                break;
            case 3:
                setBackgroundColor(R.color.huxiu);
                break;
            case 4:
                setBackgroundColor(R.color.shiwuyan);
                break;
            case 5:
                setBackgroundColor(R.color.douban);
                break;
            case 6:
                setBackgroundColor(R.color.collection);
            default:
                break;
        }
    }
    private void intentSetting(){
        Intent intent=new Intent(this,SettingActivity.class);
        startActivity(intent);
    }
    private void intentAbout(){
        Intent intent=new Intent(this, AboutActivity.class);
        startActivity(intent);
    }
    private void setCollection(){
        mSwipeLayout.setRefreshing(false);
        data.clear();
        data=getCollectionData();
        MessageAdapter adapter = new MessageAdapter(this, data,
                R.layout.message_item, new String[]{"title","label"},
                new int[]{R.id.title,R.id.message_label});
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        setBackgroundColor( R.color.collection);
        setTitleText("我的收藏");
    }
    private List<Map<String, Object>> getCollectionData(){
        titleList.clear();                      //title只存储当前列表的数据，启动新的createPage时清空titleList
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.query("Collection",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                String title=cursor.getString(cursor.getColumnIndex("title"));
                String url=cursor.getString(cursor.getColumnIndex("url"));
                String label=cursor.getString(cursor.getColumnIndex("label"));
                String note=cursor.getString(cursor.getColumnIndex("note"));

                Map<String,Object> map=new HashMap<>();
                map.put("title",title);
                map.put("url",url);
                map.put("label",label);
                map.put("note",note);

                titleList.add(title);
                titleList.add(url);

                result.add(map);
            }while(cursor.moveToNext());
        }
        return result;
    }
    @Override
    public void onDestroy(){
        cleanCache();
        super.onDestroy();
    }
    private void cleanCache(){
        boolean clean=pref.getBoolean("clean_when_close", false);
        boolean clean2=pref.getBoolean("clean_when_size", false);
        if(clean) DataCleanManager.cleanInternalCache(this);
        if(clean2) {
            try {
                String size = DataCleanManager.getCacheSize(this.getCacheDir());
                float num=Float.valueOf(size.substring(0,size.length()-3));
                if(size.endsWith("MB")&&num>50){
                    DataCleanManager.cleanInternalCache(this);
                }
            }catch (Exception e){
            }
        }
    }

    @Override
    public void onRefresh(){
        if(index==6){
            setCollection();
            mSwipeLayout.setRefreshing(false);
            return;
        }
        setPage(index);
    }

    private void initMenuList() {
        mListView = (ListView)findViewById(R.id.item_list_view);
        itemList=new ArrayList<Item>();

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

        MenuAdapter madapter = new MenuAdapter(this,R.layout.menu_item,itemList);
        mListView.setAdapter(madapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View item, int position, long id) {
                item.setSelected(true);
                setPage(position);
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
    }

    private void setHttpList(){
        httpList=new ArrayList<Map<String, String>>();

        Map<String,String> httpMap1=new HashMap<>();
        httpMap1.put("url","http://daily.zhihu.com/");
        httpMap1.put("strPattern","<div class=\"box\"><a href=\"(.*?)\" class=\"link-button\">.*?<span class=\"title\">(.*?)</span></a>");
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
        httpMap6.put("strPattern", "<span class=\"chapter-title-wrapper\"><a href=\"(.*?)\" class=\"chapter-title\">(.*?)</a>");
        httpMap6.put("UrlHead","https://read.douban.com");

        httpList.add(httpMap1);
        httpList.add(httpMap2);
        httpList.add(httpMap3);
        httpList.add(httpMap4);
        httpList.add(httpMap5);
        httpList.add(httpMap6);
    }

    public void setPage(int index){
        this.index=index;

        Map<String,String> map;
        switch (index){
            case 0:
                setBackgroundColor( R.color.zhihu);
                setTitleText("知乎·日报");
                 map=httpList.get(0);
                createPage(map.get("url"), map.get("strPattern"), map.get("UrlHead"));
                break;
            case 1:
                setBackgroundColor( R.color.guoke);
                setTitleText("果壳·科学人");
                 map=httpList.get(1);
                createPage(map.get("url"), map.get("strPattern"),map.get("UrlHead"));
                break;
            case 2:
                setBackgroundColor( R.color.yiyan);
                setTitleText("译言·精选");
                 map=httpList.get(2);
                createPage(map.get("url"), map.get("strPattern"),map.get("UrlHead"));
                break;
            case 3:
                setBackgroundColor( R.color.huxiu);
                setTitleText("虎嗅·资讯");
                 map=httpList.get(3);
                createPage(map.get("url"), map.get("strPattern"),map.get("UrlHead"));
                break;
            case 4:
                setBackgroundColor( R.color.shiwuyan);
                setTitleText("十五言·推荐");
                 map=httpList.get(4);
                createPage(map.get("url"), map.get("strPattern"),map.get("UrlHead"));
                break;
            case 5:
                setBackgroundColor( R.color.douban);
                setTitleText("豆瓣阅读·专栏");
                map=httpList.get(5);
                createPage(map.get("url"), map.get("strPattern"),map.get("UrlHead"));
                break;
            default:
                break;
        }
    }
    private void setBackgroundColor(int id){
        if(night) return;
        titleLayout.setBackgroundColor(getResources().getColor(id));
        mSwipeLayout.setColorSchemeResources(id);
    }
    private void setTitleText(String text){
        titleView.setText(text);
    }
    private  void createPage(String url,String strPattern,String urlHead){
        mSwipeLayout.setRefreshing(true);
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
        if(index==6) return;
        mSwipeLayout.setRefreshing(false);

        MessageAdapter adapter = new MessageAdapter(this, data,
                R.layout.message_item, new String[]{"title"},
                new int[]{R.id.title});
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View item, int position,
                                    long id) {
                Map<String, Object> map = data.get(position);
                String url = (String) map.get("url");
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                   ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, 1, Menu.NONE, "收藏");
        menu.add(0, 2, Menu.NONE, "取消收藏");
        if(index==6){
            menu.add(0, 3, Menu.NONE, "标签");
            menu.add(0, 4, Menu.NONE, "笔记");
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()){
            case 1:
                collect(menuInfo.position);
                break;
            case 2:
                discollect(menuInfo.position);
                break;
            case 3:
                setLabel(menuInfo.position);
                break;
            case 4:
                setNote(menuInfo.position);
                break;
            default:
                break;
        }
        return true;
    }
    private void setNote(int position){
        Map<String, Object> map = data.get(position);
        final String title = (String) map.get("title");
        intentNoteActivity(title);
    }
    private void intentNoteActivity(String title){
        Intent intent=new Intent(MainActivity.this,NoteActivity.class);
        intent.putExtra("title",title);
        startActivity(intent);
    }
    private void setLabel(int position){
        Map<String, Object> map = data.get(position);
        final String title = (String) map.get("title");

        final EditText label=new EditText(this);
        label.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});//字数不能超过10

        new AlertDialog.Builder(this).setTitle("设置标签").setIcon(android.R.drawable.ic_dialog_info)
                .setView(label)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String labelStr=label.getText().toString();

                        SQLiteDatabase db=dbHelper.getWritableDatabase();
                        ContentValues values=new ContentValues();
                        values.put("label", labelStr);
                        db.update("Collection", values, "title= ? ", new String[]{title});
                        setCollection();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                })
                .show();

    }
    private void collect(int position){
        Map<String, Object> map = data.get(position);
        String title = (String) map.get("title");
        String url = (String) map.get("url");
        if(hasCollected(title)){
            Toast.makeText(MainActivity.this, "该条目已收藏", Toast.LENGTH_SHORT).show();
        }else{
            putCollection(title, url);
            if(index==6) setCollection();
            Toast.makeText(MainActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
        }
    }
    private void discollect(int position){
        Map<String, Object> map = data.get(position);
        String title = (String) map.get("title");
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        int row=db.delete("Collection","title = ?",new String[]{title});
        if(row==0){
            Toast.makeText(MainActivity.this, "该条目未收藏", Toast.LENGTH_SHORT).show();
        }else{
            if(index==6) setCollection();
            Toast.makeText(MainActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
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
        }else close();
    }

    private void close(){
        if(System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }
    public void find(View view){
        Intent intent=new Intent(MainActivity.this,SearchActivity.class);
        intent.putStringArrayListExtra("titleList", titleList);
        startActivity(intent);
    }
    public void collection(View view){
        setCollection();
    }
    public void menu(View view){
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }
}
