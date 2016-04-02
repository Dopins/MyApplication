package com.example.dopin.androidpractice2;

import android.content.Context;
import android.util.Log;
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
        View view= LayoutInflater.from(getContext()).inflate(resourceId, null);
        Item item=getItem(position);

        ImageView itemImage=(ImageView)view.findViewById(R.id.item_image);
        TextView itemName=(TextView)view.findViewById(R.id.item_name);
        if(MainActivity.night){
            itemName.setTextColor(view.getResources().getColor(R.color.night_item_font));
        }else{
            itemName.setTextColor(view.getResources().getColor(R.color.menu_item));
        }
        itemImage.setImageResource(item.getImageId());
        itemName.setText(item.getName());

        return view;
    }

}
