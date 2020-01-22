package com.example.cavread2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cavread2.data.LessonSQL;
import com.example.cavread2.data.WorldSQL;

public class OneLessonActivity extends Activity {

    public static final String LOG_TAG = OneLessonActivity.class.getSimpleName();

    int idLesson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_lesson);


        if (getIntent().getExtras() != null) {
            idLesson = getIntent().getExtras().getInt("idLesson");
            String nameLesson = getIntent().getExtras().getString("nameLesson");

            EditText mEditText = findViewById(R.id.EditText);
            TextView mTextView = findViewById(R.id.textView);

            mEditText.setText(nameLesson);
            mTextView.setText(Integer.toString(idLesson));

            Log.i(LOG_TAG, Integer.toString(idLesson)+ " " + nameLesson);
        }
    }

    public void ClearEditText(View view) {
        EditText mEditText = findViewById(R.id.EditText);
        mEditText.setText("");

        TextView mTextView = findViewById(R.id.textView);
        mTextView.setText("");
        idLesson =0;

    }

    @SuppressLint("SetTextI18n")
    public void SaveLesson(View view) {
        EditText mEditText = findViewById(R.id.EditText);
        TextView mTextView = findViewById(R.id.textView);
        LessonSQL mLessonSQL = new LessonSQL(this);

        String nameLesson = mEditText.getText().toString();

        if (nameLesson.trim().length() > 0) {

            int currentID;

            if (idLesson ==0){
                // входящий ID =0 значит новый элемент
                currentID = mLessonSQL.сheckAvailabilityLessonInID(nameLesson);

            } else {
                // иначе работаем в томже самом элементе
                currentID = idLesson;
            }

            mLessonSQL.update(idLesson, nameLesson);

            Log.i(LOG_TAG,"currentID  "+currentID);

            if (idLesson == 0) {
                mTextView.setText(String.valueOf(mLessonSQL.сheckAvailabilityLessonInID(nameLesson)));
            }
        }
    }

    public void ReturnActivity(View view) {
        // переходим обратно на активность ListLessonActivity
        Intent intent = new Intent(OneLessonActivity.this, ListLessonActivity.class);
        startActivity(intent);
    }

    public void BattonRemove(View view) {
        EditText editText = findViewById(R.id.EditText);
        String lesson = editText.getText().toString();


        if (lesson.length()!=0){

            LessonSQL lessonSQL= new LessonSQL(this);
            lessonSQL.delete(lesson);

            if (!lessonSQL.сheckAvailabilityLessonBool(lesson)) {
                editText.setText("");
            }

        }


    }
}
