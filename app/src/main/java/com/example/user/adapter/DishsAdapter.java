package com.example.user.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.model.Dish;
import com.example.user.util.Data;
import com.example.user.waimai.*;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by user on 2017/12/13.
 */
public class DishsAdapter extends BaseAdapter {

    private List<Dish> list;
    private Context context;
    private TypeAdapter typeAdapter;

    public DishsAdapter(Context context, List<Dish> list2, TypeAdapter catograyAdapter) {
        this.context=context;
        this.list=list2;
        this.typeAdapter=catograyAdapter;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Viewholder viewholder;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.shopcar_right_listview,null);
            viewholder=new Viewholder();
            viewholder.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
            viewholder.tv_original_price= (TextView) convertView.findViewById(R.id.tv_original_price);
            viewholder.tv_price= (TextView) convertView.findViewById(R.id.tv_price);
            viewholder.iv_add= (ImageView) convertView.findViewById(R.id.iv_add);
            viewholder.iv_remove= (ImageView) convertView.findViewById(R.id.iv_remove);
            viewholder.tv_acount= (TextView) convertView.findViewById(R.id.tv_acount);
            viewholder.iv_pic= (ImageView) convertView.findViewById(R.id.iv_pic);
            viewholder.rl_item= (RelativeLayout) convertView.findViewById(R.id.rl_item);
            convertView.setTag(viewholder);
        }else {
            viewholder = (Viewholder) convertView.getTag();

        }
        viewholder.tv_name.setText(list.get(position).getName());
        double price = list.get(position).getPrice() + 10;
        viewholder.tv_original_price.setText("￥"+ price);
        viewholder.tv_original_price.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
        viewholder.tv_price.setText("￥"+list.get(position).getPrice());


        if(list.get(position)!=null){
            //默认进来数量
            if (list.get(position).getSellcount()<1){
                viewholder.tv_acount.setVisibility(View.INVISIBLE);
                viewholder.iv_remove.setVisibility(View.INVISIBLE);
                typeAdapter.notifyDataSetChanged();
            }else{
                viewholder.tv_acount.setVisibility(View.VISIBLE);
                viewholder.iv_remove.setVisibility(View.VISIBLE);
                viewholder.tv_acount.setText(String.valueOf(list.get(position).getSellcount()));
                typeAdapter.notifyDataSetChanged();
            }
        }else{
            viewholder.tv_acount.setVisibility(View.INVISIBLE);
            viewholder.iv_remove.setVisibility(View.INVISIBLE);
        }

        //商品图片
        if(list.get(position).getImg()!=null){
            ImageLoader.getInstance().displayImage(list.get(position).getImg(), viewholder.iv_pic);
        }


        viewholder.iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = ((ShopCarActivity)context).getSelectedItemCountById(list.get(position).getDid());
                Log.i("fyg","iv_add"+String.valueOf(count));
                if (count < 1) {
                    viewholder.iv_remove.setAnimation(getShowAnimation());
                    viewholder.iv_remove.setVisibility(View.VISIBLE);
                    viewholder.tv_acount.setVisibility(View.VISIBLE);
                }

                ((ShopCarActivity)context).handlerCarNum(1,list.get(position),true);
                typeAdapter.notifyDataSetChanged();

                int[] loc = new int[2];
                viewholder.iv_add.getLocationInWindow(loc);
                for (int i=0;i<loc.length;i++)
                {
                    Log.i("fyg",String.valueOf(loc[i]));
                }
                int[] startLocation = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
                v.getLocationInWindow(startLocation);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
                ImageView ball = new ImageView(context);
                ball.setImageResource(R.drawable.number);
                ((ShopCarActivity)context).setAnim(ball, startLocation);// 开始执行动画

            }
        });

        viewholder.iv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = ((ShopCarActivity)context).getSelectedItemCountById(list.get(position).getDid());
                Log.i("fyg","iv_remove"+String.valueOf(count));
                if (count < 2) {
                    viewholder.iv_remove.setAnimation(getHiddenAnimation());
                    viewholder.iv_remove.setVisibility(View.GONE);
                    viewholder.tv_acount.setVisibility(View.GONE);
                }
                ((ShopCarActivity)context).handlerCarNum(0,list.get(position),true);
                typeAdapter.notifyDataSetChanged();

            }
        });

        viewholder.rl_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(list.get(position).getPackage_product_info()!=null&&list.get(position).getTitle()!=null){
//                    catograyAdapter.notifyDataSetChanged();
//                  ((MainActivity)context).showDetailSheet(list.get(position).getPackage_product_info(),list.get(position).getTitle());
//                }else{
////                    Toast.makeText(context, "没有详情!", Toast.LENGTH_SHORT).show();
//                }
            }
        });
        return convertView;
    }
    class Viewholder{
        TextView tv_name,tv_original_price,tv_price;
        ImageView iv_add,iv_remove,iv_pic;
        TextView tv_acount;
        RelativeLayout rl_item;
    }



    //显示减号的动画
    private Animation getShowAnimation(){
        AnimationSet set = new AnimationSet(true);
        RotateAnimation rotate = new RotateAnimation(0,720,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        set.addAnimation(rotate);
        TranslateAnimation translate = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF,2f
                ,TranslateAnimation.RELATIVE_TO_SELF,0
                ,TranslateAnimation.RELATIVE_TO_SELF,0
                ,TranslateAnimation.RELATIVE_TO_SELF,0);
        set.addAnimation(translate);
        AlphaAnimation alpha = new AlphaAnimation(0,1);
        set.addAnimation(alpha);
        set.setDuration(500);
        return set;
    }
    //隐藏减号的动画
    private Animation getHiddenAnimation(){
        AnimationSet set = new AnimationSet(true);
        RotateAnimation rotate = new RotateAnimation(0,720,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        set.addAnimation(rotate);
        TranslateAnimation translate = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF,0
                ,TranslateAnimation.RELATIVE_TO_SELF,2f
                ,TranslateAnimation.RELATIVE_TO_SELF,0
                ,TranslateAnimation.RELATIVE_TO_SELF,0);
        set.addAnimation(translate);
        AlphaAnimation alpha = new AlphaAnimation(1,0);
        set.addAnimation(alpha);
        set.setDuration(500);
        return set;
    }
}
