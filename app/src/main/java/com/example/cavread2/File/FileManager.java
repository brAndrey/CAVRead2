package com.example.cavread2.File;

import android.content.Context;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileManager {
    final String TAG = FileManager.class.getSimpleName();

    private File currendDirectory;

    private final File rootDirectory;


    public FileManager(Context context) {
        File directory;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            directory = Environment.getExternalStorageDirectory();
        } else {
            directory = ContextCompat.getDataDir(context);
        }
        rootDirectory = directory;
        navigateTo(directory);
    }

    public boolean navigateTo(File directory) {
        // Проверяем, является ли файл директорией
        if (!directory.isDirectory()) {
            Log.e(TAG, directory.getAbsolutePath() + " is not directory");
            return false;
        }
        // Проверяем , не поднялись ли выше rootDirectory
        if (!directory.equals(rootDirectory) && rootDirectory.getAbsolutePath().contains(directory.getAbsolutePath())) {
            Log.w(TAG, "Trying to navigate upper than root directory to " + directory.getAbsolutePath());
            return false;
        }
        currendDirectory = directory;

        return true;
    }

    /**
     * Подняться на один уровень выше
     */



    public List<File> getFile() {
        List<File> files = new ArrayList<>();
        files.addAll(Arrays.asList(currendDirectory.listFiles()));

        return files;
    }

    public boolean navigateUp() {
        return navigateTo(currendDirectory.getParentFile());
    }
}
