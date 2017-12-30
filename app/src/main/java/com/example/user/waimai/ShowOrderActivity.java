package com.example.user.waimai;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.model.Dish;
import com.example.user.model.Order;
import com.example.user.util.Data;
import com.example.user.util.DialogUtil;
import com.example.user.util.JsonParse;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.image.SmartImageView;

import java.util.List;

public class ShowOrderActivity extends AppCompatActivity {
    private LinearLayout loading;
    private ListView lvOrders;
    private List<Order> list;
    private TextView order_name;
    private TextView order_state;
    private TextView order_description;
    private TextView order_price;
    private Order order;
    private SmartImageView siv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_order);
        initView();
        fillData();
    }

    //初始化控件
    private void initView() {
        loading = (LinearLayout) findViewById(R.id.loding);
        lvOrders = (ListView)findViewById(R.id.lv_orders);
    }

    //暂时展示order
    private void fillData() {
        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");
        DialogUtil.showDialogForLoading(ShowOrderActivity.this, "加载中");
        AsyncHttpClient client = new AsyncHttpClient();
        String url = Data.serverurl + "servlet/ShowOrder?userName=" + userName;
        client.get(url,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, org.apache.http.Header[] headers, byte[] bytes) {
                //调用JsonParse解析json
                try {
                    String json = new String(bytes, "utf-8");
                    list = JsonParse.getOrder(json);
                    if(list == null) {
                        DialogUtil.stop();
                        Toast.makeText(ShowOrderActivity.this, "解析order列表失败", Toast.LENGTH_SHORT).show();
                    } else {
                        DialogUtil.stop();
                        loading.setVisibility(View.INVISIBLE);
                        lvOrders.setAdapter(new OrderAdapter());
                    }
                } catch (Exception e) {
                    DialogUtil.stop();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, org.apache.http.Header[] headers, byte[] bytes, Throwable throwable) {
                DialogUtil.stop();
                System.out.println("网络连接失败");
                Toast.makeText(ShowOrderActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class OrderAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public View getView(int position, View converView, ViewGroup parent) {
            View view = View.inflate(ShowOrderActivity.this, R.layout.order_item, null);
            siv = (SmartImageView) view.findViewById(R.id.siv_icon);
            order_name = (TextView) view.findViewById(R.id.dish_name);
            order_state = (TextView)view.findViewById(R.id.dish_state);
            order_description = (TextView) view.findViewById(R.id.dish_description);
            order_price = (TextView) view.findViewById(R.id.dish_price);
            order = list.get(position);
            //SmartImageView加载图片
            siv.setImageUrl(order.getRemark(),R.mipmap.ic_launcher, R.mipmap.ic_launcher);
            //名字
            order_name.setText(order.getName());
            if(order.getState() == 1) {
                order_state.setText(">未接单");
                order_state.setTextColor(Color.GRAY);

            }else {
                order_state.setText(">已接单");
                order_state.setTextColor(Color.BLACK);
            }
            order_description.setText(order.getTime());
            order_price.setText("¥ " + order.getPrice());

            //监听器
            lvOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //跳转到某个Activity里
                    Intent intent1 = getIntent();
                    String username = intent1.getStringExtra("userName");
                    Intent intent = new Intent(ShowOrderActivity.this, ShowDishActivity.class);
                    int oid = list.get(position).getOid();
                    String oidstr = Integer.toString(oid);
                    intent.putExtra("oid", oidstr);
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
