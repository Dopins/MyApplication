package com.example.dopin.androidpractice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import android.widget.Spinner;

public class RegisterActivity extends Activity {

    private MyDatabaseHelper dbHelper;
    private Button register;
    private Button back_to_login;
    private EditText accountText;
    private EditText passwordText;
    private EditText confirmPasswordText;
    private EditText name;
    private Spinner sex;
    private EditText location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        createDatabase();
        setContentView(R.layout.activity_register);
        dbHelper=new MyDatabaseHelper(this,"User.db",null,1);

        register = (Button) findViewById(R.id.register);
        back_to_login = (Button) findViewById(R.id.back);
        accountText = (EditText) findViewById(R.id.account);
        passwordText = (EditText) findViewById(R.id.password);
        confirmPasswordText = (EditText) findViewById(R.id.confirm_password);
        name=(EditText)findViewById(R.id.name);
        sex=(Spinner)findViewById(R.id.sex);
        location=(EditText)findViewById(R.id.location);

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
        String confirmPassword=confirmPasswordText.getText().toString();
            String nameStr=name.getText().toString();
            String sexStr=sex.getSelectedItem().toString();
            String locationStr=location.getText().toString();

        if(TextUtils.isEmpty(account)){
            focusView = accountText;
            focusView.requestFocus();
            accountText.setError("账号不能为空");
        }else if(TextUtils.isEmpty(password)){
            focusView = passwordText;
            focusView.requestFocus();
            passwordText.setError("密码不能为空");
        }else if(TextUtils.isEmpty(confirmPassword)){
            focusView = confirmPasswordText;
            focusView.requestFocus();
            confirmPasswordText.setError("确认密码账号不能为空");
        }else if(TextUtils.isEmpty(nameStr)){
            focusView = confirmPasswordText;
            focusView.requestFocus();
            confirmPasswordText.setError("姓名账号不能为空");
        }else if(TextUtils.isEmpty(locationStr)){
            focusView = confirmPasswordText;
            focusView.requestFocus();
            confirmPasswordText.setError("地区账号不能为空");
        }else if(isHaveAc(account)){
            showWrongDialog2();
            accountText.setText("");
            passwordText.setText("");
        }else if(password.equals(confirmPassword)){
            register(account,password,nameStr,sexStr,locationStr);

        }else{
            showWrongDialog();
        }
      }

    private boolean isHaveAc(String account){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.query("User",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                String accountGet=cursor.getString(cursor.getColumnIndex("account"));
                if(account.equals(accountGet)){
                    return true;
                }
            }while(cursor.moveToNext());
        }
        return false;
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
    private void showWrongDialog2(){
        new AlertDialog.Builder(this)
                .setTitle("错误")
                .setMessage("该账号已存在")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        clearPassword();
                    }
                })
                .show();
    }
    private void clearPassword(){
        passwordText.setText("");
        confirmPasswordText.setText("");
    }
    private void register(String account,String password,String name,String sex,String location){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("account",account);
        values.put("password",password);
        values.put("name",name);
        values.put("sex",sex);
        values.put("location",location);
        db.insert("User",null,values);
        values.clear();
        finish();
    }
    private void createDatabase(){
        dbHelper=new MyDatabaseHelper(this,"User.db",null,1);
        dbHelper.getWritableDatabase();
    }

}
