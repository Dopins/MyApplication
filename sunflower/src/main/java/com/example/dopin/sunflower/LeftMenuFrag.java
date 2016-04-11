package com.example.dopin.sunflower;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
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


/**
 * Created by dopin on 2016/3/22.
 */

public class LeftMenuFrag extends Fragment implements View.OnClickListener{

    public static String user_account;
    private String registerUrl = MainActivity.serverIP+"/SunflowerService/RegisterServlet";
    private String loginUrl = MainActivity.serverIP+"/SunflowerService/LoginServlet";
    ProgressDialog progressDialog;
    String account;
    String password;
    String password2;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private TextView ID;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.left_mune_frag, container, false);
        init(view);
        return view;
    }
    private void init(View view){
        account="";password="";
        pref= PreferenceManager.getDefaultSharedPreferences(view.getContext());
        editor=pref.edit();
        ID=(TextView)view.findViewById(R.id.ID);
        view.findViewById(R.id.user_image).setOnClickListener(this);
        view.findViewById(R.id.logout).setOnClickListener(this);

        setAccount();
    }
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.user_image:
                if(user_account.equals("")){
                    showLogin();
                }else{
                    intentPersonActivity();
                }
                break;
            case R.id.logout:
                showConfirmDialog();
                break;
            default:
                break;
        }
    }
    private void intentPersonActivity(){
        Intent intent=new Intent(view.getContext(),PersonActivity.class);
        startActivity(intent);
    }
    private void saveAccount(){
        editor.putString("user_account", user_account);
        editor.commit();
    }
    private void setAccount(){
        user_account=pref.getString("user_account","");
        if(!user_account.equals("")){
            ID.setText(user_account);
        }
    }
    private void showConfirmDialog(){
        new AlertDialog.Builder(view.getContext())
                .setTitle("退出登录")
                .setMessage("是否确认退出登录")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
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
    private void logout(){
        user_account="";
        editor.putString("user_account", user_account);
        editor.commit();
        ID.setText("未登录");
        MainActivity.mDrawerLayout.closeDrawer(Gravity.LEFT);
    }
    private void showLogin() {
        final View viewDia = LayoutInflater.from(view.getContext()).inflate(R.layout.activity_login, null);
        final AlertDialog.Builder customDia = new AlertDialog.Builder(view.getContext());

        final EditText accountText=(EditText)viewDia.findViewById(R.id.account);
        final EditText passwordText=(EditText)viewDia.findViewById(R.id.password);
        Button toRegister=(Button)viewDia.findViewById(R.id.btn_register);
        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegister();
            }
        });
        customDia.setTitle("登录");
        customDia.setView(viewDia);
        customDia.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                account = accountText.getText().toString();
                password = passwordText.getText().toString();

                attemptLogin();
            }
        });
        customDia.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        customDia.create().show();
    }
    private void showRegister(){
        final View viewDia = LayoutInflater.from(view.getContext()).inflate(R.layout.activity_register, null);
        final AlertDialog.Builder customDia = new AlertDialog.Builder(view.getContext());

        final EditText accountText=(EditText)viewDia.findViewById(R.id.account);
        final EditText passwordText=(EditText)viewDia.findViewById(R.id.password);
        final EditText password2Text=(EditText)viewDia.findViewById(R.id.confirm_password);

        customDia.setTitle("注册");
        customDia.setView(viewDia);
        customDia.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                account = accountText.getText().toString();
                password = passwordText.getText().toString();
                password2 = password2Text.getText().toString();

                attemptRegister();
            }
        });
        customDia.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        customDia.create().show();
    }
    private void attemptRegister(){
        showProgressDialog("请稍后", "注册中...");
        if(!password.equals(password2)) {
            progressDialog.dismiss();
            showRegisterWrongDialog("两次密码输入不一致");
            return;
        }else if(password.equals("")||password2.equals("")||account.equals("")){
            progressDialog.dismiss();
            showRegisterWrongDialog("账号和密码不能为空");
            return;
        }
        new Thread(registerTask).start();
    }
    private void attemptLogin(){
        showProgressDialog("请稍后", "登录中...");
        if(account.equals("")||password.equals("")) {
            progressDialog.dismiss();
            showLoginWrongDialog("账号和密码不能为空");
            return;
        }
        new Thread(loginTask).start();
    }
    private void showProgressDialog(String title,String message){
        progressDialog=new ProgressDialog(view.getContext());
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();
    }
    private void showLoginWrongDialog(String message){
            new AlertDialog.Builder(view.getContext())
                    .setTitle("错误")
                    .setMessage(message)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            showLogin();
                        }
                    })
                    .show();
    }
    private void showRegisterWrongDialog(String message){
        new AlertDialog.Builder(view.getContext())
                .setTitle("错误")
                .setMessage(message)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        showRegister();
                    }
                })
                .show();
    }

    Handler handlerLogin = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            JSONObject jo=(JSONObject)msg.obj;
            try{
                if(jo.getBoolean("result")){
                    progressDialog.dismiss();
                    user_account=jo.getString("account");
                    ID.setText(user_account);
                    Toast.makeText(view.getContext(),"登录成功",Toast.LENGTH_SHORT).show();
                    saveAccount();
                    MainActivity.mDrawerLayout.closeDrawer(Gravity.LEFT);
                }else{
                    progressDialog.dismiss();
                    showLoginWrongDialog("账号或密码不正确");
                }
            }catch (Exception e){

            }
        }
    };

       Handler handlerRegister = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            JSONObject jo=(JSONObject)msg.obj;
            try{
                if(jo.getBoolean("result")){
                    progressDialog.dismiss();
                    Toast.makeText(view.getContext(),"注册成功",Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.dismiss();
                    showLoginWrongDialog("该账号已被注册");
                }
            }catch (Exception e){

            }
        }
    };

    /**
     * 网络操作相关的子线程
     */
    Runnable loginTask = new Runnable() {

        @Override
        public void run() {

            NameValuePair pair1 = new BasicNameValuePair("account", account);
            NameValuePair pair2 = new BasicNameValuePair("password", password);

            List<NameValuePair> pairList = new ArrayList<NameValuePair>();
            pairList.add(pair1);
            pairList.add(pair2);
            try
            {
                HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList, HTTP.UTF_8);//设置编码
                HttpPost httpPost = new HttpPost(loginUrl);
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
                    handlerLogin.sendMessage(msg);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    };

    Runnable registerTask = new Runnable() {

        @Override
        public void run() {
            NameValuePair pair1 = new BasicNameValuePair("account", account);
            NameValuePair pair2 = new BasicNameValuePair("password", password);

            List<NameValuePair> pairList = new ArrayList<NameValuePair>();
            pairList.add(pair1);
            pairList.add(pair2);
            try
            {
                HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList, HTTP.UTF_8);//设置编码
                HttpPost httpPost = new HttpPost(registerUrl);
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
                    handlerRegister.sendMessage(msg);
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

