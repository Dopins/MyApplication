package com.example.dopin.androidpractice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class ModifyPasswordActivity extends Activity {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private MyDatabaseHelper dbHelper;
    private EditText oldPassword;
    private EditText newPassword;
    private EditText newConfirmPassword;
    private Button confirmModify;
    private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_modify_password);
        dbHelper=new MyDatabaseHelper(this,"User.db",null,1);
        pref= PreferenceManager.getDefaultSharedPreferences(this);

        init();
    }
    private void init(){
        oldPassword=(EditText)findViewById(R.id.old_password);
        newPassword=(EditText)findViewById(R.id.new_password);
        newConfirmPassword=(EditText)findViewById(R.id.new_password_confirm);
        confirmModify=(Button)findViewById(R.id.confirm_modify_password);
        back=(Button)findViewById(R.id.back);
        confirmModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptModifyPass();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void attemptModifyPass(){
        View focusView;
        String oldpass=oldPassword.getText().toString();
        String newpass=newPassword.getText().toString();
        String newpassCon=newConfirmPassword.getText().toString();
        if (TextUtils.isEmpty(oldpass)) {
            focusView = oldPassword;
            focusView.requestFocus();
            oldPassword.setError("旧密码不能为空");
        }else if (TextUtils.isEmpty(newpass)) {
            focusView = newPassword;
            focusView.requestFocus();
            newPassword.setError("新密码不能为空");
        }else if (TextUtils.isEmpty(newpassCon)) {
            focusView = newConfirmPassword;
            focusView.requestFocus();
            newConfirmPassword.setError("确认新密码不能为空");
        }else if(!newpass.equals(newpassCon)){
            dialogShowWrong();
        }else if(isOldPassTrue(MainActivity.account,oldpass)){
            modifyPass(MainActivity.account,newpass);
        }else{
            dialogShowWrong2();
        }
    }
    private void dialogShowWrong() {
        new AlertDialog.Builder(this)
                .setTitle("错误")
                .setMessage("两次输入新密码不一致")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        newPassword.setText("");
                        newConfirmPassword.setText("");
                    }
                })
                .show();
    }
    private void dialogShowWrong2() {
        new AlertDialog.Builder(this)
                .setTitle("错误")
                .setMessage("输入旧密码不正确")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        newPassword.setText("");
                        newConfirmPassword.setText("");
                    }
                })
                .show();
    }

    private void dialogShowSuccess() {
        new AlertDialog.Builder(this)
                .setTitle("消息")
                .setMessage("密码修改成功")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    private void modifyPass(String account,String newPass){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("password", newPass);
        db.update("User", values, "account=?", new String[]{account});

        editor=pref.edit();
        editor.putBoolean("isRemember", true);
        editor.putString("account",account);
        editor.putString("password", newPass);
        editor.commit();

        dialogShowSuccess();
    }

    private boolean isOldPassTrue(String account,String oldpass){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.query("User",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                String accountGet=cursor.getString(cursor.getColumnIndex("account"));
                String passwordGet=cursor.getString(cursor.getColumnIndex("password"));
                if(account.equals(accountGet)&&oldpass.equals(passwordGet)){
                    return true;
                }
            }while(cursor.moveToNext());
        }
        return false;
    }
}
