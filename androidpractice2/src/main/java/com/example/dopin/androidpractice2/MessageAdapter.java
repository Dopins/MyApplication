package com.example.dopin.androidpractice2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.util.List;
import java.util.Map;

/**
 * Created by dopin on 2016/4/1.
 */
public class MessageAdapter extends SimpleAdapter {
    private LayoutInflater mInflater;
    private int mResource;
    private List<Map<String, Object>> data;
    public MessageAdapter(Context context, List<Map<String, Object>>items,int resource,String[] from,int[] to) {
        super(context, items, resource, from, to);
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResource=resource;
        data=items;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        Map<String, Object> item=data.get(position);
         if (convertView == null) {
             view = mInflater.inflate(mResource, parent, false);
             viewHolder=new ViewHolder();
             viewHolder.title=(TextView)view.findViewById(R.id.title);
             viewHolder.image=(ImageView)view.findViewById(R.id.message_image);
             viewHolder.label=(TextView)view.findViewById(R.id.message_label);
             view.setTag(viewHolder);
         } else {
             view = convertView;
             viewHolder=(ViewHolder)view.getTag();
         }

        if(MainActivity.night){
            viewHolder.title.setTextColor(view.getResources().getColor(R.color.night_item_font));
        }else{
            viewHolder.title.setTextColor(view.getResources().getColor(R.color.menu_item));
        }
        if(!("".equals((String)item.get("label"))||item.get("label")==null)){
            viewHolder.label.setBackground(view.getResources().getDrawable(R.drawable.label_shape));
        }
        viewHolder.title.setText((String)item.get("title"));
        viewHolder.label.setText((String)item.get("label"));
        return view;
    }
    class ViewHolder{
        TextView title;
        TextView label;
        TextView note;
        ImageView image;
    }

}
