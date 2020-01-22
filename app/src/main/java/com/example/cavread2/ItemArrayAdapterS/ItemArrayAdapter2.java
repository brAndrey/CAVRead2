package com.example.cavread2.ItemArrayAdapterS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.cavread2.R;

import java.util.ArrayList;
import java.util.List;

public class ItemArrayAdapter2 extends ArrayAdapter {

        private List scoreList = new ArrayList();

        static class ItemViewHolder {
            TextView idPrint;
            TextView worldPrint;
        }

        public ItemArrayAdapter2(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        //@Override
        public void add(String[] object) {
            //   Log.i(" ItemArrayAdapter  "," add метод ");
            //   Log.i(" ItemArrayAdapter  ","object  "+object);
            //   Log.i(" ItemArrayAdapter  ","length  "+Integer.toString(object.length));

            scoreList.add(object);
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
            //    Log.i(" ItemArrayAdapter  "," getView метод "+Integer.toString(position));

            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.item_layout, parent, false);
                viewHolder = new ItemViewHolder();
                viewHolder.idPrint = (TextView) row.findViewById(R.id.idPrint);
                viewHolder.worldPrint = (TextView) row.findViewById(R.id.worldPrint);

                row.setTag(viewHolder);
            } else {
                viewHolder = (ItemViewHolder)row.getTag();
            }
            String[] stat = getItem(position);


            viewHolder.idPrint.setText(stat[0]);
            try {
                if (stat[1] != null) { viewHolder.worldPrint.setText(stat[1]);}
            }
            catch ( Exception e){
                //    Log.i(" ItemArrayAdapter  ",stat[0]+"  "+stat[1]);
            }

            //viewHolder.word.setText(stat[3]);

            return row;
        }
    }


