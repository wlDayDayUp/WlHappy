package com.wl1217.mybox;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;


/**
 * 可在任意线程执行本类方法
 * User: ljx
 * Date: 2017/3/8
 * Time: 10:31
 */
public class Tip {

    private static Handler mHandler = new Handler(Looper.getMainLooper());
    private static Toast mToast;

    public static boolean show(int msgResId) {
        return show(msgResId, false);
    }

    public static boolean show(int msgResId, boolean timeLong) {
        return show(MyApp.getInstance().getString(msgResId), timeLong);
    }

    public static boolean show(CharSequence msg) {
        return show(msg, false);
    }

    public static boolean show(final CharSequence msg, final boolean timeLong) {
        return runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int duration = timeLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
                if (mToast == null) {
                    mToast = Toast.makeText(MyApp.getInstance(), msg, duration);
                } else {
                    mToast.setDuration(duration);
                    mToast.setText(msg);
                }
                mToast.show();
            }
        });
    }

    private static boolean runOnUiThread(Runnable runnable) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            runnable.run();
        } else {
            mHandler.post(runnable);
        }
        return true;
    }
}
