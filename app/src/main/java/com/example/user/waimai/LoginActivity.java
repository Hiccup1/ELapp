package com.example.user.waimai;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.util.Data;
import com.example.user.util.DialogUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.wang.avi.AVLoadingIndicatorView;

public class LoginActivity extends AppCompatActivity {
    private Button login = null;
    private Button regist = null;
    View relativeLayout = null;
    private EditText userNmae = null;
    private EditText passWord = null;
    private String userNameString = null;
    private String passWordString = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        /*
        This activity is created and opened when SplashScreen finishes its animations.
        To ensure a smooth transition between activities, the activity creation animation
        is removed.
        RelativeLayout with EditTexts and Button is animated with a default fade in.
         */

        overridePendingTransition(0,0);
        initview();


        //Animation animation=AnimationUtils.loadAnimation(this,android.R.anim.fade_in);
        //relativeLayout.startAnimation(animation);

        //登录按钮监听器
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userNameString = userNmae.getText().toString().trim();
                passWordString = passWord.getText().toString().trim();
                if(userNameString == null || userNameString.equals("")) {
                    Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(passWordString == null || passWordString.equals("")) {
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                login();

            }
        });
        //注册按钮监听器
        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
                startActivity(intent);
            }
        });

    }

    private void login() {
        DialogUtil.showDialogForLoading(LoginActivity.this, "加载中");
        AsyncHttpClient client = new AsyncHttpClient();
        String url = Data.serverurl + "servlet/Login?userName=" + userNameString + "&passWord=" + passWordString;
        //get请求
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, org.apache.http.Header[] headers, byte[] bytes) {

                try {
                    String result = new String(bytes, "utf-8");
                    //stopAnim();

                    if(result == null) {
                        DialogUtil.stop();
                        Toast.makeText(LoginActivity.this, "服务器故障", Toast.LENGTH_SHORT).show();
                    } else {
                        if(result.equals("0")) {
                            DialogUtil.stop();
                            Toast.makeText(LoginActivity.this, "登录失败，没有此用户", Toast.LENGTH_SHORT).show();
                        }
                        else if(result.equals("2")) {
                            DialogUtil.stop();
                            Toast.makeText(LoginActivity.this, "密码输入错误,请检查用户名/密码", Toast.LENGTH_SHORT).show();
                        }
                        else if(result.equals("3")) {
                            DialogUtil.stop();
                            Toast.makeText(LoginActivity.this, "商家和普通用户账号不通用", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            DialogUtil.stop();
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            //跳转到ShowShops
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("userName", userNameString);
                            intent.putExtra("flag", "0");
                            startActivity(intent);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, org.apache.http.Header[] headers, byte[] bytes, Throwable throwable) {
                DialogUtil.stop();
                Toast.makeText(LoginActivity.this, "网络连接失败，请查看是否联网", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initview() {
        relativeLayout=findViewById(R.id.login_container);
        login = (Button) findViewById(R.id.login);
        regist = (Button) findViewById(R.id.regist);
        userNmae = (EditText) findViewById(R.id.username_edit_text);
        passWord = (EditText)findViewById(R.id.password_edit_text);
    }
}

