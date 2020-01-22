package com.example.cavread2;

import android.app.job.JobInfo;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cavread2.ItemArrayAdapterS.ItemArrayAdapter;
import com.example.cavread2.data.LessonSQL;
import com.example.cavread2.data.StudyWords;

import java.lang.reflect.Array;


public class ListLessonActivity extends AppCompatActivity {

    public static final String LOG_TAG = ListLessonActivity.class.getSimpleName();

    // Создадим адаптер для отображения списка
    private ItemArrayAdapter itemArrayAdapter;
    //ArrayAdapter<String> mMonthAdapter, mWeekOfDayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);

        itemArrayAdapter = new ItemArrayAdapter(getApplicationContext(), R.layout.item_layout);

        // получаем выборку из SQL
        LessonSQL mLessonSQL= new LessonSQL(this);
        Cursor cursor = mLessonSQL.databaseQuery();

        try {

            int idColumnIndex = cursor.getColumnIndex(StudyWords.LessonEntry._ID);
            int LessonColumnIndex = cursor.getColumnIndex(StudyWords.LessonEntry.COLUMN_LESSON);
            //Проходим по рядам
            while (cursor.moveToNext()) {
                // используем индексы для получения значений как строк
                String currentIdEntry = Integer.toString( cursor.getInt(idColumnIndex));
                String currentLessonEntry = cursor.getString(LessonColumnIndex );

                itemArrayAdapter.add(getStringArray(currentIdEntry,currentLessonEntry,""));
            }
        }finally {

            //Log.i(LOG_TAG," --- intLengthSQLite  "+Integer.toString(cursor.getCount()));
            // закрываем курсор после чтения
            cursor.close();
        }

        ListView listView = (ListView) findViewById(R.id.listView);

        Parcelable state = listView.onSaveInstanceState();

        listView.setAdapter(itemArrayAdapter);
        listView.setOnItemClickListener(myOnItemClickListener);
        listView.onRestoreInstanceState(state);

        /* for( String[] scoreData : scoreList ) {
           itemArrayAdapter.add(scoreData);
       }
       Это "сокращённый" вариант синтаксиса цикла перебора for - each.
       Из списка-коллекции strings на каждом шаге-итерации берется очередной элемент этого списка
       и присваивается переменной s типа String (эта переменная живет только внутри цикла).
       Ну, а внутри тела цикла, по-моему, всё понятно.
       */


        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                finish();
            }
        });
    }

    AdapterView.OnItemClickListener myOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {

            Toast.makeText(getApplicationContext(),
                    "Вы выбрали " + (position + 1) + " элемент", Toast.LENGTH_SHORT).show();

            String[] elemitemArrayAdapter;

            elemitemArrayAdapter = itemArrayAdapter.getItem(position);

            Intent intent = new Intent(ListLessonActivity.this,OneLessonActivity.class);

            intent.putExtra("idLesson",Integer.parseInt(elemitemArrayAdapter[0]));
            intent.putExtra("nameLesson",elemitemArrayAdapter[1]);

            startActivity(intent);


        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_lesson_menu,menu);
        return true;
        //super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Toast toast;
        int id = item.getItemId();

        Log.i(" onOptionsItemSelected ",Integer.toString(id));
        Intent intent;

        switch (id){

            case R.id.settingAll:

                toast = Toast.makeText(getApplicationContext(), "Переход на создание элемента", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                // переходим на новую активность настроек
                intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                return true;

            case R.id.newLesson:

                toast = Toast.makeText(getApplicationContext(), "Переход на создание элемента", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                // переходим на новую активность в которой отображается один элемент Lesson
                intent = new Intent(this, OneLessonActivity.class);
                startActivity(intent);
                return true;

            case R.id.previousLayout:

                toast = Toast.makeText(getApplicationContext(), "Выход", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                // переходим на новую активность в которой отображается список
                intent = new Intent(this, BattonSQLActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public String[] getStringArray(String el1,String el2,String el3) {
        // без этого изврата не работает
        String ki[] = new String[3];
        ki[0]=el1;
        ki[1]=el2;
        ki[2]=el3;
        return ki;
    }
}
