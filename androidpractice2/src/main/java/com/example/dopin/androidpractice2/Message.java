package com.example.dopin.androidpractice2;

/**
 * Created by dopin on 2016/3/22.
 */
public class Message {
    private String title;
    private String content;
    public Message(String title,String content){
        this.title=title;
        this.content=content;
    }
    public  String getTitle(){
        return title;
    }
    public String getContent(){
        return content;
    }

}
