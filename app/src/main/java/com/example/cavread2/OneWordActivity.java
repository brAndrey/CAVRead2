package com.example.cavread2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cavread2.ListChoice.ListChoiceActivity;
import com.example.cavread2.data.WorldSQL;

import java.util.ArrayList;


public class OneWordActivity extends AppCompatActivity {
    static final private int CHOOSE_THIEF = 0;
    int idWord=0;
    String LOG_TAG = OneWordActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_word);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getIntent().getExtras() != null) {
            String nameWord = getIntent().getExtras().getString("nameWord");

            WorldSQL worldSQL = new WorldSQL(this);
            ArrayList recordingOptions = worldSQL.сheckAvailabilityWorldInLIst(nameWord);

            if (recordingOptions!=null) {
                EditText  editTextWord = findViewById(R.id.editTextWord);
                EditText editTextTranscription = findViewById(R.id.editTextTranscription);
                EditText editTextRus = findViewById(R.id.editTextRus);
                TextView textViewLesson = findViewById(R.id.textViewLesson);

                editTextWord.setText(String.valueOf(recordingOptions.get(1)));
                editTextTranscription.setText(String.valueOf(recordingOptions.get(2)));
                editTextRus.setText(String.valueOf(recordingOptions.get(3)));
                textViewLesson.setText(String.valueOf(recordingOptions.get(4)));
            }

            Log.i(LOG_TAG, "nameWord " + nameWord);
        }



        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void ReturnActivity(View view) {
        // Toast.makeText(getApplicationContext(), " Процедура возврата ", Toast.LENGTH_SHORT).show();

        finish();
    }

    public void saveWord(View view) {
        Toast toast = Toast.makeText(getApplicationContext(), " Процедура сохранения ", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        EditText editTextWord = findViewById(R.id.editTextWord);
        EditText editTextTranscription = findViewById(R.id.editTextTranscription);
        EditText editTextRus = findViewById(R.id.editTextRus);

        TextView textViewLesson = findViewById(R.id.textViewLesson);

        WorldSQL mWorldSQL = new WorldSQL(this);

        String word = editTextWord.getText().toString();

        String wordRus = editTextRus.getText().toString();


        if (word.trim().length()>0 && wordRus.length()>0){
            int currentID;

            if (idWord ==0){
                // входящий ID =0 значит новый элемент
                currentID = mWorldSQL.сheckAvailabilityWorldInID(word);
            } else {
                // иначе работаем в томже самом элементе
                currentID = idWord;
            }

            mWorldSQL.update(currentID,word,editTextTranscription.getText().toString(),wordRus,textViewLesson.getText().toString());

            Log.i(LOG_TAG,"currentID  "+currentID);

            //if (idLesson == 0) {
            //    mTextView.setText(String.valueOf(mLessonSQL.сheckAvailabilityLessonInID(nameLesson)));
           // }
        }



    }

    public void removeWord(View view) {

        EditText editTextWord = findViewById(R.id.editTextWord);
        EditText editTextTranscription = findViewById(R.id.editTextTranscription);
        EditText editTextRus = findViewById(R.id.editTextRus);
        TextView textViewLesson = findViewById(R.id.textViewLesson);

        WorldSQL mWorldSQL = new WorldSQL(this);

        String word = editTextWord.getText().toString();

        if (word.trim().length()>0 ){
            int currentID;

            currentID = mWorldSQL.сheckAvailabilityWorldInID(word);

            mWorldSQL.delete(currentID,word);

            Log.i(LOG_TAG,"currentID  "+currentID);
        }

        editTextWord.setText("");
        editTextTranscription.setText("");
        editTextRus.setText("");
        textViewLesson.setText("");

    }

    public void chooseLesson(View view) {
        Toast toast = Toast.makeText(getApplicationContext(), " Процедура выбора урока ", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        Intent intent = new Intent(this, ListChoiceActivity.class);
        intent.putExtra("Class",String.valueOf(0));
        startActivityForResult(intent, CHOOSE_THIEF);
    }

    public void chooseWord(View view) {
        Toast toast = Toast.makeText(getApplicationContext(), " Новое слово ", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        EditText editTextWord = findViewById(R.id.editTextWord);
        editTextWord.setText("");

        EditText editTextTranscription = findViewById(R.id.editTextTranscription);
        editTextTranscription.setText("");

        EditText editTextRus = findViewById(R.id.editTextRus);
        editTextRus.setText("");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==CHOOSE_THIEF){
            if (resultCode == RESULT_OK) {
                TextView textViewLesson = findViewById(R.id.textViewLesson);

                String LESSONNAME = data.getStringExtra(ListChoiceActivity.LESSONNAME);

                textViewLesson.setText(LESSONNAME);
            }
        }
    }



}