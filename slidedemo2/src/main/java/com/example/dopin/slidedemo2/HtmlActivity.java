package com.example.dopin.slidedemo2;

/**
 * Created by dopin on 2016/3/22.
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class HtmlActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = (TextView) findViewById(R.id.out);
        String myString = new String();

        try {
            Document doc = Jsoup.connect("https://www.zhihu.com/explore").get();
            //Elements
            Elements topnews = doc.getElementsByClass("question_link");
            //Elements
            Elements links = topnews.select("a[href]");
            for (Element link : links) {
                myString+=link.text();
                myString+="\n";
            }

        } catch (Exception e) {

            myString = e.getMessage();
            e.printStackTrace();
        }
		/* 将信息设置到TextView */
        tv.setText(myString);

    }
}