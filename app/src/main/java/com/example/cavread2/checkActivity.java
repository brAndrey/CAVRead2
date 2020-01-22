package com.example.cavread2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.database.Cursor;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cavread2.data.LessonSQL;
import com.example.cavread2.data.StudyWords;
import com.example.cavread2.data.WorldSQL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class checkActivity extends AppCompatActivity {

    public static final String LOG_TAG = checkActivity.class.getSimpleName();

    // это будет именем файла настроек
    public static final String APP_PREFERENCES = "mysettings";
    // сохраняемые параметры настроек
    public static final String APP_PREFERENCES_SURVEY = "Survey";

    private Cursor cursor;

    private TextView TexViewUp;

    private EditText editTextDown;


    private boolean surveyTipe;
    // if enter english word - true
    // if enter russian word - false

    /*<item>вводить английские слова</item>
        <item>enter russian word</item>*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        // берем слова с экрана
        TexViewUp = findViewById(R.id.textViewUp);
        editTextDown = findViewById(R.id.editTextDown_Check);
        String[] tipeSurveys;

        // блок работы с сохранением настроек \/
        final SharedPreferences[] mSettings = {getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)};

        if (mSettings[0].contains(APP_PREFERENCES_SURVEY)) {

            Resources res = getResources();
            tipeSurveys = res.getStringArray(R.array.tipeSurvey);

            //textViewTipeSurvey.setText(tipeSurveys[

                if   ( Integer.parseInt( mSettings[0].getString(APP_PREFERENCES_SURVEY, ""))==1){ surveyTipe=true;}else {surveyTipe=false;};
        }



        takeCusor();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //   Log.i(TAG, "onRestart");

        takeCusor();

    }

    public void previousWord(View view) {

        movementCurcor(true,false);
    }

    public void nextWord(View view) {

        LinearLayout mLinearLayout = findViewById(R.id.checkLayout);

        String currentUPWorld = TexViewUp.getText().toString();
        String currentEditWord = editTextDown.getText().toString();

        int engWordsColumnIndex = cursor.getColumnIndex(StudyWords.WorldEntry.COLUMN_ENGWORLD);
        int rusWordsColumnIndex = cursor.getColumnIndex(StudyWords.WorldEntry.COLUMN_RUSWORLD);

        // используем индексы для получения строки или числа
        String currentEngWorldBase = cursor.getString(engWordsColumnIndex);
        String currentRusWorldBase = cursor.getString(rusWordsColumnIndex);


        String checkWordBase;
        if (surveyTipe) {checkWordBase=currentEngWorldBase;} else {checkWordBase = currentRusWorldBase ;}


        Log.i("checkActivity", currentEditWord.trim() + "  " + checkWordBase + " " + currentEditWord.equalsIgnoreCase(checkWordBase));

        if (compareWords(currentEditWord,checkWordBase)){
            mLinearLayout.setBackgroundColor(getResources().getColor(R.color.colorGreen));
        } else {
            mLinearLayout.setBackgroundColor(getResources().getColor(R.color.colorRed));
        }


        // задердка в переключения реализована через потоки
        // подробнее тут http://developer.alexanderklimov.ru/android/theory/thread.php
        Thread thtead = new Thread(runnable);
        // Существует и другой способ, когда все запускаемые потоки объявляются демонами.
        // В этом случае все запущенные потоки будут автоматически завершены при завершении основного потока приложения:
        thtead.setDaemon(true);
        thtead.start();


    }

    private boolean compareWords(String currentEditWord, String checkWordBase) {
        // процедура сравнения строки введения и строки из  базы

        Log.i("compareWords", currentEditWord + "   |   " + checkWordBase);
        List<String> EditWord = new ArrayList();
        List<String> WordBase = new ArrayList();

        if (currentEditWord.contains(",")) {
            Log.i(LOG_TAG, currentEditWord + " содержит ,");
            EditWord = Arrays.asList(currentEditWord.split(","));
        } else {
            EditWord.add(currentEditWord);
        }

        if (checkWordBase.contains(",")) {
            Log.i(LOG_TAG, checkWordBase + " содержит ,");
            WordBase = Arrays.asList(checkWordBase.split(","));
        } else {
            WordBase.add(checkWordBase);
        }

        boolean flagTrue = false;
        for (String editWord : EditWord) {
            for (String wordBase : WordBase) {
               if (editWord.equals(wordBase)){flagTrue = true;}
            }
            // don't work \/
            /*if (WordBase.contains(WordBase)) {
                flagTrue = true;
            }*/
        }

        return flagTrue;

        /*if (currentEditWord.equalsIgnoreCase(checkWordBase)) {
            return true;
        }else {
        return false;}*/
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            long endTime = System.currentTimeMillis() + 2 * 100;

            while (System.currentTimeMillis() < endTime) {
                synchronized (this) {
                    try {
                        wait(endTime - System.currentTimeMillis());
                    } catch (Exception e) {
                    }
                }
            }


            Log.i("Thread", "Задержка на 2 секунды  " + System.currentTimeMillis());
            Log.i("Thread", "corentPosition = " + String.valueOf(cursor.getPosition()));

            handrer.sendEmptyMessage(0);
        }
    };

    @SuppressLint("HandlerLeak")
    Handler handrer = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            movementCurcor(false,true);

        }
    };


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

        if (mWorldSQLite.CountLength() == 0) {
            return;
        }

        cursor = mWorldSQLite.databaseQuery();

        cursor.moveToFirst();
        stepToWind();
    }


    public void movementCurcor (boolean previous, boolean next  )
    {
        Log.i(LOG_TAG,"cursor.getPosition()  " + cursor.getPosition());

        if (previous ){
            if(cursor.getPosition()!=0){cursor.moveToPrevious();}}

        if (next ){
            if (cursor.getPosition()!=cursor.getCount()-1){cursor.moveToNext();}}

        stepToWind();
    }

    public void stepToWind() {

        Log.i(LOG_TAG, "corentPosition = " + String.valueOf(cursor.getPosition())+"  "+cursor.getCount());


        LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.checkLayout);
        mLinearLayout.setBackgroundColor(getResources().getColor(R.color.colorWhite));

        int idColumnIndex = cursor.getColumnIndex(StudyWords.WorldEntry._ID);
        int engWordsColumnIndex = cursor.getColumnIndex(StudyWords.WorldEntry.COLUMN_ENGWORLD);
        int transletionColumnIndex = cursor.getColumnIndex(StudyWords.WorldEntry.COLUMN_TRANSCRIPTION);
        int rusWordsColumnIndex = cursor.getColumnIndex(StudyWords.WorldEntry.COLUMN_RUSWORLD);
        int lessonColumnIndex = cursor.getColumnIndex(StudyWords.WorldEntry.COLUMN_LESSON);

        // используем индексы для получения строки или числа
        //int currentID = cursor.getInt(idColumnIndex);
        String currentEngWorld = cursor.getString(engWordsColumnIndex);
        String currentTransletion = cursor.getString(transletionColumnIndex);
        String currentRusWorld = cursor.getString(rusWordsColumnIndex);
        String currentLessonWorld = cursor.getString(lessonColumnIndex);

        //  Log.i(LOG_TAG, currentEngWorld + " -> " + currentRusWorld + " " + Integer.toString(cursor.getPosition()) + " " + Integer.toString(corentPosition));


        TextView TexViewUp = findViewById(R.id.textViewUp);
        EditText TexViewDown = findViewById(R.id.editTextDown_Check);

        TextView positionWord = findViewById(R.id.positionWord);


        if (surveyTipe){TexViewUp.setText(currentRusWorld);} else {TexViewUp.setText(currentEngWorld);}
        TexViewDown.setText("");
        positionWord.setText("Слово " + Integer.toString(cursor.getPosition()+1) + " из " + Integer.toString(cursor.getCount()));

    }


    public void previousLayout(View view) {

        // переходим обратно на активность ListSQLActivity
        Intent intent = new Intent(checkActivity.this, BattonSQLActivity.class);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return true;
        //super.onCreateOptionsMenu(menu);
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

}
