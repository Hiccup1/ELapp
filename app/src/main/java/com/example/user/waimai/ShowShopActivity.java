package com.example.user.waimai;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.List;

import com.example.user.model.Shop;
import com.example.user.util.Data;
import com.example.user.util.DialogUtil;
import com.example.user.util.JsonParse;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.image.SmartImageView;

public class ShowShopActivity extends AppCompatActivity {
    private LinearLayout loading;
    private ListView lvShopss;
    private List<Shop> list;
    private TextView shop_name;
    private TextView shop_description;
    private Shop shop;
    private SmartImageView siv_shop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_shop);
        initView();
        fillData();
    }

    //初始化控件
    private void initView() {
        loading = (LinearLayout) findViewById(R.id.lodingShop);
        lvShopss = (ListView)findViewById(R.id.lv_shops);
    }

    //使用AsyncHttpClient访问网络
    private void fillData() {
        DialogUtil.showDialogForLoading(ShowShopActivity.this, "加载中");
        AsyncHttpClient client = new AsyncHttpClient();
        String url = Data.serverurl + "servlet/ShowShops";
        //post请求
        client.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, org.apache.http.Header[] headers, byte[] bytes) {
                //调用JsonParse解析json
                try {
                    String json = new String(bytes, "utf-8");
                    list = JsonParse.getShop(json);
                    if(list == null) {
                        DialogUtil.stop();
                        Toast.makeText(ShowShopActivity.this, "解析列表失败", Toast.LENGTH_SHORT).show();
                    } else {
                        DialogUtil.stop();
                        loading.setVisibility(View.INVISIBLE);
                        lvShopss.setAdapter(new ShopAdapter());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, org.apache.http.Header[] headers, byte[] bytes, Throwable throwable) {
                DialogUtil.stop();
                Toast.makeText(ShowShopActivity.this, "网络连接失败，请查看是否联网", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class ShopAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public View getView(int position, View converView, ViewGroup parent) {
            View view = View.inflate(ShowShopActivity.this, R.layout.shops_item, null);
            siv_shop = (SmartImageView) view.findViewById(R.id.siv_icon);
            shop_name = (TextView) view.findViewById(R.id.shop_name);
            shop_description = (TextView) view.findViewById(R.id.shop_description);
            shop = list.get(position);
            //SmartImageView加载图片
            siv_shop.setImageUrl(shop.getImg(),R.mipmap.ic_launcher, R.mipmap.ic_launcher);
            //名字
            shop_name.setText(shop.getName());
            shop_description.setText(shop.getLocation());

            //监听器
            lvShopss.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //跳转到某个Activity里
                    Intent intent1 = getIntent();
                    String username = intent1.getStringExtra("userName");
                    Intent intent = new Intent(ShowShopActivity.this, ShopCarActivity.class);
                    int sid = list.get(position).getSid();
                    String shopname = list.get(position).getName();
                    String shopimg = list.get(position).getImg();
                    String sid1 = Integer.toString(sid);
                    intent.putExtra("sid", sid1);
                    intent.putExtra("userName", username);
                    startActivity(intent);

                }
            });
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