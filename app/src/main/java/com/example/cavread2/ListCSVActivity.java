package com.example.cavread2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cavread2.File.CSVReader;
import com.example.cavread2.ItemArrayAdapterS.ItemArrayAdapter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class ListCSVActivity extends AppCompatActivity {

    public static final String LOG_TAG = ListWordsActivity.class.getSimpleName();

    //private ListView listView;
    private ItemArrayAdapter itemArrayAdapter;
    private String ki[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        CSVReader csvReader = null;
        List scoreList;
        try {
            // старт чтения из файла произвольного файла
            // получаем адрес файла
            String pathToCSV = getIntent().getExtras().getString("EXTRA_FILE_PATH");

            if (pathToCSV != null) {
                Log.i(LOG_TAG, "ListCSVActivity  " + pathToCSV);
                File csvfile = new File(pathToCSV);
                csvReader = new CSVReader(new BufferedInputStream(new FileInputStream(csvfile.getAbsolutePath())));
            }

        } catch (NullPointerException e){
            Log.i(LOG_TAG, "ListCSVActivity  читаем ресурс");
            // старт чтения из файла ресурса
            InputStream inputStream1 = getResources().openRawResource(R.raw.stats);
            csvReader = new CSVReader(inputStream1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // а на всякий случай
        if (csvReader ==null){return;}
        scoreList = csvReader.read();

        // конец чтения из файла с перебором


        itemArrayAdapter = new ItemArrayAdapter(getApplicationContext(), R.layout.item_layout);

        for (int i = 0; i < scoreList.size(); i++) {

            ki = Arrays.copyOf((String[]) scoreList.get(i), ((String[]) scoreList.get(i)).length);

            // Log.i(LOG_TAG,"step - "+ Integer.toString(i));
            // Log.i(LOG_TAG,"элемент "+scoreList.get(i).toString()+"  "+((String[]) scoreList.get(i)).length);
            // Log.i(LOG_TAG,"тип элемента "+scoreList.get(i).getClass().toString());
            // Log.i(LOG_TAG,"тип элемента ki "+ki.getClass().toString());

            if (((String[]) scoreList.get(i)).length == 1) {
                Log.i(" ListCSVActivity ", "ki 0 " + ki[0].toString());
                ki[1] = "пусто";
            }
            // scoreList.get(i)
            // Log.i( LOG_TAG ,Integer.toString(ki.length));
            // Log.i( LOG_TAG ,ki[0]+"  "+ki[1]);

            //itemArrayAdapter.add(scoreList.get(i));
            itemArrayAdapter.add(ki);

        }

        ListView listView = (ListView) findViewById(R.id.listView);

        Parcelable state = listView.onSaveInstanceState();

        listView.setAdapter(itemArrayAdapter);

        listView.onRestoreInstanceState(state);


        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return true;
        //super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Toast toast;
        int id = item.getItemId();

        Log.i(" onOptionsItemSelected ", Integer.toString(id));

        switch (id) {

            case R.id.settingAll:

                toast = Toast.makeText(getApplicationContext(), "Переход на создание элемента", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                // переходим на новую активность в которой отображается список
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.previousLayout:

                toast = Toast.makeText(getApplicationContext(), "Выход", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                // переходим на новую активность в которой отображается список
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

