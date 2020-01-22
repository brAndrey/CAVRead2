package com.example.cavread2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cavread2.File.FilePickerActivity;
import com.example.cavread2.ListChoice.ListChoiceActivity;
import com.example.cavread2.data.LessonSQL;
import com.example.cavread2.data.WorldSQL;

public class activity_words_from_CSV extends AppCompatActivity {

    static final private int CHOOSE_THIEF = 0;
    static final private int CHOOSE_THIEF1 = 1;

    // параметры используется при временном сохранении пути
    static final private String CHOICE_FILE = "CHOICE_FILE";
    static final private String CHOICE_LESSEN = "CHOICE_LESSEN";

    String LessonName;
    int idLessonName;

    Context context;

    String LOG = " activity_words_from_CSV ";

    // это будет именем файла настроек
    public static final String APP_PREFERENCES = "mysettings";
    // сохраняемые параметры настроек
    public static final String APP_PREFERENCES_PATH_CSV = "PATH_CSV";

    //public static final String APP_PREFERENCES_LESSON = "Lesson";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_from__csv);
        //      Toolbar toolbar = findViewById(R.id.toolbar);
        //      setSupportActionBar(toolbar);


        // блок работы с сохранением настроек \/
        final SharedPreferences[] mSettings = {getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)};

        if (mSettings[0].contains(APP_PREFERENCES_PATH_CSV)) {

            Resources res = getResources();


            //востанавливаем значение
            //sortTextView.setText(sorts[ Integer.parseInt(
            // mSettings[0].getString(APP_PREFERENCES_PATH_CSV, ""))]);
        }

        // блок работы с сохранением настроек /\


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button buttonChoiceFile = (Button) findViewById(R.id.buttonChoiceFile);
        Button buttonChoiceLesson = (Button) findViewById(R.id.buttonChoiceLesson);
        Button buttonReadCSV = (Button) findViewById(R.id.buttonReadCSV);
        Button buttonDownloadCSV = (Button) findViewById(R.id.buttonDownloadCSV);


        View.OnClickListener myButtonClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.i(LOG, " onClick  ");
                Intent intent;
                //               final int CHOOSE_THIEF = 0;
//                final int CHOOSE_THIEF1 = 1;
                TextView textViewChoiceFile = findViewById(R.id.textViewChoiceFile);

                switch (v.getId()) {
                    case R.id.buttonChoiceFile:
                        //onBackPressed();
                      /*  Toast toast = Toast.makeText(getApplicationContext(), "Пытаемся уйти на ... ", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();*/

                        intent = new Intent(activity_words_from_CSV.this, FilePickerActivity.class);
                        startActivityForResult(intent, CHOOSE_THIEF);

                        break;
                    case R.id.buttonReadCSV:
                        //onBackPressed();
                      /*  Toast toast = Toast.makeText(getApplicationContext(), "Пытаемся уйти на ... ", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();*/


                        intent = new Intent(activity_words_from_CSV.this, ListCSVActivity.class);
                        intent.putExtra("EXTRA_FILE_PATH", textViewChoiceFile.getText());
                        startActivity(intent);

                        break;

                    case R.id.buttonChoiceLesson:

                        TextView textViewChoiceLesson = findViewById(R.id.textViewChoiceLesson);

                        intent = new Intent(activity_words_from_CSV.this, ListChoiceActivity.class);
                        intent.putExtra("Class",String.valueOf( 0 ));
                        //intent.putExtra("EXTRA_FILE_PATH",textViewChoiceFile.getText());
                        startActivityForResult(intent, CHOOSE_THIEF1);

                        break;

                    case R.id.buttonDownloadCSV:

                        if (textViewChoiceFile.getText() != null) {

                            WorldSQL worldSQL = new WorldSQL(activity_words_from_CSV.this);
                            // здесь нельзя указать this так как возьмется объект OnClickListener вместо всей активности

                            worldSQL.EXTRA_FILE_PATH = textViewChoiceFile.getText().toString();

                            worldSQL.fullSQL_file(textViewChoiceFile.getText().toString(), LessonName, idLessonName);
                        }
                        break;
                }
            }
        };

        buttonChoiceFile.setOnClickListener(myButtonClickListener);
        buttonChoiceLesson.setOnClickListener(myButtonClickListener);
        buttonReadCSV.setOnClickListener(myButtonClickListener);
        buttonDownloadCSV.setOnClickListener(myButtonClickListener);
    }

    // сохранение состояния перед поворотом
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        TextView textViewChoiceFile = findViewById(R.id.textViewChoiceFile);
        TextView textViewChoiceLesson = findViewById(R.id.textViewChoiceLesson);

        String ChoiceFile = textViewChoiceFile.getText().toString();
        String ChoiceLesson = textViewChoiceLesson.getText().toString();

        outState.putString(CHOICE_FILE,ChoiceFile );
        outState.putString(CHOICE_LESSEN,ChoiceLesson);

        super.onSaveInstanceState(outState);
    }
    // получение ранее сохраненного состояния
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey(CHOICE_FILE)) {

            TextView textViewChoiceFile = findViewById(R.id.textViewChoiceFile);
            String ChoiceFile = savedInstanceState.getString(CHOICE_FILE);

            textViewChoiceFile.setText(ChoiceFile);
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(CHOICE_LESSEN)) {

            TextView textViewChoiceFile = findViewById(R.id.textViewChoiceFile);
            TextView textViewChoiceLesson = findViewById(R.id.textViewChoiceLesson);

            String ChoiceFile = savedInstanceState.getString(CHOICE_FILE);
            String ChoiceLessen = savedInstanceState.getString(CHOICE_LESSEN);

            textViewChoiceFile.setText(ChoiceFile);
            textViewChoiceLesson.setText(ChoiceLessen);

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        TextView textViewChoiceFile = findViewById(R.id.textViewChoiceFile);
        TextView textViewChoiceLesson = findViewById(R.id.textViewChoiceLesson);

        if (requestCode == CHOOSE_THIEF) {
            try {
                String FILE_PATH = data.getStringExtra(FilePickerActivity.EXTRA_FILE_PATH);
                textViewChoiceFile.setText(FILE_PATH);

              //  Toast toast = Toast.makeText(getApplicationContext(), "Получили " + FILE_PATH, Toast.LENGTH_SHORT);
              //  toast.setGravity(Gravity.CENTER, 0, 0);
              //  toast.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (requestCode == CHOOSE_THIEF1) {

            if (resultCode == RESULT_OK) {
                String LESSONNAME = data.getStringExtra(ListChoiceActivity.LESSONNAME);

                textViewChoiceLesson.setText(LESSONNAME);

                LessonSQL lessonSQL = new LessonSQL(this);
                LessonName = LESSONNAME;
                idLessonName = lessonSQL.сheckAvailabilityLessonInID(LESSONNAME);
                Log.i(LOG, "Lesson  " + LessonName + "  " + String.valueOf(idLessonName));

                //Toast toast = Toast.makeText(getApplicationContext(), "Получили " + LESSONNAME, Toast.LENGTH_SHORT);
                //toast.setGravity(Gravity.CENTER, 0, 0);
                //toast.show();
            }
        }
    }
}
