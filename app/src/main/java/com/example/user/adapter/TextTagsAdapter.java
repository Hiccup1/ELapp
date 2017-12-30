package com.example.user.adapter;

/**
 * Created by user on 2017/12/21.
 */
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moxun.tagcloudlib.view.TagsAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by moxun on 16/1/19.
 */
public class TextTagsAdapter extends TagsAdapter {

    private List<String> dataSet = new ArrayList<>();
    private String[] names= {"饿了", "我的", "外卖", "店铺", "购物车", "下单", "后台", "数据库", "发现", "订单", "用户名", "登录", "注册"};

    public TextTagsAdapter(@NonNull String... data) {
        dataSet.clear();
        Collections.addAll(dataSet,data);
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public View getView(Context context, final int position, ViewGroup parent) {
        TextView tv = new TextView(context);
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(100, 100);
        tv.setLayoutParams(lp);
        tv.setText(names[position]);
        tv.setGravity(Gravity.CENTER);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("Click","Tag " + position + " clicked.");
            }
        });
        return tv;
    }

    @Override
    public Object getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public int getPopularity(int position) {
        return position % 7;
    }

    @Override
    public void onThemeColorChanged(View view, int themeColor) {
        ((TextView)view).setTextColor(themeColor);
    }
}