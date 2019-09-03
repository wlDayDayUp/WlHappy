package com.wl1217.mybox;

import android.app.Application;

import io.reactivex.plugins.RxJavaPlugins;
import rxhttp.wrapper.param.RxHttp;
import rxhttp.wrapper.ssl.SSLSocketFactoryImpl;
import rxhttp.wrapper.ssl.X509TrustManagerImpl;

/**
 * @ProjectName: WlHappy
 * @Package: com.wl1217.mybox
 * @ClassName: MyApp
 * @Description: 类作用描述
 * @Author: wlDayDayUp
 * @CreateDate: 2019-09-03 16:16
 * @UpdateUser: 更新者
 * @UpdateDate: 2019-09-03 16:16
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class MyApp extends Application {

    private static MyApp instance;

    public static MyApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initRxHttp();
    }

    private void initRxHttp() {
        //设置RxJava 全局异常处理
        RxJavaPlugins.setErrorHandler(throwable -> {
            /*
              RxJava2的一个重要的设计理念是：不吃掉任何一个异常,即抛出的异常无人处理，便会导致程序崩溃
              这就会导致一个问题，当RxJava2“downStream”取消订阅后，“upStream”仍有可能抛出异常，
              这时由于已经取消订阅，“downStream”无法处理异常，此时的异常无人处理，便会导致程序崩溃
             */
        });
        RxHttp.setDebug(BuildConfig.DEBUG);
    }
}