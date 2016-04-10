package com.example.dopin.sunflower;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.dopin.androidpractice2.R;

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

import java.util.ArrayList;
import java.util.List;

public class NoteActivity extends Activity {

    private String getNoteUrl = "http://125.216.249.194:8888//SunflowerService/GetNoteServlet";
    private String setNoteUrl = "http://125.216.249.194:8888//SunflowerService/SetNoteServlet";
    private FrameLayout back;
    private FrameLayout save;
    private String title;
    private EditText noteText;
    private String note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(MainActivity.night){
            setContentView(R.layout.activity_note_night);
        }else {
            setContentView(R.layout.activity_note);
        }
        init();
    }
    private void init(){
        back=(FrameLayout)findViewById(R.id.back);
        save=(FrameLayout)findViewById(R.id.save);
        noteText=(EditText)findViewById(R.id.note);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");

        showNote();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });
    }
    private void saveNote(){
        note=noteText.getText().toString();
        new Thread(setNoteTask).start();
        Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
    }
    private void showNote(){
        new Thread(getNoteTask).start();
    }

    Handler handlerGetNote = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            JSONObject jo=(JSONObject)msg.obj;
            try{
                note=jo.getString("note");
                noteText.setText(note);
            }catch (Exception e){
            }
        }
    };

    Runnable setNoteTask = new Runnable() {

        @Override
        public void run() {
            NameValuePair pair1 = new BasicNameValuePair("account", LeftMenuFrag.user_account);
            NameValuePair pair2 = new BasicNameValuePair("title", title);
            NameValuePair pair3 = new BasicNameValuePair("note", note);

            List<NameValuePair> pairList = new ArrayList<NameValuePair>();
            pairList.add(pair1);
            pairList.add(pair2);
            pairList.add(pair3);
            try {
                HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList, HTTP.UTF_8);//设置编码
                HttpPost httpPost = new HttpPost(setNoteUrl);
                httpPost.setEntity(requestHttpEntity);
                HttpClient httpClient = new DefaultHttpClient();
                httpClient.execute(httpPost);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    };
    Runnable getNoteTask = new Runnable() {

        @Override
        public void run() {
            NameValuePair pair1 = new BasicNameValuePair("account", LeftMenuFrag.user_account);
            NameValuePair pair2 = new BasicNameValuePair("title", title);

            List<NameValuePair> pairList = new ArrayList<NameValuePair>();
            pairList.add(pair1);
            pairList.add(pair2);
            try {
                HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList, HTTP.UTF_8);//设置编码
                HttpPost httpPost = new HttpPost(getNoteUrl);
                httpPost.setEntity(requestHttpEntity);
                HttpClient httpClient = new DefaultHttpClient();
                HttpResponse httpResponse = httpClient.execute(httpPost);

                if (httpResponse.getStatusLine().getStatusCode()==200)
                {
                    HttpEntity httpEntity = httpResponse.getEntity();
                    String response= EntityUtils.toString(httpEntity, "utf-8");

                    JSONObject jsonObject=parseJSON(response);
                    Message msg = new Message();
                    msg.obj=jsonObject;
                    handlerGetNote.sendMessage(msg);
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
