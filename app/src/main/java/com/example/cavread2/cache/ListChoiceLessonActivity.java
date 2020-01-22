package com.example.cavread2.cache;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.cavread2.ItemArrayAdapterS.ItemArrayAdapter1;
import com.example.cavread2.R;
import com.example.cavread2.data.LessonSQL;
import com.example.cavread2.data.StudyWords;

public class ListChoiceLessonActivity extends AppCompatActivity {

    public static final String LOG_TAG = ListChoiceLessonActivity.class.getSimpleName();

    // Создадим адаптер для отображения списка
    private ItemArrayAdapter1 itemArrayAdapter;

    // переменные возврата
    public final static String LESSONID= "com.example.andrey_vb.Test_settings.LESSONID";
    public final static String LESSONNAME = "com.example.andrey_vb.Test_settings.LESSONNAME";
   /* private test() {
        Resources res = getResources();
        sorts = res.getStringArray(R.array.sortWorc);
    }*/

   LessonSQL mLessonSQL= new LessonSQL(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        String workClass = getIntent().getExtras().getString("Class");;

        Log.i(LOG_TAG,"workClass = " +workClass );

        itemArrayAdapter = new ItemArrayAdapter1(getApplicationContext(), R.layout.item_layout);

        //ArrayList catNames = new ArrayList();
        //catNames.add(sort);

        // получаем выборку из SQL

        Cursor cursor = mLessonSQL.databaseQuery();

        try {

            int idColumnIndex = cursor.getColumnIndex(StudyWords.LessonEntry._ID);
            int LessonColumnIndex = cursor.getColumnIndex(StudyWords.LessonEntry.COLUMN_LESSON);
            //Проходим по рядам
            while (cursor.moveToNext()) {
                // используем индексы для получения значений как строк
                String currentIdEntry = Integer.toString( cursor.getInt(idColumnIndex));
                String currentLessonEntry = cursor.getString(LessonColumnIndex );
                String[] temp = {currentLessonEntry};
                itemArrayAdapter.add(temp);
            }
        }finally {

            //Log.i(LOG_TAG," --- intLengthSQLite  "+Integer.toString(cursor.getCount()));
            // закрываем курсор после чтения
            cursor.close();
        }


        // отработка нажатия
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(itemArrayAdapter);

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
            Resources res = getResources();

            Intent answerIntent = new Intent();

            String[] elemitemArrayAdapter;

            elemitemArrayAdapter = itemArrayAdapter.getItem(position);

            answerIntent.putExtra( LESSONID , String.valueOf(mLessonSQL.сheckAvailabilityLessonInID(elemitemArrayAdapter[0])));
            answerIntent.putExtra( LESSONNAME , elemitemArrayAdapter[0]  );
            //answerIntent.putExtra( LESSONNAME , sorts[position]  );

            Log.i("test_cl",String.valueOf(id)+" "+String.valueOf(position)+"   " +elemitemArrayAdapter[0]);

            setResult(RESULT_OK,answerIntent);

            finish();

        }
    };

}
