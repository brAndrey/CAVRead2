package com.example.cavread2.cache;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.cavread2.ItemArrayAdapterS.ItemArrayAdapter1;
import com.example.cavread2.R;

public class ListChoiceSortActivity extends AppCompatActivity {

    public static final String LOG_TAG = ListChoiceSortActivity.class.getSimpleName();

    private String[] sorts;

    // переменные возврата
    public final static String THIEF = "com.example.andrey_vb.Test_settings.THIEF";
    public final static String REZULT = "com.example.andrey_vb.Test_settings.REZULT";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        String workClass = getIntent().getExtras().getString("Class");;

        Log.i(LOG_TAG,"workClass  = " +workClass );

        Resources res = getResources();

        // уроки
        sorts = res.getStringArray(R.array.sortWorc);

        ItemArrayAdapter1 itemArrayAdapter1 = new ItemArrayAdapter1(getApplicationContext(),R.layout.item_layout);


        for (String sort : sorts) {
            Log.i("test", sort);
            String[] temp = {sort};
            itemArrayAdapter1.add(temp);
        }


        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(itemArrayAdapter1);

        // отработка нажатия
        listView.setOnItemClickListener(myOnItemClickListener);

        //Parcelable state = listView.onSaveInstanceState();
        //listView.onRestoreInstanceState(state);
    }


    // процедура отработки нажатия
    AdapterView.OnItemClickListener myOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            //   Toast.makeText(getApplicationContext(),"Вы выбрали " + (position + 1) + " элемент", Toast.LENGTH_SHORT).show();

            Intent answerIntent = new Intent();

            answerIntent.putExtra( REZULT , String.valueOf(position)  );

            setResult(RESULT_OK,answerIntent);

            finish();

        }
    };

}
