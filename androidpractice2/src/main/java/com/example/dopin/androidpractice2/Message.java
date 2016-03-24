package com.example.dopin.androidpractice2;

/**
 * Created by dopin on 2016/3/22.
 */
public class Message {
    private String url;
    private String title;
    private String content;

    public Message(String url,String title,String content){
        this.url=url;
        this.title=title;
        this.content=content;
    }
    public  String getTitle(){
        return title;
    }
    public String getContent(){
        return content;
    }

    public String getUrl(){
        return url;
    }

}
