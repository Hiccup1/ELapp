package com.example.user.waimai;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.example.user.util.DialogUtil;

public class MainActivity extends Activity {
    //用于添加每一个选项卡的id
    private String[] tags = {"A_tag", "B_tag", "C_tag", "D_tag"};
    //所添加选项卡的文本信息
    private String[] titles = {"外卖", "订单", "发现", "我的"};
    //所添加选项卡的图片信息
    private int[] images = {R.drawable.waimai, R.drawable.dingdan,R.drawable.faxian,R.drawable.account};
    //用于跳转至不同的Activity
    private Intent[] intents = new Intent[4];

    private String userName;
    private String flag;

    private  TabHost tabHost;
    private  TabWidget tabWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabHost = (TabHost) findViewById(R.id.tabhost);
        tabWidget = (TabWidget) findViewById(android.R.id.tabs);

        //
        Intent intent1 = getIntent();
        userName = intent1.getStringExtra("userName");
        flag = intent1.getStringExtra("flag");
        System.out.println("main " + userName);
//        tabWidget.setDividerDrawable(null);//设置tabWeight没有竖线分割
        tabWidget.setBackgroundColor(Color.WHITE);
        //初始化activity管理者
        LocalActivityManager manager = new LocalActivityManager(MainActivity.this, false);
        //通过管理者保存当前页面状态
        manager.dispatchCreate(savedInstanceState);
        //将管理者类对象添加至TabHost
        tabHost.setup(manager);
        init_intent();
        for (int i = 0; i < intents.length; i++) {
            //加载底部导航栏布局
            LayoutInflater inflater = this.getLayoutInflater();
            View view = inflater.inflate(R.layout.tab, null);
            TextView textView = (TextView) view.findViewById(R.id.tv_item);
            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            textView.setText(titles[i]);
            imageView.setImageResource(images[i]);
            //创建选项卡
            TabHost.TabSpec spec = tabHost.newTabSpec(tags[i]);
            spec.setIndicator(view);
            //设置每个页面的内容
            spec.setContent(intents[i].addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            //将创建的选项卡添加至tabHost上
            tabHost.addTab(spec);
        }
       if(flag.equals("1")){
           tabHost.postDelayed(new Runnable() {
               @Override
               public void run() {
                   tabHost.setCurrentTab(1);
                   DialogUtil.stop();
               }
           }, 100);
           DialogUtil.stop();
       }

    }
    //每个页面放置的Activity
    public void init_intent() {
        intents[0] = new Intent(this, ShowShopActivity.class);
        intents[0].putExtra("userName", userName);
        intents[1] = new Intent(this, ShowOrderActivity.class);
        intents[1].putExtra("userName", userName);
        intents[2] = new Intent(this, FindActivity.class);
        intents[3] = new Intent(this, MyActivity.class);
    }


}