package com.example.noticekangwon.notifiThread;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.noticekangwon.Activity.MainActivity;
import com.example.noticekangwon.DataBase.Notice;
import com.example.noticekangwon.Jsoup.Jsoup;
import com.example.noticekangwon.R;

import java.util.ArrayList;
import java.util.Random;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MyService extends Service {

    private boolean isRun = false;
    private Random rand = new Random();
    private int randomTime = 10; // min
    private int basicTime = 3 * 1000 * 60 * 60; // 맨 앞이 시간
    private int sleepTime = basicTime;
    private Jsoup jsoup;

    private static final String TAG ="### MyService";

    @SuppressLint("WrongConstant") NotificationCompat.Builder builder;
    NotificationManager notificationManager;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        jsoup = new Jsoup(getApplicationContext());
        builder = new NotificationCompat.Builder(this, "default");

        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
                //.setContentIntent(intent)
        builder.setContentIntent(pendingIntent);

        // 오레오에선 채널 설정해줘야함
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        notificationManager.createNotificationChannel(new NotificationChannel("default","기본 채널",
                NotificationManager.IMPORTANCE_DEFAULT));
        notificationManager.notify(0, builder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                while (isRun == false) {
                    ArrayList<Notice> lists = jsoup.fetchData(0);
                    for(int i=0; i<lists.size(); i++){
                        builder.setContentTitle(lists.get(i).mTitle);
                    }
                    notificationManager.notify(0, builder.build());
                    sleepTime = basicTime + rand.nextInt(randomTime)*1000*60;
                    System.out.println("Waiting " + sleepTime / 1000 + " sec");
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
