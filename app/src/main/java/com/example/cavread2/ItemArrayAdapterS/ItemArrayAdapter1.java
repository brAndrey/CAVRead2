package com.example.cavread2.ItemArrayAdapterS;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.cavread2.R;

import java.util.ArrayList;
import java.util.List;

public class ItemArrayAdapter1 extends ArrayAdapter {

    private List<Object> scoreList = new ArrayList<>();

    static class ItemViewHolder {
        TextView name;
    }

        public ItemArrayAdapter1(Context context, int resource) {
        super(context, resource);
        TextView elementItem;
    }

    @Override
    public void add( Object object) {
        scoreList.add(object);
        Log.i("ItemArrayAdapter1 ", "object  "+String.valueOf(object));
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.scoreList.size();
    }

    @Override
    public String[] getItem(int index) {
        //  Log.i("getItem", Integer.toString(index));
        return (String[]) this.scoreList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ItemViewHolder viewHolder;

        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        row = inflater.inflate(R.layout.item_layout1,parent,false);

                //R.layout.item_layout, parent, false);
        viewHolder = new ItemViewHolder();
        viewHolder.name = (TextView) row.findViewById(R.id.elementItem);

        String[] temp= getItem(position);
        String element =temp[0];

        Log.i("ItemArrayAdapter1 ","element  "+element);
        viewHolder.name.setText(element);
        row.setTag(viewHolder);

        return row;
    }
}
