package com.example.cavread2.ListChoice;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
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

public class ListChoiceActivity extends AppCompatActivity {

    public static final String LOG_TAG = ListChoiceActivity.class.getSimpleName();

    private ItemArrayAdapter1 itemArrayAdapter1;
    private String[] sorts;
    private String[] surveys;

    // переменные возврата
    public final static String LESSONID= "com.example.andrey_vb.Test_settings.LESSONID";
    public final static String LESSONNAME = "com.example.andrey_vb.Test_settings.LESSONNAME";

    public final static String REZULT = "com.example.andrey_vb.Test_settings.REZULT";

    public final static String THIEF = "com.example.andrey_vb.Test_settings.THIEF";

    LessonSQL mLessonSQL= new LessonSQL(this);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        String SortClass="SortClass";

        itemArrayAdapter1 = new ItemArrayAdapter1(getApplicationContext(),R.layout.item_layout);

        // а сдесь расходимся по наполнению ListView

        String workClass = getIntent().getExtras().getString("Class");

        Resources res = getResources();
        // сортировки
        String[] settingsType = res.getStringArray(R.array.settingsType);

        Log.i(LOG_TAG, "workClass  =" + workClass+"**"+settingsType[Integer.parseInt(workClass)]);

        if (Integer.parseInt(workClass ) == 0) {fillingListLesson();}

        if (Integer.parseInt(workClass ) == 1) {fillingListSort();}

        if (Integer.parseInt(workClass ) == 2) {fillingListSurvey();}

        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(itemArrayAdapter1);

        // отработка нажатия
        listView.setOnItemClickListener(myOnItemClickListener);


    }

    private void fillingListSort() {

        Log.i(LOG_TAG,"fillingListSort()");
        Resources res = getResources();

        // уроки
        sorts = res.getStringArray(R.array.sortWorc);

        //ItemArrayAdapter1 itemArrayAdapter1 = new ItemArrayAdapter1(getApplicationContext(),R.layout.item_layout);


        for (String sort : sorts) {
            Log.i("test", sort);
            String[] temp = {sort};
            itemArrayAdapter1.add(temp);
        }
    }

    private void fillingListLesson() {

        Log.i(LOG_TAG,"fillingListLesson()");
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
                String[] temp = {currentLessonEntry};
                itemArrayAdapter1.add(temp);
            }
        }finally {

            //Log.i(LOG_TAG," --- intLengthSQLite  "+Integer.toString(cursor.getCount()));
            // закрываем курсор после чтения
            cursor.close();
        }


    }

    private void fillingListSurvey() {

        Log.i(LOG_TAG,"fillingListSurvey()");

        Resources res = getResources();

        // уроки
        surveys = res.getStringArray(R.array.tipeSurvey);

        //ItemArrayAdapter1 itemArrayAdapter1 = new ItemArrayAdapter1(getApplicationContext(),R.layout.item_layout);


        for (String survey : surveys) {
            Log.i("survey", survey);
            String[] temp = {survey};
            itemArrayAdapter1.add(temp);
        }
    }

    // процедура отработки нажатия
    AdapterView.OnItemClickListener myOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            //   Toast.makeText(getApplicationContext(),"Вы выбрали " + (position + 1) + " элемент", Toast.LENGTH_SHORT).show();
            String workClass = getIntent().getExtras().getString("Class");

            Intent answerIntent = new Intent();

            answerIntent.putExtra(REZULT, String.valueOf(position));


            if (Integer.parseInt(workClass) == 0) {

                String[] elemitemArrayAdapter;

                elemitemArrayAdapter = itemArrayAdapter1.getItem(position);

                answerIntent.putExtra(LESSONID, String.valueOf(mLessonSQL.сheckAvailabilityLessonInID(elemitemArrayAdapter[0])));
                answerIntent.putExtra(LESSONNAME, elemitemArrayAdapter[0]);

            }

            setResult(RESULT_OK, answerIntent);

            finish();

        }
    };

}
