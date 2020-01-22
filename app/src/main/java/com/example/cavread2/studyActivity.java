package com.example.cavread2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cavread2.data.LessonSQL;
import com.example.cavread2.data.StudyWords;
import com.example.cavread2.data.WorldSQL;

public class studyActivity extends AppCompatActivity {

    public static final String LOG_TAG = studyActivity.class.getSimpleName();

    private Cursor cursor;

    private int corentPosition;

    private int corentPositionKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stydy);

        takeCusor();

        //Toast toast = Toast.makeText(getApplicationContext(), "Переход на изучение слов", Toast.LENGTH_SHORT);
        //toast.setGravity(Gravity.CENTER, 0, 0);
        //toast.show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //   Log.i(TAG, "onRestart");

        takeCusor();

    }

    // сохранение состояния
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putInt(String.valueOf(corentPosition), cursor.getPosition());

        super.onSaveInstanceState(outState);
    }

    // получение ранее сохраненного состояния
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        corentPosition = (int) savedInstanceState.getInt(String.valueOf(corentPosition));

        Log.i(LOG_TAG,"onRestoreInstanceState corentPosition " +corentPosition);

        //takeCusor();

    }

    public void moveToPosition(View view) {
        // переход на позицию указанную пользователем
        EditText editText =findViewById(R.id.editPostText);
        int pos = Integer.parseInt (editText.getText().toString());

        if (pos<cursor.getCount()) {
            cursor.moveToPosition(pos-1);
        }else {cursor.moveToLast();
        }

        stepToWind();
    }

    public void nextStep(View view) {

        movementCurcor(false,true );

    }

    public void previousStep(View view) {

        movementCurcor(true,false);
    }


    public void previousLayout(View view) {

        // переходим обратно на активность ListSQLActivity
        Intent intent = new Intent(studyActivity.this, BattonSQLActivity.class);
        startActivity(intent);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_study_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Toast toast;
        int id = item.getItemId();

        Log.i(" onOptionsItemSelected ", Integer.toString(id));
        Intent intent;

        switch (id) {

            case R.id.settingAll:

                // переходим на новую активность настроек
                intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    void takeCusor() {
// метод берёт данные из базы


        // всё для отображения текущего урока \/
        // это будет именем файла настроек
        final String APP_PREFERENCES = "mysettings";
        // сохраняемые параметры настроек
        final String APP_PREFERENCES_LESSON = "Lesson";


        // блок работы с сохранением настроек \/
        SharedPreferences mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        String lesson = "";

        if (mSettings.contains(APP_PREFERENCES_LESSON)) {
            //тут получили урок из настроек
            lesson = mSettings.getString(APP_PREFERENCES_LESSON, "");

            Log.i(LOG_TAG, "takeCusor lesson = " + lesson);

            if (lesson != "") {

                LessonSQL lessonSQL = new LessonSQL(this);
                lesson = lessonSQL.сheckAvailabilityIDInLesson(Integer.parseInt(lesson));
            }

        }

        TextView header = findViewById(R.id.header);

        header.setText("урок " + lesson);

        // всё для отображения текущего урока /\

        WorldSQL mWorldSQLite = new WorldSQL(this);

        // блок прерывания при незаполненности базы словами

        int intLengthSQLite = mWorldSQLite.CountLength();
        if (intLengthSQLite == 0) {
            return;
        }

        cursor = mWorldSQLite.databaseQuery();

        cursor.moveToFirst();
        //movementCurcor(false,false);
        stepToWind();

    }


    public void movementCurcor(boolean previous, boolean next) {
        Log.i(LOG_TAG, "cursor.getPosition()  " + cursor.getPosition());
        Log.i(LOG_TAG,""+previous+"  "+next);

        if (previous) {
            if (cursor.getPosition() != 0) {
                cursor.moveToPrevious();
            }
        }

        if (next) {
            if (cursor.getPosition() != cursor.getCount() - 1) {
                cursor.moveToNext();
            }
        }

        stepToWind();
    }

    public void stepToWind() {

        Log.i(LOG_TAG, "stepToWind() getPosition = " + cursor.getPosition());

        int idColumnIndex = cursor.getColumnIndex(StudyWords.WorldEntry._ID);
        int engWordsColumnIndex = cursor.getColumnIndex(StudyWords.WorldEntry.COLUMN_ENGWORLD);
        int transletionColumnIndex = cursor.getColumnIndex(StudyWords.WorldEntry.COLUMN_TRANSCRIPTION);
        int rusWordsColumnIndex = cursor.getColumnIndex(StudyWords.WorldEntry.COLUMN_RUSWORLD);
        int lessonColumnIndex = cursor.getColumnIndex(StudyWords.WorldEntry.COLUMN_LESSON);

        Log.i(LOG_TAG, "engWordsColumnIndex = " + String.valueOf(engWordsColumnIndex));
        // используем индексы для получения строки или числа
        //int currentID;
        String currentEngWorld = null;
        String currentTransletion = null;
        String currentRusWorld = null;
        String currentLessonWorld = null;

        try {
            // используем индексы для получения строки или числа
            //currentID = cursor.getInt(idColumnIndex);
            currentEngWorld = cursor.getString(engWordsColumnIndex);
            currentTransletion = cursor.getString(transletionColumnIndex);
            currentRusWorld = cursor.getString(rusWordsColumnIndex);
            currentLessonWorld = cursor.getString(lessonColumnIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }


        //  Log.i(LOG_TAG, currentEngWorld + " -> " + currentRusWorld + " " + Integer.toString(cursor.getPosition()) + " " + Integer.toString(corentPosition));


        TextView rusTexView = (TextView) findViewById(R.id.rusTexView);
        TextView transTexView = (TextView) findViewById(R.id.transTexView);
        TextView engTexView = (TextView) findViewById(R.id.engTexView);
        TextView positionWord = findViewById(R.id.positionWord);

        rusTexView.setText(currentRusWorld);
        transTexView.setText(currentTransletion);
        engTexView.setText(currentEngWorld);
        positionWord.setText("Слово " + Integer.toString(cursor.getPosition() + 1) + " из " + Integer.toString(cursor.getCount()));

    }



}
