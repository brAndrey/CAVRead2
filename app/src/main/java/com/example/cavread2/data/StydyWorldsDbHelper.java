package com.example.cavread2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.cavread2.data.StudyWords.WorldEntry;
import com.example.cavread2.data.StudyWords.LessonEntry;

public class StydyWorldsDbHelper extends SQLiteOpenHelper {

private static final String LOG_TAG = StydyWorldsDbHelper.class.getSimpleName();

// имя файла базы данных
    private static final String DATABASE_NAME = "leningworlds.db";
// Версия базы данных. ПРи изменении схемы увеличивается на единицу
    private static final int DATABASE_VERSION=13;

    public StydyWorldsDbHelper (Context contextInt){
        super(contextInt, DATABASE_NAME,null,DATABASE_VERSION);}



    /*public DBHelper(Context contextInt) {
        // конструктор суперкласса
        super(contextInt, DATABASE_NAME, null, DATABASE_VERSION);
    }*/

    @Override
    public  void onCreate (SQLiteDatabase db){



        String SQL_CREATE_WORLD_TABLE =

                "CREATE TABLE " + WorldEntry.TABLE_NAME + " ("
                + WorldEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + WorldEntry.COLUMN_ENGWORLD + " TEXT NOT NULL, "
                + WorldEntry.COLUMN_TRANSCRIPTION + " TEXT, "
                + WorldEntry.COLUMN_RUSWORLD + " TEXT NOT NULL, "
                + WorldEntry.COLUMN_COURSE + " INT DEFAULT 0, "
                + WorldEntry.COLUMN_LESSON + " TEXT);";

                //+ WorldEntry.COLUMN_LESSON + " INT, FOREIGN KEY ("+WorldEntry.COLUMN_LESSON+")REFERENCES "+ LessonEntry.TABLE_NAME +" ("+LessonEntry._ID+"));";

        db.execSQL(SQL_CREATE_WORLD_TABLE);

     //   NOT NULL не разрешает оставлять пустым определённый столбец. Если при вставке мы пропустим этот столбец, то снова увидим сообщение об ошибке.
     //
     //  DEFAULT value - если при вставке новой строки мы не зададим значения для столбца, то применится значение по умолчанию. Данный параметр можно комбинировать с предыдущим.


        String SQL_CREATE_LESSON_TABLE =

                "CREATE TABLE " + LessonEntry.TABLE_NAME + " ("
                        + LessonEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + LessonEntry.COLUMN_LESSON + " TEXT NOT NULL);";

        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_LESSON_TABLE);


        Log.i(LOG_TAG,"Должны сформировать leningworlds.db");
    }
    /**
     * Вызывается при обновлении схемы базы данных
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + WorldEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LessonEntry.TABLE_NAME);
        onCreate(db);
    }
}
