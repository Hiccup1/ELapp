package com.example.user.waimai;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;

public class Splash_loadingActivity extends Activity {

    private final int SPLASH_DISPLAY_LENGHT = 850;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_loading);

        handler = new Handler();
        // 延迟SPLASH_DISPLAY_LENGHT时间然后跳转到MainActivity
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(Splash_loadingActivity.this,
                        SplashActivity.class);
                startActivity(intent);
                Splash_loadingActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGHT);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            return  true;
            }
        return  super.onKeyDown(keyCode, event);

    }
}