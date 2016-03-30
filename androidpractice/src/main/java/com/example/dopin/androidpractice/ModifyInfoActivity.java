package com.example.dopin.androidpractice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class ModifyInfoActivity extends Activity {

    private MyDatabaseHelper dbHelper;
    private EditText newName;
    private EditText newLocation;
    private Spinner newSex;
    private Button modifyInfo;
    private Button back2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_modify_info);
        dbHelper=new MyDatabaseHelper(this,"User.db",null,1);

        init();
    }
    private void init(){
        newName=(EditText)findViewById(R.id.new_name);
        newSex=(Spinner)findViewById(R.id.new_sex);
        newLocation=(EditText)findViewById(R.id.new_location);
        modifyInfo=(Button)findViewById(R.id.confirm_modify_info);
        back2=(Button)findViewById(R.id.back2);
        modifyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptModifyInfo();
            }
        });
        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void attemptModifyInfo(){
        View focusView;
        String name=newName.getText().toString();
        String sex=newSex.getSelectedItem().toString();
        String location=newLocation.getText().toString();
        if (TextUtils.isEmpty(name)) {
            focusView = newName;
            focusView.requestFocus();
            newName.setError("姓名不能为空");
        }else if (TextUtils.isEmpty(location)) {
            focusView = newLocation;
            focusView.requestFocus();
            newLocation.setError("地区不能为空");
        }else{
            modifyInfo(MainActivity.account,name,sex,location);
        }
    }

    private void dialogShowSuccess() {
        new AlertDialog.Builder(this)
                .setTitle("消息")
                .setMessage("个人信息修改成功")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    private void modifyInfo(String account,String name,String sex,String location){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("name", name);
        values.put("sex", sex);
        values.put("location", location);
        db.update("User", values, "account=?", new String[]{account});

        dialogShowSuccess();
    }

}
