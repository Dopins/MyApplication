package com.example.dopin.sunflower;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.dopin.androidpractice2.R;

import java.util.List;
import java.util.Map;

/**
 * Created by dopin on 2016/4/1.
 */
public class MessageAdapter extends SimpleAdapter {
    private AsyncImageLoader imageLoader;
    private LayoutInflater mInflater;
    private int mResource;
    private List<Map<String, Object>> data;
    private Context context;
    public MessageAdapter(Context context, List<Map<String, Object>>items,int resource,String[] from,int[] to) {
        super(context, items, resource, from, to);
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResource=resource;
        data=items;
        imageLoader = new AsyncImageLoader(context);
        this.context=context;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        final ViewHolder viewHolder;
        Map<String, Object> item=data.get(position);
         if (convertView == null) {
             view = mInflater.inflate(mResource, parent, false);
             viewHolder=new ViewHolder();
             viewHolder.title=(TextView)view.findViewById(R.id.title);
             viewHolder.image=(ImageView)view.findViewById(R.id.message_image);
             viewHolder.label=(TextView)view.findViewById(R.id.message_label);
             viewHolder.hasNote=(ImageView)view.findViewById(R.id.note);
             viewHolder.labelLayout=(LinearLayout)view.findViewById(R.id.label_layout);
             viewHolder.space=(TextView)view.findViewById(R.id.space);
             viewHolder.cardView=(CardView)view.findViewById(R.id.cardView);
             view.setTag(viewHolder);
         } else {
             view = convertView;
             viewHolder=(ViewHolder)view.getTag();
         }
        if(MainActivity.night){
            viewHolder.title.setTextColor(view.getResources().getColor(R.color.night_item_font));
            viewHolder.cardView.setCardBackgroundColor(view.getResources().getColor(R.color.night_item_back));
        }else{
            viewHolder.title.setTextColor(view.getResources().getColor(R.color.menu_item));
        }
        if(MainActivity.index==6){
            viewHolder.labelLayout.setVisibility(View.VISIBLE);
            viewHolder.image.setVisibility(View.GONE);
            if(!("".equals((String)item.get("label"))||item.get("label")==null)){
                viewHolder.label.setBackground(view.getResources().getDrawable(R.drawable.label_shape));
                viewHolder.label.setText((String) item.get("label"));
            }if(!("".equals((String)item.get("note"))||item.get("note")==null)){
                viewHolder.hasNote.setImageResource(android.R.drawable.ic_menu_edit);
            }
        }else if(MainActivity.index==3||MainActivity.index==4||MainActivity.index==5||is_no_image(view)){
            viewHolder.image.setVisibility(View.GONE);
            viewHolder.space.setVisibility(View.VISIBLE);
        } else{

            final String imgUrl = data.get(position).get("imageUrl").toString();
            // 给 ImageView 设置一个tag
            viewHolder.image.setTag(imgUrl);
            // 预设一个图片
            viewHolder.image.setImageResource(R.drawable.background);

            if (!TextUtils.isEmpty(imgUrl)) {
                Bitmap bitmap = imageLoader.loadImage(viewHolder.image, imgUrl);
                if (bitmap != null) {
                    Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {
                            if (MainActivity.night==true) {
                                Palette.Swatch DarkMuted = palette.getDarkMutedSwatch();
                                if(DarkMuted!=null) {
                                    viewHolder.cardView.setCardBackgroundColor(DarkMuted.getRgb());
                                    viewHolder.title.setTextColor(DarkMuted.getTitleTextColor());
                                }
                            }else if(MainActivity.night==false){
                                Palette.Swatch LightMuted = palette.getLightMutedSwatch();
                                if(LightMuted!=null) {
                                    viewHolder.cardView.setCardBackgroundColor(LightMuted.getRgb());
                                    viewHolder.title.setTextColor(LightMuted.getTitleTextColor());
                                }
                            }
                        }
                    });

                    viewHolder.image.setImageBitmap(bitmap);
                }
            }
        }
        viewHolder.title.setText((String)item.get("title"));

        return view;
    }
    private boolean is_no_image(View view){
        if(MainActivity.save_flow){
            if(MainActivity.isWifi(view.getContext())){
                return  false;
            }else return true;
        }else{
            return false;
        }
    }

    class ViewHolder{
        LinearLayout labelLayout;
        CardView cardView;
        TextView title;
        TextView label;
        ImageView hasNote;
        ImageView image;
        TextView space;
    }

}