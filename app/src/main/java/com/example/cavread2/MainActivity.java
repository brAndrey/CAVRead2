package com.example.cavread2;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.view.View;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = (Button) findViewById(R.id.button);
        final Button buttonSQL = (Button) findViewById(R.id.buttonSQL);

        button.setOnClickListener(this);

        buttonSQL.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        //Intent intent = new Intent(MainActivity.this, ListSQLActivity.class);
        //startActivity(intent);

        switch (v.getId()) {
            case R.id.button:
                //Intent intent = new Intent(MainActivity.this, ListMayActivity.class);
                Intent intent = new Intent(MainActivity.this, ListCSVActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonSQL:
                Intent intent2 = new Intent(MainActivity.this, BattonSQLActivity.class);
                startActivity(intent2);
                break;
        }
    }
}