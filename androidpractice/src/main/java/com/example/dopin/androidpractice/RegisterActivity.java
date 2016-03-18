package com.example.dopin.androidpractice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends Activity {

    private Button register;
    private Button back_to_login;
    private EditText accountText;
    private EditText passwordText;
    private  EditText comfirmPasswordText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        register = (Button) findViewById(R.id.register);
        back_to_login = (Button) findViewById(R.id.back);
        accountText = (EditText) findViewById(R.id.account);
        passwordText = (EditText) findViewById(R.id.password);
        comfirmPasswordText = (EditText) findViewById(R.id.confirm_password);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });
        back_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
        private void attemptRegister(){
        View focusView;
        String account=accountText.toString();
        String password=passwordText.toString();
        String comfirmPassword=comfirmPasswordText.toString();
        if(TextUtils.isEmpty(account)){
            focusView = accountText;
            focusView.requestFocus();
            accountText.setError("账号不能为空");
        }
        else if(TextUtils.isEmpty(password)){
            focusView = passwordText;
            focusView.requestFocus();
            passwordText.setError("密码不能为空");
        }
        else if(TextUtils.isEmpty(comfirmPassword)){
            focusView = comfirmPasswordText;
            focusView.requestFocus();
            comfirmPasswordText.setError("确认密码账号不能为空");
        }else if(!password.equals(comfirmPassword)){
            showWrongDialog();
        }else{
            register(account,password);
        }
      }
        private void showWrongDialog(){
            new AlertDialog.Builder(this)
                    .setTitle("错误")
                    .setMessage("两次输入密码不一致")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            clearPassword();
                        }
                    })
                    .show();
        }
    private void clearPassword(){
        passwordText.setText("");
        comfirmPasswordText.setText("");
    }
    private void register(String account,String password){

    }
}
