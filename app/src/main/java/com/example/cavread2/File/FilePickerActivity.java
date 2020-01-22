package com.example.cavread2.File;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.example.cavread2.R;

import java.io.File;
import java.util.List;

public class FilePickerActivity extends AppCompatActivity {

//    public static final String EXTRA_FILE_PATH = ;
    final String TAG = FilePickerActivity.class.getSimpleName();

    // переменные возврата
//    public final static String FILE_PATH= "com.example.andrey_vb.Test_settings.LESSONID";

    private static final int PERMISSION_REQUEST_CODE = 1;

    public static final String EXTRA_FILE_PATH = "file_path";

    private FileManager fileManager;

    private FilesAdapter filesAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_picker);

        RecyclerView recyclerView = findViewById(R.id.files_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        filesAdapter = new FilesAdapter();
        recyclerView.setAdapter(filesAdapter);

        initFileManager();
    }

    @Override
    protected void onStart() {
        super.onStart();
        filesAdapter.setOnFileClickListener(onFileClickListener);
    }

    @Override
    protected void onStop() {
        filesAdapter.setOnFileClickListener(null);
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (fileManager != null && fileManager.navigateUp()) {
            updateFileList();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission granted!");
            initFileManager();
        } else {
            Log.i(TAG, "Permission denied!");
            requestPermissions();
        }
    }



    private void initFileManager() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Разрешение дано
            fileManager = new FileManager(this);
            updateFileList();
        } else {
            requestPermissions();
        }
    }

    private void updateFileList() {
        List<File> files = fileManager.getFile();

        filesAdapter.setFiles(files);
        filesAdapter.notifyDataSetChanged();
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    /**
     * Listener события клика на файл
     */
    private final FilesAdapter.OnFileClickListener onFileClickListener = new FilesAdapter.OnFileClickListener() {
        @Override
        public void onFileClick(File file) {
            if (file.isDirectory()) {
                fileManager.navigateTo(file);
                updateFileList();
            } else {
                if (file.getName().endsWith(".csv")) {

                    // работы по выбранному файлу
                    Intent intent = new Intent();


                    //Intent intent = new Intent(this, FilePickerActivity.class);
                    intent.putExtra(EXTRA_FILE_PATH,file.getAbsolutePath());
                    setResult(RESULT_OK,intent);
                    finish();


                }
            }
        }
    };


}
