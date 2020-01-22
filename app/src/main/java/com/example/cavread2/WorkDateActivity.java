package com.example.cavread2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.cavread2.data.LessonSQL;
import com.example.cavread2.data.WorldSQL;

public class WorkDateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_date);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
               // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
               //         .setAction("Action", null).show();
            }
        });
    }

    public void SettingALL(View view) {
        // переходим на новую активность настроек
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }


    public void NewWord(View view) {
        // переходим на новую активность в которой вводится новое слово
        Intent intent = new Intent(this, OneWordActivity.class);
        startActivity(intent);

    }

    public void DownloadCSV(View view) {
        // переходим на новую активность в которой в базу дополняются слова из CSV
        Intent intent = new Intent(this, activity_words_from_CSV.class);
        startActivity(intent);
    }

    public void DownloadBase(View view) {
        // заполняем базу слов из файла по умолчанию
        WorldSQL mWorldSQL = new WorldSQL(this);
        mWorldSQL.fillSQL();
    }

    public void ClearBase(View view) {

        WorldSQL worldSQL = new WorldSQL(this);
        worldSQL.ClearWorldSQL();

        LessonSQL lessonSQL = new LessonSQL(this);
        lessonSQL.ClearLessonSQL();

    }

    public void BattonReturn(View view) {
        finish();
    }
}
