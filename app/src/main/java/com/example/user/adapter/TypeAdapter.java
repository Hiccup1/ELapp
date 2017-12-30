package com.example.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.model.Catogray;
import com.example.user.model.Types;
import com.example.user.waimai.*;

import java.util.List;

/**
 * Created by user on 2017/12/13.
 */
public class TypeAdapter extends BaseAdapter {
    private Context context;
    private List<Catogray> list;
    int selection = 0;
    public TypeAdapter(Context context, List<Catogray> list) {
        this.context = context;
        this.list = list;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final Viewholder viewholder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.shopcar_left_listview, null);
            viewholder = new Viewholder();
            viewholder.tv_catogray = (TextView) view.findViewById(R.id.tv_catogray);
            viewholder.tv_count = (TextView) view.findViewById(R.id.tv_count);
            view.setTag(viewholder);
        } else {
            viewholder = (Viewholder) view.getTag();
        }

        viewholder.tv_catogray.setText(list.get(position).getKind());
        int count = 0;
        for (int i = 0; i < list.get(position).getList().size(); i++) {
            count += list.get(position).getList().get(i).getSellcount();
        }
        list.get(position).setCount(count);

        if (count <= 0) {
            viewholder.tv_count.setVisibility(View.INVISIBLE);
        } else {

            viewholder.tv_count.setVisibility(View.VISIBLE);
            viewholder.tv_count.setText(list.get(position).getCount()+"");
        }

        if (position == selection) {
            viewholder.tv_catogray.setBackgroundResource(R.drawable.rec_red_left_stroke);
            viewholder.tv_catogray.setTextColor(context.getResources().getColor(R.color.black));
        } else {

            viewholder.tv_catogray.setBackgroundResource(R.drawable.empty);
            viewholder.tv_catogray.setTextColor(context.getResources().getColor(R.color.gray));
        }
        return view;
    }

    public void setSelection(int selection) {
        this.selection = selection;
    }

    class Viewholder {
        TextView tv_catogray;
        TextView tv_count;
    }

}