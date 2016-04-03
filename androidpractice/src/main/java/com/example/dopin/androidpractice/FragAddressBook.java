package com.example.dopin.androidpractice;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dopin on 2016/3/13.
 */
public class FragAddressBook extends Fragment{
    private ListView contactView;
    private List<Map<String,String>> data;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address_book,container,false);
        init(view);
        return view;
    }
    private void init(View view){
        data=getContacts();
        contactView=(ListView) view.findViewById(R.id.contact_view);
        SimpleAdapter adapter=new SimpleAdapter(getContext(),data,R.layout.contact_item,
                new String[]{"name","phone_number"},new int[]{R.id.name,R.id.phone_number});
        contactView.setAdapter(adapter);
    }
    private List<Map<String,String>>  getContacts(){
        Cursor cursor=null;
        List<Map<String,String>> result=new ArrayList<>();
        try{
            cursor=getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,null,null,null);
            while(cursor.moveToNext()) {
                String displayName = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                Map<String, String> map = new HashMap();
                map.put("name", displayName);
                map.put("phone_number", number);
                result.add(map);
            }
        }catch (Exception e){
                e.printStackTrace();
        }finally {
            if(cursor!=null) cursor.close();
        }
        return result;
    }
}
