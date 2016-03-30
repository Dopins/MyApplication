package com.example.dopin.androidpractice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dopin on 2016/3/18.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREAK_USER="create table User("
            + "id integer primary key autoincrement,"
            + "account text,"
            + "password text,"
            + "name,"
            + "sex,"
            + "location)";
    private Context mContext;
    public MyDatabaseHelper(Context context,String name,SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        mContext=context;
    }
    @Override
    public void onCreate(SQLiteDatabase ad){
        ad.execSQL(CREAK_USER);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }
}
