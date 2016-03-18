package com.example.dopin.androidpractice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dopin on 2016/3/18.
 */
public class SessionAdapter extends ArrayAdapter<String> {
    private int resourceId;
    public SessionAdapter(Context context,int textViewResourceId,List<String> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }
    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        String string =getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView==null){
            view= LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder=new ViewHolder();
            viewHolder.session=(TextView)view.findViewById(R.id.session);
            view.setTag(viewHolder);
        }else{
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.session.setText(string);
        return view;
    }
    class ViewHolder{
        TextView session;
    }
}
