package com.example.dopin.androidpractice;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
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
    public static final int CHOOSE_PHOTO=1;

    private String head_sculpture_path;
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
    private void saveImagePath(String imagePath){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("image_path",imagePath);
        db.update("User", values, "account=?",new String[]{MainActivity.account} );
        values.clear();
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
                    String imagePath=cursor.getString(cursor.getColumnIndex("image_path"));
                    name.setText(nameStr);
                    sex.setText(sexStr);
                    location.setText(locationStr);
                    head_sculpture_path=imagePath;
                    return;
                }
            }while(cursor.moveToNext());
        }
    }
    @Override
    public void onStart(){
        super.onStart();
        setInfo(MainActivity.account);
        displayImage(head_sculpture_path);
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
                intentAbout();
                break;
            case R.id.head_sculpture:
                choosePhoto();
                break;
            default:
                break;
        }
    }
    private void choosePhoto(){
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        switch (requestCode){
            case CHOOSE_PHOTO:
                if(resultCode==getActivity().RESULT_OK){
                    if(Build.VERSION.SDK_INT>=19){
                        handleImageOnKitKat(data);
                    }else{
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagePath=null;
        Uri uri=data.getData();
        if(DocumentsContract.isDocumentUri(getActivity(),uri)){
            String docId=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id=docId.split(":")[1];
                String selection=MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath=getImagePath(uri,null);
        }
        saveImagePath(imagePath);
    }
    private  void handleImageBeforeKitKat(Intent data){
        Uri uri=data.getData();
        String imagePath=getImagePath(uri, null);
        saveImagePath(imagePath);
    }
    private String getImagePath(Uri uri,String selection){
        String path=null;
        Cursor cursor=getActivity().getContentResolver().query(uri, null, selection, null, null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    private void displayImage(String imagePath){
        if(imagePath!=null){
            Bitmap bitmap= BitmapFactory.decodeFile(imagePath);
            head_sculpture.setImageBitmap(bitmap);
        }
    }

    private void intentAbout(){
        Intent intent=new Intent(getActivity(),AboutActivity.class);
        startActivity(intent);
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
