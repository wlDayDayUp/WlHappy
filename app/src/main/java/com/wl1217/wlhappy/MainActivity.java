package com.wl1217.wlhappy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.Manifest;
import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.rxjava.rxlife.RxLife;
import com.wl1217.mybox.base.BaseActivity;
import com.wl1217.mybox.config.Url;
import com.wl1217.mybox.http.ErrorInfo;
import com.wl1217.mybox.http.OnError;
import com.wl1217.mybox.utils.CheckUpdate;
import com.wl1217.wlhappy.bean.GetCsBean;
import com.wl1217.wlhappy.bean.NotictionBean;
import com.wl1217.wlhappy.bean.UploadReslutBean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import rxhttp.wrapper.entity.UpFile;
import rxhttp.wrapper.param.RxHttp;
import rxhttp.wrapper.parse.Parser;
import rxhttp.wrapper.parse.SimpleParser;

public class MainActivity extends BaseActivity {

    private TextView resultTv;
    private Intent dwIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PackageManager pkgManager = getPackageManager();

        // 读写 sd card 权限非常重要, android6.0默认禁止的, 建议初始化之前就弹窗让用户赋予该权限
        boolean sdCardWritePermission =
                pkgManager.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getPackageName()) == PackageManager.PERMISSION_GRANTED;

        // 读sd card 权限非常重要, android6.0默认禁止的, 建议初始化之前就弹窗让用户赋予该权限
        boolean sdCardReadPermission =
                pkgManager.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, getPackageName()) == PackageManager.PERMISSION_GRANTED;

        // read phone state用于获取 imei 设备信息
        boolean phoneSatePermission =
                pkgManager.checkPermission(Manifest.permission.READ_PHONE_STATE, getPackageName()) == PackageManager.PERMISSION_GRANTED;

        // 相机权限
        boolean cramerSatePermission =
                pkgManager.checkPermission(Manifest.permission.CAMERA, getPackageName()) == PackageManager.PERMISSION_GRANTED;

        // 地理位置权限
        boolean coarseLocationPermission =
                pkgManager.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, getPackageName()) == PackageManager.PERMISSION_GRANTED;

        // 地理位置权限
        boolean accessFineLocationPermission =
                pkgManager.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, getPackageName()) == PackageManager.PERMISSION_GRANTED;


        if (Build.VERSION.SDK_INT >= 23
                && !sdCardWritePermission ||
                !phoneSatePermission ||
                !cramerSatePermission ||
                !coarseLocationPermission ||
                !accessFineLocationPermission
        ) {
            requestPermission();
        }

        dwIntent = new Intent(MainActivity.this, MyService.class);

        resultTv = findViewById(R.id.resultTv);

        // GET 请求测试
        findViewById(R.id.getBt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxHttp.get(Url.getCs)
                        .add("username", "wg")
                        .add("age", "123")
                        .asObject(GetCsBean.class)
                        .as(RxLife.asOnMain(MainActivity.this))
                        .subscribe(s -> {
                            resultTv.setText(
                                    s.isSuccess() + "\n" +
                                            s.getMsg() + "\n" +
                                            s.getInfo().getPhone()
                            );

                        }, (OnError) ErrorInfo::show);
            }
        });

        // POST 请求测试
        findViewById(R.id.postBt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxHttp.postForm(Url.login)
//                        .add("userid", "402881e543773a5e01437749bb820000")
                        .add("username", "wg")
                        .add("password", "123")
                        .asObject(NotictionBean.class)
                        .as(RxLife.asOnMain(MainActivity.this))
                        .subscribe(s -> {
                            resultTv.setText(
                                    s.isSuccess() + "\n" +
                                            s.getMsg() + "\n"
                            );
                        }, (OnError) ErrorInfo::show);
            }
        });

        // 下载
        findViewById(R.id.downLoadBt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CheckUpdate(MainActivity.this).checkAndUpdate(MainActivity.this);
            }
        });

        // 单个文件上传
        findViewById(R.id.oneUploaddBt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("wg", "onClick: " + getCacheDir().getPath());
                RxHttp.postForm(Url.oneUploadFile)
                        .addFile(new UpFile("test_png", getCacheDir() + "/test.png"))
                        .asString()
                        .as(RxLife.asOnMain(MainActivity.this))
                        .subscribe(s -> {
                            Log.d("wg", "单文件上传: " + s);
                        }, (OnError) ErrorInfo::show)
                ;
            }
        });
//

        // 多个文件上传
        findViewById(R.id.moreUploaddBt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("wg", "onClick: " + getCacheDir().getPath());
                List<UpFile> files = new ArrayList<>();

                files.add(new UpFile("test_png1", getCacheDir() + "/小布丁.jpg"));
                files.add(new UpFile("test_mp4", getCacheDir() + "/test_mp4.mp4"));
                files.add(new UpFile("test_ly", getCacheDir() + "/录音1.aac"));

                ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setTitle("上传文件");
                progressDialog.setMax(100);
                progressDialog.show();

                RxHttp.postForm(Url.moreUploadFile)
                        .addFile(files)
                        .asUpload(SimpleParser.get(UploadReslutBean.class), progress -> {
                            //上传进度回调,0-100，仅在进度有更新时才会回调,最多回调101次，最后一次回调Http执行结果
                            int currentProgress = progress.getProgress(); //当前进度 0-100
                            long currentSize = progress.getCurrentSize(); //当前已上传的字节大小
                            long totalSize = progress.getTotalSize();     //要上传的总字节大小

                            Log.d("wg", "moreUploadFile 当前进度: " + currentProgress);
                            Log.d("wg", "moreUploadFile 当前已上传的字节大小: " + currentSize);
                            Log.d("wg", "moreUploadFile 总字节大小: " + totalSize);
                            progressDialog.setProgress(currentProgress);
                        }, AndroidSchedulers.mainThread())
                        .as(RxLife.asOnMain(MainActivity.this))
                        .subscribe(s -> {
                            Log.d("wg", "moreUploadFile 结果: " + s);
                            progressDialog.dismiss();
                        }, (OnError) ErrorInfo::show);
            }
        });


        findViewById(R.id.dsBt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                开启前台服务
                startService(dwIntent);

//                Constraints myConstraints = new Constraints.Builder()
//                        .setRequiredNetworkType(NetworkType.CONNECTED)
//                        .build();

//                PeriodicWorkRequest oneTimeWork = new PeriodicWorkRequest.Builder(MyWorker.class, 5, TimeUnit.SECONDS)
////                        .setConstraints(myConstraints)
//                        .build();
//                WorkManager.getInstance(MainActivity.this).enqueue(oneTimeWork);

            }
        });

        findViewById(R.id.stopDwBt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(dwIntent);
            }
        });
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                },
                1001);
    }
}
