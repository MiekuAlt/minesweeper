package com.maltair.minesweeper;

import android.content.Context;
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
        return view;
    }
}
