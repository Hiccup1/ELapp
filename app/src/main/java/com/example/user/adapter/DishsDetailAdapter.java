package com.example.user.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.model.Dish;
import com.example.user.waimai.R;
import com.example.user.waimai.ShopCarActivity;

import java.util.List;

/**
 * Created by user on 2017/12/13.
 */
public class DishsDetailAdapter extends BaseAdapter {
    private List<Dish> list;
    private Activity activity;
    public DishsDetailAdapter(ShopCarActivity activity, List<Dish> list2) {
        this.activity=activity;
        this.list=list2;
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
            convertView= LayoutInflater.from(activity).inflate(R.layout.activity_dishs_detail_item,null);
            viewholder=new Viewholder();
            viewholder.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
            viewholder.tv_price= (TextView) convertView.findViewById(R.id.tv_price);

            convertView.setTag(viewholder);
        }else {
            viewholder = (Viewholder) convertView.getTag();

        }
        viewholder.tv_name.setText(list.get(position).getPrice()+"*"+list.get(position).getSellcount());
        viewholder.tv_price.setText("￥"+list.get(position).getPrice() * list.get(position).getSellcount());
        return convertView;
    }
    class Viewholder{
        TextView tv_name,tv_price;

    }

}