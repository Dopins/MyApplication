package com.example.dopin.androidpractice;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

public class LoginActivity extends Activity {
    private EditText mEmailView;
    private EditText mPasswordView;
    private MyDatabaseHelper dbHelper;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox rememberMe;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        mEmailView = (EditText) findViewById(R.id.account);
        mPasswordView = (EditText) findViewById(R.id.password);
        rememberMe=(CheckBox)findViewById(R.id.rememberMe);
        //设置账号密码
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember=pref.getBoolean("isRemember",false);
        if(isRemember){
            String account =pref.getString("account","");
            String password=pref.getString("password","");
            mEmailView.setText(account);
            mPasswordView.setText(password);
            rememberMe.setChecked(true);
        }

        dbHelper=new MyDatabaseHelper(this,"User.db",null,1);

        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button signUp = (Button) findViewById(R.id.signUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
               IntentRegister();
            }
        });

        //忘记密码
        Button forgetPassword = (Button) findViewById(R.id.forgetPassword);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialogShowTest();
            }
        });

        //第三方登录
        ImageButton wechatLogin = (ImageButton) findViewById(R.id.weChatLogin);
        wechatLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialogShowTest();
            }
        });
        ImageButton sinaLogin = (ImageButton) findViewById(R.id.sinaLogin);
        sinaLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialogShowTest();
            }
        });
        ImageButton qqLogin = (ImageButton) findViewById(R.id.qqLogin);
        qqLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialogShowTest();
            }
        });
    }
    private void attemptLogin() {
        View focusView;
        mEmailView.setError(null);
        mPasswordView.setError(null);
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        if (TextUtils.isEmpty(email)) {
            focusView = mEmailView;
            focusView.requestFocus();
            mEmailView.setError("账号不能为空");
        }
        else if (TextUtils.isEmpty(password)) {
            focusView = mPasswordView;
            focusView.requestFocus();
            mPasswordView.setError("密码不能为空");
        }
        else if (!isTrue(email,password)) {
            dialogShowWrong();
        }else{
            editor=pref.edit();
            if(rememberMe.isChecked()){
                editor.putBoolean("isRemember",true);
                editor.putString("account",email);
                editor.putString("password",password);
            }else{
                editor.clear();
            }
            editor.commit();
           IntentIndex();
        }
    }

    private void IntentRegister(){
        Intent intent=new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }

    private void dialogShowWrong() {
        new AlertDialog.Builder(this)
                .setTitle("错误")
                .setMessage("账号或密码不正确！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        clearPassword();
                    }
                })
                .show();
    }
    private void dialogShowTest(){
        new AlertDialog.Builder(this)
                .setTitle("测试")
                .setMessage("")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //按钮事件
                    }
                })
                .show();
    }

    private boolean isTrue(String account,String password) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.query("User",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                String accountGet=cursor.getString(cursor.getColumnIndex("account"));
                String passwordGet=cursor.getString(cursor.getColumnIndex("password"));
                if(account.equals(accountGet)&&password.equals(passwordGet)){
                    return true;
                }
            }while(cursor.moveToNext());
        }
        return false;
    }
    private void IntentIndex(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void clearPassword(){
        mEmailView.setText("");
        mPasswordView.setText("");
    }
}
