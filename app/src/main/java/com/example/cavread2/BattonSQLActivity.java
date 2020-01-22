package com.example.cavread2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.example.cavread2.data.LessonSQL;
import com.example.cavread2.data.StudyWords;
import com.example.cavread2.data.WorldSQL;

public class BattonSQLActivity extends Activity {
    public static final String LOG_TAG = BattonSQLActivity.class.getSimpleName();

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_KEY_FIRST_START = "spKeyFirstStart";
    // сохраняемые параметры настроек
    public static final String APP_PREFERENCES_SORT = "Sort";
    public static final String APP_PREFERENCES_SURVEY = "Survey";

    //boolean firstStart= true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sql_list);

        // блок первого входа в систему
        SharedPreferences mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        //if (mSettings.contains(APP_KEY_FIRST_START)) {
            //Log.i(LOG_TAG,"In contains (APP_KEY_FIRST_START) ");

        boolean firstStart = mSettings.getBoolean(APP_KEY_FIRST_START, true);

        if(firstStart) {
            SharedPreferences.Editor editor = mSettings.edit();
            Log.i(LOG_TAG," if(firstStart) { ");

            editor.putBoolean(APP_KEY_FIRST_START, false);
            editor.putString(APP_PREFERENCES_SURVEY, "0");
            editor.putString(APP_PREFERENCES_SORT, "0");
            editor.apply();

            // заполняем базу слов из файла по умолчанию
            WorldSQL mWorldSQL = new WorldSQL(this);
            mWorldSQL.fillSQL();

            //тут запускай код для первого старта
        } else {

            Log.i(LOG_TAG, "APP_KEY_FIRST_START "+String.valueOf(mSettings.getBoolean(APP_KEY_FIRST_START, Boolean.parseBoolean(""))));
            Log.i(LOG_TAG,"APP_PREFERENCES_SORT "+mSettings.getString(APP_PREFERENCES_SORT, ""));
            Log.i(LOG_TAG,"APP_PREFERENCES_SURVEY "+mSettings.getString(APP_PREFERENCES_SURVEY, ""));
            //тут уже не первый старт
        }


    }

    public void ListSQL(View view) {
        // переходим на новую активность в которой отображается список
        Intent intent = new Intent(BattonSQLActivity.this, ListWordsActivity.class);
        startActivity(intent);

    }

    public void ListLessonSQL(View view) {
        // переходим на новую активность в которой отображается список
        Intent intent = new Intent(BattonSQLActivity.this, ListLessonActivity.class);
        startActivity(intent);

    }

    public void WorkDateTransition(View view) {
        Intent intent = new Intent(BattonSQLActivity.this,WorkDateActivity.class);
        startActivity(intent);

    }

    // переход на Activity изучения
    public void studyWorlds(View view) {
        WorldSQL LengthSQLite = new WorldSQL(this);
        int intLengthSQLite = LengthSQLite.CountLength();

        if (intLengthSQLite != 0) {
            // переходим на новую активность в которой отображается меню изучения слов
            Intent intent = new Intent(BattonSQLActivity.this, studyActivity.class);
            startActivity(intent);

        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "База слов пуста", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    // переход на Activity проверки
    public void checkWorlds(View view) {

        WorldSQL LengthSQLite = new WorldSQL(this);
        int intLengthSQLite = LengthSQLite.CountLength();

        if (intLengthSQLite != 0) {
            // переходим на новую активность в которой отображается меню изучения слов
            Intent intent = new Intent(BattonSQLActivity.this, checkActivity.class);
            startActivity(intent);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "База слов пуста", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    //Печать базы в Log
    public void fillSQL(View view) {
       // Toast toast = Toast.makeText(getApplicationContext(), "Процедура заполнения списка SQL", Toast.LENGTH_LONG);
       // toast.setGravity(Gravity.CENTER, 0, 0);
       // toast.show();

        WorldSQL mWorldSQL = new WorldSQL(this);

        // получаем выборку из SQL для печати
        LessonSQL mLessonSQL = new LessonSQL(this);
        Cursor cursor = mLessonSQL.databaseQuery();
        try {

            int idColumnIndex = cursor.getColumnIndex(StudyWords.LessonEntry._ID);
            int LessonColumnIndex = cursor.getColumnIndex(StudyWords.LessonEntry.COLUMN_LESSON);
            //Проходим по рядам
            while (cursor.moveToNext()) {
                // используем индексы для получения значений как строк
                int currentIdEntry = cursor.getInt(idColumnIndex);
                String currentLessonEntry = cursor.getString(LessonColumnIndex);


                Log.i("fillSQL", "    " + currentLessonEntry + "   " + String.valueOf(currentIdEntry));
                mWorldSQL.WorldInLogLesson(currentLessonEntry, currentIdEntry);

                //  itemArrayAdapter.add(getStringArray(currentIdEntry,currentLessonEntry,""));
            }
        } finally {

            //Log.i(LOG_TAG," --- intLengthSQLite  "+Integer.toString(cursor.getCount()));
            // закрываем курсор после чтения
            cursor.close();
        }


        //WorldSQL mWorldSQL = new WorldSQL(this);
        //mWorldSQL.fillSQL();

    }


    public void SettingBase(View view) {

        Intent intent = new Intent(BattonSQLActivity.this, SettingActivity.class);
        startActivity(intent);
    }
}
