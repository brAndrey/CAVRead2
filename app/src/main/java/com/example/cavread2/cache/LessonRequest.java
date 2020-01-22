package com.example.cavread2.cache;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.cavread2.data.StudyWords;
import com.example.cavread2.data.StydyWorldsDbHelper;

public class LessonRequest {

    public static final String LOG_TAG = LessonRequest.class.getSimpleName();

    private StydyWorldsDbHelper mDbHelper;

    Context enterContext;

    public LessonRequest(Context enterContext) {
        this.enterContext = enterContext;

        Log.i(LOG_TAG, "Создали экземпляр класса LessonRequest");
        if (enterContext == null) {
            Log.i(LOG_TAG, "Входящий context is null");
        }
    }

    public Cursor SQLiteDataRead() {
        Log.i(LOG_TAG, "Процедура заполнения списка SQL в обучении");

        mDbHelper = new StydyWorldsDbHelper(this.enterContext);

        if (enterContext != null) {
            // открываем базу для чтения
            SQLiteDatabase db = mDbHelper.getReadableDatabase();


            // задаём условия выборки
            String[] projection = {
                    StudyWords.WorldEntry._ID,
                    StudyWords.WorldEntry.COLUMN_ENGWORLD,
                    StudyWords.WorldEntry.COLUMN_TRANSCRIPTION,
                    StudyWords.WorldEntry.COLUMN_RUSWORLD,
                    StudyWords.WorldEntry.COLUMN_LESSON

            };

            //Делаем запрос

            Cursor cursor = db.query(
                    StudyWords.WorldEntry.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    null);

            Log.i(LOG_TAG, "Всего слов " + Integer.toString(cursor.getCount()));

            return cursor;
        } else {
            Log.i(LOG_TAG, "" + "LessonRequest -> Входящий context is null ");
            return null;
        }
    }

}
 /* Toast toast = Toast.makeText(enterContext.getApplicationContext(), "Процедура заполнения списка SQL в обучении", Toast.LENGTH_LONG);
       toast.setGravity(Gravity.CENTER, 0, 0);
       toast.show(); */