package com.example.user.waimai;

/**
 * Created by user on 2017/12/13.
 */

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;
public class ShopCarStartActivity extends Application {

    private static List<Activity> con_list = new ArrayList<Activity>();
    private static ShopCarStartActivity instance;
    private SharedPreferences sp;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = (ShopCarStartActivity) getApplicationContext();

        //imageload初始化
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext()).defaultDisplayImageOptions(
                defaultOptions).build();
        ImageLoader.getInstance().init(config);
    }

}
