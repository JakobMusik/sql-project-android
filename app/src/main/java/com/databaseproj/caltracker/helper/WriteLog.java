package com.databaseproj.caltracker.helper;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class WriteLog {
    private static void write(String data, File fileDir) throws IOException {
        FileOutputStream stream = new FileOutputStream(fileDir);
        try {
            stream.write(data.getBytes());
        } finally {
            stream.close();
        }
    }

    public static void writeToFile(final String data, final Context context) {

        File path = new File(context.getExternalFilesDir(null).getPath() + "/log");
        final File fileDir = new File(path, "log.txt");
        new Thread(new Runnable() {
            public void run() {
                try {
                    write(data, fileDir);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
