package com.example.user.waimai;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.List;

import com.example.user.model.Dish;
import com.example.user.util.Data;
import com.example.user.util.DialogUtil;
import com.example.user.util.JsonParse;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.image.SmartImageView;

public class ShowDishActivity extends AppCompatActivity {
    private LinearLayout loading;
    private ListView lvDishs;
    private List<Dish> list;
    private TextView dish_name;
    private TextView dish_description;
    private TextView dish_count;
    private Dish dish;
    private SmartImageView siv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_dish);
        initView();
        fillData();

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //初始化控件
    private void initView() {
        loading = (LinearLayout) findViewById(R.id.loding);
        lvDishs = (ListView)findViewById(R.id.lv_dishs);
    }

    //暂时展示order dish
    private void fillData() {
        Intent intent = getIntent();
        String oid = intent.getStringExtra("oid");
        System.out.println(oid);
        DialogUtil.showDialogForLoading(ShowDishActivity.this, "加载中");
        AsyncHttpClient client = new AsyncHttpClient();
        String url = Data.serverurl + "servlet/ShowDishByOid?oid=" + oid;
        client.get(url,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, org.apache.http.Header[] headers, byte[] bytes) {
                //调用JsonParse解析json
                try {
                    String json = new String(bytes, "utf-8");
                    list = JsonParse.getDish(json);
                    if(list == null) {
                        DialogUtil.stop();
                        Toast.makeText(ShowDishActivity.this, "解析dish列表失败", Toast.LENGTH_SHORT).show();
                    } else {
                        DialogUtil.stop();
                        loading.setVisibility(View.INVISIBLE);
                        lvDishs.setAdapter(new DishAdapter());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, org.apache.http.Header[] headers, byte[] bytes, Throwable throwable) {
                DialogUtil.stop();
                System.out.println("网络连接失败");
                Toast.makeText(ShowDishActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class DishAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public View getView(int position, View converView, ViewGroup parent) {
            View view = View.inflate(ShowDishActivity.this, R.layout.dishs_item, null);
            siv = (SmartImageView) view.findViewById(R.id.siv_icon);
            dish_name = (TextView) view.findViewById(R.id.dish_name);
            dish_description = (TextView) view.findViewById(R.id.dish_description);
            dish_count = (TextView)view.findViewById(R.id.dish_count);
            dish = list.get(position);
            //SmartImageView加载图片
            siv.setImageUrl(dish.getImg(),R.mipmap.ic_launcher, R.mipmap.ic_launcher);
            //名字
            dish_name.setText(dish.getName());
            dish_count.setText( " * " + dish.getSellcount());
            dish_description.setText("¥ " + dish.getPrice() * dish.getSellcount());
            return view;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }
        @Override
        public long getItemId(int position) {
            return 0;
        }
    }
}
