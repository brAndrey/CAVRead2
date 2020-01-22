package com.example.cavread2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cavread2.ListChoice.ListChoiceActivity;
import com.example.cavread2.data.LessonSQL;

public class SettingActivity extends AppCompatActivity {

    public static final String LOG_TAG = SettingActivity.class.getSimpleName();

    SharedPreferences mSettings;

    static final private int CHOOSE_SORT = 0; // выбираем способ сортировки
    static final private int CHOOSE_LESSON = 1; // выбираем урок
    static final private int CHOOSE_SURVEY = 2; // выбираем способ опроса

    // это будет именем файла настроек
    public static final String APP_PREFERENCES = "mysettings";
    // сохраняемые параметры настроек
    public static final String APP_PREFERENCES_SORT = "Sort";
    public static final String APP_PREFERENCES_LESSON = "Lesson";
    public static final String APP_PREFERENCES_SURVEY = "Survey";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        final TextView lessonTextView     = (TextView) findViewById(R.id.LessonWrite);
        final TextView sortTextView       = (TextView) findViewById(R.id.sortWrite);
        final TextView textViewTipeSurvey = (TextView) findViewById(R.id.textViewTipeSurvey);

        String[] sorts;
        String[] tipeSurveys;

        // блок работы с сохранением настроек \/
        final SharedPreferences[] mSettings = {getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)};

        if (mSettings[0].contains(APP_PREFERENCES_SORT)) {

            Resources res = getResources();
            sorts = res.getStringArray(R.array.sortWorc);

            Log.i(LOG_TAG,"APP_PREFERENCES_SORT "+mSettings[0].getString(APP_PREFERENCES_SORT,"") );

            sortTextView.setText(sorts[Integer.parseInt(mSettings[0].getString(APP_PREFERENCES_SORT, ""))]);
        }

        if (mSettings[0].contains(APP_PREFERENCES_SURVEY)) {

            Resources res = getResources();
            tipeSurveys = res.getStringArray(R.array.tipeSurvey);
            Log.i(LOG_TAG,"APP_PREFERENCES_SURVEY "+mSettings[0].getString(APP_PREFERENCES_SURVEY,"") );

            textViewTipeSurvey.setText(tipeSurveys[Integer.parseInt(mSettings[0].getString(APP_PREFERENCES_SURVEY, ""))]);
        }

        if (mSettings[0].contains(APP_PREFERENCES_LESSON)) {
            LessonSQL mLessonSQL = new LessonSQL(this);
            String idLesson = mSettings[0].getString(APP_PREFERENCES_LESSON, "");

            Log.i(LOG_TAG, "idLesson = " + idLesson);
            if (idLesson != "") {
                lessonTextView.setText(mLessonSQL.сheckAvailabilityIDInLesson(Integer.parseInt(idLesson)));
            }

            //sortTextView.setText(" сохраненный ID "+mSettings.getString(APP_PREFERENCES_LESSON, ""));
        }
        // блок работы с сохранением настроек /\


        Button lessonBatton = (Button) findViewById(R.id.button);
        final Button lessonClear = (Button) findViewById(R.id.clearLesson);
        Button sortBatton = (Button) findViewById(R.id.button2);
        Button buttonTipeSurvey = findViewById(R.id.buttonTipeSurvey);
        Button returnBatton = (Button) findViewById(R.id.battonReturn);

        View.OnClickListener myButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                //final int CHOOSE_THIEF = 0;
                Resources res = getResources();
                // уроки
                String[] settingsType = res.getStringArray(R.array.settingsType);

                switch (v.getId()) {
                    case R.id.button:
                        // переходим на новую активность в которой отображается меню изучения слов
                        //intent = new Intent(SettingActivity.this, ListChoiceLessonActivity.class);
                        intent = new Intent(SettingActivity.this, ListChoiceActivity.class);

                        intent.putExtra("Class",String.valueOf(0));


                        startActivityForResult(intent, CHOOSE_LESSON);
                        break;

                    case R.id.button2:
                        //intent = new Intent(SettingActivity.this, ListChoiceSortActivity.class);
                        intent = new Intent(SettingActivity.this, ListChoiceActivity.class);
                        intent.putExtra("Class",String.valueOf(1));
                        //startActivity(intent);
                        startActivityForResult(intent, CHOOSE_SORT);
                        break;

                    case R.id.buttonTipeSurvey:
                        //intent = new Intent(SettingActivity.this, ListChoiceSurveyActivity.class);
                        intent = new Intent(SettingActivity.this, ListChoiceActivity.class);
                        intent.putExtra("Class",String.valueOf(2));
                        //startActivity(intent);
                        startActivityForResult(intent, CHOOSE_SURVEY);
                        break;

                    case R.id.battonReturn:
                        //onBackPressed();
                        Toast toast = Toast.makeText(getApplicationContext(), "Пытаемся вернутся", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                        finish();
                        break;

                    case R.id.clearLesson:
                        lessonTextView.setText("  ");

                        SharedPreferences mSettings2 = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor = mSettings2.edit();

                        editor.putString(APP_PREFERENCES_LESSON, null);
                        editor.apply();

                        break;
                }
            }
        };

        lessonBatton.setOnClickListener(myButtonClickListener);
        sortBatton.setOnClickListener(myButtonClickListener);
        returnBatton.setOnClickListener(myButtonClickListener);
        lessonClear.setOnClickListener(myButtonClickListener);
        buttonTipeSurvey.setOnClickListener(myButtonClickListener);

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        final TextView lessonTextView = (TextView) findViewById(R.id.LessonWrite);
        final TextView sortTextView = (TextView) findViewById(R.id.sortWrite);
        final TextView textViewTipeSurvey = (TextView) findViewById(R.id.textViewTipeSurvey);

        if (requestCode == CHOOSE_SORT) {
            if (resultCode == RESULT_OK) {

                String thiefname = data.getStringExtra(ListChoiceActivity.REZULT);

                Toast.makeText(getApplicationContext(), "Вы выбрали " + (thiefname) + " элемент", Toast.LENGTH_SHORT).show();

                SharedPreferences.Editor editor = mSettings.edit();

                editor.putString(APP_PREFERENCES_SORT, thiefname);
                editor.apply();

                Resources res = getResources();

                String[] sorts = res.getStringArray(R.array.sortWorc);

                sortTextView.setText(sorts[Integer.parseInt(thiefname)]);

                /*for (String sort : sorts) {
                    Log.i("test", sort);
                    String[] temp = {sort};
                   // itemArrayAdapter1.add(temp);
                }*/

            } else {

                Toast.makeText(getApplicationContext(), "Вы не выбрали элемент", Toast.LENGTH_SHORT).show();

            }
        }
        if (requestCode == CHOOSE_LESSON) {
            if (resultCode == RESULT_OK) {

                String lessonID = data.getStringExtra(ListChoiceActivity.LESSONID);
                String sortposition = data.getStringExtra(ListChoiceActivity.LESSONNAME);

                LessonSQL mLessonSQL = new LessonSQL(this);
                lessonTextView.setText(mLessonSQL.сheckAvailabilityIDInLesson(Integer.parseInt(lessonID)));
                Toast.makeText(getApplicationContext(), "Вы выбрали " + (sortposition) + " элемент", Toast.LENGTH_SHORT).show();


                SharedPreferences.Editor editor = mSettings.edit();

                editor.putString(APP_PREFERENCES_LESSON, lessonID);
                editor.apply();

            } else {

                Toast.makeText(getApplicationContext(), "Вы не выбрали элемент", Toast.LENGTH_SHORT).show();

            }
        }
        if (requestCode == CHOOSE_SURVEY) {
            if (resultCode == RESULT_OK) {

                String thiefname = data.getStringExtra(ListChoiceActivity.REZULT);

                //Toast.makeText(getApplicationContext(), "Вы выбрали " + (thiefname) + " элемент", Toast.LENGTH_SHORT).show();

                SharedPreferences.Editor editor = mSettings.edit();

                editor.putString(APP_PREFERENCES_SURVEY, thiefname);
                editor.apply();

                Resources res = getResources();

                String[] tipeSurveys = res.getStringArray(R.array.tipeSurvey);

                textViewTipeSurvey.setText(tipeSurveys[Integer.parseInt(thiefname)]);

            } else {

                Toast.makeText(getApplicationContext(), "Вы не выбрали элемент", Toast.LENGTH_SHORT).show();

            }
        }
    }

    //зonBackPressed

}
