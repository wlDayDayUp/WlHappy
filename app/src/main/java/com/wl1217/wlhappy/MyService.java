package com.wl1217.wlhappy;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate() {
        super.onCreate();
        Intent nfIntent = new Intent(this, MainActivity.class);

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel mChannel = null;
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel("CHANNEL_ID_STRING", "正在定位中……", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(this, "CHANNEL_ID_STRING")
                    .setContentIntent(PendingIntent.
                            getActivity(this, 0, nfIntent, 0)) // 设置PendingIntent
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                            R.mipmap.ic_launcher)) // 设置下拉列表中的图标(大图标)
                    .setContentTitle("下拉列表中的Title") // 设置下拉列表里的标题
                    .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏内的小图标
                    .setContentText("要显示的内容") // 设置上下文内容
                    .setWhen(System.currentTimeMillis()).build(); // 设置该通知发生的时间


        } else {

            Notification.Builder builder = new Notification.Builder
                    (this.getApplicationContext()); //获取一个Notification构造器


            builder.setContentIntent(PendingIntent.
                    getActivity(this, 0, nfIntent, 0)) // 设置PendingIntent
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                            R.mipmap.ic_launcher)) // 设置下拉列表中的图标(大图标)
                    .setContentTitle("下拉列表中的Title") // 设置下拉列表里的标题
                    .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏内的小图标
                    .setContentText("要显示的内容") // 设置上下文内容
                    .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间

            notification = builder.build(); // 获取构建好的Notification
            notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音


        }
        startForeground(1, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("wg", "onStartCommand: ");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                PeriodicWorkRequest oneTimeWork = new PeriodicWorkRequest.Builder(MyWorker.class, 5, TimeUnit.SECONDS)
//                        .setConstraints(myConstraints)
                        .build();
                WorkManager.getInstance(MyService.this).enqueue(oneTimeWork);

//                MyService.this.stopSelf();
            }
        }, 5000L);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("wg", "onDestroy: --------------");
    }
}
