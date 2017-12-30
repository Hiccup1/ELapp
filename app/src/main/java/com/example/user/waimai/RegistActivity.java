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
import com.example.user.util.JsonParse;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class RegistActivity extends AppCompatActivity {
    private Button regist = null;
    private View relativeLayout = null;
    private EditText userNmae = null;
    private EditText passWord = null;
    private EditText againPassWord = null;
    private String userNmaeString;
    private String passWordString;
    private String againPassWordSrring;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        /*
        This activity is created and opened when SplashScreen finishes its animations.
        To ensure a smooth transition between activities, the activity creation animation
        is removed.
        RelativeLayout with EditTexts and Button is animated with a default fade in.
         */

        overridePendingTransition(0,0);

        initView();
        //Animation animation=AnimationUtils.loadAnimation(this,android.R.anim.fade_in);
        //relativeLayout.startAnimation(animation);

        //注册按钮监听器
        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断两次密码是否一样
                userNmaeString = userNmae.getText().toString().trim();
                passWordString = passWord.getText().toString().trim();
                againPassWordSrring = againPassWord.getText().toString();

                if(userNmaeString == null || userNmaeString.equals("")) {
                    Toast.makeText(RegistActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(passWordString == null || passWordString.equals("")) {
                    Toast.makeText(RegistActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(againPassWordSrring == null || againPassWordSrring.equals("")) {
                    Toast.makeText(RegistActivity.this, "请再次输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!passWordString.equals(againPassWordSrring)) {
                    Toast.makeText(RegistActivity.this, "两次填入的密码不同", Toast.LENGTH_SHORT).show();
                    return;
                }
                regist();
            }
        });

    }

    private void initView() {
        relativeLayout=findViewById(R.id.regist_container);
        regist = (Button) findViewById(R.id.regist);
        userNmae = (EditText) findViewById(R.id.username_edit_text);
        passWord = (EditText)findViewById(R.id.password_edit_text);
        againPassWord = (EditText)findViewById(R.id.again_password_edit_text);
    }

    private void regist() {
        DialogUtil.showDialogForLoading(RegistActivity.this, "加载中");
        AsyncHttpClient client = new AsyncHttpClient();
        String url = Data.serverurl + "servlet/Regist?userName=" + userNmaeString + "&passWord=" + passWordString;
        //get请求
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, org.apache.http.Header[] headers, byte[] bytes) {

                try {
                    String result = new String(bytes, "utf-8");
                    if(result == null) {
                        DialogUtil.stop();
                        Toast.makeText(RegistActivity.this, "服务器故障", Toast.LENGTH_SHORT).show();
                    } else {
                        if(result.equals("0")) {
                            DialogUtil.stop();
                            Toast.makeText(RegistActivity.this, "注册失败,未知错误", Toast.LENGTH_SHORT).show();
                        }else if(result.equals("2")) {
                            DialogUtil.stop();
                            Toast.makeText(RegistActivity.this, "注册失败,此用户名已被注册", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            DialogUtil.stop();
                            Toast.makeText(RegistActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            //跳转到ShowShops
                            Intent intent = new Intent(RegistActivity.this, MainActivity.class);
                            intent.putExtra("userName", userNmaeString);
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
                Toast.makeText(RegistActivity.this, "网络连接失败，请查看是否联网", Toast.LENGTH_SHORT).show();
            }
        });
    }
}