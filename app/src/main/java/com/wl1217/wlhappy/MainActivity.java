package com.wl1217.wlhappy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.rxjava.rxlife.RxLife;
import com.wl1217.mybox.ErrorInfo;
import com.wl1217.mybox.OnError;
import com.wl1217.mybox.config.Url;

import okhttp3.OkHttpClient;
import rxhttp.wrapper.param.RxHttp;
import rxhttp.wrapper.utils.LogUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.getBt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxHttp.get(Url.listNotice)
                        .add("userid", "402881e543773a5e01437749bb820000")
                        .asObject(NotictionBean.class)
                        .as(RxLife.asOnMain(MainActivity.this))
                        .subscribe(s -> {
                            Log.d("wg", Thread.currentThread().getName());
                            Log.d("wg", "get 请求: " + s.isSuccess());
                        }, (OnError) ErrorInfo::show);
            }
        });
    }
}
