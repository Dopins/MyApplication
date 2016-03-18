package com.example.dopin.androidpractice;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class LoginActivity extends Activity {
    private EditText mEmailView;
    private EditText mPasswordView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mEmailView = (EditText) findViewById(R.id.account);
        mPasswordView = (EditText) findViewById(R.id.password);

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
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
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
                        //按钮事件
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
        return account.equals("admin")&&password.equals("123456");
    }
}
