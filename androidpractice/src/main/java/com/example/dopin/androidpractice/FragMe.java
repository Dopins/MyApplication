package com.example.dopin.androidpractice;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by dopin on 2016/3/13.
 */
public class FragMe extends Fragment implements View.OnClickListener{
    private MyDatabaseHelper dbHelper;
    private Button btn_modify_password;
    private Button btn_modify_info;
    private Button btn_logout;
    private Button btn_close;
    private Button btn_about;
    private TextView name;
    private TextView sex;
    private TextView location;
    private ImageView head_sculpture;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me,container,false);
        dbHelper=new MyDatabaseHelper(getContext(),"User.db",null,1);
        init(view);
        return view;
    }
    public void init(View view){
        btn_modify_password=(Button)view.findViewById(R.id.btn_modify_password);
        btn_modify_info=(Button)view.findViewById(R.id.btn_modify_info);
        btn_logout=(Button)view.findViewById(R.id.btn_logout);
        btn_close=(Button)view.findViewById(R.id.btn_close);
        btn_about=(Button)view.findViewById(R.id.btn_about);

        btn_modify_password.setOnClickListener(this);
        btn_modify_info.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
        btn_close.setOnClickListener(this);
        btn_about.setOnClickListener(this);

        name=(TextView)view.findViewById(R.id.name);
        sex=(TextView)view.findViewById(R.id.sex);
        location=(TextView)view.findViewById(R.id.location);

        head_sculpture=(ImageView)view.findViewById(R.id.head_sculpture);
        head_sculpture.setOnClickListener(this);

    }


    private void setInfo(String account){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.query("User",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                String accountGet=cursor.getString(cursor.getColumnIndex("account"));
                if(account.equals(accountGet)){
                    String nameStr=cursor.getString(cursor.getColumnIndex("name"));
                    String sexStr=cursor.getString(cursor.getColumnIndex("sex"));
                    String locationStr=cursor.getString(cursor.getColumnIndex("location"));
                    name.setText(nameStr);
                    sex.setText(sexStr);
                    location.setText(locationStr);
                    return;
                }
            }while(cursor.moveToNext());
        }
    }
    @Override
    public void onStart(){
        super.onStart();
        setInfo(MainActivity.account);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_modify_password:
                intentModifyPassword();
                break;
            case R.id.btn_modify_info:
                intentModifyInfo();
                break;
            case R.id.btn_logout:
                dialogShowConfirmLogout();
                break;
            case R.id.btn_close:
                dialogShowConfirmClose();
                break;
            case R.id.btn_about:

                break;
            case R.id.head_sculpture:

                break;
            default:
                break;
        }
    }

    private void dialogShowConfirmClose() {
        new AlertDialog.Builder(getActivity())
                .setTitle("消息")
                .setMessage("确定要退出应用？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    private void dialogShowConfirmLogout() {
        new AlertDialog.Builder(getActivity())
                .setTitle("消息")
                .setMessage("确定要返回到登录界面？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        intentLogin();
                        getActivity().finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    private void intentModifyPassword(){
        Intent intent=new Intent(getActivity(),ModifyPasswordActivity.class);
        startActivity(intent);
    }
    private void intentModifyInfo(){
        Intent intent=new Intent(getActivity(),ModifyInfoActivity.class);
        startActivity(intent);
    }
    private void intentLogin(){
        Intent intent=new Intent(getActivity(),LoginActivity.class);
        startActivity(intent);
    }
}
