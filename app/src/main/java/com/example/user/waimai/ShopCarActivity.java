package com.example.user.waimai;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import me.drakeet.materialdialog.MaterialDialog;

import com.example.user.util.Data;
import com.example.user.util.DialogUtil;
import com.example.user.view.MyListView;
import com.example.user.util.JsonParse;
import com.flipboard.bottomsheet.BottomSheetLayout;

import com.example.user.adapter.DishsDetailAdapter;
import com.example.user.adapter.TypeAdapter;
import com.example.user.model.*;
import com.example.user.adapter.*;
import com.google.gson.JsonArray;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShopCarActivity extends AppCompatActivity {

    //控件
    private ListView lv_catogary, lv_good;
    private ImageView iv_logo;
    private TextView tv_car;
    private  TextView tv_count,tv_totle_money;
    Double totleMoney = 0.00;
    private TextView bv_unm;
    private RelativeLayout rl_bottom;
    //分类和商品
    private String sid;//商店sid
    private String userName;//用户username
    private String shopname;//
    private String shopimg;
    private List<Catogray> list = new ArrayList<Catogray>();
    private List<Dish> list2 = new ArrayList<Dish>();
    private List<Types> listtypes = new ArrayList<Types>();

    private ShopCarStartActivity shopCarStartActivity;
    private TypeAdapter typeAdapter;//分类的adapter
    private DishsAdapter dishsAdapter;//分类下商品adapter
    ProductAdapter productAdapter;//底部购物车的adapter
    DishsDetailAdapter dishsDetailAdapter;//套餐详情的adapter
    private static DecimalFormat df;
    private LinearLayout ll_shopcar;
    //底部数据
    private BottomSheetLayout bottomSheetLayout;
    private View bottomSheet;
    private SparseArray<Dish> selectedList;
    //套餐dishs
    private View bottomDetailSheet;

    private Handler mHanlder;
    private ViewGroup anim_mask_layout;//动画层

    private MaterialDialog mMaterialDialog;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_car);
        shopCarStartActivity = (ShopCarStartActivity) getApplicationContext();
        mHanlder = new Handler(getMainLooper());
        initView();
        initData();
        addListener();
        ll_shopcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheet();
            }
        });

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

    public void initView() {
        lv_catogary = (ListView) findViewById(R.id.lv_catogary);
        lv_good = (ListView) findViewById(R.id.lv_good);
        tv_car = (TextView) findViewById(R.id.tv_car);
        //底部控件
        rl_bottom = (RelativeLayout) findViewById(R.id.rl_bottom);
        tv_count = (TextView) findViewById(R.id.tv_count);
        bv_unm = (TextView) findViewById(R.id.bv_unm);
        tv_totle_money= (TextView) findViewById(R.id.tv_totle_money);
        ll_shopcar= (LinearLayout) findViewById(R.id.ll_shopcar);
        selectedList = new SparseArray<>();
        df = new DecimalFormat("0.00");
    }

    //填充数据
    private void initData() {
        //商品
        DialogUtil.showDialogForLoading(ShopCarActivity.this, "加载中");
        AsyncHttpClient client = new AsyncHttpClient();
        //获取对应的shop sid
        Intent intent = getIntent();
        sid = intent.getStringExtra("sid");
        userName = intent.getStringExtra("userName");
        shopname = intent.getStringExtra("shopname");
        shopimg = intent.getStringExtra("shopimg");
        System.out.println("请求types :sid = " + sid);
        //get请求,根据sid获取types
        String url = Data.serverurl + "servlet/ShowTypes?sid=" + sid;
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, org.apache.http.Header[] headers, byte[] bytes) {
                //调用JsonParse解析json
                try {
                    String json = new String(bytes, "utf-8");
                    listtypes = JsonParse.getTypes(json);
                    if(listtypes == null) {
                        DialogUtil.stop();
                        System.out.println("解析types失败");
                        Toast.makeText(ShopCarActivity.this, "解析Types失败", Toast.LENGTH_SHORT).show();
                    } else {
                        DialogUtil.stop();
                        System.out.println("请求+解析types成功");
                        //int len = listtypes.size();
                        //根据sid和tid获取菜单dishs
                        initDishs(sid);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, org.apache.http.Header[] headers, byte[] bytes, Throwable throwable) {
                DialogUtil.stop();
                System.out.println("请求types失败");
                Toast.makeText(ShopCarActivity.this, "请求Types失败", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void initDishs(String sid) {
        AsyncHttpClient client = new AsyncHttpClient();
        //get请求
        String url = Data.serverurl + "servlet/ShowDishs?sid=" + sid;
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, org.apache.http.Header[] headers, byte[] bytes) {
                //调用JsonParse解析json
                try {
                    String json = new String(bytes, "utf-8");
                    List<Dish> listdishs = JsonParse.getDish(json);
                    if(listdishs == null) {
                        System.out.println("解析dishs失败");
                        Toast.makeText(ShopCarActivity.this, "解析dishs失败", Toast.LENGTH_SHORT).show();
                    } else {
                        System.out.println("请求+解析dishs成功");
                        int lentypes = listtypes.size();
                        int lendishs = listdishs.size();
                        int i2 = 0;
                        for(int i1 = 0; i1 < lentypes; i1++) {
                            List<Dish> listdishs1 = new ArrayList<Dish>();
                            while(i2 < lendishs) {
                                System.out.println(listdishs.get(i2).getName());
                                if(listtypes.get(i1).getTid() == listdishs.get(i2).getTid()) {
                                    listdishs1.add(listdishs.get(i2));
                                    System.out.println("添加一个dish成功");
                                    i2++;
                                }
                                else {
                                    //if(listdishs1.size() > 0) {
                                        Catogray catogray = new Catogray();
                                        catogray.setCount(4);
                                        catogray.setKind(listtypes.get(i1).getName());
                                        catogray.setList(listdishs1);
                                        list.add(catogray);
                                        System.out.println("添加一个types分类成功");
                                        break;
                                    //}
                                }
                            }
                            if(i2 >= lendishs) {
                                Catogray catogray = new Catogray();
                                catogray.setCount(4);
                                catogray.setKind(listtypes.get(i1).getName());
                                catogray.setList(listdishs1);
                                list.add(catogray);
                                System.out.println("添加一个types分类成功");
                            }

                        }
                        if(list != null) {
                            //商品
                            bottomSheetLayout = (BottomSheetLayout) findViewById(R.id.bottomSheetLayout);

                            //默认值
                            list2.clear();
                            list2.addAll(list.get(0).getList());

                            //分类
                            typeAdapter = new TypeAdapter(ShopCarActivity.this, list);
                            lv_catogary.setAdapter(typeAdapter);
                            typeAdapter.notifyDataSetChanged();

                            //商品
                            dishsAdapter = new DishsAdapter(ShopCarActivity.this, list2, typeAdapter);
                            lv_good.setAdapter(dishsAdapter);
                            dishsAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, org.apache.http.Header[] headers, byte[] bytes, Throwable throwable) {
                System.out.println("请求types失败");
                Toast.makeText(ShopCarActivity.this, "请求dishs失败", Toast.LENGTH_SHORT).show();
            }
        });

    }


    //添加监听
    private void addListener() {
        lv_catogary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("fyg","list.get(position).getList():"+list.get(position).getList());
                list2.clear();
                list2.addAll(list.get(position).getList());
                typeAdapter.setSelection(position);
                typeAdapter.notifyDataSetChanged();
                dishsAdapter.notifyDataSetChanged();
            }
        });

        //去结算添加监听
        tv_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("点击结算");

                mMaterialDialog= new MaterialDialog(ShopCarActivity.this)
                        .setTitle("下单确认")
                        .setMessage("确认下单么？")
                        .setPositiveButton("YES", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMaterialDialog.dismiss();
                                goPay();
                            }
                        })
                        .setNegativeButton("NO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMaterialDialog.dismiss();
                            }
                        });

                mMaterialDialog.show();
            }
        });
    }





    //创建套餐详情view
    public void showDetailSheet(List<Dish> listItem, String mealName){
        bottomDetailSheet = createMealDetailView(listItem,mealName);
        if(bottomSheetLayout.isSheetShowing()){
            bottomSheetLayout.dismissSheet();
        }else {
            if(listItem.size()!=0){
                bottomSheetLayout.showWithSheetView(bottomDetailSheet);
            }
        }
    }

    //查看套餐详情
    private View createMealDetailView(List<Dish> listItem, String mealName){
        View view = LayoutInflater.from(this).inflate(R.layout.activity_dishs_detail,(ViewGroup) getWindow().getDecorView(),false);
        ListView lv_product = (MyListView) view.findViewById(R.id.lv_product);
        TextView tv_meal = (TextView) view.findViewById(R.id.tv_meal);
        TextView tv_num = (TextView) view.findViewById(R.id.tv_num);
        int count=0;
        for(int i=0;i<listItem.size();i++){
            count = count+listItem.get(i).getSellcount();
        }
        tv_meal.setText(mealName);
        tv_num.setText("(共"+count+"件)");
        dishsDetailAdapter = new DishsDetailAdapter(ShopCarActivity.this,listItem);
        lv_product.setAdapter(dishsDetailAdapter);
        dishsDetailAdapter.notifyDataSetChanged();
        return view;
    }



    //结算，生成订单
    private void goPay() {
        /*
        Intent intent = new Intent(ShopCarActivity.this, ShowDishActivity.class);
        List<Dish> orderlist = new ArrayList<Dish>();
        int len = selectedList.size();
       for(int i4 = 0; i4 < len; i4++) {
           int key = key = selectedList.keyAt(i4);
           orderlist.add(selectedList.get(key));
           System.out.println("结算添加一个dish成功");
       }
        JsonArray jsonArray = JsonParse.getDishTOjson(orderlist);
        String orderstring = jsonArray.toString(); // 将JSONArray转换得到String
        intent.putExtra("orderstring", orderstring);
        startActivity(intent);
        */
        //生成对应的list orderitem
        int len = selectedList.size();
        List<OrderItem> orderItemList = new ArrayList<OrderItem>();

        int oid = (int)(new Date().getTime());

        for(int i4 = 0; i4 < len; i4++) {
            int key = key = selectedList.keyAt(i4);
            OrderItem orderItem = new OrderItem();
            orderItem.setOid(oid);
            orderItem.setDid(selectedList.get(key).getDid());
            orderItem.setNum(selectedList.get(key).getSellcount());
            orderItem.setSid(Integer.parseInt(sid));
            orderItemList.add(orderItem);
            System.out.println("结算添加一个orderItem成功");
        }
        if(orderItemList.size() > 0) {
            JsonArray jsonArray = JsonParse.getOrderItemTOjson(orderItemList);
            String orderitemstring = jsonArray.toString(); // 将JSONArray转换得到String

            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("orderitemstring", orderitemstring);
            params.put("userName", userName);
            System.out.println(userName);

            //获取对应的orderItem oid
            System.out.println("creat order :oid = " + oid);

            //post请求,根据sid获取types
            String url = Data.serverurl + "servlet/CreatOrder";
            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, org.apache.http.Header[] headers, byte[] bytes) {
                    //调用JsonParse解析json
                    try {
                        String result = new String(bytes, "utf-8");
                        if (result == null) {
                            System.out.println("服务器连接失败");
                            Toast.makeText(ShopCarActivity.this, "服务器连接失败", Toast.LENGTH_SHORT).show();
                        } else if (result.equals("0")) {
                            System.out.println("下单失败");
                            Toast.makeText(ShopCarActivity.this, "下单失败", Toast.LENGTH_SHORT).show();
                        } else {
                            System.out.println("下单成功");
                            Toast.makeText(ShopCarActivity.this, "下单成功", Toast.LENGTH_SHORT).show();


                            Intent intent = new Intent(ShopCarActivity.this, MainActivity.class);

                            intent.putExtra("userName", userName);
                            intent.putExtra("flag", "1");
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int i, org.apache.http.Header[] headers, byte[] bytes, Throwable throwable) {
                    System.out.println("网络连接失败");
                    Toast.makeText(ShopCarActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    //创建购物车view
    private void showBottomSheet(){
        bottomSheet = createBottomSheetView();
        if(bottomSheetLayout.isSheetShowing()){
            bottomSheetLayout.dismissSheet();
        }else {
            if(selectedList.size()!=0){
                bottomSheetLayout.showWithSheetView(bottomSheet);
            }
        }
    }




    //查看购物车布局
    private View createBottomSheetView(){
        View view = LayoutInflater.from(this).inflate(R.layout.layout_bottom_sheet,(ViewGroup) getWindow().getDecorView(),false);
        MyListView lv_product = (MyListView) view.findViewById(R.id.lv_product);
        TextView clear = (TextView) view.findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCart();
            }
        });
        productAdapter = new ProductAdapter(ShopCarActivity.this,dishsAdapter, selectedList);
        lv_product.setAdapter(productAdapter);
        return view;
    }

    //清空购物车
    public void clearCart(){
        selectedList.clear();
        list2.clear();
        if (list.size() > 0) {
            for (int j=0;j<list.size();j++){
                list.get(j).setCount(0);
                for(int i=0;i<list.get(j).getList().size();i++){
                    list.get(j).getList().get(i).setSellcount(0);
                }
            }
            list2.addAll(list.get(0).getList());
            typeAdapter.setSelection(0);
            //刷新不能删
            typeAdapter.notifyDataSetChanged();
            dishsAdapter.notifyDataSetChanged();
        }
        update(true);
    }


    //根据商品id获取当前商品的采购数量
    public int getSelectedItemCountById(int id){
        Dish temp = selectedList.get(id);
        if(temp==null){
            return 0;
        }
        return temp.getSellcount();
    }


    public void handlerCarNum(int type, Dish goodsBean, boolean refreshGoodList){
        if (type == 0) {
            Dish temp = selectedList.get(goodsBean.getDid());
            if(temp!=null){
                if(temp.getSellcount()<2){
                    goodsBean.setSellcount(0);
                    selectedList.remove(goodsBean.getDid());

                }else{
                    int i =  goodsBean.getSellcount();
                    goodsBean.setSellcount(--i);
                }
            }



        } else if (type == 1) {
            Dish temp = selectedList.get(goodsBean.getDid());
            if(temp==null){
                goodsBean.setSellcount(1);
                selectedList.append(goodsBean.getDid(), goodsBean);
            }else{
                int i= goodsBean.getSellcount();
                goodsBean.setSellcount(++i);
            }
        }

        update(refreshGoodList);

    }



    //刷新布局 总价、购买数量等
    private void update(boolean refreshGoodList){
        int size = selectedList.size();
        int count =0;
        for(int i=0;i<size;i++){
            Dish item = selectedList.valueAt(i);
            count += item.getSellcount();
            totleMoney += item.getSellcount()*item.getPrice();
        }
        tv_totle_money.setText("￥"+String.valueOf(df.format(totleMoney)));
        totleMoney = 0.00;
        if(count<1){
            bv_unm.setVisibility(View.GONE);
        }else{
            bv_unm.setVisibility(View.VISIBLE);
        }

        bv_unm.setText(String.valueOf(count));

        if(productAdapter!=null){
            productAdapter.notifyDataSetChanged();
        }

        if(dishsAdapter!=null){
            dishsAdapter.notifyDataSetChanged();
        }

        if(typeAdapter!=null){
            typeAdapter.notifyDataSetChanged();
        }

        if(bottomSheetLayout.isSheetShowing() && selectedList.size()<1){
            bottomSheetLayout.dismissSheet();
        }
    }


    /**
     * @Description: 创建动画层
     * @param
     * @return void
     * @throws
     */
    private ViewGroup createAnimLayout() {
        ViewGroup rootView = (ViewGroup) this.getWindow().getDecorView();
        LinearLayout animLayout = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setId(Integer.MAX_VALUE-1);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;
    }

    private View addViewToAnimLayout(final ViewGroup parent, final View view,
                                     int[] location) {
        int x = location[0];
        int y = location[1];
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setLayoutParams(lp);
        return view;
    }

    public void setAnim(final View v, int[] startLocation) {
        anim_mask_layout = null;
        anim_mask_layout = createAnimLayout();
        anim_mask_layout.addView(v);//把动画小球添加到动画层
        final View view = addViewToAnimLayout(anim_mask_layout, v, startLocation);
        int[] endLocation = new int[2];// 存储动画结束位置的X、Y坐标
        tv_car.getLocationInWindow(endLocation);
        // 计算位移
        int endX = 0 - startLocation[0] + 40;// 动画位移的X坐标
        int endY = endLocation[1] - startLocation[1];// 动画位移的y坐标

        TranslateAnimation translateAnimationX = new TranslateAnimation(0,endX, 0, 0);
        translateAnimationX.setInterpolator(new LinearInterpolator());
        translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);

        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0, 0, endY);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationY.setFillAfter(true);

        AnimationSet set = new AnimationSet(false);
        set.setFillAfter(false);
        set.addAnimation(translateAnimationY);
        set.addAnimation(translateAnimationX);
        set.setDuration(800);// 动画的执行时间
        view.startAnimation(set);
        // 动画监听事件
        set.setAnimationListener(new Animation.AnimationListener() {
            // 动画的开始
            @Override
            public void onAnimationStart(Animation animation) {
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            // 动画的结束
            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.GONE);
            }
        });

    }
}
