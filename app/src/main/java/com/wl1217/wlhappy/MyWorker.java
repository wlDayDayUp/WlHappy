package com.wl1217.wlhappy;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import androidx.work.impl.WorkerWrapper;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ProjectName: WlHappy
 * @Package: com.wl1217.wlhappy
 * @ClassName: MyWorker
 * @Description: 类作用描述
 * @Author: wlDayDayUp
 * @CreateDate: 2019-09-04 11:31
 * @UpdateUser: 更新者
 * @UpdateDate: 2019-09-04 11:31
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class MyWorker extends Worker {
    private Context mContext;

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("wg", "doWork: -----------------" + Thread.currentThread().getName());

        FileOutputStream out = null;
        BufferedWriter writer = null;

        try {
            out = mContext.openFileOutput("test_data", Context.MODE_APPEND);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write("doWork: -----------------: " + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
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

        return Result.success();
    }
}
