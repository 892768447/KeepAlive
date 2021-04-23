package com.boolbird.keepalive.demo;

import android.content.Intent;
import android.util.Log;

import com.boolbird.keepalive.KeepAliveService;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Service1 extends KeepAliveService {
    private static final String TAG = "KeepAliveService1";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final boolean exit = false;
    private FileOutputStream fos;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        try {
            fos = new FileOutputStream(getExternalFilesDir(null).getAbsolutePath() + "WhileSleep.log");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!exit) {
                    try {
                        Thread.sleep(3000);
                        // 执行任务
                        Log.i(TAG, "wait for 3s");
                        if (fos != null) {
                            fos.write(dateFormat.format(new Date()).getBytes());
                            fos.write(" wait for 3s\n".getBytes());
                            fos.flush();
                        }
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        Log.d(TAG, "onCreate thread start");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        if (fos != null) {
            try {
                fos.write(dateFormat.format(new Date()).getBytes());
                fos.write(" onStartCommand\n".getBytes());
                fos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
