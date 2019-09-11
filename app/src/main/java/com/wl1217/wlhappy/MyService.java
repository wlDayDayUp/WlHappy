package com.wl1217.wlhappy;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.wl1217.mybox.utils.MapLocationUtils;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyService extends Service {
    private Handler handler;
    private boolean isRuning;
    private Runnable dwRunnable;
    private MapLocationUtils mapLocationUtils;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate() {
        super.onCreate();

//        handler = new Handler();

        mapLocationUtils = new MapLocationUtils(this, new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                Log.d("wg", "onLocationChanged: " + aMapLocation.getLatitude() + aMapLocation.getLongitude() + " --------- " + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));

                /*测试*/
                FileOutputStream out = null;
                BufferedWriter writer = null;

                try {
                    out = openFileOutput("test_data", Context.MODE_APPEND);
                    writer = new BufferedWriter(new OutputStreamWriter(out));
                    writer.write("地理位置信息: -----------------: " +
                            aMapLocation.getLongitude() + "," + aMapLocation.getLatitude() + " " +
                            (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
                    writer.newLine();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        mapLocationUtils.startLocation();

//        dwRunnable = new Runnable() {
//            @Override
//            public void run() {
//
//
//                handler.postDelayed(this, 5000L);
////                FileOutputStream out = null;
////                BufferedWriter writer = null;
////
////                try {
////                    out = openFileOutput("test_data", Context.MODE_APPEND);
////                    writer = new BufferedWriter(new OutputStreamWriter(out));
////                    writer.write("地理位置信息: -----------------: " + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
////                    writer.newLine();
////                } catch (FileNotFoundException e) {
////                    e.printStackTrace();
////                } catch (IOException e) {
////                    e.printStackTrace();
////                } finally {
////                    if (writer != null) {
////                        try {
////                            writer.close();
////                            handler.postDelayed(this, 30000L);
////                        } catch (IOException e) {
////                            e.printStackTrace();
////                        }
////                    }
////                }
//            }
//        };

        Intent mainIntent = new Intent(this, MainActivity.class);
        PendingIntent goMainPendIntent = PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        // 获取系统 通知管理 服务
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = null;
        // 构建 Notification
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("实时定位中")
                .

                        setSmallIcon(R.mipmap.ic_launcher)
                .

                        setContentIntent(goMainPendIntent)
                .

                        setContentText("正在实时定位并同步位置");

        // 兼容  API 26，Android 8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 第三个参数表示通知的重要程度，默认则只在通知栏闪烁一下
            NotificationChannel notificationChannel = new NotificationChannel("SSDWNotificationId", "SSDWNotificationName", NotificationManager.IMPORTANCE_HIGH);
            // 注册通道，注册后除非卸载再安装否则不改变
            notificationManager.createNotificationChannel(notificationChannel);
            builder.setChannelId("SSDWNotificationId");
            notification = builder.build();
        } else {
            notification = builder.getNotification();
        }

        startForeground(1, notification);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        if (!isRuning) {
//            isRuning = true;
//            Log.d("wg", "onStartCommand: ");
//
//            handler.post(dwRunnable);
//        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
//        isRuning = false;
//        if (dwRunnable != null) {
//            handler.removeCallbacks(dwRunnable);
//        }
        mapLocationUtils.destroyLocation();
        super.onDestroy();
        Log.d("wg", "onDestroy: --------------");
    }
}
