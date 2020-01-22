package com.example.cavread2.File;

import android.util.Log;

import com.example.cavread2.data.StydyWorldsDbHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
    private static final String LOG_TAG = CSVReader.class.getSimpleName();

    InputStream inputStream;

    int k; // для Log.i
    private String row[]  ;

    ArrayList<String> wordLine = new ArrayList<>();


    public CSVReader(InputStream inputStream){
        this.inputStream = inputStream;
    }

    public List read(){

        List resultList = new ArrayList();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            // в этом блоке читаем данные из CSV файла
            // построчно, строку разлагаем в массив
            // результатом получаем массив массивов
            String csvLine;
            int k=0;
            while ((csvLine = reader.readLine()) != null) {
                //String[] row = csvLine.split(","); // раскладываем строку в массив
                if (csvLine.trim().length()>=0) {
                    row = csvLine.split(";"); // раскладываем строку в массив

                    k++;

                    //блок проверки заполненности строки
                    row[0] = row[0].trim();


                    if (0 == row.length) {
                        row = csvLine.split(",");
                    } // раскладываем строку в массив

                    Log.i(LOG_TAG+" строка row ", "step - " + Integer.toString(k));
                    Log.i(LOG_TAG+" строка row ", " csvLine - " + csvLine + "  " + csvLine.getClass());
//                   Log.i("строка row "," row - "+row+" "+String.valueOf(row.length) +"  "+row.getClass());
//                   Log.i("строка row ", row.toString());

                    if (row[0].length() != 0) {
                        resultList.add(row);
                        Log.i(LOG_TAG, k + "  " + row[0]);
                    }
                }
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: "+e);
            }
        }

        return resultList;
    }


}