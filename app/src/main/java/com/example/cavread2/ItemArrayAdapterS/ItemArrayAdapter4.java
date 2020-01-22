package com.example.cavread2.ItemArrayAdapterS;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.cavread2.ListWordsActivity;
import com.example.cavread2.R;

import java.util.ArrayList;
import java.util.List;

public class ItemArrayAdapter4 extends ArrayAdapter {

    private List scoreList = new ArrayList();

    public static final String LOG_TAG = ListWordsActivity.class.getSimpleName();

    static class ItemViewHolder {
        TextView engWord;
        TextView translate;
        TextView rusWord;
        TextView lesson;
    }

    public ItemArrayAdapter4(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    //@Override
    public void add(String[] object) {

        String words ="";
        //Log.i(" ItemArrayAdapter4  "," add метод ");
        //Log.i(" ItemArrayAdapter4  ","object  "+object);
        //Log.i(" ItemArrayAdapter4  ","length  "+Integer.toString(object.length));

        for (int i=0;i!=object.length;i++) {words=words +" -> "+ object[i];};
       // Log.i(" ItemArrayAdapter4  ",words);

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
       // Log.i(" ItemArrayAdapter4  "," getView метод "+Integer.toString(position));
       // Log.i(LOG_TAG, String.valueOf(R.id.name));

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            row = inflater.inflate(R.layout.item_layout4, parent, false);

            viewHolder = new ItemViewHolder();
            viewHolder.engWord    = (TextView) row.findViewById(R.id.engWord_il4);
            viewHolder.translate  = (TextView) row.findViewById(R.id.translate_il4);
            viewHolder.rusWord    = (TextView) row.findViewById(R.id.rusWord_il4);
            viewHolder.lesson     = (TextView) row.findViewById(R.id.lesson_il4);

            row.setTag(viewHolder);
        } else {
            viewHolder = (ItemViewHolder)row.getTag();
        }
        String[] stat = getItem(position);


        viewHolder.engWord.setText(stat[0]);
        viewHolder.translate.setText(stat[1]);
        viewHolder.rusWord.setText(stat[2]);
        viewHolder.lesson.setText(stat[3]);


        /*try {
            if (stat[1] != null){ viewHolder.translate.setText(stat[1]);}
            if (stat.length>1){ viewHolder.rusWord.setText(stat[2]);}
            if (stat.length>2){ viewHolder.lesson.setText(stat[3]);
            }
        }
        catch ( Exception e){
        //    Log.i(" ItemArrayAdapter  ",stat[0]+"  "+stat[1]);
        }*/

        return row;
    }
}