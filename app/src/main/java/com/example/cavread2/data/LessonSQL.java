package com.example.cavread2.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class LessonSQL {

    public static final String LOG_TAG = LessonSQL.class.getSimpleName();
    Context contextInt;

    public LessonSQL(Context contextInt) {

        this.contextInt = contextInt;
    }

    public void add(String lesson) {


        //Log.i(" --- LessonSQL add ", "step - "+lesson+"  "+сheckAvailability(lesson));

        if (!сheckAvailabilityLessonBool(lesson)) {

            StydyWorldsDbHelper mDbHelper = new StydyWorldsDbHelper(contextInt);

            SQLiteDatabase db = mDbHelper.getWritableDatabase();


            ContentValues values = new ContentValues();

            values.put(StudyWords.LessonEntry.COLUMN_LESSON, lesson.trim());

            db.insert(StudyWords.LessonEntry.TABLE_NAME, null, values);

            mDbHelper.close();
            // Uri newUri = getContentResolver().insert(GuestEntry.CONTENT_URI, values);
        }
    }

    public void update(int Id, String lesson) {

        // проверяем, а не пишем ли мы 2-й раз тоже имя урока
        if (сheckAvailabilityLessonBool(lesson)) {
            // Log.i(" --- LessonSQL update "," Выходим по повторению ");
            return;
        }
        // проверяем, а есть ли запись с таким ID, если нет то создаём новую запись
        if (Id == 0) {
            add(lesson);
            return;
        }

        StydyWorldsDbHelper mDbHelper = new StydyWorldsDbHelper(contextInt);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(StudyWords.LessonEntry.COLUMN_LESSON, lesson);

        db.update(StudyWords.LessonEntry.TABLE_NAME, values, StudyWords.LessonEntry._ID + "= ?", new String[]{Integer.toString(Id)});

        mDbHelper.close();


            /*
             ontentValues values = new ContentValues();
             values.put(GuestEntry.COLUMN_NAME, "Мурзик");
             db.update(GuestEntry.TABLE_NAME, values, GuestEntry.COLUMN_NAME + "= ?", new String[]{"Мурка"});

            Первый параметр метода update() содержит имя таблицы.
            Второй параметр указывает, какие значения должны использоваться для обновления.
            Третий параметр задает условия отбора обновляемых записей (WHERE). В приведенном примере "NAME = ?" означает, что столбец NAME должен быть равен некоторому значению.
            Символ ? обозначает значение столбца, которое определяется содержимым последнего параметра.
            Если в двух последних параметрах метода передаётся значение null, будут обновлены ВСЕ записи в таблице.

            db.update(GuestEntry.TABLE_NAME.TABLE_NAME,values,"_id = ?",new String[] {Integer.toString(1)});
            */

        // Uri newUri = getContentResolver().insert(GuestEntry.CONTENT_URI, values);

    }

    public boolean сheckAvailabilityLessonBool(String lesson) {
        //boolean
        int beg = сheckAvailabilityLessonInID(lesson);
        //Log.i(" --- LessonSQL", "сheckAvailabilityOnLesson beg- "+Integer.toString(beg)+"  "+lesson);
        if (beg == 0) {
            return false;
        } else {
            return true;
        }
    }

    public int сheckAvailabilityLessonInID(String lesson) {
        // берем lesson возвращаем id

        StydyWorldsDbHelper mDbHelper = new StydyWorldsDbHelper(contextInt);
        // открываем базу для чтения
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        if (lesson==null){return 0;}

        // задаём столбци выборки
        String[] projection = {
                StudyWords.LessonEntry._ID,
                StudyWords.LessonEntry.COLUMN_LESSON

        };
        //http://developer.alexanderklimov.ru/android/sqlite/android-sqlite.php#SQLiteDatabase

        //Делаем запрос
        Cursor cursor = db.query(
                StudyWords.LessonEntry.TABLE_NAME,
                projection,
                StudyWords.LessonEntry.COLUMN_LESSON + "= ?",
                new String[]{lesson.trim()},
                null,
                null,
                null);

        try {

            int idColumnIndex = cursor.getColumnIndex(StudyWords.LessonEntry._ID);
            int LessonColumnIndex = cursor.getColumnIndex(StudyWords.LessonEntry.COLUMN_LESSON);
            //Проходим по рядам
            while (cursor.moveToNext()) {
                // используем индексы для получения строки или числа
                int currentID = cursor.getInt(idColumnIndex);
                String currentLessonEntry = cursor.getString(LessonColumnIndex);
                //Log.i(" --- LessonSQL", "сheckAvailabilityIdOnLesson  step - "+Integer.toString(currentID)+"  "+currentLessonEntry+"  "+lesson);
                return currentID;
            }
        } finally {

            // закрываем курсор после чтения
            cursor.close();
        }

        return 0;
    }

    public boolean сheckAvailabilityIdBool(int id) {

        String beg = сheckAvailabilityIDInLesson(id);
        //Log.i(" --- LessonSQL", "сheckAvailabilityOnLesson beg- "+Integer.toString(beg)+"  "+lesson);
        if (beg == "") {
            return false;
        } else {
            return true;
        }
    }

    public String сheckAvailabilityIDInLesson(int id) {

       // Log.i(LOG_TAG,"сheckAvailabilityIDInLesson id = "+String.valueOf(id));

        StydyWorldsDbHelper mDbHelper = new StydyWorldsDbHelper(contextInt);
        // открываем базу для чтения
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // задаём столбци выборки
        String[] projection = {
                StudyWords.LessonEntry._ID,
                StudyWords.LessonEntry.COLUMN_LESSON

        };
        //http://developer.alexanderklimov.ru/android/sqlite/android-sqlite.php#SQLiteDatabase

        //Делаем запрос
        Cursor cursor = db.query(
                StudyWords.LessonEntry.TABLE_NAME,
                projection,
                StudyWords.LessonEntry._ID+ "= ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null);

        try {

            int idColumnIndex = cursor.getColumnIndex(StudyWords.LessonEntry._ID);
            int LessonColumnIndex = cursor.getColumnIndex(StudyWords.LessonEntry.COLUMN_LESSON);
            //Проходим по рядам
            while (cursor.moveToNext()) {
                // используем индексы для получения строки или числа
                int currentID = cursor.getInt(idColumnIndex);
                String currentLessonEntry = cursor.getString(LessonColumnIndex);
                //Log.i(" --- LessonSQL", "сheckAvailabilityIdOnLesson  step - "+Integer.toString(currentID)+"  "+currentLessonEntry+"  "+lesson);
                return currentLessonEntry;
            }
        } finally {

            // закрываем курсор после чтения
            cursor.close();
        }

        return "";
    }


    public Cursor databaseQuery() {
        StydyWorldsDbHelper mDbHelper = new StydyWorldsDbHelper(contextInt);

        // открываем базу для чтения
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // задаём условия выборки
        String[] projection = {
                StudyWords.LessonEntry._ID,
                StudyWords.LessonEntry.COLUMN_LESSON

        };

        //Делаем запрос

        Cursor cursor = db.query(
                StudyWords.LessonEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        return cursor;

    }

    public void ClearLessonSQL() {

        StydyWorldsDbHelper mDbHelper = new StydyWorldsDbHelper(contextInt);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.delete(StudyWords.LessonEntry.TABLE_NAME, null, null);
        mDbHelper.close();

    }

    public void delete(String lesson) {

        int curId=сheckAvailabilityLessonInID(lesson);

        Log.i(LOG_TAG,"delete lesson = "+lesson);

        if (сheckAvailabilityIdBool(curId)) {

            Log.i(LOG_TAG,"delete lesson in if");

            WorldSQL worldSQL = new WorldSQL(contextInt);
            if (worldSQL.CountLength(lesson) != 0) {
                Toast.makeText(contextInt,"У урока есть слова , его невозможно удалить",Toast.LENGTH_SHORT).show();
                return;
            }

            Log.i(LOG_TAG,"delete lesson go delete");

            StydyWorldsDbHelper mDbHelper = new StydyWorldsDbHelper(contextInt);

            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            //int delCount =
            db.delete(StudyWords.LessonEntry.TABLE_NAME, StudyWords.LessonEntry._ID + "=" + curId, null);

        }
//Toast.makeText(this," У этого урока есть слова",Toast.LENGTH_SHORT).show();

    }
}