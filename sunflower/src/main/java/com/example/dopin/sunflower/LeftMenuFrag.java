package com.example.dopin.sunflower;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by dopin on 2016/3/22.
 */

public class LeftMenuFrag extends Fragment implements View.OnClickListener{

    public static String user_account;
    private String registerUrl = MainActivity.serverIP+"/SunflowerService/RegisterServlet";
    private String loginUrl = MainActivity.serverIP+"/SunflowerService/LoginServlet";
    private String nickNameUrl=MainActivity.serverIP+"/SunflowerService/NicknameServlet";
    private String passwordUrl=MainActivity.serverIP+"/SunflowerService/PasswordServlet";
    private String imageUrl=MainActivity.serverIP+"/SunflowerService/ImageServlet";
    public static final int CHOOSE_PHOTO=1;
    ProgressDialog progressDialog;
    String nickname;
    String account;
    String oldPassword;
    String password;
    String password2;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private TextView ID;
    private ImageView user_image;
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
        user_image=(ImageView)view.findViewById(R.id.user_image);
        user_image.setOnClickListener(this);
        view.findViewById(R.id.logout).setOnClickListener(this);

        setAccount();
        if(!user_account.equals("")) displayImage();
    }
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.user_image:
                if(user_account.equals("")){
                    showLogin();
                }else{
                   showChoose();
                }
                break;
            case R.id.logout:
                if(!user_account.equals("")){
                showConfirmDialog();
                }
                break;
            default:
                break;
        }
    }
    private void showChoose(){
         View chooseView = LayoutInflater.from(view.getContext()).inflate(R.layout.activity_choose, null);
         AlertDialog.Builder customDia = new AlertDialog.Builder(view.getContext());
        customDia.setTitle("选择");
        customDia.setView(chooseView);
        final AlertDialog chooseDia=customDia.create();

        Button changeNickname=(Button)chooseView.findViewById(R.id.btn_nick_name);
        Button changeHeadImage=(Button)chooseView.findViewById(R.id.btn_head_image);
        Button changePassword=(Button)chooseView.findViewById(R.id.btn_password);

        changeNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeNicknameDia();
                chooseDia.dismiss();
            }
        });
        changeHeadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
                chooseDia.dismiss();
            }
        });
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangePasswordDia();
                chooseDia.dismiss();
            }
        });
        chooseDia.show();
    }
    private void choosePhoto(){
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        //根据版本号不同使用不同的Action
        if (Build.VERSION.SDK_INT <19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        }else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        }
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CHOOSE_PHOTO) {
            if (null!=data) {
                Uri uri = data.getData();
                //根据需要，也可以加上Option这个参数
                try {
                    InputStream inputStream = view.getContext().getContentResolver().openInputStream(uri);
                    Bitmap bitmap = toRoundBitmap(compressImage(BitmapFactory.decodeStream(inputStream)));
                    displayImage();
                }catch (Exception e){

                }
            }
        }
    }
    private void uploadImage(Bitmap bitmap){

    }

    private void displayImage(){
        File file=new File("/sdcard/image.png");
        if(!file.exists()) {
            return;
        }
        try {
            FileInputStream fis = new FileInputStream(file);
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            user_image.setImageBitmap(bitmap);
        }catch (Exception e){
            Toast.makeText(view.getContext(),"显示头像失败",Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>65) {	//循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public Bitmap toRoundBitmap(Bitmap bitmap) throws IOException{
        //圆形图片宽高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        //正方形的边长
        int r = 0;
        //取最短边做边长
        if(width > height) {
            r = height;
        } else {
            r = width;
        }
        //构建一个bitmap
        Bitmap backgroundBmp = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        //new一个Canvas，在backgroundBmp上画图
        Canvas canvas = new Canvas(backgroundBmp);
        Paint paint = new Paint();
        //设置边缘光滑，去掉锯齿
        paint.setAntiAlias(true);
        //宽高相等，即正方形
        RectF rect = new RectF(0, 0, r, r);
        //通过制定的rect画一个圆角矩形，当圆角X轴方向的半径等于Y轴方向的半径时，
        //且都等于r/2时，画出来的圆角矩形就是圆形
        canvas.drawRoundRect(rect, r/2, r/2, paint);
        //设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //canvas将bitmap画在backgroundBmp上
        canvas.drawBitmap(bitmap, null, rect, paint);
        //返回已经绘画好的backgroundBmp
        File photo=new File("/sdcard/image.png");
        if(photo.exists()) photo.delete();
        OutputStream stream = new FileOutputStream(photo);
        backgroundBmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        stream.close();
        return backgroundBmp;
    }

    private void showChangeNicknameDia(){
        final EditText nicknameText=new EditText(view.getContext());
        nicknameText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});//字数不能超过10

        new AlertDialog.Builder(view.getContext())
                .setTitle("修改昵称")
                .setIcon(android.R.drawable.ic_menu_edit)
                .setView(nicknameText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nickname=nicknameText.getText().toString();
                        new Thread(nicknameTask).start();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
    private void showChangePasswordDia(){
        View passwordView = LayoutInflater.from(view.getContext()).inflate(R.layout.activity_password, null);
        final EditText oldPassText=(EditText)passwordView.findViewById(R.id.old_password);
        final EditText newPassText=(EditText)passwordView.findViewById(R.id.new_password);
        final EditText newPassText2=(EditText)passwordView.findViewById(R.id.new_password_confirm);
        new AlertDialog.Builder(view.getContext())
                .setTitle("修改密码")
                .setIcon(android.R.drawable.ic_menu_edit)
                .setView(passwordView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        oldPassword = oldPassText.getText().toString();
                        password = newPassText.getText().toString();
                        password2 = newPassText2.getText().toString();
                        if (password.equals(password2)) {
                            new Thread(passwordTask).start();
                        }else{
                            showChangePasswordWrongDia("新密码输入不一致");
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
    private void showChangePasswordWrongDia(String message){
        new AlertDialog.Builder(view.getContext())
                .setTitle("错误")
                .setMessage(message)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        showChangePasswordDia();
                    }
                })
                .show();
    }
    private void saveAccount(){
        editor.putString("user_account", user_account);
        editor.putString("nickname",nickname);
        editor.commit();
    }
    private void setAccount(){
        user_account=pref.getString("user_account","");
        if(!user_account.equals("")){
            nickname=pref.getString("nickname",user_account);
            ID.setText(nickname);
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
        user_image.setImageResource(R.drawable.user);
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
                    nickname=jo.getString("nickname");
                    ID.setText(nickname);
                    Toast.makeText(view.getContext(),"登录成功",Toast.LENGTH_SHORT).show();
                    saveAccount();
                    displayImage();
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

    Handler handlerNickname = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            JSONObject jo=(JSONObject)msg.obj;
            try{
                if(jo.getBoolean("result")){
                    ID.setText(nickname);
                    editor.putString("nickname",nickname);
                    editor.commit();
                    Toast.makeText(view.getContext(),"修改成功",Toast.LENGTH_SHORT).show();
                    MainActivity.mDrawerLayout.closeDrawer(Gravity.LEFT);
                }
            }catch (Exception e){

            }
        }
    };

    Handler handlerPassword = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            JSONObject jo=(JSONObject)msg.obj;
            try{
                if(jo.getBoolean("result")){
                    Toast.makeText(view.getContext(),"修改成功",Toast.LENGTH_SHORT).show();
                    MainActivity.mDrawerLayout.closeDrawer(Gravity.LEFT);
                }else{
                    showChangePasswordWrongDia("输入旧密码不正确");
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

    Runnable nicknameTask = new Runnable() {

        @Override
        public void run() {
            NameValuePair pair1 = new BasicNameValuePair("account", user_account);
            NameValuePair pair2 = new BasicNameValuePair("nickname", nickname);

            List<NameValuePair> pairList = new ArrayList<NameValuePair>();
            pairList.add(pair1);
            pairList.add(pair2);
            try
            {
                HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList, HTTP.UTF_8);//设置编码
                HttpPost httpPost = new HttpPost(nickNameUrl);
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
                    handlerNickname.sendMessage(msg);
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    };

    Runnable passwordTask = new Runnable() {

        @Override
        public void run() {
            NameValuePair pair1 = new BasicNameValuePair("account", user_account);
            NameValuePair pair2 = new BasicNameValuePair("old_password", oldPassword);
            NameValuePair pair3 = new BasicNameValuePair("new_password", password);

            List<NameValuePair> pairList = new ArrayList<NameValuePair>();
            pairList.add(pair1);
            pairList.add(pair2);
            pairList.add(pair3);
            try
            {
                HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList, HTTP.UTF_8);//设置编码
                HttpPost httpPost = new HttpPost(passwordUrl);
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
                    handlerPassword.sendMessage(msg);
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

