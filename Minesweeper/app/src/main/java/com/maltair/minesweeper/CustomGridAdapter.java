package com.maltair.minesweeper;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Michael on 10/17/2017.
 */

public class CustomGridAdapter extends BaseAdapter {

    private Context context;
    private String[] items;
    LayoutInflater inflater;

    public CustomGridAdapter(Context context, String[] items) {
        this.context = context;
        this.items = items;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            view = inflater.inflate(R.layout.cell, null);
        }
        TextView tv = view.findViewById(R.id.textview);
        tv.setText(items[i]);
        if(items[i] != null && items[i].equals(" ")) {
            tv.setBackgroundColor(Color.parseColor("#81CC47"));
        } else if(items[i] != null && items[i].equals("*")) {
            tv.setBackgroundColor(Color.parseColor("#FF4081"));
        } else if(items[i] != null && items[i].equals("F")) {
            tv.setBackgroundColor(Color.parseColor("#6C5CE8"));
        } else if(items[i] != null && items[i].equals("-1")) {
            tv.setBackgroundColor(Color.parseColor("#FF4081"));
            tv.setText("");
        } else if(items[i] != null && !items[i].equals(" ")) {
            tv.setBackgroundColor(Color.parseColor("#BEC9FF"));
        }
        return view;
    }

}
