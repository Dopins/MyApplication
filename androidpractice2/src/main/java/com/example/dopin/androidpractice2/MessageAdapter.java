package com.example.dopin.androidpractice2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


/**
 * Created by dopin on 2016/3/22.
 */
public class MessageAdapter extends ArrayAdapter<Message> {
    private int resourceId;
    public MessageAdapter(Context context,int textViewResourceId, List<Message> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }
    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        Message message=getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView==null){
            view= LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder=new ViewHolder();
            viewHolder.title=(TextView)view.findViewById(R.id.title);
            viewHolder.content=(TextView)view.findViewById(R.id.content);
            view.setTag(viewHolder);
        }else{
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.title.setText(message.getTitle());
        viewHolder.content.setText(message.getContent());
        return view;

    }
    class ViewHolder{
        TextView title;
        TextView content;
    }
}
