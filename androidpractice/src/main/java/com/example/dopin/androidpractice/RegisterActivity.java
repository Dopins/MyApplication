package com.example.dopin.androidpractice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends Activity {

    private MyDatabaseHelper dbHelper;
    private Button register;
    private Button back_to_login;
    private EditText accountText;
    private EditText passwordText;
    private  EditText comfirmPasswordText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        createDatabase();
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
        String account=accountText.getText().toString();
        String password=passwordText.getText().toString();
        String confirmPassword=comfirmPasswordText.getText().toString();
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
        else if(TextUtils.isEmpty(confirmPassword)){
            focusView = comfirmPasswordText;
            focusView.requestFocus();
            comfirmPasswordText.setError("确认密码账号不能为空");
        }else if(password.equals(confirmPassword)){
            register(account,password);

        }else{
            showWrongDialog();

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
        dbHelper=new MyDatabaseHelper(this,"User.db",null,1);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("account",account);
        values.put("password",password);
        db.insert("User",null,values);
        values.clear();
        finish();
    }
    private void createDatabase(){
        dbHelper=new MyDatabaseHelper(this,"User.db",null,1);
        dbHelper.getWritableDatabase();
    }

}
