package com.example.dopin.sunflower;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.dopin.androidpractice2.R;

public class NoteActivity extends Activity {

    private CollDatabaseHelper dbHelper;
    private FrameLayout back;
    private FrameLayout save;
    private EditText note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(MainActivity.night){
            setContentView(R.layout.activity_note_night);
        }else {
            setContentView(R.layout.activity_note);
        }
        init();
    }
    private void init(){
        back=(FrameLayout)findViewById(R.id.back);
        save=(FrameLayout)findViewById(R.id.save);
        note=(EditText)findViewById(R.id.note);
        dbHelper=new CollDatabaseHelper(this,"Collection.db",null,1);
        Intent intent = getIntent();
        final String title = intent.getStringExtra("title");

        showNote(title);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote(title);
            }
        });
    }
    private void saveNote(String title){
        String noteStr=note.getText().toString();
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("note", noteStr);
        db.update("Collection", values, "title= ? ", new String[]{title} );
        Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
    }
    private void showNote(String title){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.query("Collection",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                String titleGet=cursor.getString(cursor.getColumnIndex("title"));
                if(title.equals(titleGet)) {
                    String noteStr=cursor.getString(cursor.getColumnIndex("note"));
                    note.setText(noteStr);
                }
            }while(cursor.moveToNext());
        }

    }
}
