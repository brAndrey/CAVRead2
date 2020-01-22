package com.example.cavread2.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.example.cavread2.File.CSVReader;
import com.example.cavread2.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorldSQL {

    public static final String LOG_TAG = WorldSQL.class.getSimpleName();

    private  StydyWorldsDbHelper mDbHelper;

    private Context contextInt;
    private Cursor cursor;

    public String EXTRA_FILE_PATH;

    public WorldSQL( Context contextInt) {

        this.contextInt = contextInt;
        mDbHelper = new StydyWorldsDbHelper(contextInt);
    }

    public void fullSQL_file(String EXTRA_FILE_PATH, String LessonName, int idLessonName ){

        try {
            // старт чтения из произвольного файла
            // получаем адрес файла
            String pathToCSV = EXTRA_FILE_PATH;

            if (pathToCSV != null) {
                Log.i(LOG_TAG, "WorldSQL fullSQL_file " + pathToCSV);
                File csvfile = new File(pathToCSV);
                CSVReader csvReader = new CSVReader(new BufferedInputStream(new FileInputStream(csvfile.getAbsolutePath())));
                List scoreList = csvReader.read();

                Log.i(LOG_TAG,LessonName+"___"+String.valueOf(idLessonName));
                int i = 1;
                while (i < scoreList.size()) {
                    //tempList.add(scoreList.get(i));


                    //Arrays.toString((String[]) scoreList.get(i));
                    try {
                        Log.i(LOG_TAG, String.valueOf(i) + "   " + String.valueOf(Arrays.toString((String[]) scoreList.get(i))));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    i++;

                }
                    // занесение слов списком
                    ListInSQL(scoreList,LessonName,idLessonName);


            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void fillSQL() {

        //   Toast toast = Toast.makeText(contextInt.getApplicationContext(), "Процедура заполнения списка SQL", Toast.LENGTH_LONG);
        //   toast.setGravity(Gravity.CENTER, 0, 0);
        //   toast.show();

        //Добавляем новый урок
        LessonSQL mLessonSQL = new LessonSQL(contextInt);
        mLessonSQL.add("stats ");



        // старт чтения из файла
        InputStream inputStream = contextInt.getResources().openRawResource(R.raw.stats);
        CSVReader csvFile = new CSVReader(inputStream);
        List scoreList = csvFile.read();
        // конец чтения из файла с перебором


        int lenthLesson=30;
        List tempList = new ArrayList();
        int i = 1;
        while (i < scoreList.size()) {
            tempList.add(scoreList.get(i));
            i++;

            // накидываем уроков
            if (i % lenthLesson == 0) {
                mLessonSQL.add("base lesson " + Integer.toString(i / lenthLesson));
                ListInSQL(tempList, "base lesson " + Integer.toString(i / lenthLesson), mLessonSQL.сheckAvailabilityLessonInID("base lesson " + Integer.toString(i / lenthLesson)));
                tempList.clear();
            }
        }
                mLessonSQL.add("base lesson");
        ListInSQL(scoreList, "base lesson", mLessonSQL.сheckAvailabilityLessonInID("base lesson"));


        Log.i(LOG_TAG+" fillSQL",String.valueOf(i));
    }

    public void ListInSQL(List scoreList, String LessonName, int idLessonName) {
         // процедура записывающая слова в базу из списка scoreList по уроку с именем и id

        Log.i(LOG_TAG," Enter in ListInSQL ");
       // mDbHelper = new StydyWorldsDbHelper(contextInt);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        for (int i = 0; i < scoreList.size(); i++) {
            // Log.i(LOG_TAG, scoreList.get(i).toString());

            String[] ki = Arrays.copyOf((String[]) scoreList.get(i), ((String[]) scoreList.get(i)).length);

            Log.i(LOG_TAG+" ListInSQL ", i+"  "+ki[0]);

            if (сheckAvailabilityWorldInID(ki[0]) == 0) {
                ;
                // Gets the database in write mode

                // Создаем объект ContentValues, где имена столбцов ключи,
                // а информация является значениями ключей
                ContentValues values = new ContentValues();

                values.put(StudyWords.WorldEntry.COLUMN_ENGWORLD, ki[0]);
                values.put(StudyWords.WorldEntry.COLUMN_TRANSCRIPTION, ki[1]);//"Транскрипция"

                if (((String[]) scoreList.get(i)).length == 3) {
                    values.put(StudyWords.WorldEntry.COLUMN_RUSWORLD, ki[2]); //"Русское слово"
                }else{values.put(StudyWords.WorldEntry.COLUMN_RUSWORLD, ""); }

                values.put(StudyWords.WorldEntry.COLUMN_COURSE, idLessonName);
                values.put(StudyWords.WorldEntry.COLUMN_LESSON, LessonName);

                //long newRowId =
                db.insert(StudyWords.WorldEntry.TABLE_NAME, null, values);
            }
        }
        mDbHelper.close();

    }

    public void ClearWorldSQL(){
        //mDbHelper = new StydyWorldsDbHelper(contextInt);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.delete(StudyWords.WorldEntry.TABLE_NAME,null, null);
        mDbHelper.close();

    }

    public int CountLength (){

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

        cursor = db.query(
                StudyWords.WorldEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        int LengthSQLite = cursor.getCount();

        cursor.close();

        return LengthSQLite;
    }

    public int CountLength (String lesson){

        LessonSQL mLessonSQL = new LessonSQL(contextInt);
        int idLesson=mLessonSQL.сheckAvailabilityLessonInID(lesson);
        if (idLesson==0){return 0; }

        cursor=databaseQueryLesson(lesson,idLesson);

        int LengthSQLite = cursor.getCount();

        cursor.close();

        return LengthSQLite;
    }

    public Cursor databaseQuery() {

        // это будет именем файла настроек
        final String APP_PREFERENCES = "mysettings";
        // сохраняемые параметры настроек
        final String APP_PREFERENCES_SORT = "Sort";
        final String APP_PREFERENCES_LESSON = "Lesson";


        // блок работы с сохранением настроек \/
        SharedPreferences mSettings = contextInt.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        String lesson=null;
        String selectionOfLesson=null;
        String[] selectionOfLessonArgs={null};

        if(mSettings.contains(APP_PREFERENCES_LESSON)) {
            //LessonSQL mLessonSQL = new LessonSQL(contextInt);

            //тут получили урок из настроек

            lesson = mSettings.getString(APP_PREFERENCES_LESSON, "");
            // тут пишем переменную для отбора

            if (lesson != "") {
                selectionOfLesson = StudyWords.WorldEntry.COLUMN_COURSE + "= ?";
                selectionOfLessonArgs[0] = String.valueOf(lesson);
            }

            //mLessonSQL.сheckAvailabilityIDInLesson(Integer.parseInt());

            //sortTextView.setText(" сохраненный ID "+mSettings.getString(APP_PREFERENCES_LESSON, ""));
        }

        // если нет отбора то список уроков превращаем в null
        if (selectionOfLesson==null){selectionOfLessonArgs=null;}
        Resources res = contextInt.getResources();

        // уроки
        String[] sortWorc = res.getStringArray(R.array.sortWorc);

        String sortType=null;
        String sortColyms = null;
        if (mSettings.contains(APP_PREFERENCES_SORT)) {
            sortType = mSettings.getString(APP_PREFERENCES_SORT, "");
            Log.i(LOG_TAG," sortType = "+sortType );
            if (!sortType.equals(null)) {

                if (sortWorc[Integer.parseInt(sortType)].equals("A B C D")) {
                    sortColyms = StudyWords.WorldEntry.COLUMN_ENGWORLD;
                }
                if (sortWorc[Integer.parseInt(sortType)].equals("А Б С Д")) {
                    sortColyms = StudyWords.WorldEntry.COLUMN_RUSWORLD;
                }
                if (sortWorc[Integer.parseInt(sortType)].equals("Lesson")) {
                    sortColyms = StudyWords.WorldEntry.COLUMN_LESSON;
                }
            }
        }

        Log.i(LOG_TAG," sortColyms = "+sortColyms);

        // блок работы с сохранением настроек /\

        //  mDbHelper = new StydyWorldsDbHelper(contextInt);
        // открываем базу для чтения
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // задаём условия выборки
        String[] projection = {
                StudyWords.WorldEntry._ID,
                StudyWords.WorldEntry.COLUMN_ENGWORLD,
                StudyWords.WorldEntry.COLUMN_TRANSCRIPTION,
                StudyWords.WorldEntry.COLUMN_RUSWORLD,
                StudyWords.WorldEntry.COLUMN_COURSE,
                StudyWords.WorldEntry.COLUMN_LESSON

        };

// не сортирует

        //Делаем запрос
        cursor = db.query(
                StudyWords.WorldEntry.TABLE_NAME,
                projection,
                selectionOfLesson,
                selectionOfLessonArgs,
                null,
                null,
                sortColyms
                );

        Log.i(LOG_TAG+" databaseQuery ","  exit");

        return cursor;



    }

    public Cursor databaseQueryLesson(String lesson, int idLesson) {

        //  mDbHelper = new StydyWorldsDbHelper(contextInt);
        // открываем базу для чтения
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        if (!(idLesson > 0)) {
            LessonSQL mLessonSQL = new LessonSQL(contextInt);
            idLesson=mLessonSQL.сheckAvailabilityLessonInID(lesson);
        }
        if (idLesson==0){return null; }

        // задаём условия выборки
        String[] projection = {
                StudyWords.WorldEntry._ID,
                StudyWords.WorldEntry.COLUMN_ENGWORLD,
                StudyWords.WorldEntry.COLUMN_TRANSCRIPTION,
                StudyWords.WorldEntry.COLUMN_RUSWORLD,
                StudyWords.WorldEntry.COLUMN_COURSE,
                StudyWords.WorldEntry.COLUMN_LESSON
        };

        //Делаем запрос
        cursor = db.query(
                StudyWords.WorldEntry.TABLE_NAME,
                projection,
                StudyWords.WorldEntry.COLUMN_COURSE+ "= ?",
                new String[]{String.valueOf(idLesson)},
                null,
                //StudyWords.WorldEntry.COLUMN_COURSE,
                null,
                null);

        return cursor;
    }

    public void WordInLog(){

        final String LOG_TAG = WorldSQL.class.getSimpleName();

        Cursor cursor = databaseQuery();

        String messageLog;

        try {

            int idColumnIndex = cursor.getColumnIndex(StudyWords.WorldEntry._ID);
            int engWordsColumnIndex = cursor.getColumnIndex(StudyWords.WorldEntry.COLUMN_ENGWORLD);
            int transletionColumnIndex = cursor.getColumnIndex(StudyWords.WorldEntry.COLUMN_TRANSCRIPTION);
            int rusWordsColumnIndex = cursor.getColumnIndex(StudyWords.WorldEntry.COLUMN_RUSWORLD);
            int courseColumnIndex = cursor.getColumnIndex(StudyWords.WorldEntry.COLUMN_COURSE);
            int lessonColumnIndex = cursor.getColumnIndex(StudyWords.WorldEntry.COLUMN_LESSON);

            //Проходим по рядам
            while (cursor.moveToNext()) {
                // используем индексы для получения строки или числа

                messageLog = cursor.getInt(idColumnIndex)+" | "
                 + cursor.getString(engWordsColumnIndex) +" | "
                 + cursor.getString(transletionColumnIndex) +" | "
                 + cursor.getString(rusWordsColumnIndex) +" | "
                 + cursor.getInt(courseColumnIndex)+" | "
                 + cursor.getString(lessonColumnIndex);

                Log.i(LOG_TAG,messageLog );

                messageLog="";
            }
        }finally {

            Log.i(LOG_TAG," --- intLengthSQLite  "+cursor.getCount());

            // закрываем курсор после чтения
            cursor.close();
        }
    }

    public void WorldInLogLesson(String lesson, int idLesson ){

        final String LOG_TAG = WorldSQL.class.getSimpleName();

        Cursor cursor = databaseQueryLesson(lesson,idLesson);


        String messageLog;

        try {

            int idColumnIndex = cursor.getColumnIndex(StudyWords.WorldEntry._ID);
            int engWordsColumnIndex = cursor.getColumnIndex(StudyWords.WorldEntry.COLUMN_ENGWORLD);
            int transletionColumnIndex = cursor.getColumnIndex(StudyWords.WorldEntry.COLUMN_TRANSCRIPTION);
            int rusWordsColumnIndex = cursor.getColumnIndex(StudyWords.WorldEntry.COLUMN_RUSWORLD);
            int courseColumnIndex = cursor.getColumnIndex(StudyWords.WorldEntry.COLUMN_COURSE);
            int lessonColumnIndex = cursor.getColumnIndex(StudyWords.WorldEntry.COLUMN_LESSON);

            //Проходим по рядам
            while (cursor.moveToNext()) {
                // используем индексы для получения строки или числа

                messageLog = cursor.getInt(idColumnIndex)+" | "
                        + cursor.getString(engWordsColumnIndex) +" | "
                        + cursor.getString(transletionColumnIndex) +" | "
                        + cursor.getString(rusWordsColumnIndex) +" | "
                        + cursor.getInt(courseColumnIndex)+" | "
                        + cursor.getString(lessonColumnIndex);

                Log.i(LOG_TAG,messageLog );

                messageLog="";
            }


        }finally {
            Log.i(LOG_TAG," --- intLengthSQLite WorldInLogLesson()" +cursor.getCount());
            // закрываем курсор после чтения
            cursor.close();
        }
    }

    public void update(int Id, String word, String transcription, String rusWords,String lesson) {

        // проверяем, а есть ли запись с таким ID, если нет то создаём новую запись
        if (Id == 0) {

            add(word, transcription, rusWords,lesson);
            return;
        }
        // проверяем, а есть ли такое слово в базе. Доп проверка
        if (!сheckAvailabilityWordBool(word)) {
            // Log.i(" --- LessonSQL update "," Выходим по повторению ");
            return;
        }
        LessonSQL mLessonSQL = new LessonSQL(contextInt);

        int currentIdLesson = mLessonSQL.сheckAvailabilityLessonInID(lesson);


        StydyWorldsDbHelper mDbHelper = new StydyWorldsDbHelper(contextInt);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(StudyWords.WorldEntry.COLUMN_ENGWORLD, word.trim());

        values.put(StudyWords.WorldEntry.COLUMN_TRANSCRIPTION, transcription.trim());

        values.put(StudyWords.WorldEntry.COLUMN_RUSWORLD, rusWords.trim());

        values.put(StudyWords.WorldEntry.COLUMN_LESSON, lesson);

        values.put(StudyWords.WorldEntry.COLUMN_COURSE, currentIdLesson);

        db.update(StudyWords.WorldEntry.TABLE_NAME, values, StudyWords.WorldEntry._ID + "= ?", new String[]{Integer.toString(Id)});

        mDbHelper.close();

        Log.i(LOG_TAG+"update","  exit");
    }

    public void add(String word, String transcription, String rusWords, String lesson) {

        if (!сheckAvailabilityWordBool(word)) {

            LessonSQL mLessonSQL = new LessonSQL(contextInt);

            int currentIdLesson = mLessonSQL.сheckAvailabilityLessonInID(lesson);

            StydyWorldsDbHelper mDbHelper = new StydyWorldsDbHelper(contextInt);

            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(StudyWords.WorldEntry.COLUMN_ENGWORLD, word.trim());

            values.put(StudyWords.WorldEntry.COLUMN_TRANSCRIPTION, transcription.trim());

            values.put(StudyWords.WorldEntry.COLUMN_RUSWORLD, rusWords.trim());

            values.put(StudyWords.WorldEntry.COLUMN_LESSON, lesson);

            values.put(StudyWords.WorldEntry.COLUMN_COURSE, currentIdLesson);

            db.insert(StudyWords.WorldEntry.TABLE_NAME, null, values);

            mDbHelper.close();

        }
    }

    public void delete(int Id, String word) {
        //
        if (Id == 0) {
            return;
        }
        // проверяем, а есть ли такое слово в базе. Доп проверка
        if (!сheckAvailabilityWordBool(word)) {
            return;
        }

        StydyWorldsDbHelper mDbHelper = new StydyWorldsDbHelper(contextInt);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //int delCount =
                db.delete(StudyWords.WorldEntry.TABLE_NAME, StudyWords.WorldEntry._ID + "=" + Id, null);

    }

    private boolean сheckAvailabilityWordBool(String word) {
       // определяет есть такое слово в базе ли нет

        int beg = сheckAvailabilityWorldInID(word);

        if (beg == 0) {
            return false;
        } else {
            return true;
        }
    }

    public int сheckAvailabilityWorldInID(String word) {
        // take name world, return id record

        StydyWorldsDbHelper mDbHelper = new StydyWorldsDbHelper(contextInt);
        // открываем базу для чтения
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // задаём столбци выборки
        String[] projection = {
                StudyWords.WorldEntry._ID,
                StudyWords.WorldEntry.COLUMN_ENGWORLD

        };
        //http://developer.alexanderklimov.ru/android/sqlite/android-sqlite.php#SQLiteDatabase

        //Делаем запрос
        Cursor cursor = db.query(
                StudyWords.WorldEntry.TABLE_NAME,
                projection,
                StudyWords.WorldEntry.COLUMN_ENGWORLD + "= ?",
                new String[]{word.trim()},
                null,
                null,
                null);

        try {

            int idColumnIndex = cursor.getColumnIndex(StudyWords.WorldEntry._ID);
            int WorldColumnIndex = cursor.getColumnIndex(StudyWords.WorldEntry.COLUMN_ENGWORLD);
            //Проходим по рядам
            while (cursor.moveToNext()) {
                // используем индексы для получения строки или числа
                int currentID = cursor.getInt(idColumnIndex);
                String currentLessonEntry = cursor.getString(WorldColumnIndex);
                //Log.i(" --- LessonSQL", "сheckAvailabilityIdOnLesson  step - "+Integer.toString(currentID)+"  "+currentLessonEntry+"  "+lesson);
                return currentID;
            }
        } finally {
            // закрываем курсор после чтения
            cursor.close();
        }

        return 0;
    }

    public ArrayList сheckAvailabilityWorldInLIst(String word) {
        // take name world, return id record

        ArrayList returnList = new ArrayList();
        StydyWorldsDbHelper mDbHelper = new StydyWorldsDbHelper(contextInt);
        // открываем базу для чтения
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // задаём столбци выборки
        String[] projection = {
                StudyWords.WorldEntry._ID,
                StudyWords.WorldEntry.COLUMN_ENGWORLD,
                StudyWords.WorldEntry.COLUMN_TRANSCRIPTION,
                StudyWords.WorldEntry.COLUMN_RUSWORLD,
                StudyWords.WorldEntry.COLUMN_COURSE,
                StudyWords.WorldEntry.COLUMN_LESSON

        };
        //http://developer.alexanderklimov.ru/android/sqlite/android-sqlite.php#SQLiteDatabase

        //Делаем запрос
        Cursor cursor = db.query(
                StudyWords.WorldEntry.TABLE_NAME,
                projection,
                StudyWords.WorldEntry.COLUMN_ENGWORLD + "= ?",
                new String[]{word.trim()},
                null,
                null,
                null);

        try {

            int idColumnIndex = cursor.getColumnIndex(StudyWords.WorldEntry._ID);
            int engWordsColumnIndex = cursor.getColumnIndex(StudyWords.WorldEntry.COLUMN_ENGWORLD);
            int transletionColumnIndex = cursor.getColumnIndex(StudyWords.WorldEntry.COLUMN_TRANSCRIPTION);
            int rusWordsColumnIndex = cursor.getColumnIndex(StudyWords.WorldEntry.COLUMN_RUSWORLD);
            int lessonColumnIndex = cursor.getColumnIndex(StudyWords.WorldEntry.COLUMN_LESSON);
            int courseColumnIndex = cursor.getColumnIndex(StudyWords.WorldEntry.COLUMN_COURSE);

            //Проходим по рядам
            while (cursor.moveToNext()) {
                // используем индексы для получения строки или числа
                /* int currentID = cursor.getInt(idColumnIndex);
                String currentLessonEntry = cursor.getString(WorldColumnIndex);
                String currentEngWorld = cursor.getString(engWordsColumnIndex);
                String currentTransletion = cursor.getString(transletionColumnIndex);
                String currentRusWorld = cursor.getString(rusWordsColumnIndex);
                String currentLessonWorld = cursor.getString(lessonColumnIndex);
                int currentCourseWorld = cursor.getInt(courseColumnIndex);*/
                //returnList.add()
                returnList.add(cursor.getInt(idColumnIndex));
                returnList.add(cursor.getString(engWordsColumnIndex));
                returnList.add(cursor.getString(transletionColumnIndex));
                returnList.add(cursor.getString(rusWordsColumnIndex));
                returnList.add(cursor.getString(lessonColumnIndex));
                returnList.add(cursor.getInt(courseColumnIndex));
                //String currentCourseWorld = Integer.toString(cursor.getInt(courseColumnIndex));
                //Log.i(" --- LessonSQL", "сheckAvailabilityIdOnLesson  step - "+Integer.toString(currentID)+"  "+currentLessonEntry+"  "+lesson);
                return returnList;
            }
        } finally {
            // закрываем курсор после чтения
            cursor.close();
        }

        return null;
    }

}
