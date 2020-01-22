package com.example.cavread2.data;

import android.provider.BaseColumns;

public final class StudyWords {

    private  StudyWords(){

    }

    public static abstract class WorldEntry implements BaseColumns {

        public final static String TABLE_NAME =  "worlds";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_ENGWORLD = "engworld";
        public final static String COLUMN_TRANSCRIPTION = "transcription";
        public final static String COLUMN_RUSWORLD = "rusworld";
        public final static String COLUMN_COURSE = "course";
        public final static String COLUMN_LESSON = "lesson";


    }

    public static abstract class LessonEntry implements BaseColumns {

        public final static String TABLE_NAME =  "lessons";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_LESSON = "lessonName";

    }

}
