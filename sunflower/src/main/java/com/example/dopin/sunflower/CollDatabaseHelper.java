package com.example.dopin.sunflower;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dopin on 2016/3/18.
 */
public class CollDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREAK_USER="create table Collection("
            + "id integer primary key autoincrement,"
            + "title text,"
            + "url text,"
            + "label text,"
            + "note text)";
    private Context mContext;
    public CollDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
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
