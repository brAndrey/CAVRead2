package com.example.cavread2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
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
import com.example.cavread2.ItemArrayAdapterS.ItemArrayAdapter4;
import com.example.cavread2.data.LessonSQL;
import com.example.cavread2.data.StudyWords;
import com.example.cavread2.data.WorldSQL;


public class ListWordsActivity extends AppCompatActivity {

    public static final String LOG_TAG = ListWordsActivity.class.getSimpleName();

    private ItemArrayAdapter4 itemArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        fillListView();

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
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Toast.makeText(getApplicationContext(),
                    "Вы выбрали " + (position + 1) + " элемент", Toast.LENGTH_SHORT).show();

            String[] elemitemArrayAdapter;

            elemitemArrayAdapter = itemArrayAdapter.getItem(position);

            Intent intent = new Intent(ListWordsActivity.this,OneWordActivity.class);

            //intent.putExtra("idLesson",Integer.parseInt(elemitemArrayAdapter[0]));

            intent.putExtra("nameWord",elemitemArrayAdapter[0]);

            startActivity(intent);


        }
    };


        @Override
    protected void onRestart() {
        super.onRestart();
        //   Log.i(TAG, "onRestart");

        fillListView();

    }



    void fillListView() {

        itemArrayAdapter = new ItemArrayAdapter4(getApplicationContext(), R.layout.item_layout4);

        WorldSQL mWorldSQLite = new WorldSQL(this);
        LessonSQL mLessonSQL = new LessonSQL(this);

        Cursor cursor = mWorldSQLite.databaseQuery();


        try {

            int idColumnIndex = cursor.getColumnIndex(StudyWords.WorldEntry._ID);
            int engWordsColumnIndex = cursor.getColumnIndex(StudyWords.WorldEntry.COLUMN_ENGWORLD);
            int transletionColumnIndex = cursor.getColumnIndex(StudyWords.WorldEntry.COLUMN_TRANSCRIPTION);
            int rusWordsColumnIndex = cursor.getColumnIndex(StudyWords.WorldEntry.COLUMN_RUSWORLD);
            //int lessonColumnIndex = cursor.getColumnIndex(StudyWords.WorldEntry.COLUMN_LESSON);
            int courseColumnIndex = cursor.getColumnIndex(StudyWords.WorldEntry.COLUMN_COURSE);

            //Проходим по рядам
            while (cursor.moveToNext()) {
                // используем индексы для получения строки или числа
                int currentID = cursor.getInt(idColumnIndex);
                String currentEngWorld = cursor.getString(engWordsColumnIndex);
                String currentTransletion = cursor.getString(transletionColumnIndex);
                String currentRusWorld = cursor.getString(rusWordsColumnIndex);
                //String currentLessonWorld = cursor.getString(lessonColumnIndex);
                int currentCourseWorld = cursor.getInt(courseColumnIndex);
                //String currentCourseWorld = Integer.toString(cursor.getInt(courseColumnIndex));

                Log.i(LOG_TAG," fillListView  ID = "+ String.valueOf(currentID));
                itemArrayAdapter.add(getStringArray(currentEngWorld, currentTransletion, currentRusWorld, mLessonSQL.сheckAvailabilityIDInLesson(currentCourseWorld)));

            }
        } finally {

            Log.i(LOG_TAG, " --- intLengthSQLite  " + cursor.getCount());

            // закрываем курсор после чтения

        }

        ListView listView = findViewById(R.id.listView);

        Parcelable state = listView.onSaveInstanceState();

        listView.setAdapter(itemArrayAdapter);
        listView.setOnItemClickListener(myOnItemClickListener);

        listView.onRestoreInstanceState(state);

        cursor.close();

        //mInfoTextView.setText("Все коты: " + catName);

        /* for( String[] scoreData : scoreList ) {
           itemArrayAdapter.add(scoreData);
       }
       Это "сокращённый" вариант синтаксиса цикла перебора for - each.
       Из списка-коллекции strings на каждом шаге-итерации берется очередной элемент этого списка
       и присваивается переменной s типа String (эта переменная живет только внутри цикла).
       Ну, а внутри тела цикла, по-моему, всё понятно.
       */

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_word_menu, menu);
        return true;
        //super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Toast toast;
        Intent intent;

        int id = item.getItemId();

        Log.i(" onOptionsItemSelected ", Integer.toString(id));

        switch (id) {

            case R.id.settingAll:

                //toast = Toast.makeText(getApplicationContext(), "Переход на создание элемента", Toast.LENGTH_SHORT);
               // toast.setGravity(Gravity.CENTER, 0, 0);
               // toast.show();

                // переходим на новую активность настроек
                intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                return true;


            case R.id.newWord:

               // toast = Toast.makeText(getApplicationContext(), "Переход на создание элемента", Toast.LENGTH_SHORT);
               // toast.setGravity(Gravity.CENTER, 0, 0);
               // toast.show();

                // переходим на новую активность в которой отображается список
                intent = new Intent(this, OneWordActivity.class);
                startActivity(intent);
                return true;

            case R.id.downloadCSV:

              //  toast = Toast.makeText(getApplicationContext(), "Процедура заполнения списка SQL", Toast.LENGTH_LONG);
              //  toast.setGravity(Gravity.CENTER, 0, 0);
              //  toast.show();


                // переходим на новую активность в которой загружаются слова из выбраннного CSV файла
                //intent = new Intent(this, FilePickerActivity.class);
                intent = new Intent(this, activity_words_from_CSV.class);
                startActivity(intent);

                // перезапускаем активность
                //RestartActivity();

                return true;

            case R.id.downloadBase:

                toast = Toast.makeText(getApplicationContext(), "Процедура заполнения списка базовыми словами", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                WorldSQL mWorldSQL = new WorldSQL(this);
                mWorldSQL.fillSQL();

                // перезапускаем активность
                RestartActivity();
                return true;


            case R.id.clearBase:
              //  toast = Toast.makeText(getApplicationContext(), "Процедура очистки таблици SQL", Toast.LENGTH_LONG);
              //  toast.setGravity(Gravity.CENTER, 0, 0);
              //  toast.show();

                WorldSQL worldSQL = new WorldSQL(this);
                worldSQL.ClearWorldSQL();

                LessonSQL lessonSQL = new LessonSQL(this);
                lessonSQL.ClearLessonSQL();

                // перезапускаем активность
                RestartActivity();
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

    public String[] getStringArray(String el1, String el2, String el3, String el4) {
        // без этого изврата не работает
        String[] ki = new String[4];
        ki[0] = el1;
        ki[1] = el2;
        ki[2] = el3;
        ki[3] = el4;
        return ki;
    }

    private void RestartActivity() {
        // перезапускаем активность
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}

