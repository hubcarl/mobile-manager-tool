package com.blue.sky.common.adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.blue.sky.common.entity.Item;
import com.blue.sky.common.utils.Strings;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.doc.model.FileInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SimpleCommonListAdapter extends BaseAdapter {

    private Context context;
    private List<Item> list;
    private int type;

    public SimpleCommonListAdapter(Context context, List<Item> list, int type) {
        super();
        this.context = context;
        this.list = list;
        this.type = type;
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
    public View getView(final int position, View rootView, ViewGroup parent) {
        ViewHolder holder;

        if (rootView == null) {
            holder = new ViewHolder();
            // txtDescription.setText(Html.fromHtml(description));
            rootView = View.inflate(context, R.layout.sky_simple_common_list_item, null);
            holder.one = (TextView) rootView.findViewById(R.id.item_one);
            holder.two = (TextView) rootView.findViewById(R.id.item_two);
            holder.three = (TextView) rootView.findViewById(R.id.item_three);
            holder.four = (TextView) rootView.findViewById(R.id.item_four);
            holder.five = (TextView) rootView.findViewById(R.id.item_five);
            rootView.setTag(holder);
        } else {
            holder = (ViewHolder) rootView.getTag();
        }

        final  Item  item= list.get(position);

        holder.one.setText(item.getItemOne());
        holder.two.setText(item.getItemTwo());
        holder.three.setText(Html.fromHtml(item.getItemThree()));
        holder.four.setText(item.getItemFour());
        if(Strings.isEmpty(item.getItemFive())){
            holder.five.setVisibility(View.GONE);
        }else{
            holder.five.setText(item.getItemFive());
        }

        return rootView;
    }


    class ViewHolder {
        TextView one;
        TextView two;
        TextView three;
        TextView four;
        TextView five;
    }

}