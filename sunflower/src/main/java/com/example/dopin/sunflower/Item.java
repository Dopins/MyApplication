package com.example.dopin.sunflower;

/**
 * Created by dopin on 2016/3/22.
 */
public class Item {
    private String name;
    private int imageId;
    public Item(String name,int imageId){
        this.name=name;
        this.imageId=imageId;
    }
    public String getName(){
        return name;
    }
    public int getImageId(){
        return imageId;
    }
}
