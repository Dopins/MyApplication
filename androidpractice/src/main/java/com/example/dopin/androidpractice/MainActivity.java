package com.example.dopin.androidpractice;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;


import android.view.Window;


import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends FragmentActivity  implements View.OnClickListener{

    private ViewPager mViewPager;
    private int currIndex;//当前页卡编号
    private int bmpW;//横线图片宽度
    private int offset;//图片移动的偏移量
    private FragWeChat weChat;
    private FragAddressBook discovery;
    private FragDiscovery address_book;
    private FragMe me;
    private ImageButton weChatButton;
    private ImageButton addressBookButton;
    private ImageButton discoveryButton;
    private ImageButton meButton;
    private TextView weChatText;
    private TextView address_listText;
    private TextView discoveryText;
    private TextView meText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initViewPager();
        initButton();
    }

    private void initViewPager(){
        mViewPager=(ViewPager)findViewById(R.id.frag_content);

        weChat= new FragWeChat();
        discovery= new FragAddressBook();
        address_book= new FragDiscovery();
        me= new FragMe();

        ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(weChat);
        fragmentList.add(discovery);
        fragmentList.add(address_book);
        fragmentList.add(me);
        mViewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
        mViewPager.setCurrentItem(0);//设置当前显示标签页为第一页
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());//页面变化时的监听器
    }

    private void initButton(){
        weChatText=(TextView)findViewById(R.id.weChatText);
        address_listText=(TextView)findViewById(R.id.address_bookText);
        discoveryText=(TextView)findViewById(R.id.discoveryText);
        meText=(TextView)findViewById(R.id.meText);

        weChatButton=(ImageButton)findViewById(R.id.weChatButton);
        addressBookButton=(ImageButton)findViewById(R.id.address_bookButton);
        discoveryButton=(ImageButton)findViewById(R.id.discoveryButton);
        meButton=(ImageButton)findViewById(R.id.meButton);

        weChatButton.setOnClickListener(this);
        addressBookButton.setOnClickListener(this);
        discoveryButton.setOnClickListener(this);
        meButton.setOnClickListener(this);

        findViewById(R.id.weChat).setOnClickListener(this);
        findViewById(R.id.address_book).setOnClickListener(this);
        findViewById(R.id.discovery).setOnClickListener(this);
        findViewById(R.id.me).setOnClickListener(this);
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private int one = offset *2 +bmpW;//两个相邻页面的偏移量

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }
        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
        @Override
        public void onPageSelected(int arg0) {
            Animation animation = new TranslateAnimation(currIndex*one,arg0*one,0,0);//平移动画
            animation.setFillAfter(true);//动画终止时停留在最后一帧，不然会回到没有执行前的状态
            animation.setDuration(200);//动画持续时间0.2秒
            //image.startAnimation(animation);//是用ImageView来显示动画的

            currIndex = arg0;
            turnPage(currIndex);
        }
    }
    private void turnPage(int index){
        switch (index){
            case 0:
                turnWeChat();
                break;
            case 1:
                turnAddress_book();
                break;
            case 2:
                turnDiscovery();
                break;
            case 3:
                turnMe();
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
            if(id==R.id.weChat||id==R.id.weChatButton) {
                mViewPager.setCurrentItem(0);
                turnWeChat();
            }
            else if(id==R.id.address_book||id==R.id.address_bookButton) {
                mViewPager.setCurrentItem(1);
               turnAddress_book();
            }
            else if(id==R.id.discovery||id==R.id.discoveryButton) {
                mViewPager.setCurrentItem(2);
                turnDiscovery();
            }
            else if(id==R.id.me||id==R.id.meButton){
                mViewPager.setCurrentItem(3);
                turnMe();
            }
    }

   private void turnWeChat(){

        weChatButton.setBackgroundResource(R.drawable.message_blue);
        addressBookButton.setBackgroundResource(R.drawable.address_list);
        discoveryButton.setBackgroundResource(R.drawable.compass);
        meButton.setBackgroundResource(R.drawable.user);

        weChatText.setTextColor(getResources().getColor(R.color.blue));
        address_listText.setTextColor(getResources().getColor(R.color.gray));
        discoveryText.setTextColor(getResources().getColor(R.color.gray));
        meText.setTextColor(getResources().getColor(R.color.gray));
    }
    private void turnAddress_book(){

        weChatButton.setBackgroundResource(R.drawable.message);
        addressBookButton.setBackgroundResource(R.drawable.address_list_blue);
        discoveryButton.setBackgroundResource(R.drawable.compass);
        meButton.setBackgroundResource(R.drawable.user);

        address_listText.setTextColor(getResources().getColor(R.color.blue));
        weChatText.setTextColor(getResources().getColor(R.color.gray));
        discoveryText.setTextColor(getResources().getColor(R.color.gray));
        meText.setTextColor(getResources().getColor(R.color.gray));
    }
    private void turnDiscovery(){

        weChatButton.setBackgroundResource(R.drawable.message);
        addressBookButton.setBackgroundResource(R.drawable.address_list);
        discoveryButton.setBackgroundResource(R.drawable.compass_blue);
        meButton.setBackgroundResource(R.drawable.user);

        discoveryText.setTextColor(getResources().getColor(R.color.blue));
        address_listText.setTextColor(getResources().getColor(R.color.gray));
        weChatText.setTextColor(getResources().getColor(R.color.gray));
        meText.setTextColor(getResources().getColor(R.color.gray));
    }
    private void turnMe(){

        weChatButton.setBackgroundResource(R.drawable.message);
        addressBookButton.setBackgroundResource(R.drawable.address_list);
        discoveryButton.setBackgroundResource(R.drawable.compass);
        meButton.setBackgroundResource(R.drawable.user_blue);

        meText.setTextColor(getResources().getColor(R.color.blue));
        address_listText.setTextColor(getResources().getColor(R.color.gray));
        discoveryText.setTextColor(getResources().getColor(R.color.gray));
        weChatText.setTextColor(getResources().getColor(R.color.gray));
    }


}
