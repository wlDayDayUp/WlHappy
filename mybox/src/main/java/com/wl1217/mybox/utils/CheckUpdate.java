package com.wl1217.mybox.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.rxjava.rxlife.RxLife;
import com.wl1217.mybox.R;
import com.wl1217.mybox.base.BaseActivity;
import com.wl1217.mybox.bean.VersionUpdate;
import com.wl1217.mybox.callback.IDialogBtCallback;
import com.wl1217.mybox.config.GlobConfig;
import com.wl1217.mybox.config.Url;

import java.io.File;
import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import rxhttp.wrapper.param.RxHttp;

/**
 * @ProjectName: WlHappy
 * @Package: com.wl1217.mybox.utils
 * @ClassName: CheackUpdate
 * @Description: 类作用描述
 * @Author: wlDayDayUp
 * @CreateDate: 2019-09-06 14:10
 * @UpdateUser: 更新者
 * @UpdateDate: 2019-09-06 14:10
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class CheckUpdate {

    private Context mContext;
    private ProgressDialog mProgressDialog; /*进度框*/
    private AlertDialog mAlertDialog; /* 用户提示更新框*/

    public CheckUpdate(Context context) {
        this.mContext = context;
    }

    public void checkAndUpdate(BaseActivity baseActivity) {
        // 调接口检查，判断VersionCode
        RxHttp.get(Url.updateVserion)
                .asObject(VersionUpdate.class)
                .as(RxLife.asOnMain(baseActivity))
                .subscribe(versionUpdate -> {
                    try {
                        // 本地版本好
                        int localVersionCode = baseActivity.getPackageManager()
                                .getPackageInfo(baseActivity.getPackageName(), 0).versionCode;
                        // 服务器版本好
                        int remoteVersionCode = Integer.parseInt(versionUpdate.getCode());
                        if (remoteVersionCode > localVersionCode) {
                            // 下载更新
                            showUserUpdateDialog(versionUpdate.getMsg(), new IDialogBtCallback() {
                                @Override
                                public void confirm() {
                                    downApk(baseActivity);
                                }

                                @Override
                                public void cancel() {
                                    exit(); /*强制更新*/
                                }
                            });
                        } else {
                            // 不需要更新，提示
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
    }

    /**
     * 下载更新
     *
     * @param baseActivity 用于观察生命周期
     */
    private void downApk(BaseActivity baseActivity) {
        showDownApkProgressDialog(); // 显示下载进度条
        final String fileName = "/" + GlobConfig.APP_NAME;
        String fileStoreDir = mContext.getCacheDir().getPath(); // 不需要动态获取权限/data/user/0/com.daydayup.mecookies/cache
        RxHttp.get(Url.downApk)
                .asDownload(fileStoreDir + fileName, progress -> {
                    //下载进度回调,0-100，仅在进度有更新时才会回调，最多回调101次，最后一次回调文件存储路径
                    int currentProgress = progress.getProgress(); //当前进度 0-100
//                    long currentSize = progress.getCurrentSize(); //当前已下载的字节大小
//                    long totalSize = progress.getTotalSize();     //要下载的总字节大小
                    updateProgress(currentProgress); // 更新进度条
                }, AndroidSchedulers.mainThread())
                .as(RxLife.asOnMain(baseActivity))
                .subscribe(s -> { // s 文件保存的路径
                    missDownApkProgressDialog(); // 关闭进度条
                    installApk(s); // 安装APK
                }, throwable -> {
                    throwable.printStackTrace();
                    missDownApkProgressDialog(); // 关闭进度条
                });
    }

    /**
     * 安装APK
     *
     * @param apkPath 下载后apk保存的全路径
     */
    private void installApk(String apkPath) {

        String command = "chmod 777 " + apkPath;
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec(command); /* 设置文件执行权限*/
            // 发起系统安装apk的intent
            Log.d("wg", "installApk: " + apkPath);
            File file = new File(apkPath);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 7.0+以上版本
                Uri apkUri = FileProvider.getUriForFile(mContext, GlobConfig.UriForFile, file);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            }
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 结束程序
     */
    private void exit() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(startMain);
        System.exit(0);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 显示下载进度条dialog
     */
    private void showDownApkProgressDialog() {
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setTitle("重要更新");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMax(100);
        mProgressDialog.show();
    }

    /**
     * 关闭下载进度条dialog
     */
    private void missDownApkProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * 更新进度条
     *
     * @param progress 下载进度
     */
    private void updateProgress(int progress) {
        if (mProgressDialog != null) {
            mProgressDialog.setProgress(progress);
        }
    }

    private void showUserUpdateDialog(String msg, IDialogBtCallback iDialogBtCallback) {
        mAlertDialog = new AlertDialog.Builder(mContext)
                .setTitle("重要提示")
                .setCancelable(false)
                .setMessage(Html.fromHtml(msg).toString())
                .setPositiveButton(R.string.confirm, (dialogInterface, i) -> {
                    iDialogBtCallback.confirm();
                })
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
                    iDialogBtCallback.cancel();
                })
                .create();
        mAlertDialog.show();
    }

    private void missUserUpdateDialog() {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
    }
}
