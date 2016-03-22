package com.example.dopin.androidpractice2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


/**
 * Created by dopin on 2016/3/22.
 */
public class MenuAdapter extends ArrayAdapter<Item> {
    private int resourceId;
    public MenuAdapter(Context context,int textViewResourceId, List<Item> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }
    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        Item item=getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView==null){
            view= LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder=new ViewHolder();
            viewHolder.itemImage=(ImageView)view.findViewById(R.id.item_image);
            viewHolder.itemName=(TextView)view.findViewById(R.id.item_name);
        }

    }
    class ViewHolder{
        ImageView itemImage;
        TextView itemName;
    }
}
